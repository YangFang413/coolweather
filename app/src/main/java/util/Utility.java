package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

import database.CoolWeatherDB;
import model.City;
import model.County;
import model.Province;

/**
 * 解析和处理服务器返回的省级数据、市级数据、县级数据及XML数据
 * Created by Administrator on 2016/8/1.
 */
public class Utility {

    // 解析和处理服务器返回的省级数据
    public synchronized static boolean handleProvincesRequest(CoolWeatherDB coolWeatherDB, String response){
        if (!TextUtils.isEmpty(response)){
            // Log.d("YF-Utility", response);
            String[] allProvinces = response.split(",");
            for (String s : allProvinces){
                Log.d("YF-Utility-1", s);
            }

            if (allProvinces != null && allProvinces.length > 0){
                for (String p : allProvinces){
                    String[] array = p.split("\\|");

                    Log.d("YF-Utility-2", array[0]);
                    Log.d("YF-Utility-3", array[1]);

                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    // 将解析出来的数据存储到Province表中
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    // 解析和处理服务器返回的市级数据
    public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceid){
        if (!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0){
                for (String c : allCities){
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceid);
                    // 将解析出来的数据存储到City表中
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    // 解析和处理服务器返回的县级数据
    public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response, int cityId){
        if (!TextUtils.isEmpty(response)){
            String[] allCounties = response.split(",");
            if (allCounties != null && allCounties.length > 0){
                for (String c : allCounties){
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    // 解析从服务器端返回的XML数据
    public static void parseWeatherXMLWithPull (Context context, String response){
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(response));
            int eventType = xmlPullParser.getEventType();
            String cityName = ""; // 存放当前查询的城市名
            String temp1 = ""; // 存放温度1
            String temp2 = ""; // 存放温度2
            String weather = ""; // 存放天气情况
            String updateTime = ""; // 更新时间
            String currentTime = ""; // 当前时间
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    // 开始解析某个结点
                    case XmlPullParser.START_TAG: {
                        if ("city".equals(nodeName)) {
                            cityName = xmlPullParser.nextText();
                        } else if ("status1".equals(nodeName)) {
                            weather = xmlPullParser.nextText();
                        } else if ("temperature1".equals(nodeName)) {
                            temp1 = xmlPullParser.nextText();
                        } else if ("temperature2".equals(nodeName)) {
                            temp2 = xmlPullParser.nextText();
                        } else if ("udatetime".equals(nodeName)) {
                            String[] array = xmlPullParser.nextText().split(" ");
                            updateTime = array[1];
                            currentTime = array[0];
                        }
                        break;
                    }
                    // 完成解析某个结点
                    case XmlPullParser.END_TAG: {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                        editor.putBoolean("city_selected", true);
                        editor.putString("city_name", cityName);
                        editor.putString("temp1", temp1);
                        editor.putString("temp2", temp2);
                        editor.putString("weather", weather);
                        editor.putString("update_time", updateTime);
                        editor.putString("current_date", currentTime);
                        editor.commit();
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}

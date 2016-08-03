package util;

import android.text.TextUtils;
import android.util.Log;

import database.CoolWeatherDB;
import model.City;
import model.County;
import model.Province;

/**
 * 解析和处理服务器返回的省级数据
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

}

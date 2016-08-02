package util;

import android.text.TextUtils;

import database.CoolWeatherDB;
import model.Province;

/**
 * 解析和处理服务器返回的省级数据
 * Created by Administrator on 2016/8/1.
 */
public class Utility {
    public synchronized static boolean handleProvincesRequest(CoolWeatherDB coolWeatherDB, String response){
        if (!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0){
                for (String p : allProvinces){
                    String[] array = p.split("//|");
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
}

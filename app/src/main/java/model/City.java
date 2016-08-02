package model;

/**
 * Created by Administrator on 2016/8/1.
 */
public class City {
    private int id;
    private String cityName;
    private String cityCode;
    private int provinceId;

    public int getId(){
        return id;
    }

    public String getCityName(){
        return cityName;
    }

    public String getCityCode(){
        return cityCode;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setCityName(String cityName){
        this.cityName = cityName;
    }

    public void setCityCode(String cityCode){
        this.cityCode = cityCode;
    }

    public int getProvinceId(){
        return provinceId;
    }

    public void setProvinceId(int provinceId){
        this.provinceId = provinceId;
    }
}

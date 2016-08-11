package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yf.coolweather.R;

import java.io.UnsupportedEncodingException;

import database.CoolWeatherDB;
import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utility;

/**
 * Created by Administrator on 2016/8/11.
 * 显示天气
 */
public class WeatherActivity extends Activity {

    private CoolWeatherDB coolWeatherDB;

    private LinearLayout weatherInfoLayout;
    private TextView cityNameText; // 用于显示城市名
    private TextView publishText;  // 用于显示发布时间
    private TextView weatherDespText; // 用于显示天气的描述信息
    private TextView temp1Text; // 用于显示气温1
    private TextView temp2Text; // 用于显示气温2
    private TextView currentDateText; // 用于显示当前日期

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);

        // 初始化各控件
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDateText = (TextView) findViewById(R.id.current_data);

        String countyCode = getIntent().getStringExtra("county_code");
        coolWeatherDB = CoolWeatherDB.getInstance(this);

        // 有县级代号就从数据库中取出县级名称，使用GB2312转码，向服务器查询天气
        if (!TextUtils.isEmpty(countyCode)){
            publishText.setText("同步中。。。");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            String countyName = coolWeatherDB.queryCountyName(countyCode);
            String countyGBCode = "";
            try {
                // byte[] sour = countyName.getBytes("utf-8");
                // countyGBCode = new String(sour, "gb2312");
                countyGBCode = java.net.URLEncoder.encode(countyName, "gb2312");
                Log.d("WeatherActivity-2", countyGBCode);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            queryWeather(countyGBCode);
        } else {
            // 没有县级代号就直接显示本地天气
            showWeather();
        }
    }

    // 根据转码后的县级代码组成查询地址
    private void queryWeather(String countyGBCode){
        String address = "http://php.weather.sina.com.cn/xml.php?city=" +
                countyGBCode + "&password=DJOYnieT8234jlsK&day=0";

        Log.d("WeatherActivity-1", address);
        queryFromServer(address);
    }

    // 从SharePreferences文件中读取存储的天气信息，并显示到界面上。
    private void showWeather(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(preferences.getString("city_name", ""));
        temp1Text.setText(preferences.getString("temp2", ""));
        temp2Text.setText(preferences.getString("temp1", ""));
        weatherDespText.setText(preferences.getString("weather", ""));
        publishText.setText("今天" + preferences.getString("update_time", "") + "发布");
        currentDateText.setText(preferences.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);

    }

    // 根据传入的地址向服务器查询天气信息
    private void queryFromServer(final String address){
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.parseWeatherXMLWithPull(WeatherActivity.this, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }
}

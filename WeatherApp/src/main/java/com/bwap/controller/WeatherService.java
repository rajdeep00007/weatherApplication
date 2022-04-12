package com.bwap.weatherapp.WeatherApp.controller;

//import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
//import elemental.json.JsonArray;
//import elemental.json.JsonException;
//import elemental.json.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import java.io.IOException;


@Service
public class WeatherService {

    private OkHttpClient client;
    private Response response;
    private String cityname;
    String unit;
    private String API ="https://api.openweathermap.org/data/2.5/weather?q=london&appid=5c3254a5be229a767c293f9f72ceec76";

    public JSONObject getWeather(){
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?q="+getCityname()+"&units="+getUnit()+"&appid=5c3254a5be229a767c293f9f72ceec76")
                .build();

        try {
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string()) {
            };
        } catch (IOException |JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray returnWeatherArray() throws JSONException {
        JSONArray weatherArray = getWeather().getJSONArray("weather");
        return weatherArray;
    }

    public JSONObject returnMain() throws JSONException {
        JSONObject main = getWeather().getJSONObject("main");
        return main;
    }

    public JSONObject returnWind() throws JSONException {
        JSONObject wind = getWeather().getJSONObject("wind");
        return wind;
    }

    public JSONObject returnSys() throws JSONException {
        JSONObject sys = getWeather().getJSONObject("sys");
        return sys;
    }



    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

package com.andela.bookmarkit.data.api.pojos;


import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WeatherItem {
    @SerializedName("main")
    public Temperature temperature;

    @SerializedName("weather")
    public List<Weather> weatherList;

    @SerializedName("dt_txt")
    public Date date;

    public Uri getIconURI() {
        return weatherList.size() > 0
                ? weatherList.get(0).getIconURI()
                : null;
    }

    public int temperatureInCelsius() {
        return temperature.tempInCelsius();
    }

    public String getDayString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
        return simpleDateFormat.format(this.date).toUpperCase();
    }

    public WeatherItem(Temperature temperature, List<Weather> weatherList, Date date) {
        this.temperature = temperature;
        this.weatherList = weatherList;
        this.date = date;
    }

    @Override
    public String toString() {
        return "WeatherItem{" +
                "temperature=" + temperature +
                ", weatherList=" + weatherList +
                ", date=" + date +
                '}';
    }
}

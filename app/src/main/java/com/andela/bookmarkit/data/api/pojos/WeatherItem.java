package com.andela.bookmarkit.data.api.pojos;


import android.net.Uri;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
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

    private String getDateString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(date);
    }

    public Date getShortDate() {
        Date date = new Date();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            date = simpleDateFormat.parse(getDateString());
        } catch (ParseException e) {
            Log.e("getShortDate", e.getMessage());
        }
        return date;
    }

    public String getNormalDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE MM yyyy");
        return simpleDateFormat.format(date);
    }

    public String getHours() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh a");
        return simpleDateFormat.format(date);
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

package com.andela.bookmarkit.data.api.pojos;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {
    @SerializedName("list")
    private List<WeatherItem> weatherItemList;

    public ApiResponse(List<WeatherItem> weatherItemList) {
        this.weatherItemList = weatherItemList;
    }

    public List<WeatherItem> getWeatherItemList() {
        return weatherItemList;
    }

    public void setWeatherItemList(List<WeatherItem> weatherItemList) {
        this.weatherItemList = weatherItemList;
    }
}

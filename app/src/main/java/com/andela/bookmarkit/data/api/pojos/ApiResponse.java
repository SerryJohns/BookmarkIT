package com.andela.bookmarkit.data.api.pojos;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {
    @SerializedName("list")
    public List<WeatherItem> weatherItemList;

    public ApiResponse(List<WeatherItem> weatherItemList) {
        this.weatherItemList = weatherItemList;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "weatherItemList=" + weatherItemList +
                '}';
    }
}

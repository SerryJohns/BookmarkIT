package com.andela.bookmarkit.data.api;


import com.andela.bookmarkit.BuildConfig;
import com.andela.bookmarkit.data.api.pojos.ApiResponse;

import retrofit2.Call;

public class APIServiceImpl {
    private APIService apiService;
    private String API_KEY;

    public APIServiceImpl() {
        apiService = ApiClient.getInstance().create(APIService.class);
        API_KEY = BuildConfig.OPEN_WEATHER_API_KEY;
    }

    public Call<ApiResponse> getWeatherForecast(double lat, double lon) {
        return apiService.getWeatherForecast(lat, lon, API_KEY);
    }
}

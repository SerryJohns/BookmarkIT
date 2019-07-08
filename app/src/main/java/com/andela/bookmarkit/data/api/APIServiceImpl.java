package com.andela.bookmarkit.data.api;


import com.andela.bookmarkit.BuildConfig;
import com.andela.bookmarkit.data.api.pojos.ApiResponse;

import retrofit2.Call;

public class APIServiceImpl {
    private APIService apiService;

    public APIServiceImpl() {
        apiService = ApiClient.getInstance().create(APIService.class);
    }

    public Call<ApiResponse> getWeatherForecast(double lat, double lon) {
        String apiKey = BuildConfig.OPEN_WEATHER_API_KEY;
        return apiService.getWeatherForecast(lat, lon, apiKey);
    }
}

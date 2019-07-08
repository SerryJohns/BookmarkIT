package com.andela.bookmarkit.data.api;


import com.andela.bookmarkit.data.api.pojos.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("forecast")
    Call<ApiResponse> getWeatherForecast(@Query("lat") double latitude,
                                         @Query("lon") double longitude,
                                         @Query("appid") String apiKey);
}

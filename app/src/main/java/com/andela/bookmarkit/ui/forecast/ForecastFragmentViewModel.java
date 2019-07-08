package com.andela.bookmarkit.ui.forecast;


import androidx.lifecycle.ViewModel;

import com.andela.bookmarkit.data.AppRepo;
import com.andela.bookmarkit.data.api.pojos.ApiResponse;

import retrofit2.Call;

public class ForecastFragmentViewModel extends ViewModel {
    private final AppRepo repo;

    public ForecastFragmentViewModel(AppRepo repo) {
        this.repo = repo;
    }

    public Call<ApiResponse> getWeatherForecast(double lat, double lon) {
        return repo.weatherApi.getWeatherForecast(lat, lon);
    }
}

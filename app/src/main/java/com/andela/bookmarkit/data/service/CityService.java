package com.andela.bookmarkit.data.service;


import androidx.lifecycle.LiveData;

import com.andela.bookmarkit.data.model.City;

import java.util.List;

public interface CityService {
    LiveData<City> getCityById(int cityId);

    LiveData<City> getCityByCoordinates(double latitude, double longitude);

    LiveData<List<City>> getCities();

    void createCities(City... cities);

    void updateCity(String description, int cityId);
}

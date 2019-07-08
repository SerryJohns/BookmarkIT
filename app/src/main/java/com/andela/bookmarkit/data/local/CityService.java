package com.andela.bookmarkit.data.local;


import androidx.lifecycle.LiveData;

import com.andela.bookmarkit.data.local.City;

import java.util.List;

public interface CityService {
    LiveData<City> getCityById(int cityId);

    LiveData<City> getCityByCoordinates(double latitude, double longitude);

    LiveData<List<City>> getCities();

    LiveData<List<City>> searchCityByName(String query);

    void createCities(City... cities);

    void updateCity(String description, int cityId);
}

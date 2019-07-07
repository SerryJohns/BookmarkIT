package com.andela.bookmarkit.data.service;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.andela.bookmarkit.data.AppDatabase;
import com.andela.bookmarkit.data.dao.CityDao;
import com.andela.bookmarkit.data.model.City;

import java.util.List;

public class CityServiceImpl implements CityService {
    private CityDao cityDao;

    public CityServiceImpl(Context context) {
        this.cityDao = AppDatabase.getInstance(context).cityDao();
    }

    @Override
    public LiveData<City> getCityById(int cityId) {
        return cityDao.getCityById(cityId);
    }

    @Override
    public LiveData<City> getCityByCoordinates(double latitude, double longitude) {
        return cityDao.getCityByCoordinates(latitude, longitude);
    }

    @Override
    public LiveData<List<City>> getCities() {
        return cityDao.getCities();
    }

    @Override
    public void createCities(City... cities) {
        cityDao.createCities(cities);
    }
}

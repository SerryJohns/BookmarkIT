package com.andela.bookmarkit.ui.cities.details;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.andela.bookmarkit.data.AppRepo;
import com.andela.bookmarkit.data.local.City;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CityDetailsFragmentViewModel extends ViewModel {
    private final AppRepo repo;

    public CityDetailsFragmentViewModel(AppRepo repo) {
        this.repo = repo;
    }

    public void createCity(final City city) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                repo.localStore.createCities(city);
            }
        });
    }

    public LiveData<City> getCityByCoordinates(double latitude, double longitude) {
        return repo.localStore.getCityByCoordinates(latitude, longitude);
    }

    public void updateCity(final String description, final int cityId) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                repo.localStore.updateCity(description, cityId);
            }
        });
    }
}

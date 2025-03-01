package com.andela.bookmarkit.ui.cities;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.andela.bookmarkit.data.AppRepo;
import com.andela.bookmarkit.data.local.City;

import java.util.List;

public class CitiesFragmentViewModel extends ViewModel {
    private final AppRepo repo;

    public CitiesFragmentViewModel(AppRepo repo) {
        this.repo = repo;
    }

    public LiveData<List<City>> getCities() {
        return repo.localStore.getCities();
    }

    public LiveData<List<City>> searchCityByName(String query) {
        return repo.localStore.searchCityByName(query);
    }
}

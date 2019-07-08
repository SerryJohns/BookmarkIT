package com.andela.bookmarkit.ui.base;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.andela.bookmarkit.data.AppRepo;
import com.andela.bookmarkit.data.api.APIServiceImpl;
import com.andela.bookmarkit.data.local.CityServiceImpl;
import com.andela.bookmarkit.ui.cities.CitiesFragmentViewModel;
import com.andela.bookmarkit.ui.cities.details.CityDetailsFragmentViewModel;

public class CustomViewModelFactory implements ViewModelProvider.Factory {
    private AppRepo repo;

    public CustomViewModelFactory(Context context) {
        CityServiceImpl cityService = new CityServiceImpl(context);
        APIServiceImpl apiService = new APIServiceImpl();
        repo = new AppRepo(cityService, apiService);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CityDetailsFragmentViewModel.class)) {
            return (T) new CityDetailsFragmentViewModel(repo);
        } else if (modelClass.isAssignableFrom(CitiesFragmentViewModel.class)) {
            return (T) new CitiesFragmentViewModel(repo);
        } else {
            throw new IllegalArgumentException("View Model Not Found!");
        }
    }
}

package com.andela.bookmarkit.ui.cities;


import androidx.lifecycle.ViewModel;

import com.andela.bookmarkit.data.AppRepo;

public class CityDetailsFragmentViewModel extends ViewModel {
    private AppRepo repo;

    public CityDetailsFragmentViewModel(AppRepo repo) {
        this.repo = repo;
    }
}

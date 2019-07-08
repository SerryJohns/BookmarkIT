package com.andela.bookmarkit.ui.base;

import com.andela.bookmarkit.data.model.City;

public interface FragmentSwitcherInterface {
    void removeFragment(String tag);
    void showMapFragment();
    void showDetailsFragment(City city);
    void showCitiesFragment();
}

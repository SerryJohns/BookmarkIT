package com.andela.bookmarkit.ui.base;

import com.andela.bookmarkit.data.local.City;

public interface FragmentSwitcherInterface {
    void removeFragment(String tag);
    void showMapFragment();
    void showDetailsFragment(City city);
    void showCitiesFragment(String query);
}

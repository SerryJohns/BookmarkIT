package com.andela.bookmarkit.ui.cities;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.andela.bookmarkit.MainActivity;
import com.andela.bookmarkit.R;
import com.andela.bookmarkit.data.model.City;
import com.andela.bookmarkit.ui.base.BaseFragment;

import java.util.Date;


public class CityDetailsFragment extends BaseFragment {
    private static final String CITY_NAME = "CITY_NAME";
    private static final String CITY_ADDRESS = "CITY_ADDRESS";
    private static final String CITY_LATITUDE = "CITY_LATITUDE";
    private static final String CITY_LONGITUDE = "CITY_LONGITUDE";
    private static final String CITY_ID = "CITY_ID";

    private City currentCity;
    private Toolbar toolbar;

    public CityDetailsFragment() {
        // Required empty public constructor
    }

    public static CityDetailsFragment newInstance(City city) {
        Bundle bundle = new Bundle();
        bundle.putString(CITY_NAME, city.getName());
        bundle.putDouble(CITY_LATITUDE, city.getLatitude());
        bundle.putDouble(CITY_LONGITUDE, city.getLatitude());
        bundle.putInt(CITY_ID, city.getId());

        CityDetailsFragment fragment = new CityDetailsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        if (getArguments() != null) {
            Bundle args = getArguments();
            String cityName = args.getString(CITY_NAME);
            String address = args.getString(CITY_ADDRESS);
            double latitude = args.getDouble(CITY_LATITUDE);
            double longitude = args.getDouble(CITY_LONGITUDE);
            int cityId = args.getInt(CITY_ID);

            if (cityId > 0) {
                loadCityData(cityId);
            } else {
                currentCity = new City(new Date(), cityName, address, latitude, longitude);
            }

            updateAppBar(cityName);
        } else {
            showCitiesListFragment();
        }
    }

    private void updateAppBar(String cityName) {
        MainActivity activity = (MainActivity) getActivity();

        setHasOptionsMenu(true);
        toolbar.setTitle(cityName);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void loadCityData(int cityId) {
        // Load existing data in an editable edit ext
    }

    private void showCitiesListFragment() {
        MainActivity activity = (MainActivity) getActivity();
        activity.fragmentSwitcher.showCititesFragment();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}

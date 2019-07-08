package com.andela.bookmarkit.ui.cities.details;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.andela.bookmarkit.MainActivity;
import com.andela.bookmarkit.R;
import com.andela.bookmarkit.data.local.model.City;
import com.andela.bookmarkit.ui.base.BaseFragment;
import com.andela.bookmarkit.ui.base.CustomViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;


public class CityDetailsFragment extends BaseFragment {
    private static final String CITY_NAME = "CITY_NAME";
    private static final String CITY_ADDRESS = "CITY_ADDRESS";
    private static final String CITY_LATITUDE = "CITY_LATITUDE";
    private static final String CITY_LONGITUDE = "CITY_LONGITUDE";
    private static final String CITY_ID = "CITY_ID";

    private City currentCity;
    private Toolbar toolbar;
    private boolean isNewEntry = false;
    private boolean updateEntry = false;

    private TextInputEditText txtCityDesc;
    private FloatingActionButton fabSave;
    private CityDetailsFragmentViewModel viewModel;
    private CustomViewModelFactory viewModelFactory;

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
        viewModelFactory = new CustomViewModelFactory(getContext());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CityDetailsFragmentViewModel.class);

        initViews();
        initListeners();
        getCityData();
    }

    private void initViews() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        txtCityDesc = (TextInputEditText) getActivity().findViewById(R.id.txt_city_desc);
        fabSave = (FloatingActionButton) getActivity().findViewById(R.id.fab_add);
    }

    private void initListeners() {
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityDesc = txtCityDesc.getText() != null ? txtCityDesc.getText().toString() : "";
                currentCity.setDescription(cityDesc);

                if (!isNewEntry && !updateEntry) {
                    // Mark entry as updatable so that we don't create a new city entry in the db
                    updateEntry = true;

                    // Toggle fab icon to a save icon when the user starts to edit a city
                    updateFabIcon(true);

                    // Enable Text input edit text so that a user can update the description
                    txtCityDesc.setEnabled(true);
                } else if (!isNewEntry) {
                    // Update city entry
                    updateCityDescription(cityDesc);
                } else {
                    // Create new city entry
                    saveCity(currentCity);
                }
            }
        });
    }

    private void getCityData() {
        if (getArguments() != null) {
            Bundle args = getArguments();
            final String cityName = args.getString(CITY_NAME);
            final String address = args.getString(CITY_ADDRESS);
            final double latitude = args.getDouble(CITY_LATITUDE);
            final double longitude = args.getDouble(CITY_LONGITUDE);
            updateAppBar(cityName);

            viewModel.getCityByCoordinates(latitude, longitude).observe(this, new Observer<City>() {
                @Override
                public void onChanged(City city) {
                    if (city != null) {
                        currentCity = city;
                        isNewEntry = false;
                    } else {
                        isNewEntry = true;
                        currentCity = new City(new Date(), cityName, address, latitude, longitude);
                        isNewEntry = true;
                    }
                    updateFabIcon(isNewEntry);
                    updateUI(currentCity);
                }
            });
        } else {
            getActivity().onBackPressed();
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

    private void updateFabIcon(boolean status) {
        Drawable icon;
        if (status) {
            icon = getResources().getDrawable(R.drawable.ic_save_black_24dp);
        } else {
            icon = getResources().getDrawable(R.drawable.ic_edit_black_24dp);
        }
        fabSave.setImageDrawable(icon);
    }

    private void updateUI(City currentCity) {
        txtCityDesc.setText(currentCity.getDescription());
        if (isNewEntry) {
            txtCityDesc.setEnabled(true);
        } else {
            txtCityDesc.setEnabled(false);
        }
    }

    private void showCitiesListFragment() {
        MainActivity activity = (MainActivity) getActivity();
        activity.fragmentSwitcher.removeFragment(getTag());
        activity.fragmentSwitcher.showCitiesFragment();
    }

    private void saveCity(City currentCity) {
        String cityDesc = txtCityDesc.getText().toString();

        if (validateEntry(cityDesc)) {
            viewModel.createCity(currentCity);
            Toast.makeText(getContext(), getString(R.string.msg_city_saved), Toast.LENGTH_SHORT).show();
            showCitiesListFragment();
        }
    }

    private void updateCityDescription(String desc) {
        if (validateEntry(desc)) {
            // Make edit text view editable again
            updateEntry = false;

            viewModel.updateCity(desc, currentCity.getId());
            Toast.makeText(getContext(), getString(R.string.msg_city_updated), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateEntry(String str) {
        TextInputLayout textInputLayout = (TextInputLayout) txtCityDesc.getParent().getParent();
        textInputLayout.setError(null);
        if (str.matches("")) {
            textInputLayout.setError(getString(R.string.err_description_required));
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}

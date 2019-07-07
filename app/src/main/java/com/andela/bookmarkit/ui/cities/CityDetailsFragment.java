package com.andela.bookmarkit.ui.cities;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andela.bookmarkit.R;
import com.andela.bookmarkit.data.model.City;
import com.andela.bookmarkit.ui.base.BaseFragment;


public class CityDetailsFragment extends BaseFragment {


    public CityDetailsFragment() {
        // Required empty public constructor
    }

    public static CityDetailsFragment newInstance(City city) {
        Bundle bundle = new Bundle();
//        bundle.putString(CITY_NAME, city.getName());
        CityDetailsFragment fragment = new CityDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_details, container, false);
    }

}

package com.andela.bookmarkit.ui.cities;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andela.bookmarkit.R;
import com.andela.bookmarkit.ui.base.BaseFragment;


public class CityDetailsFragment extends BaseFragment {


    public CityDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_details, container, false);
    }

}

package com.andela.bookmarkit.ui.cities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.andela.bookmarkit.R;


public class CitiesFragment extends Fragment {

    public CitiesFragment() {
        // Required empty public constructor
    }

    public static CitiesFragment newInstance(String param1, String param2) {
        return new CitiesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cities, container, false);
    }
}

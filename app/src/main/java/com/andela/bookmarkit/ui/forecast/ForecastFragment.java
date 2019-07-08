package com.andela.bookmarkit.ui.forecast;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.andela.bookmarkit.R;
import com.andela.bookmarkit.data.api.pojos.ApiResponse;
import com.andela.bookmarkit.data.local.City;
import com.andela.bookmarkit.ui.base.BaseFragment;
import com.andela.bookmarkit.ui.base.CustomViewModelFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForecastFragment extends BaseFragment {

    private static final String CITY_NAME = "CITY_NAME";
    private static final String CITY_LATITUDE = "CITY_LATITUDE";
    private static final String CITY_LONGITUDE = "CITY_LONGITUDE";
    private static final String CITY_ADDRESS = "CITY_ADDRESS";

    private ForecastFragmentViewModel viewModel;
    private CustomViewModelFactory viewModelFactory;
    private String cityName;
    private String cityAddress;
    private double latitude;
    private double longitude;

    private ImageView imgWeatherIcon;
    private TextView txtCityName;
    private TextView txtDate;
    private TextView txtTemperature;
    private TextView txtMoreInfo;
    private RecyclerView hourlyFocusRecyclerView;
    private RecyclerView daysRecyclerView;

    public ForecastFragment() {
        // Required empty public constructor
    }

    public static ForecastFragment newInstance(City city) {
        Bundle bundle = new Bundle();
        bundle.putString(CITY_NAME, city.getName());
        bundle.putDouble(CITY_LATITUDE, city.getLatitude());
        bundle.putDouble(CITY_LONGITUDE, city.getLatitude());
        bundle.putString(CITY_ADDRESS, city.getAddress());

        ForecastFragment fragment = new ForecastFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModelFactory = new CustomViewModelFactory(getContext());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ForecastFragmentViewModel.class);

        initViews();
        getCityData();
    }

    private void initViews() {
        imgWeatherIcon = (ImageView) getActivity().findViewById(R.id.weather_icon);
        txtCityName = (TextView) getActivity().findViewById(R.id.txt_city_name);
        txtDate = (TextView) getActivity().findViewById(R.id.txt_date);
        txtTemperature = (TextView) getActivity().findViewById(R.id.txt_temperature);
        txtMoreInfo = (TextView) getActivity().findViewById(R.id.txt_more_info);
        hourlyFocusRecyclerView = (RecyclerView) getActivity().findViewById(R.id.hourly_focus_recycler_view);
        daysRecyclerView = (RecyclerView) getActivity().findViewById(R.id.days_recycler_view);
    }

    private void getCityData() {
        Bundle args = getArguments();
        if (getArguments() != null) {
            cityName = args.getString(CITY_NAME);
            cityAddress = args.getString(CITY_ADDRESS);
            latitude = args.getDouble(CITY_LATITUDE);
            longitude = args.getDouble(CITY_LONGITUDE);

            loadWeatherInformation(cityName, latitude, longitude);
        } else {
            getActivity().onBackPressed();
        }
    }

    private void loadWeatherInformation(String cityName, double latitude, double longitude) {
        viewModel.getWeatherForecast(latitude, latitude).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("", "onResponse: Weather response:" + response);
                if (response.isSuccessful()) {
                    // TODO: Handle Results
                    Log.d("Weather results: ", response.body().toString());
                } else {
                    // TODO: Show error
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // TODO: Handle Error
            }
        });
    }

    private void toggleLoader(boolean status) {
        // TODO: Toggle fullscreen loader
    }

}

package com.andela.bookmarkit.ui.forecast;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.andela.bookmarkit.MainActivity;
import com.andela.bookmarkit.R;
import com.andela.bookmarkit.data.api.pojos.ApiResponse;
import com.andela.bookmarkit.data.api.pojos.WeatherItem;
import com.andela.bookmarkit.data.local.City;
import com.andela.bookmarkit.ui.base.BaseFragment;
import com.andela.bookmarkit.ui.base.CustomViewModelFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForecastFragment extends BaseFragment {

    private static final String CITY_NAME = "CITY_NAME";
    private static final String CITY_LATITUDE = "CITY_LATITUDE";
    private static final String CITY_LONGITUDE = "CITY_LONGITUDE";
    private static final String CITY_ADDRESS = "CITY_ADDRESS";
    private static final String TAG = ForecastFragment.class.getSimpleName();

    private ForecastFragmentViewModel viewModel;
    private CustomViewModelFactory viewModelFactory;
    private String cityName;

    private ImageView imgWeatherIcon;
    private TextView txtCityName;
    private TextView txtDate;
    private TextView txtTemperature;
    private TextView txtMoreInfo;
    private Toolbar toolbar;

    private RecyclerView hourlyFocusRecyclerView;
    private RecyclerView daysRecyclerView;
    private RelativeLayout fullScreenLoader;
    private HourlyForecastAdapter hourlyForecastAdapter;
    private DaysAdapter daysAdapter;
    private City currentCity;

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

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        fullScreenLoader = (RelativeLayout) getActivity().findViewById(R.id.full_screen_loader);

        hourlyFocusRecyclerView = (RecyclerView) getActivity().findViewById(R.id.hourly_focus_recycler_view);
        daysRecyclerView = (RecyclerView) getActivity().findViewById(R.id.days_recycler_view);

        hourlyForecastAdapter = new HourlyForecastAdapter();
        hourlyFocusRecyclerView.setAdapter(hourlyForecastAdapter);

        daysAdapter = new DaysAdapter(onItemClickListener);
        daysRecyclerView.setAdapter(daysAdapter);

        updateAppBar();

        txtMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.fragmentSwitcher.showDetailsFragment(currentCity);
            }
        });
    }

    private void updateAppBar() {
        MainActivity activity = (MainActivity) getActivity();
        setHasOptionsMenu(true);
        if (toolbar != null) {
            toolbar.setTitle(R.string.title_weather_forecast);
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private DaysAdapter.OnItemClickListener onItemClickListener = new DaysAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Date date) {
            List<WeatherItem> weatherItems = viewModel.getWeatherData().get(date);
            hourlyForecastAdapter.updateData(weatherItems);
            updateWeatherUI(weatherItems.get(0));
        }
    };

    private void getCityData() {
        Bundle args = getArguments();
        if (getArguments() != null) {
            cityName = args.getString(CITY_NAME);
            String cityAddress = args.getString(CITY_ADDRESS);
            double latitude = args.getDouble(CITY_LATITUDE);
            double longitude = args.getDouble(CITY_LONGITUDE);
            currentCity = new City(new Date(), cityName, cityAddress, latitude, longitude);

            loadWeatherInformation(cityName, latitude, longitude);
        } else {
            getActivity().onBackPressed();
        }
    }

    private void loadWeatherInformation(String cityName, double latitude, double longitude) {
        toggleLoader(true);

        viewModel.getWeatherForecast(latitude, latitude).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                toggleLoader(false);

                if (response.isSuccessful() && response.body() != null) {
                    handleResults(response.body());
                } else {
                    Toast.makeText(getContext(), getString(R.string.err_generic_msg), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: loadWeather: " + t.getMessage());
                Toast.makeText(getContext(), getString(R.string.err_generic_msg), Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });
    }

    private void handleResults(ApiResponse response) {
        viewModel.processResults(response);

        if (viewModel.getWeatherData() != null && viewModel.getWeatherData().size() > 0) {
            WeatherItem firstEntry = viewModel.getWeatherData().pollFirstEntry().getValue().get(0);
            updateWeatherUI(firstEntry);
            Date firstDay = viewModel.getWeatherData().firstKey();
            List<WeatherItem> weatherItems = viewModel.getWeatherData().get(firstDay);
            hourlyForecastAdapter.updateData(weatherItems);
        }

        List<Date> dates = new ArrayList<>();

        for (Map.Entry entry : viewModel.getWeatherData().entrySet()) {
            dates.add((Date) entry.getKey());
        }

        daysAdapter.updateData(dates);
    }

    private void updateWeatherUI(WeatherItem weatherItem) {
        Resources res = getResources();
        txtCityName.setText(cityName);
        txtTemperature.setText(String.format(res.getString(R.string.temperature_string), weatherItem.temperatureInCelsius()));
        txtDate.setText(weatherItem.getNormalDate());
        Picasso.get().load(weatherItem.getIconURI()).into(imgWeatherIcon);
    }

    private void toggleLoader(boolean status) {
        if (status) {
            fullScreenLoader.setVisibility(View.VISIBLE);
        } else {
            fullScreenLoader.setVisibility(View.GONE);
        }
    }

}

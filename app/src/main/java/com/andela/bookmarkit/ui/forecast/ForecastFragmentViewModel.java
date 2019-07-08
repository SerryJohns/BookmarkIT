package com.andela.bookmarkit.ui.forecast;


import androidx.lifecycle.ViewModel;

import com.andela.bookmarkit.data.AppRepo;
import com.andela.bookmarkit.data.api.pojos.ApiResponse;
import com.andela.bookmarkit.data.api.pojos.WeatherItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;

public class ForecastFragmentViewModel extends ViewModel {
    private final AppRepo repo;
    // Tree map keeps the keys sorted
    private TreeMap<Date, List<WeatherItem>> weatherData = new TreeMap<>();

    public ForecastFragmentViewModel(AppRepo repo) {
        this.repo = repo;
    }

    public Call<ApiResponse> getWeatherForecast(double lat, double lon) {
        return repo.weatherApi.getWeatherForecast(lat, lon);
    }

    public void processResults(ApiResponse response) {
        weatherData.clear();
        for (WeatherItem weatherItem : response.weatherItemList) {
            Date shortDate = weatherItem.getShortDate();

            if (weatherData.containsKey(shortDate)) {
                weatherData.get(shortDate).add(weatherItem);
            } else {
                weatherData.put(shortDate, new ArrayList<WeatherItem>());
                weatherData.get(shortDate).add(weatherItem);
            }
        }

        // Sort ArrayList entries
        for (Map.Entry element : weatherData.entrySet()) {
            List<WeatherItem> weatherItems = (List<WeatherItem>) element.getValue();
            Collections.sort(weatherItems, weatherItemComparator);
        }
    }

    public TreeMap<Date, List<WeatherItem>> getWeatherData() {
        return weatherData;
    }

    private Comparator<WeatherItem> weatherItemComparator = new Comparator<WeatherItem>() {
        @Override
        public int compare(WeatherItem item1, WeatherItem item2) {
            return item1.date.compareTo(item2.date);
        }
    };

}

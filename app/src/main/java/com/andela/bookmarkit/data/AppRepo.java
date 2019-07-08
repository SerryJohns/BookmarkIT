package com.andela.bookmarkit.data;


import com.andela.bookmarkit.data.api.APIServiceImpl;
import com.andela.bookmarkit.data.local.CityServiceImpl;

public class AppRepo {
    public CityServiceImpl localStore;
    public APIServiceImpl weatherApi;

    public AppRepo(CityServiceImpl localStore, APIServiceImpl apiService) {
        this.localStore = localStore;
        this.weatherApi = apiService;
    }
}

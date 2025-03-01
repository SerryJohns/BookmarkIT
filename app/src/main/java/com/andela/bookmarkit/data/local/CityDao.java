package com.andela.bookmarkit.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CityDao {
    @Query("SELECT * FROM city WHERE id = :cityId")
    LiveData<City> getCityById(int cityId);

    @Query("SELECT * FROM city WHERE latitude = :latitude AND longitude = :longitude LIMIT 1")
    LiveData<City> getCityByCoordinates(double latitude, double longitude);

    @Query("SELECT * FROM city ORDER BY date_added DESC")
    LiveData<List<City>> getCities();

    @Query("UPDATE city SET description = :description WHERE id = :cityId")
    void updateCity(String description, int cityId);

    @Query("SELECT * FROM city WHERE name LIKE :query")
    LiveData<List<City>> searchCityByName(String query);

    @Insert(onConflict = REPLACE)
    void createCities(City... cities);
}

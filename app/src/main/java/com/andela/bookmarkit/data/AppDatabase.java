package com.andela.bookmarkit.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.andela.bookmarkit.data.dao.CityDao;
import com.andela.bookmarkit.data.model.City;


@Database(entities = {City.class}, version = 1)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "bookmark-it-db"
            ).build();
        }
        return appDatabase;
    }

    public abstract CityDao cityDao();
}

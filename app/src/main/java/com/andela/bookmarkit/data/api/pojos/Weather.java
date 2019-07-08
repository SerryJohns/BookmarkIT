package com.andela.bookmarkit.data.api.pojos;


import android.net.Uri;

import com.google.gson.annotations.SerializedName;


public class Weather {
    @SerializedName("icon")
    public String icon;

    public Weather(String icon) {
        this.icon = icon;
    }

    public Uri getIconURI() {
        String iconURIstring = String.format("https://openweathermap.org/img/wn/%s@2x.png", icon);
        return Uri.parse(iconURIstring);
    }

    @Override
    public String toString() {
        return "Weather{" +
                "icon='" + icon + '\'' +
                '}';
    }
}

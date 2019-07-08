package com.andela.bookmarkit.data.api.pojos;


import android.net.Uri;


public class Weather {
    public String icon;

    public Weather(String icon) {
        this.icon = icon;
    }

    public Uri getIconURI() {
        String iconURIstring = String.format("https://openweathermap.org/img/wn/%s@2x.png", icon);
        return Uri.parse(iconURIstring);
    }
}

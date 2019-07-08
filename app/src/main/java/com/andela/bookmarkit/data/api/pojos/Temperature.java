package com.andela.bookmarkit.data.api.pojos;


import com.google.gson.annotations.SerializedName;

public class Temperature {
    @SerializedName("temp")
    public Double tempInFahrenheit;

    public Temperature(Double tempInFahrenheit) {
        this.tempInFahrenheit = tempInFahrenheit;
    }

    public int tempInCelsius() {
        double celcius = (tempInFahrenheit - 32) * 5 / 9;
        return (int) Math.round(celcius);
    }
}

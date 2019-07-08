package com.andela.bookmarkit.data.api.pojos;


import com.google.gson.annotations.SerializedName;

public class Temperature {
    @SerializedName("temp")
    public Double tempInKelvin;

    public Temperature(Double tempInKelvin) {
        this.tempInKelvin = tempInKelvin;
    }

    public int tempInCelsius() {
        double celsius = tempInKelvin - 273.15;
        return (int) Math.round(celsius);
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "tempInKelvin=" + tempInKelvin +
                '}';
    }
}

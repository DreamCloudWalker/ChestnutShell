package com.dengjian.chestnutshell.network.api;

import com.google.gson.annotations.SerializedName;

public class DayWeather {
    @SerializedName("province")
    private String province;

    @SerializedName("city")
    private String city;

    @SerializedName("adcode")
    private String adcode;

    @SerializedName("weather")
    private String weather;

    @SerializedName("temperature")
    private String temperature;

    @SerializedName("winddirection")
    private String winddirection;

    @SerializedName("windpower")
    private String windpower;

    @SerializedName("humidity")
    private String humidity;

    @SerializedName("reporttime")
    private String reporttime;
}

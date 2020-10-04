package com.dengjian.chestnutshell.network.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Weather extends BaseResponse {
    @SerializedName("count")
    private String count;

    @SerializedName("infocode")
    private String infocode;

    @SerializedName("lives")
    private List<DayWeather> liveList = new ArrayList<>();
}

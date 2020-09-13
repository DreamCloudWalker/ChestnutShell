package com.dengjian.chestnutshell.network.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherInterface {
    @GET("/v3/weather/weatherInfo")
    Observable<Weather> getWeather(@Query("city") String city, @Query("key") String key);
}

package com.mapsense.weatherapp.network

import com.mapsense.weatherapp.network.weatherdataclass.WeatherDataClass
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") location: String,
        @Query("appid") apiKey: String): WeatherDataClass
}
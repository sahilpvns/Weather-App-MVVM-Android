package com.mapsense.weatherapp.network

import com.mapsense.weatherapp.network.weatherdataclass.WeatherDataClass
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun getWeatherByName(
        @Query("appid") apiKey: String,
        @Query("q") location: String?
    ): WeatherDataClass


    @GET("/data/2.5/weather")
    suspend fun getWeatherByLatLong(
        @Query("appid") apiKey: String,
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?): WeatherDataClass
}
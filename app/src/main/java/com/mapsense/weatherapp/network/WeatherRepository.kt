package com.mapsense.weatherapp.network

import com.mapsense.weatherapp.network.weatherdataclass.WeatherDataClass
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository {
    private val weatherService: WeatherService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(WeatherService::class.java)
    }

    suspend fun getWeather(location: String, apiKey: String): WeatherDataClass {
        return weatherService.getWeather(location, apiKey)
    }

}
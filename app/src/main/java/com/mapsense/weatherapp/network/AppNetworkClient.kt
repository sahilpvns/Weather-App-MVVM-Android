package com.mapsense.weatherapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppNetworkClient {
    private val BASE_URL by lazy { "http://api.openweathermap.org/" }
    private val client by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val mNetowrkRepo: WeatherService by lazy {
        client.create(WeatherService::class.java)
    }


}
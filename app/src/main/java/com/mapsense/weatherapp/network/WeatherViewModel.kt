package com.mapsense.weatherapp.network

import androidx.lifecycle.ViewModel
import com.mapsense.weatherapp.datasource.WeatherRepo

class WeatherViewModel : ViewModel() {

    val weatherRepo by lazy { WeatherRepo() }

    fun getWeatherDataByAddress(address: String?) = weatherRepo.getWeatherDataByAddress(address)
    fun getWeatherDataLatLong(lat: Double?, long: Double?) = weatherRepo.getWeatherDataLatLong(lat, long)

    fun observerWeatherData() = weatherRepo.observerWeatherData()
    fun observerNetworkError() = weatherRepo.observerNetworkError()


}
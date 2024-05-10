package com.mapsense.weatherapp.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mapsense.weatherapp.weatherdataclass.WeatherDataClass

class WeatherRepo : WeatherRepoInterface {
    private val weatherDataSource by lazy { WeatherDataSource() }
    override fun getWeatherDataByAddress(address: String?) {
        weatherDataSource.getWeatherByAddress(address)
    }

    override fun getWeatherDataLatLong(lat: Double?, long: Double?) {
        weatherDataSource.getWeatherByLatLong(lat, long)
    }


    override fun observerWeatherData(): LiveData<WeatherDataClass> {
        return weatherDataSource.observeWeatherData()
    }

    override fun observerNetworkError(): LiveData<String> {
        return weatherDataSource.observerError
    }


}


interface WeatherRepoInterface {

    fun getWeatherDataByAddress(address: String?)
    fun getWeatherDataLatLong(lat: Double?, long: Double?)

    fun observerWeatherData(): LiveData<WeatherDataClass>
    fun observerNetworkError(): LiveData<String>

}
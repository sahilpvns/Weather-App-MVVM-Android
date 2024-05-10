package com.mapsense.weatherapp.network.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mapsense.weatherapp.network.AppNetworkClient
import com.mapsense.weatherapp.network.weatherdataclass.WeatherDataClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherDataSource {
    val mRepo = AppNetworkClient().mNetowrkRepo
    val WEATHER_KEY by lazy { "5f9c9614cd931617e99eb08c2e6852b7" }


    private val _error = MutableLiveData<String>()
    val observerError: LiveData<String> = _error

    private val _weatherData = MutableLiveData<WeatherDataClass>()
    fun observeWeatherData(): LiveData<WeatherDataClass> = _weatherData

    fun getWeatherByAddress(location: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _weatherData.postValue(mRepo.getWeatherByName(WEATHER_KEY, location))
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    fun getWeatherByLatLong(lat: Double?, long: Double?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _weatherData.postValue(mRepo.getWeatherByLatLong(WEATHER_KEY, lat, long))
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}
package com.mapsense.weatherapp.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapsense.weatherapp.network.weatherdataclass.WeatherDataClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    private val _weatherData = MutableLiveData<WeatherDataClass>()
    val weatherData: LiveData<WeatherDataClass> = _weatherData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getWeather(location: String, apiKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = repository.getWeather(location, apiKey)
                withContext(Dispatchers.Main) {
                    _weatherData.value = data
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message
                }
            }
        }
    }


}
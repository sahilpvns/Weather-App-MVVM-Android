package com.mapsense.weatherapp

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.mapsense.weatherapp.databinding.ActivityMainBinding
import com.mapsense.weatherapp.network.WeatherViewModel

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private lateinit var mapView: MapView
    private var binding: ActivityMainBinding? = null
    private lateinit var viewModel: WeatherViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        setContentView(view)

        val apiKey = getString(R.string.api_key)
        viewModelValue()
        searchLocation(apiKey)
        fetchWeatherData()
        errorHandling()
        mapViewData(savedInstanceState)
    }

    private fun mapViewData(savedInstanceState: Bundle?) {
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    private fun errorHandling() {
        viewModel.error.observe(this) { error ->
            Snackbar.make(binding!!.root, "Not Found Location: $error", Snackbar.LENGTH_SHORT).show()
            binding?.tvLocationNotFound?.visibility = View.VISIBLE
        }
    }

    private fun fetchWeatherData() {
        binding?.apply {
            viewModel.weatherData.observe(this@MainActivity) {
                tvCityName.text = it.name
                tvTemperature.text = "${it.main.temp}°C"
                tvWeatherDescription.text = it.weather[0].description
                addMarker(LatLng(it.coord.lat, it.coord.lon), it.name)
            }
        }


    }

    private fun searchLocation(apiKey: String) {
        binding?.apply {
            ivSearch.setOnClickListener {
                binding?.tvLocationNotFound?.visibility = View.GONE
                if (TextUtils.isEmpty(etSearchCity.text.toString())) {
                    Snackbar.make(binding!!.root, "Enter Location", Snackbar.LENGTH_SHORT).show()
                } else {
                    val cityName = etSearchCity.text.toString()
                    viewModel.getWeather(cityName, apiKey)
                }

            }
        }

    }

    private fun viewModelValue() {
        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

    }

    private fun addMarker(latLng: LatLng, address: String) {
        mMap?.apply {
            clear()
            addMarker(MarkerOptions().position(latLng).title(address))
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
        }
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()


    }
}
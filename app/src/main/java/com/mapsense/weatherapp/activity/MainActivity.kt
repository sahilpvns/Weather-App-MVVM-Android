package com.mapsense.weatherapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mapsense.weatherapp.R
import com.mapsense.weatherapp.databinding.ActivityMainBinding
import com.mapsense.weatherapp.network.WeatherViewModel

class MainActivity : BaseActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var binding: ActivityMainBinding? = null
    private lateinit var mapView: MapView
    private lateinit var viewModel: WeatherViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initViewModel()
        initLocation()
        initListener()
        fetchWeatherData()
        errorHandling()
        mapViewData(savedInstanceState)
        fetchCurrentLocation()
        retryLocation()
    }

    private fun retryLocation() {
        binding?.ivRetry?.setOnClickListener {
            fetchCurrentLocation()
            showError(" Current Location updated")
        }
    }


    private fun initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    private fun fetchCurrentLocation() {
        if (isLocationPermissionHave()) {
            getLastLocation()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_LOCATION_PERMISSION
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (isLocationPermissionHave()) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    viewModel.getWeatherDataLatLong(it.latitude, it.longitude)
                } else {
                    showError("Unable to retrieve location please turn on location")
                }
            }
        } else {
            showError("having not a permission")
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 100
    }

    private fun mapViewData(savedInstanceState: Bundle?) {
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    private fun errorHandling() {
        viewModel.observerNetworkError().observe(this) { error ->
            showError(error)
        }
    }

    private fun fetchWeatherData() {
        binding?.apply {
            viewModel.observerWeatherData().observe(this@MainActivity) {
                tvCityName.text = String.format("Current Address: " + it.name)
                tvTemperature.text = String.format("Temperature: " + "${it.main.temp}°C")
                tvWeatherDescription.text = String.format("Description: " + it.weather[0].description)
                tvFeelLike.text = String.format("feels like: " + it.main.feels_like.toString())
                tvCountry.text = String.format("Country Name: " + it.sys.country)
                addMarker(LatLng(it.coord.lat, it.coord.lon), it.name)
            }
        }
    }

    private fun initListener() {
        binding?.apply {
            ivSearch.setOnClickListener {
                tvLocationNotFound.visibility = View.GONE
                if (TextUtils.isEmpty(etSearchCity.text.toString())) {
                    binding?.tvLocationNotFound?.visibility = View.VISIBLE
                    showError("Please Enter Location")
                } else {
                    viewModel.getWeatherDataByAddress(etSearchCity.text.toString())
                }

            }
        }

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

    }

    private fun addMarker(latLng: LatLng, address: String) {
        mMap?.apply {
            clear()
            addMarker(MarkerOptions().position(latLng).title(address))
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
            uiSettings.isZoomControlsEnabled = true
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
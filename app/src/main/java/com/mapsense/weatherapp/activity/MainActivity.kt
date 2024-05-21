package com.mapsense.weatherapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.annotation.RequiresApi
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        }
    }


    private fun initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    private fun fetchCurrentLocation() {
        if (isLocationPermissionHave()) {
            getLastLocation()
        } else {
            requestPermissions(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_LOCATION_PERMISSION)
            alertTurnOnLocationPermission()
        }
    }

    private fun alertTurnOnLocationPermission() {
        MaterialAlertDialogBuilder(this)
            .setMessage("Require Location permission in device")
            .setPositiveButton("Continue") { dialog, which ->
                openLocationPermission()
            }
            .setNegativeButton("Not now") { dialog, which ->
            }
            .show()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (isLocationPermissionHave()) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    viewModel.getWeatherDataLatLong(it.latitude, it.longitude)
                } else {
                    alertTurnOnLocation()
                }
            }
        } else {
            showError("having not a permission")
        }
    }

    private fun alertTurnOnLocation() {
        MaterialAlertDialogBuilder(this)
            .setMessage("Please turn ON Location in device")
            .setPositiveButton("Turn on") { dialog, which ->
                openLocationSetting()
            }
            .setNegativeButton("Cancel") { dialog, which ->
            }
            .show()
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
        viewModel.observerNetworkError().observe(this) {
            binding?.tvLocationNotFound?.text = String.format("Please Enter valid location")
            binding?.tvLocationNotFound?.visibility = View.VISIBLE
            showError("$it Please Enter valid location")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchWeatherData() {
        binding?.apply {
            viewModel.observerWeatherData().observe(this@MainActivity) {

                val temp = it.main.temp - 273.25
                val temp_max = it.main.temp_max - 273.25
                val temp_min = it.main.temp_min - 273.25

                tvCityName.text = it.name
                tvTemperature.text = temp.toInt().toString().plus(".C")
                tvWeatherDescription.text = it.weather[0].description
                tvAdditionalInfo.text = String.format("Temp max: ${temp_max.toInt()}   |  Temp min: ${temp_min.toInt()} ")
                tvWinSpeed.text = String.format("Win Speed: ${it.wind.speed}  |  deg: ${it.wind.deg}")
                tvCountry.text = it.sys.country
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
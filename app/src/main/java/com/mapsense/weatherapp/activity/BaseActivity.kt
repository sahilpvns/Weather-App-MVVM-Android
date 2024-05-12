package com.mapsense.weatherapp.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    fun showError(message: String?, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(findViewById(android.R.id.content), message?:"Something went wrong", duration).show()
    }


    fun isLocationPermissionHave(): Boolean {
       return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
           this, Manifest.permission.ACCESS_COARSE_LOCATION
       ) == PackageManager.PERMISSION_GRANTED
    }

    fun openLocationPermission() {
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        })
    }
     fun openLocationSetting() {
         val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
         if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
             showError("GPS is enabled")
         } else {
             startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
         }
     }
}
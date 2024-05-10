package com.mapsense.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {


    fun showError(message: String?, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(findViewById(android.R.id.content), message?:"Something went wrong", duration).show()
    }


    fun isLocationPermissionHave(): Boolean {
       return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
           this, Manifest.permission.ACCESS_COARSE_LOCATION
       ) == PackageManager.PERMISSION_GRANTED
    }
}
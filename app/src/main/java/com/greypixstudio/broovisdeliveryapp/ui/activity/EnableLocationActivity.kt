package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.*
import com.google.android.gms.maps.SupportMapFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.EnableLocationActivityBinding
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity

class EnableLocationActivity : BaseActivity() {

    lateinit var binding: EnableLocationActivityBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enable_location)
        init()
    }

    fun init() {
        binding.enableLocationButton.setOnClickListener {
            enableLocationAndProceed()
        }
    }

    fun enableLocationAndProceed() {
        Dexter.withContext(this@EnableLocationActivity)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            openNextScreen()
                            fetchLocation()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    // Remember to invoke this method when the custom rationale is closed
                    // or just by default if you don't want to use any custom rationale.
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {
                Toast.makeText(this@EnableLocationActivity, it.name, Toast.LENGTH_SHORT).show()
            }
            .check()
    }


    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), permissionCode
            )
            return
        }
        //if (isLocationEnabled(this@EnableLocationActivity)) {
            try {
                fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(this@EnableLocationActivity)


                val locationRequest = LocationRequest.create().apply {
                    interval = 0 /// 15 minutes (key param, used for power blame)
                    fastestInterval = 0 // 60 seconds
                    priority = Priority.PRIORITY_HIGH_ACCURACY
                    maxWaitTime = 10 // 60 minutes
                    smallestDisplacement = 100f //meters
                }

                var locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        for (location in locationResult.locations) {
                            if (location != null) {

                            }
                        }
                    }
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } catch (e: Exception) {

                e.printStackTrace()
            }

    }
    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    private fun openNextScreen() {
        val activityIntent = Intent(this@EnableLocationActivity, UseCurrentAddressActivity::class.java)
        activityIntent.putExtra("type", 1)
        startActivity(activityIntent)
        overridePendingTransition(0, 0)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
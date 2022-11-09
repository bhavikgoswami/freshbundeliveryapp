package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.UseCurrentAddressActivityBinding
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.AddressData
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import com.greypixstudio.broovisdeliveryapp.utils.Utils
import com.greypixstudio.broovisdeliveryapp.viewmodel.uploaddocument.UploadDocumentViewModel
import io.paperdb.Paper
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.util.*

class UseCurrentAddressActivity : BaseActivity(), OnMapReadyCallback , GoogleMap.OnMapClickListener{

    private var permanentAddress: Boolean = false
    private var addresses: List<Address?>? = ArrayList<Address>()
    private lateinit var binding: UseCurrentAddressActivityBinding
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    private var latitude = 0.0
    private var longitude = 0.0
    private var map: GoogleMap? = null
    var locationCallback: LocationCallback? = null

    private val uploadDocumentViewModel by viewModel<UploadDocumentViewModel>()

    private var type : Int = 1
    private var flat : String = ""
    private var area : String = ""
    private var address : String = ""
    private var landmark : String = ""
    private var state: String = ""
    private var city : String = ""
    private var zipCode : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_use_current_address)


        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, Constants.API_KEY)
        }
        if (intent.hasExtra("type")) {
            type = intent.getIntExtra("type", 1)

        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this@UseCurrentAddressActivity)
        fetchLocation()

        showLocationSearchOptions()

        Dexter.withContext(this@UseCurrentAddressActivity)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    fetchLocation()
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
                Toast.makeText(this@UseCurrentAddressActivity, it.name, Toast.LENGTH_SHORT).show()
            }
            .check()

        binding.confirmLocationButton.setOnClickListener {

                if (latitude != null && longitude != null) {
                    val addressData = AddressData(address ,area,flat ,city,landmark ,latitude ,longitude ,state,zipCode)
                    if(type == 1){
                        Paper.book().write(Constants.CURRENT_ADDRESS_DATA,addressData)
                        val mIntent = Intent(this@UseCurrentAddressActivity, AddAddressActivity::class.java)
                        mIntent.putExtra("type",type)
                        startActivity(mIntent)
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    if (type == 2){
                        Paper.book().write(Constants.PERMANENT_ADDRESS_DATA,addressData)
                        val mIntent = Intent(this@UseCurrentAddressActivity, AddPermanetAddressActivity::class.java)
                        mIntent.putExtra("type",type)
                        startActivity(mIntent)
                        overridePendingTransition(0, 0)
                        finish()
                    }

                    if (type == 3){
                        Paper.book().write(Constants.CURRENT_ADDRESS_DATA,addressData)
                        val mIntent = Intent(this@UseCurrentAddressActivity, AddAddressActivity::class.java)
                        mIntent.putExtra("type",type)
                        startActivity(mIntent)
                        overridePendingTransition(0, 0)
                        finish()
                    }
                    if (type == 4){
                        Paper.book().write(Constants.PERMANENT_ADDRESS_DATA,addressData)
                        val mIntent = Intent(this@UseCurrentAddressActivity, AddPermanetAddressActivity::class.java)
                        mIntent.putExtra("type",type)
                        startActivity(mIntent)
                        overridePendingTransition(0, 0)
                        finish()
                    }

                }
        }

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
        if (isLocationEnabled(this@UseCurrentAddressActivity)) {
            try {
                val fusedLocationProviderClient: FusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(this@UseCurrentAddressActivity)

                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    if (it != null) {
                        latitude = it.latitude
                        longitude = it.longitude

                        val supportMapFragment =
                            (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)
                        supportMapFragment?.getMapAsync(this@UseCurrentAddressActivity)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val fusedLocationProviderClient: FusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(this@UseCurrentAddressActivity)


                val locationRequest = LocationRequest.create().apply {
                    interval = 0 /// 15 minutes (key param, used for power blame)
                    fastestInterval = 0 // 60 seconds
                    priority = Priority.PRIORITY_HIGH_ACCURACY
                    maxWaitTime = 10 // 60 minutes
                    smallestDisplacement = 100f //meters
                }

                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {

                        for (location in locationResult.locations) {
                            if (location != null) {
                                currentLocation = location
                                latitude = location.latitude
                                longitude = location.longitude

                                val supportMapFragment =
                                    (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)
                                supportMapFragment?.getMapAsync(this@UseCurrentAddressActivity)
                                fusedLocationProviderClient.removeLocationUpdates(
                                    locationCallback!!
                                )
                            }
                        }
                    }
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback!!,
                    Looper.getMainLooper()
                )
            }
        } else {
            onLocation(this)
        }
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    fun onLocation(context: Activity) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.lbl_location))
        builder.setMessage(getString(R.string.lbl_please_turn_on_location))
        builder.setPositiveButton(R.string.lbl_ok) { dialogInterface, _ ->
            dialogInterface.dismiss()
            fetchLocation()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)

        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun showLocationSearchOptions() {
        //binding.autocompleteFragmentContainer.visibility = View.VISIBLE

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment
        autocompleteFragment.view?.setBackgroundColor(Color.WHITE)

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
        )

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                println("Place: " + place.name + ", " + place.id)
                latitude = place.latLng.latitude
                longitude = place.latLng.longitude
                setLocation(latitude, longitude)
            }

            override fun onError(status: Status) {
                println("An error occurred: ${status.statusMessage}")
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap

        setLocation(latitude, longitude)
        var geo = Geocoder(this, Locale.getDefault())
        map?.setOnCameraIdleListener {
            val midLatLng: LatLng = googleMap?.cameraPosition!!.target
            println("latlong onMapClick" + midLatLng)
            if (midLatLng != null) {

                latitude = midLatLng.latitude
                longitude = midLatLng.longitude
                addresses = Utils.getAddressFromLocation(
                    this@UseCurrentAddressActivity,
                    latitude,
                    longitude
                )

                address = addresses!![0]?.getAddressLine(0).toString()
                city = addresses!![0]?.locality.toString()
                state = addresses!![0]?.adminArea.toString()
                zipCode = addresses!![0]?.postalCode.toString()
                flat = addresses!![0]?.featureName.toString()
                landmark = addresses!![0]?.subLocality.toString()
                area = addresses!![0]?.subAdminArea.toString()

                println(" address: strAddress:  $address")
                println(" address: strCity:  $city")
                println(" address: strState:  $state")
                println(" address: strPostalCode:  $zipCode")
                println(" address: strKnownName:  $flat")
                println(" address: subLocality:  $landmark")
                println(" address: subAdminArea:  $area")

                binding.addressTxtView.setText(address)
            }
        }
        map!!.setOnMapClickListener {
          try {
                if (geo == null)
                    geo = Geocoder(this, Locale.getDefault());
                var address: List<Address> =
                    geo.getFromLocation(it.latitude, it.longitude, 1) as List<Address>;
                if (address.isNotEmpty()) {
                    /*map!!.addMarker(
                        MarkerOptions().position(it).title(
                            "Name:" + address.get(0).getCountryName()
                                    + ". Address:" + address.get(0).getAddressLine(0)
                        )
                    );*/

                }
            } catch (ex: IOException) {
                if (ex != null)
                    Toast.makeText(
                        this,
                        "Error:" + ex.message.toString(),
                        Toast.LENGTH_LONG
                    ).show();
            }
        }

        /*map!!.setOnMarkerClickListener {
            latitude = it.position.latitude
            longitude = it.position.longitude

            setLocation(latitude, longitude)
            true
        }*/

    }


    private fun setLocation(addressLatitude: Double, addressLongitude: Double) {

        latitude = addressLatitude
        longitude = addressLongitude

        val latLng = LatLng(latitude, longitude)
//        val markerOptions = MarkerOptions().position(latLng).title("I am here!")
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
        map?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        addresses =
            Utils.getAddressFromLocation(this@UseCurrentAddressActivity, latitude, longitude)
//        googleMap?.addMarker(markerOptions)
        val strAddress = addresses!![0]?.getAddressLine(0)
        binding.addressTxtView.setText(strAddress)
    }

    private fun finishWithResult() {
        val intent = Intent().apply {
            putExtra(Constants.LATITUDE, latitude)
            putExtra(Constants.LONGITUDE, longitude)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
        overridePendingTransition(0, 0)
    }


    override fun onMapClick(latlng: LatLng) {
        println("latlong onMapClick" + latlng)
        if (latlng != null) {

            latitude = latlng.latitude
            longitude = latlng.longitude
            addresses = Utils.getAddressFromLocation(
                this@UseCurrentAddressActivity,
                latitude,
                longitude
            )

            address = addresses!![0]?.getAddressLine(0).toString()
            city = addresses!![0]?.locality.toString()
            state = addresses!![0]?.adminArea.toString()
            zipCode = addresses!![0]?.postalCode.toString()
            flat = addresses!![0]?.featureName.toString()
            landmark = addresses!![0]?.subLocality.toString()
            area = addresses!![0]?.subAdminArea.toString()

            println(" address: strAddress:  $address")
            println(" address: strCity:  $city")
            println(" address: strState:  $state")
            println(" address: strPostalCode:  $zipCode")
            println(" address: strKnownName:  $flat")
            println(" address: subLocality:  $landmark")
            println(" address: subAdminArea:  $area")

            binding.addressTxtView.setText(address)
        }

    }

}
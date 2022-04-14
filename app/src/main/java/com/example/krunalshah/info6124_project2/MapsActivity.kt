package com.example.krunalshah.info6124_project2

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.krunalshah.info6124_project2.databinding.ActivityMapsBinding
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.io.IOException
import java.lang.Exception
import java.util.*

class MapsActivity : AppCompatActivity() {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var mGoogleApiClient: GoogleApiClient? = null
    private val TAG = "MyMaps"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var loc: LatLng
    private lateinit var currentAddress: String

    private lateinit var shareButtona: Button
    private lateinit var fragmentContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transaction = supportFragmentManager.beginTransaction()
        val mapsFragment = MapsFragment()
        transaction.add(R.id.fragmentContainer, mapsFragment)
        transaction.commit()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

//        fragmentContainer = findViewById(R.id.fragmentContainer)

        shareButtona = findViewById<Button>(R.id.shareButton)
        shareButtona.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)

            currentAddress = MapsFragment().currentAddress

            dialogBuilder.setMessage(currentAddress)
                .setCancelable(true)
                .setPositiveButton("Email") { dialog, whichButton ->
                    run {
                        dialog.dismiss()

                        val transaction = supportFragmentManager.beginTransaction()
                        val emailFragment = EmailFragment()

                        val mBundle = Bundle()
                        mBundle.putString("currentAddress", currentAddress)
                        emailFragment.arguments = mBundle

                        transaction.replace(R.id.fragmentContainer, emailFragment)
                        transaction.addToBackStack("email")
                        transaction.commit()
                    }
                }
                .setNegativeButton("SMS") { dialog, whichButton ->
                    run {
                        dialog.dismiss()

                        val transaction = supportFragmentManager.beginTransaction()
                        val smsFragment = SmsFragment()

                        val mBundle = Bundle()
                        mBundle.putString("currentAddress", currentAddress)
                        smsFragment.arguments = mBundle

                        transaction.replace(R.id.fragmentContainer, smsFragment)
                        transaction.addToBackStack("sms")
                        transaction.commit()
                    }
                }
                .setNeutralButton("Cancel") { dialog, whichButton ->
                    run {
                        dialog.dismiss()
                    }
                }

            val shareAlert = dialogBuilder.create()
            shareAlert.setTitle("Share this Location")
            shareAlert.show()
        }
    }

//    private val locationPermissionRequest = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        when {
//            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
//                // Precise location access granted.
//                Log.i(TAG, "Fine Location accessed")
//                getCurrentLocation()
//            }
//            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
//                // Only approximate location access granted.
//                Log.i(TAG, "Course Location accessed")
//                getCurrentLocation()
//            }
//            else -> {
//                Log.i(TAG, "No location permissions")
//            }
//        }
//    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
////        val sydney = LatLng(-34.0, 151.0)
////        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//
//        mMap.uiSettings.setMyLocationButtonEnabled(true)
//        mMap.uiSettings.setZoomControlsEnabled(true)
//        mMap.setTrafficEnabled(true)
//    }

//    @SuppressLint("MissingPermission")
//    private fun getCurrentLocation() {
//        Log.i(TAG, "Getting current location")
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener(this) { lastLocation: Location? ->
//                // Got last known location. In some rare situations this can be null.
//                if (lastLocation != null) {
//                    loc = LatLng(lastLocation.latitude, lastLocation.longitude)
//                    Log.i(TAG, loc.toString())
//                    // Add a BLUE marker to current location and zoom
//                    // use reverse geocoding to get the current address at your location.
//                    mMap.addMarker(
//                        MarkerOptions().position(loc).icon(
//                            BitmapDescriptorFactory.defaultMarker(
//                                BitmapDescriptorFactory.HUE_RED
//                            )
//                        )
//                            .title(getAddress(loc))
//                            .snippet("Your location Lat:" + loc.latitude + ",Lng:" + loc.longitude)
//                    )
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
//                    // animate camera allows zoom
//                    mMap.animateCamera(
//                        CameraUpdateFactory.newLatLngZoom(
//                            loc,
//                            16f
//                        )
//                    )  //zoom in at 16f
//
//                    currentAddress = getAddress(loc)
//                }
//            }
//    }

//    private fun getAddress(loc: LatLng): String {
//        val geocoder = Geocoder(this, Locale.getDefault())
//        var addresses: List<Address>? = null
//        try {
//            addresses = geocoder.getFromLocation(loc!!.latitude, loc!!.longitude, 1)
//        } catch (e1: IOException) {
//            Log.e(TAG, getString(R.string.service), e1)
//        } catch (e2: IllegalArgumentException) {
//            Log.e(
//                TAG, getString(R.string.invalid_lat_long) + ". " +
//                        "Latitude = " + loc!!.latitude +
//                        ", Longitude = " +
//                        loc!!.longitude, e2
//            )
//        }
//        // If the reverse geocode returned an address
//        if (addresses != null) {
//            // Get the first address
//            val address = addresses[0]
//            val addressText = String.format(
//                "%s, %s, %s",
//                address.getAddressLine(0), // If there's a street address, add it
//                address.locality,                 // Locality is usually a city
//                address.countryName
//            )              // The country of the address
//            return addressText
//        } else {
//            Log.e(TAG, getString(R.string.no_address_found))
//            return ""
//        }
//    }

    fun onConnected(p0: Bundle?) {
        // when location services is ready to be called
        Log.i(TAG, "onConnected")
    }

    fun onConnectionSuspended(p0: Int) {
        // when it suspends location services
        Log.i(TAG, "onConnectionSuspended")
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient?.connect()
        }
    }

    override fun onStop() {
        mGoogleApiClient?.disconnect()
        super.onStop()
    }
}

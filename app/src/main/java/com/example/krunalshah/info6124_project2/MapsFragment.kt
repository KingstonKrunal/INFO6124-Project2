package com.example.krunalshah.info6124_project2

import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

class MapsFragment : Fragment() {

    private lateinit var mMap: GoogleMap

    private var mGoogleApiClient: GoogleApiClient? = null
    private val TAG = "MyMaps"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var loc: LatLng

    var currentAddress = ""

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap = googleMap

        mMap.uiSettings.setMyLocationButtonEnabled(true)
        mMap.uiSettings.setZoomControlsEnabled(true)
        mMap.setTrafficEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        fusedLocationClient = this.activity?.let {
            LocationServices.getFusedLocationProviderClient(
                it
            )
        }!!
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                Log.i(TAG, "Fine Location accessed")
                getCurrentLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                Log.i(TAG, "Course Location accessed")
                getCurrentLocation()
            }
            else -> {
                Log.i(TAG, "No location permissions")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        Log.i(TAG, "Getting current location")
        this.activity?.let {
            fusedLocationClient.lastLocation
                .addOnSuccessListener(it) { lastLocation: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if (lastLocation != null) {
                        loc = LatLng(lastLocation.latitude, lastLocation.longitude)

                        currentAddress = getAddress(loc)

                        Log.i(TAG, loc.toString())
                        // Add a BLUE marker to current location and zoom
                        // use reverse geocoding to get the current address at your location.
                        mMap.addMarker(
                            MarkerOptions().position(loc).icon(
                                BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_RED
                                )
                            )
                                .title(getAddress(loc))
                                .snippet("Your location Lat:" + loc.latitude + ",Lng:" + loc.longitude)
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
                        // animate camera allows zoom
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                loc,
                                16f
                            )
                        )  //zoom in at 16f
                    }
                }
        }
    }

    private fun getAddress(loc: LatLng): String {
        val geocoder = Geocoder(this.activity, Locale.getDefault())
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(loc!!.latitude, loc!!.longitude, 1)
        } catch (e1: IOException) {
            Log.e(TAG, getString(R.string.service), e1)
        } catch (e2: IllegalArgumentException) {
            Log.e(
                TAG, getString(R.string.invalid_lat_long) + ". " +
                        "Latitude = " + loc!!.latitude +
                        ", Longitude = " +
                        loc!!.longitude, e2
            )
        }
        // If the reverse geocode returned an address
        if (addresses != null) {
            // Get the first address
            val address = addresses[0]
            val addressText = String.format(
                "%s, %s, %s",
                address.getAddressLine(0), // If there's a street address, add it
                address.locality,                 // Locality is usually a city
                address.countryName
            )              // The country of the address
            return addressText
        } else {
            Log.e(TAG, getString(R.string.no_address_found))
            return ""
        }
    }
}
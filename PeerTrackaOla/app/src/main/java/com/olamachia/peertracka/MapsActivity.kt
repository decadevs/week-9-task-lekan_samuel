package com.olamachia.peertracka

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.olamachia.peertracka.data.LocationLogging

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val LOCATION_PERMISSION_REQUEST= 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        databaseRef = Firebase.database.reference

        databaseRef.addValueEventListener(logListener())

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        /**
         * Method requests permission to have access to location */
        getLocationAccess()
    }


    /**
     * Method fetching the location (lat and long) detail from firebase */


    private fun logListener() = object : ValueEventListener {

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(applicationContext, "Could not read from database", Toast.LENGTH_LONG).show()
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onDataChange(dataSnapshot: DataSnapshot) {

            /**
             * images for the map markers  */

            val pickupMarkerDrawable = resources.getDrawable(R.drawable.olalekanimage, null)
            val markerDrawable = resources.getDrawable(R.drawable.mapmarkersammy, null)

            if (dataSnapshot.exists()) {
                map.clear() // clearing the map object before the creation of a location to avoid duplication

                /**
                 * Fetching Olalekan and Samuel's location from the database */

                val locationlogging = dataSnapshot.child("Olalekanlocation").getValue(LocationLogging::class.java)
                var olalekanLat =locationlogging?.Latitude
                var olalekanLong=locationlogging?.Longitude

                val locationloggingTwo = dataSnapshot.child("Samuellocation").getValue(LocationLogging::class.java)
                var samuelLat = locationloggingTwo?.Latitude
                var samuelLong = locationloggingTwo?.Longitude


                /**
                 * Creating map markers for Olalekan and Samuel at their various locations */

                if (olalekanLat !=null  && olalekanLong != null) {
                    val olalekanLocation = LatLng(olalekanLat, olalekanLong)

                    val markerOptions = MarkerOptions().position(olalekanLocation)
                            .title("Olalekan")
                            .icon(BitmapDescriptorFactory.fromBitmap(pickupMarkerDrawable.toBitmap(pickupMarkerDrawable.intrinsicWidth,
                                pickupMarkerDrawable.intrinsicHeight, null)))

                    map.addMarker(markerOptions)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(olalekanLocation, 15f))

                    Toast.makeText(applicationContext, "Locations accessed from the database", Toast.LENGTH_LONG).show()
                }

                if (samuelLat !=null  && samuelLong != null) {
                    val samuelLocation = LatLng(samuelLat, samuelLong)

                    val markerOptions = MarkerOptions().position(samuelLocation)
                        .title("Samuel")
                        .icon(BitmapDescriptorFactory.fromBitmap(markerDrawable.toBitmap(markerDrawable.intrinsicWidth,
                            markerDrawable.intrinsicHeight, null)))

                    map.addMarker(markerOptions)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(samuelLocation, 15f))

                    Toast.makeText(applicationContext, "Locations accessed from the database", Toast.LENGTH_LONG).show()
                }

            }
        }
    }


    /**
     * Method to request location access
     * If permission is granted, getLocationUpdate and StartLocationUpdate */


    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
            getLocationUpdates()
            startLocationUpdates()
        }
        else
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                map.isMyLocationEnabled = true
            }
            else {
                Toast.makeText(this, "User has not granted location access permission", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    /**
     * Method to get location update */

    private fun getLocationUpdates() {
        locationRequest = LocationRequest()
        locationRequest.interval = 30000
        locationRequest.fastestInterval = 20000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation

                    val locationlogging = LocationLogging(location.latitude, location.longitude)

                    databaseRef.child("Olalekanlocation").setValue(locationlogging)
                        .addOnSuccessListener {
                        Toast.makeText(applicationContext, "Locations written into the database", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {
                        Toast.makeText(applicationContext, "Error occured while writing the locations", Toast.LENGTH_LONG).show()
                    }

                }
            }
        }

    }

    /**
     * Method to start location update */

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return
        }

        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
        )
    }


}
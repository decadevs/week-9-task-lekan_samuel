package com.byoyedele.peertracka

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val LOCATION_PERMISSION_REQUEST = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            getLocationUpdates()
        }
        else
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
    }

                /*
                *  This is the
                */

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                mMap.isMyLocationEnabled = true
            }
            else {
                Toast.makeText(this, "User has not granted location access permission", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        /* Obtain the SupportMapFragment and get notified when the map is ready to be used. */

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }


    private fun getLocationUpdates() {
        /*
         *  This is the function that gets the actual location in Latitude and Longitude and then sends it to Firebase.
         */
        locationRequest = LocationRequest()
        locationRequest.interval = 2000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation

                    val databaseRef: DatabaseReference = Firebase.database.reference
                    databaseRef.addValueEventListener(logListener)
                    val locationlogging = LocationLogging(location.latitude, location.longitude)
                    databaseRef.child("Samuellocation").setValue(locationlogging)
                            .addOnSuccessListener {
//                                Toast.makeText(applicationContext, "Locations written into the database", Toast.LENGTH_LONG).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(applicationContext, "Error occured while writing the locations", Toast.LENGTH_LONG).show()
                            }
                }
            }
        }
    }

    private fun startLocationUpdates() {
        /*
         *  This function will get the location request after confirming Permission has been granted.
         */

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
        )
    }


    val logListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(applicationContext, "Could not read from database", Toast.LENGTH_LONG).show()
        }

        //     @SuppressLint("LongLogTag")
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                /*
                *  For there not to be recreation of different map markers whenever there is a data update, we need to clear the map whenever we are
                * creating a new marker to reflect an update.
                 */
                mMap.clear()

                /*
                *  The location of my partner will be gotten from Firebase onDataChange using the path already set.
                 */
                val locationlogging = dataSnapshot.child("Olalekanlocation").getValue(LocationLogging::class.java)
                val lekanLat=locationlogging?.Latitude
                val lekanLong=locationlogging?.Longitude

                if (lekanLat !=null  && lekanLong != null) {
                    val markerDrawable = resources.getDrawable(R.drawable.olalekanimage, null)
                    val lekanLoc = LatLng(lekanLat, lekanLong)
                    val markerOptions = MarkerOptions().position(lekanLoc).title("Olalekan")
                        .icon(BitmapDescriptorFactory.fromBitmap(markerDrawable.toBitmap(markerDrawable.intrinsicWidth,
                            markerDrawable.intrinsicHeight, null)))
                    mMap.addMarker(markerOptions)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lekanLoc, 20f))
                }

                /*
                * My own location will also be gotten from the database after being sent to the Firebase database.
                *
                 */

                val samuellogging = dataSnapshot.child("Samuellocation").getValue(LocationLogging::class.java)
                if (samuellogging != null) {
                    val markerDrawable = resources.getDrawable(R.drawable.mapmarkersammy, null)
                    val latLng = LatLng(samuellogging.Latitude!!, samuellogging.Longitude!!)
                    val markerOptions = MarkerOptions().position(latLng).title("Samuel")
                        .icon(BitmapDescriptorFactory.fromBitmap(markerDrawable.toBitmap(markerDrawable.intrinsicWidth,
                        markerDrawable.intrinsicHeight, null)))
                    mMap.addMarker(markerOptions)
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     *
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLocationAccess()
        startLocationUpdates()
    }
}
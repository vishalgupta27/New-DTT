package com.example.dtt


import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*



class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var currentLocation: Location? = null
    private var markers: ArrayList<Marker> = ArrayList()
    private var currentLocationMarker: Marker? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
              mapFragment.getMapAsync(this)

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Create location request for location updates
        locationRequest = create()
            .setPriority(PRIORITY_HIGH_ACCURACY)
            .setInterval(5000)
            .setFastestInterval(2000)

        // Create location callback for location updates
        locationCallback =  object :LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.let {
                    val location = it.lastLocation
                    updateCurrentLocationMarker(location!!)
                }
            }
        }
    }



/*  // For a Particular Location
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }*/


   override fun onMapReady(googleMap: GoogleMap) {
       mMap = googleMap
       mMap.isBuildingsEnabled = true

       // Check for location permission
       if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
           == PackageManager.PERMISSION_GRANTED)
       {
           // If permission is granted, set My Location enabled and move camera to current location
           mMap.isMyLocationEnabled = true
           mMap.uiSettings.isMyLocationButtonEnabled = true
           mMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
           startLocationUpdates()
           // If permission is granted, get the user's current location
           mMap.isMyLocationEnabled = true
           fusedLocationClient.lastLocation
               .addOnSuccessListener { location: Location? ->
                   location?.let {
                       currentLocation = location
                       if (currentLocation != null) {
                           // Get current location's latitude and longitude
                           val currentLatLng = LatLng(location.latitude, location.longitude)

                           // Add a marker to the map
                          // mMap.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))

                           /* // ADD Circle to the Map
                       mMap.addCircle( CircleOptions()
                           .center (currentLatLng)
                           .radius (1000.0)
                           .fillColor (Color.GREEN)
                           .strokeColor (Color.DKGRAY))

                       // ADD Polygon to the Map
                       mMap.addPolygon (PolygonOptions()
                           .add( LatLng ( 26.2389,  73.0243),
                                 LatLng ( 26.2389,  74.0243),
                                 LatLng ( 27.2389,  74.0243),
                                 LatLng ( 27.2389,  73.0243),
                                 LatLng ( 26.2389,  73.0243))
                           .fillColor (Color.YELLOW)
                           .strokeColor (Color.BLUE))

                         //ADD Image Overlay to the Map
                       mMap.addGroundOverlay ( GroundOverlayOptions()
                           .position(currentLatLng, 1000f,  1000f)
                           .image(BitmapDescriptorFactory.fromResource(R.drawable.add_photo))
                           .clickable(true))*/


//                           addMarkers()
                           // Move the camera to the current location
                           mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                           mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))
                       }
                   }
               }
       } else {
           // If permission is not granted, request for location permission
           ActivityCompat.requestPermissions(
               this,
               arrayOf(ACCESS_FINE_LOCATION),
               LOCATION_PERMISSION_REQUEST_CODE
           )
       }
   }

//    Multiple Marker
//    private fun addMarkers() {
//        // Add markers at different locations
//        val marker1 = LatLng(currentLocation!!.latitude + 0.001, currentLocation!!.longitude + 0.001)
//        val marker2 = LatLng(currentLocation!!.latitude - 0.001, currentLocation!!.longitude - 0.001)
//        val marker3 = LatLng(currentLocation!!.latitude + 0.002, currentLocation!!.longitude + 0.002)
//
//        markers.add(mMap.addMarker(MarkerOptions().position(marker1).title("Marker 1"))!!)
//        markers.add(mMap.addMarker(MarkerOptions().position(marker2).title("Marker 2"))!!)
//        markers.add(mMap.addMarker(MarkerOptions().position(marker3).title("Marker 3"))!!)
//    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If location permission is granted, get the user's current location
                onMapReady(mMap)
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun updateCurrentLocationMarker(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        if (currentLocationMarker == null) {
            currentLocationMarker = mMap.addMarker(MarkerOptions().position(latLng).title("Current Location"))
        } else {
            currentLocationMarker?.position = latLng
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

}
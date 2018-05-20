package elmeniawy.eslam.pockemon

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    @Suppress("PrivatePropertyName")
    private val ACCESS_LOCATION_CODE = 0
    private var location: Location? = null
    private var oldLocation: Location? = null
    private lateinit var mMap: GoogleMap
    private val pockemonsList = ArrayList<Pockemon>()
    private var playerPower = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun checkPermission() {
        if (ActivityCompat
                        .checkSelfPermission(this,
                                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        } else {
            ActivityCompat
                    .requestPermissions(this,
                            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                            ACCESS_LOCATION_CODE)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val myLocation = MyLocationListener()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager
                .requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        3,
                        3f,
                        myLocation)

        val myThread = MyThread()
        myThread.start()
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
        mMap = googleMap

        checkPermission()
        loadPockemons()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            ACCESS_LOCATION_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    Toast
                            .makeText(this,
                                    "Permission not granted\nApp cannot get your location",
                                    Toast.LENGTH_LONG)
                            .show()
                }
            }
        }
    }

    // User location
    inner class MyLocationListener : LocationListener {
        init {
            location = Location("Start")
            location!!.latitude = 0.0
            location!!.longitude = 0.0
        }

        override fun onLocationChanged(newLocation: Location?) {
            location = newLocation
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
            Toast
                    .makeText(applicationContext,
                            "Location provider is OFF\nApp cannot get your location",
                            Toast.LENGTH_LONG)
                    .show()
        }
    }

    inner class MyThread : Thread() {
        init {
            oldLocation = Location("Start")
            oldLocation!!.latitude = 0.0
            oldLocation!!.longitude = 0.0
        }

        override fun run() {
            while (true) {
                try {
                    if (oldLocation!!.distanceTo(location) == 0f) {
                        continue
                    }

                    oldLocation = location

                    runOnUiThread {
                        mMap.clear()

                        // Add a marker in my location and move the camera
                        val myLocation = LatLng(location!!.latitude, location!!.longitude)

                        mMap.addMarker(
                                MarkerOptions()
                                        .position(myLocation)
                                        .title("Me")
                                        .snippet("This is my location")
                                        .icon(BitmapDescriptorFactory
                                                .fromResource(R.drawable.mario))
                        )

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 20f))

                        // Add markers for pockemons
                        for (pockemon in pockemonsList) {
                            if (!pockemon.catched) {
                                val pockemonLocation = LatLng(pockemon.location!!.latitude,
                                        pockemon.location!!.longitude)

                                mMap.addMarker(
                                        MarkerOptions()
                                                .position(pockemonLocation)
                                                .title(pockemon.name)
                                                .snippet(pockemon.description
                                                        + ", Power: "
                                                        + pockemon.power)
                                                .icon(BitmapDescriptorFactory
                                                        .fromResource(pockemon.image!!))
                                )

                                if (location!!.distanceTo(pockemon.location) < 2) {
                                    val pockemonIndex = pockemonsList.lastIndexOf(pockemon)
                                    pockemon.catched = true
                                    pockemonsList[pockemonIndex] = pockemon
                                    playerPower += pockemon.power!!

                                    Toast
                                            .makeText(applicationContext,
                                                    "Catched "
                                                            + pockemon.name
                                                            + "\nYour power now is $playerPower",
                                                    Toast.LENGTH_LONG)
                                            .show()
                                }
                            }
                        }
                    }

                    Thread.sleep(1000)
                } catch (ex: Exception) {
                    Toast
                            .makeText(applicationContext,
                                    ex.message,
                                    Toast.LENGTH_LONG)
                            .show()
                }
            }
        }
    }

    private fun loadPockemons() {
        pockemonsList.add(Pockemon("bulbasaur",
                "bulbasaur description",
                R.drawable.bulbasaur,
                55.0, 31.042320, 31.361506))

        pockemonsList.add(Pockemon("charmander",
                "charmander description",
                R.drawable.charmander,
                50.0, 31.042163, 31.361157))

        pockemonsList.add(Pockemon("squirtle",
                "squirtle description",
                R.drawable.squirtle,
                45.0, 31.042132, 31.361463))
    }
}

package com.estazo.project.seeable.app

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class SearchLocation : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var searchView : SearchView
    private lateinit var map : GoogleMap
    private lateinit var confirmBtn : Button
     var homeLocation : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_location)

        searchView = findViewById(R.id.sv_location)
        confirmBtn= findViewById(R.id.search_btn)

        if (getString(R.string.map_key).isEmpty()) {
            Toast.makeText(this, "Add your own API key in MapWithMarker/app/secure.properties as MAPS_API_KEY=YOUR_API_KEY", Toast.LENGTH_LONG).show()
        }

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {

                // Clears the previously touched position
                map.clear()

                val location: String = searchView.query.toString()
                var addressList: List<Address>? = null
                if (location != null || location != "") {
                    val geocoder = Geocoder(this@SearchLocation)
                    try {
                        addressList = geocoder.getFromLocationName(location, 1)
                        Log.i("checkhome5","$addressList")
                    } catch (e: IOException) {
                    }
                    if (addressList != null) {
                        if(addressList.isEmpty()){
                            Toast.makeText(this@SearchLocation,"Search not found!!", Toast.LENGTH_SHORT).show()
                        } else{
                            val address: Address = addressList!![0]
                            val latLng = LatLng(address.latitude, address.longitude)
                            map.addMarker(MarkerOptions().position(latLng).title(location))
                            map.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                            Log.i("checkhome3"," latLng : $latLng")
                            homeLocation = latLng.toString()
                            Log.i("checkhome4"," homeLocation : $homeLocation")
                        }
                    }
                    Log.i("checkhome6","$addressList")

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        confirmBtn.setOnClickListener{
            if(homeLocation  == ""){
                Toast.makeText(this,"Please mark your location before confirm", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"$homeLocation", Toast.LENGTH_SHORT).show()
            }

        }


    }



        override fun onMapReady(googleMap: GoogleMap) {
            map = googleMap
        // Setting a click event handler for the map
        googleMap.setOnMapClickListener { latLng -> // Creating a marker
            val markerOptions = MarkerOptions()

            // Setting the position for the marker
            markerOptions.position(latLng)

            // Setting the title for the marker.
            // This will be displayed on taping the marker
            markerOptions.title(latLng.latitude.toString() + " : " + latLng.longitude)

            // Clears the previously touched position
            googleMap.clear()

            // Animating to the touched position
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

            // Placing a marker on the touched position
            googleMap.addMarker(markerOptions)

            Log.i("checkhome1"," latLng : $latLng")
            homeLocation = latLng.toString()
            Log.i("checkhome2"," homeLocation : $homeLocation")
        }
    }

}
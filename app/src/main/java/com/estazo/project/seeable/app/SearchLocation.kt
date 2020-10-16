package com.estazo.project.seeable.app

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.estazo.project.seeable.app.HelperClass.UserBlinderHelperClass
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException

class SearchLocation : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefFullName: SharedPreferences
    private lateinit var sharedPrefNameHelper: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPhoneHelper: SharedPreferences
    private lateinit var sharedPrefUsername: SharedPreferences
    private lateinit var sharedPrefHomeLocation: SharedPreferences

    private lateinit var searchView : SearchView
    private lateinit var map : GoogleMap
    private lateinit var confirmBtn : Button
     var homeLocation : String = ""

    private lateinit var lat : String
    private lateinit var long : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_location)

        sharedPrefID = getSharedPreferences("value", 0)
        sharedPrefUsername= getSharedPreferences("value", 0)
        sharedPrefPassword= getSharedPreferences("value", 0)
        sharedPrefFullName= getSharedPreferences("value", 0)
        sharedPrefNameHelper= getSharedPreferences("value", 0)
        sharedPrefPhone= getSharedPreferences("value", 0)
        sharedPrefPhoneHelper = getSharedPreferences("value", 0)
        sharedPrefHomeLocation = getSharedPreferences("value", 0)

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
                            lat = latLng.latitude.toString()
                            long = latLng.longitude.toString()
                            map.addMarker(MarkerOptions().position(latLng).title(location))
                            map.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                            Log.i("checkhome3"," latLng : $latLng")
                            homeLocation = ("$lat,$long")
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

                val currentID = sharedPrefID.getString("stringKey2", "not found!")
                val currentUsername = sharedPrefUsername.getString("stringKeyUsername", "not found!")
                val currentPassword = sharedPrefPassword.getString("stringKeyPassword", "not found!")
                val currentFullName = sharedPrefFullName.getString("stringKeyFullName", "not found!")
                val currentNameHelper = sharedPrefNameHelper.getString("stringKeyNameHelper", "not found!")
                val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
                val currentPhoneHelper = sharedPrefPhoneHelper.getString("stringKeyPhoneHelper", "not found!")
                Log.d("Debug_sendLocation","$currentID, $currentUsername, $currentPassword , $currentFullName,$currentPhone" +
                        ",$currentNameHelper,$currentPhoneHelper ,$homeLocation")

                val ref = FirebaseDatabase.getInstance().reference


                val post = UserBlinderHelperClass("$currentID", "$currentUsername", "$currentPassword",
                    "$currentFullName","$currentPhone","$currentNameHelper",
                    "$currentPhoneHelper",lat.toDouble(),long.toDouble(),"$homeLocation")
                val postValues = post.toMap()
                val childUpdates = hashMapOf<String, Any>("users_blind/$currentID" to postValues)
                ref.updateChildren(childUpdates)

                val editorHomeLocation = sharedPrefHomeLocation.edit()
                editorHomeLocation.putString("stringKeyHomeLocation", homeLocation)
                editorHomeLocation.apply()

                val intent = Intent(this@SearchLocation, MainActivity::class.java)
                startActivity(intent)
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
            lat = latLng.latitude.toString()
            long = latLng.longitude.toString()
            // Clears the previously touched position
            googleMap.clear()

            // Animating to the touched position
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

            // Placing a marker on the touched position
            googleMap.addMarker(markerOptions)

            Log.i("checkhome1"," latLng : $latLng")
            homeLocation = ("$lat,$long")
            Log.i("checkhome2"," homeLocation : $homeLocation")
        }
    }

}
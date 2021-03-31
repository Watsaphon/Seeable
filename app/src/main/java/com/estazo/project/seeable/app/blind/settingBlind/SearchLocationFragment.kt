package com.estazo.project.seeable.app.blind.settingBlind

import android.app.AlertDialog
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.helperClass.Navigation
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentAccountBlindBinding
import com.estazo.project.seeable.app.databinding.FragmentSearchLocationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.IOException

class SearchLocationFragment : Fragment() , OnMapReadyCallback {

    private lateinit var binding : FragmentSearchLocationBinding

    private lateinit var sharedPrefPhone: SharedPreferences
//    private lateinit var sharedPrefPassword: SharedPreferences
//    private lateinit var s haredPrefID: SharedPreferences
//    private lateinit var sharedPrefDisplayName: SharedPreferences

//    private lateinit var searchView : SearchView
    private lateinit var map : GoogleMap
//    private lateinit var confirmBtn : Button
     var markLocation : String = ""
    private lateinit var lat : String
    private lateinit var long : String

    private lateinit var  mAlertDialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_location, container, false)

        sharedPrefPhone= requireActivity().getSharedPreferences("value", 0)


        if (getString(R.string.map_key).isEmpty()) {
            Toast.makeText(activity, "Add your own API key in MapWithMarker/app/secure.properties as MAPS_API_KEY=YOUR_API_KEY", Toast.LENGTH_LONG).show()
        }

        // Get the SupportMapFragment and request notification when the map is ready to be used.
//        val mapFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        binding.svLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {

                // Clears the previously touched position
                map.clear()

                val location: String = binding.svLocation.query.toString()
                var addressList: List<Address>? = null
                if (location != null || location != "") {
                    val geocoder = Geocoder(activity)
                    try {
                        addressList = geocoder.getFromLocationName(location, 1)
                        Log.i("checkhome5","$addressList")
                    } catch (e: IOException) {
                    }
                    if (addressList != null) {
                        if(addressList.isEmpty()){
                            Toast.makeText(activity,"Search not found!!", Toast.LENGTH_SHORT).show()
                        } else{
                            val address: Address = addressList[0]
                            val latLng = LatLng(address.latitude, address.longitude)
                            lat = latLng.latitude.toString()
                            long = latLng.longitude.toString()
                            map.addMarker(MarkerOptions().position(latLng).title(location))
                            map.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                            Log.i("checkhome3"," latLng : $latLng")
                            markLocation = ("$lat,$long")
                            Log.i("checkhome4"," homeLocation : $markLocation")
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

        binding.searchBtn.setOnClickListener{
            alertDialogLoading()
            if(markLocation  == "" ){
                mAlertDialog.dismiss()
                Toast.makeText(activity,R.string.empty_mark, Toast.LENGTH_SHORT).show()
            }
            else if(binding.titleBox.text.toString().isEmpty()){
                mAlertDialog.dismiss()
                Toast.makeText(activity,R.string.empty_title, Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(activity,"Update location to : $markLocation", Toast.LENGTH_SHORT).show()
                val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")

                val query = FirebaseDatabase.getInstance().getReference("users_blind").child("$currentPhone/Navigation")
                query.addListenerForSingleValueEvent(valueEventListener)
            }
        }

        binding.titleBox.addTextChangedListener(phoneTextWatcher)



    return binding.root
    }

    private val phoneTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val phone : String =  binding.titleBox.text.toString().trim()
            if(phone.isNotEmpty()){
                binding.clearButton.visibility = View.VISIBLE
                binding.clearButton.setOnClickListener {
                    binding.titleBox.text.clear()
                }
            }
            else if(phone.isEmpty()){
                binding.clearButton.visibility = View.GONE
            }
        }
        override fun afterTextChanged(s: Editable) {}
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
            markLocation = ("$lat,$long")
            Log.i("checkhome2"," markLocation : $markLocation")
        }
    }

    /**receive value from realtime database (users_blind) and check Login */
    private var valueEventListener: ValueEventListener = object : ValueEventListener {

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")

                val ref = FirebaseDatabase.getInstance().reference
                val childUpdates = hashMapOf<String, Any>("users_blind/$currentPhone/Navigation/navigate_blindUser" to "$markLocation")
                ref.updateChildren(childUpdates)

                val title = binding.titleBox.text.toString()
                val childUpdates2 = hashMapOf<String, Any>("users_blind/$currentPhone/Navigation/title_Navigate_blindUser" to "$title")
                ref.updateChildren(childUpdates2)

                findNavController().navigate(R.id.action_searchLocationFragment_to_blindFragment)


            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

    /** AlertDialog to loading  */
    private fun alertDialogLoading() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.loading_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
        //show dialog
        mAlertDialog  = mBuilder.show()
        mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)
    }

    override fun onPause() {
        super.onPause()
        Log.i("SearchLocation", "onPause call")
    }

    override fun onStop() {
        super.onStop()
        Log.i("SearchLocation", "onStop call")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("SearchLocation", "onDestroyView call")
        if (this::mAlertDialog.isInitialized) {
            if (mAlertDialog.isShowing) {
                Log.i("pppp", "alert is showing in if")
                mAlertDialog.dismiss()
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("SearchLocation", "onDestroy call")
    }
    override fun onDetach() {
        super.onDetach()
        Log.i("SearchLocation", "onDetach call")
    }

}
package com.estazo.project.seeable.app.caretaker.settingCaretaker.blindList

import android.app.AlertDialog
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentSearchLocationCaretakerBinding
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

class SearchLocationCaretakerFragment : Fragment() , OnMapReadyCallback {

    private lateinit var binding : FragmentSearchLocationCaretakerBinding

    private lateinit var map : GoogleMap
    var markLocation : String = ""
    private lateinit var lat : String
    private lateinit var long : String

    private lateinit var  mAlertDialog : AlertDialog
    private lateinit var phoneBlind : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val mobile = SearchLocationCaretakerFragmentArgs.fromBundle(it).phoneBlind
            phoneBlind = mobile
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_location_caretaker, container,false)

        if (getString(R.string.map_key).isEmpty()) {
            Toast.makeText(activity, "Add your own API key in MapWithMarker/app/secure.properties as MAPS_API_KEY=YOUR_API_KEY", Toast.LENGTH_LONG).show()
        }

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

                val query = FirebaseDatabase.getInstance().getReference("users_blind").child("$phoneBlind/Navigation")
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

                val ref = FirebaseDatabase.getInstance().reference
                val childUpdates = hashMapOf<String, Any>("users_blind/$phoneBlind/Navigation/navigate_careUser" to "$markLocation")
                ref.updateChildren(childUpdates)

                val title = binding.titleBox.text.toString()
                val childUpdates2 = hashMapOf<String, Any>("users_blind/$phoneBlind/Navigation/title_Navigate_careUser" to "$title")
                ref.updateChildren(childUpdates2)

                requireActivity().onBackPressed()
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
        Log.i("SearchLocationCaretaker", "onPause call")
    }

    override fun onStop() {
        super.onStop()
        Log.i("SearchLocationCaretaker", "onStop call")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("SearchLocationCaretaker", "onDestroyView call")
        if (this::mAlertDialog.isInitialized) {
            if (mAlertDialog.isShowing) {
                Log.i("pppp", "alert is showing in if")
                mAlertDialog.dismiss()
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("SearchLocationCaretaker", "onDestroy call")
    }
    override fun onDetach() {
        super.onDetach()
        Log.i("SearchLocationCaretaker", "onDetach call")
    }

}
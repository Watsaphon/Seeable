package com.estazo.project.seeable.app.blind

import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentNavigateBlindBinding
import com.google.android.gms.maps.GoogleMap


class NavigateBlindFragment : Fragment()
//    , OnMapReadyCallback
{

    private lateinit var binding: FragmentNavigateBlindBinding
    private lateinit var map : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("NavigateBlindFragment", "onCreate call")

//        val launchIntent = requireActivity().packageManager.getLaunchIntentForPackage("com.estazo.project.seeable.app")
//        startActivity(launchIntent)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_navigate_blind, container, false)

//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = childFragmentManager.findFragmentById(R.id.NavigateBlindFragment) as SupportMapFragment
//        mapFragment.getMapAsync(this)


        Log.i("NavigateBlindFragment", "onCreateView call")
        val manager : ActivityManager = requireActivity().getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager

        binding.button.setOnClickListener{
            manager.killBackgroundProcesses("android.service.notification.NotificationListenerService")
            Log.d("wow"," click ja")
        }

//        val test = manager.runningAppProcesses[1].toString()
//        Log.i("wow","test : $test")

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        Log.i("NavigateBlindFragment", "onPause call")

    }

    override fun onStop() {
        super.onStop()
        Log.i("NavigateBlindFragment", "OnStop call")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("NavigateBlindFragment", "onDestroyView call")
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        map = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//
//
//
//        // Navigation : current place direct to gmmIntentUri
//        //val gmmIntentUri = Uri.parse("google.navigation:q=$location&mode=w&avoid=thf")
//        val gmmIntentUri = Uri.parse("google.navigation:q=13.57427,100.8355117&mode=w&avoid=thf")
//        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//        mapIntent.setPackage("com.google.android.apps.maps")
//        mapIntent.resolveActivity(requireActivity().packageManager)?.let {
//            startActivityForResult(mapIntent, 0)
//
//        }
//    }

}
   package com.estazo.project.seeable.app.blind

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.*
import android.speech.tts.TextToSpeech
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentBlindBinding
import com.estazo.project.seeable.app.helperClass.Locations
import com.estazo.project.seeable.app.helperClass.Notification
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.location.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.alert_dialog_set_name.view.*
import java.util.*


   class BlindFragment : Fragment() {


    private lateinit var  mAlertDialog : AlertDialog
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefDisplayName: SharedPreferences

    //Declaring the needed Variables
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val PERMISSION_ID = 1010
    var textToSpeech: TextToSpeech? = null

    private lateinit var binding: FragmentBlindBinding

       private lateinit var viewModel : BlindViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                requireActivity().finishAffinity()
            }
        })
        sharedPrefPhone = requireActivity().getSharedPreferences("value", 0)
        val phone = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
        getTitleLocation(phone)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentBlindBinding.inflate(inflater, container, false)
        binding = fragmentBinding
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blind, container, false)

        Log.i("BlindFragment", "onCreateView call")
        checkPermission()

        sharedPrefLanguage = requireActivity().getSharedPreferences("value", 0)
        sharedPrefPhone = requireActivity().getSharedPreferences("value", 0)
        sharedPrefPassword = requireActivity().getSharedPreferences("value", 0)
        sharedPrefID = requireActivity().getSharedPreferences("value", 0)
        sharedPrefDisplayName = requireActivity().getSharedPreferences("value", 0)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        requestPermission()

        textToSpeech = TextToSpeech(activity?.applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech!!.language = Locale.US
            }
        })
        textToSpeech!!.setSpeechRate(0.9f)

        val displayName = sharedPrefDisplayName.getString("stringKeyDisplayName", "not found!")
        if(displayName == "-"){
            alertDialogSetName()
        }

//        val phone = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
//        getTitleLocation(phone)

        return fragmentBinding.root

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("BlindFragment", "onViewCreated call")

        binding.blindFragment = this@BlindFragment

        sharedPrefPhone = requireActivity().getSharedPreferences("value", 0)
        val phone = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
        val ref = FirebaseDatabase.getInstance().reference
        val currentTime = Calendar.getInstance().time.toString()

        binding.FAB.setOnClickListener {
            view.findNavController().navigate(R.id.action_blindFragment_to_settingBlindFragment2)
        }
        binding.selfNavButton.setOnVeryLongClickListener{
            vibrate()
            textToSpeech!!.speak("Self-Navigation Activate", TextToSpeech.QUEUE_FLUSH, null)
            navigationBlind()
            Toast.makeText(activity, getString(R.string.button_self_navigation), Toast.LENGTH_SHORT).show()

            Log.d("testCalender","currentTime : $currentTime")
            val postNotification =  Notification("$currentTime","navigate")
            val postValues = postNotification.toMap()
            val childUpdates = hashMapOf<String, Any>("users_blind/$phone/Notification" to postValues)
            ref.updateChildren(childUpdates)
            view.findNavController().navigate(R.id.action_blindFragment_to_navigateBlindFragment)
        }
        binding.careNavButton.setOnVeryLongClickListener{
            vibrate()
            textToSpeech!!.speak("Caretaker-Navigation Activate", TextToSpeech.QUEUE_FLUSH, null)
            navigationCaretaker()
            Toast.makeText(activity, getString(R.string.button_caretaker_navigation), Toast.LENGTH_SHORT).show()

            Log.d("testCalender","currentTime : $currentTime")
            val postNotification =  Notification("$currentTime","navigate")
            val postValues = postNotification.toMap()
            val childUpdates = hashMapOf<String, Any>("users_blind/$phone/Notification" to postValues)
            ref.updateChildren(childUpdates)
            view.findNavController().navigate(R.id.action_blindFragment_to_navigateBlindFragment)
        }
        binding.callEmergency.setOnVeryLongClickListener{
            vibrate()
            textToSpeech!!.speak("Call Emergency Activate", TextToSpeech.QUEUE_FLUSH, null)
            emergencyCall()
            Toast.makeText(activity, getString(R.string.button_emergency_call), Toast.LENGTH_SHORT).show()

            Log.d("testCalender","currentTime : $currentTime")
            val postNotification =  Notification("$currentTime","callEmergency")
            val postValues = postNotification.toMap()
            val childUpdates = hashMapOf<String, Any>("users_blind/$phone/Notification" to postValues)
            ref.updateChildren(childUpdates)
        }
        binding.sendLocation.setOnVeryLongClickListener{
            vibrate()
            Log.d("Debug:","sharedLocationBtn -> CheckPermission : "  + checkPermission().toString())
            Log.d("Debug:", "sharedLocationBtn -> isLocationEnabled : " +  isLocationEnabled().toString())
            textToSpeech!!.speak("send Location Activate", TextToSpeech.QUEUE_FLUSH, null)
            getLastLocation()
            sendLocation()

            Log.d("testCalender","currentTime : $currentTime")
            val postNotification =  Notification("$currentTime","location")
            val postValues = postNotification.toMap()
            val childUpdates = hashMapOf<String, Any>("users_blind/$phone/Notification" to postValues)
            ref.updateChildren(childUpdates)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("BlindFragment", "onActivityCreated call")
        viewModel = ViewModelProviders.of(this).get(BlindViewModel::class.java)

        viewModel.titleBlind.observe(viewLifecycleOwner,Observer<String> { blind ->
            if(blind != "-"){
                binding.selfNavButton.text = getString(R.string.button_self_navigation) + " to  $blind"
            }
        })
        viewModel.titleCaretaker.observe(viewLifecycleOwner,Observer<String> { caretaker ->
            if (caretaker != "-"){
                binding.careNavButton.text = getString(R.string.button_caretaker_navigation) + " to  $caretaker"
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, dataIntent)
        // Check which request we're responding to
        Log.i("MainActivity","BlindFragment : onActivityResult call")

        if (requestCode == 1) {
               // Make sure the request was successful
            Log.i("MainActivity","BlindFragment : requestCode success ja")

            if (resultCode == RESULT_OK) {
                Log.i("MainActivity","BlindFragment : resultCode ok ja")
                   //OK received detail
               }
           }
       }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("BlindFragment", "onDestroyView call")
    }

    @SuppressLint("MissingPermission", "DefaultLocale")
    private fun sendLocation(){
        Log.d("Debug_sendLocation:" ,"sendLocation call" )
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
            val location: Location? = task.result
            newLocationData()
            if(location == null){
                Log.i("Debug_sendLocation","call if")
                newLocationData()
            }else{
                val link = java.lang.String.format("%f,%f", location.latitude,location.longitude)
                Log.i("Debug_sendLocation","call else")
                Log.d("Debug_sendLocation:" ,"link : $link" )
                Log.d("Debug_sendLocation:" ,"location : $location" )

                val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
                val currentPassword = sharedPrefPassword.getString("stringKeyPassword", "not found!")
                val currentID = sharedPrefID.getString("stringKey2", "not found!")
                val currentDisplayName = sharedPrefDisplayName.getString("stringKeyDisplayName", "not found!")

                Log.d("Debug_sendLocation","$currentPhone, $currentPassword ,$currentID, $currentDisplayName")

                val ref = FirebaseDatabase.getInstance().reference
                val postLocation =  Locations(location.latitude,location.longitude)
                val postValues = postLocation.toMap()
                val childUpdates = hashMapOf<String, Any>("users_blind/$currentPhone/Location" to postValues)
                ref.updateChildren(childUpdates)
            }
        }
    }

    /** Config location */
    @SuppressLint("MissingPermission")
    private fun getLastLocation(){
        Log.d("Debug:" ,"getLastLocation call" )
        if(checkPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
                    val location: Location? = task.result
                    if(location == null){
                        newLocationData()
                    }else{
                        Log.d("Debug:" ,"getLastLocation() -> Your Location : Long: "+ location.longitude + " , Lat: " + location.latitude )
                        val text = "You Current Location is : Long: "+ location.longitude + " , Lat: " + location.latitude + "\n"
                        Toast.makeText(activity,"$text",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(activity,"Please Turn on Your device Location",Toast.LENGTH_SHORT).show()
            }
        }else{
            requestPermission()
        }
    }
    private fun newLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,locationCallback,Looper.myLooper()
        )
    }
    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:","onLocationResult -> your last location: latitude = "+ lastLocation.latitude.toString()+", longitude = "+ lastLocation.longitude.toString())
            val text = "You Last Location is : Long: "+ lastLocation.longitude + " , Lat: " + lastLocation.latitude + "\n"

            Toast.makeText(activity,"$text",Toast.LENGTH_SHORT).show()
        }
    }
    private fun checkPermission():Boolean {
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if(
            ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED||
            ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
        return false
    }
    private fun requestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE), PERMISSION_ID)
    }
    private fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug:","onRequestPermissionsResult -> You have the Permission")
            }
        }
    }


    /** function press and hold button for few seconds */
    private fun View.setOnVeryLongClickListener(listener: () -> Unit) {
        setOnTouchListener(object : View.OnTouchListener {
            private val longClickDuration = 2000L
            private val handler = Handler()
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    handler.postDelayed({ listener.invoke() }, longClickDuration)
                } else if (event?.action == MotionEvent.ACTION_UP) {
                    handler.removeCallbacksAndMessages(null)
                }
                return true
            }
        })
    }

    /** function vibrations */
    private fun vibrate(){
        // Vibrate for 300 milliseconds
        val v = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        }
        else {
            //deprecated in API 26
            v.vibrate(300)
        }
    }

    private fun emergencyCall(){
        val emergency = "1112"
        val intent = Intent(Intent.ACTION_CALL, Uri.fromParts("tel", emergency, null))
        startActivity(intent)
    }

    private fun navigationBlind() {
        val phone = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
        val query = FirebaseDatabase.getInstance().getReference("users_blind").child("$phone/Navigation")
        query.addListenerForSingleValueEvent(valueEventListenerBlindNav)
    }
    /** Navigation with blind-nav in Google Map  */
    private var valueEventListenerBlindNav: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                val location = dataSnapshot.child("navigate_blindUser").value.toString()
                Log.d("test_locationNavigate B","Navigate to location : $location")
                if(location =="-"){
                    val tts = getString(R.string.locatoin_null)
                    textToSpeech!!.speak(tts, TextToSpeech.QUEUE_FLUSH, null)
                    Toast.makeText(activity, R.string.locatoin_null,Toast.LENGTH_SHORT).show()

                }
                else{
                    // Navigation : current place direct to gmmIntentUri
                    val gmmIntentUri = Uri.parse("google.navigation:q=$location&mode=w&avoid=thf")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
//                    mapIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                    mapIntent.resolveActivity(activity!!.packageManager)?.let {
                    startActivityForResult(mapIntent,1)
//                        startActivity(mapIntent)
                    }

//                    val navigation = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=$location&mode=w&avoid=thf"))
//                    navigation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
//                    navigation.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
////                    navigation.setClassName("com.google.android.apps.maps", "com.estazo.project.seeable.app")
//                    startActivityForResult(navigation,0)

                }

            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

    private fun navigationCaretaker() {
        val phone = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
        val query = FirebaseDatabase.getInstance().getReference("users_blind").child("$phone/Navigation")
        query.addListenerForSingleValueEvent(valueEventListenerCaretakerNav)
    }
    /** Navigation with caretaker-nav in Google Map  */
    private var valueEventListenerCaretakerNav: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                val location = dataSnapshot.child("navigate_careUser").value.toString()
                Log.d("test_locationNavigate C","Navigate to location : $location")
                if(location =="-"){
                    val tts = getString(R.string.locatoin_null)
                    textToSpeech!!.speak(tts, TextToSpeech.QUEUE_FLUSH, null)
                    Toast.makeText(activity, R.string.locatoin_null,Toast.LENGTH_SHORT).show()
                }
                else{
                    // Navigation : current place direct to gmmIntentUri
                    val gmmIntentUri = Uri.parse("google.navigation:q=$location&mode=w&avoid=thf")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    mapIntent.resolveActivity(activity!!.packageManager)?.let {
                        startActivity(mapIntent)
                    }
                }

            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

    private fun getTitleLocation(phone:String){
        Log.d("testName","phone : $phone")
        val firebaseRef = FirebaseDatabase.getInstance().getReference("users_blind/$phone/Navigation")
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val titleBlind = snapshot.child("title_Navigate_blindUser").value.toString()
                val locationBlind = snapshot.child("navigate_blindUser").value.toString()
                val titleCaretaker = snapshot.child("title_Navigate_careUser").value.toString()
                val locationCaretaker = snapshot.child("navigate_careUser").value.toString()
                Log.d("testName ","titleBlind : $titleBlind , titleCaretaker : $titleCaretaker")
                if(titleBlind != "-" && locationBlind != "-" ){
                    viewModel.titleBlind.value = titleBlind
                    binding.selfNavButton.text = getString(R.string.button_self_navigation) + " to  $titleBlind"
                }
                else{
                    viewModel.titleBlind.value = "-"
                    binding.selfNavButton.text = getString(R.string.button_self_navigation)

                }
                if(titleCaretaker != "-" && locationCaretaker != "-" ){
                    viewModel.titleCaretaker.value = titleCaretaker
                    binding.careNavButton.text = getString(R.string.button_caretaker_navigation) + " to  $titleCaretaker"
                }
                else{
                    viewModel.titleCaretaker.value = "-"
                    binding.careNavButton.text = getString(R.string.button_caretaker_navigation)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }


    /** AlertDialog to set DisplayName in user_bind  */
    private fun alertDialogSetName() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.alert_dialog_set_name,null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
        //show dialog
        mAlertDialog  = mBuilder.show()
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)
        mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val subHeader : TextView = mDialogView.findViewById(R.id.info1)
        val txtEN = "Or you can Change it later."
        val txtTH = "หรือคุณสามารถเปลี่ยนชื่อในภายหลัง"
        val ssbEN = SpannableStringBuilder(txtEN)
        val ssbTH = SpannableStringBuilder(txtTH)
        val fcsSky = ForegroundColorSpan(resources.getColor(R.color.txtDisplay))
        val fcsGray = ForegroundColorSpan(Color.GRAY)
        val language = sharedPrefLanguage.getString("stringKey", "not found!")
        if(language == "en"){
            ssbEN.setSpan(fcsGray, 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ssbEN.setSpan(fcsSky, 11, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            subHeader.text = ssbEN
        }
        else if(language =="th"){
            ssbTH.setSpan(fcsGray, 0, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ssbTH.setSpan(fcsSky, 13, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            subHeader.text = ssbTH
        }

        //confirm button click of custom layout
        mDialogView.dialogConfirmBtn.setOnClickListener {
            val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
            val editName : EditText = mDialogView.findViewById(R.id.addDisplayName)
            val name = editName.text.toString()

            if(name.isEmpty() || name.isBlank() || name=="-"){
                Toast.makeText(activity, R.string.check_setName,Toast.LENGTH_SHORT).show()
            }
            else{
                val ref = FirebaseDatabase.getInstance().reference
                ref.child("users_blind/$currentPhone/displayName").setValue(name)
                val editor = sharedPrefDisplayName.edit()
//                editor.putString("stringKeyDisplayName", name)
                editor.putString("stringKeyDisplayName", "not found!")
                editor.apply()
                mAlertDialog.dismiss()
            }
        }
        mDialogView.info1.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }


}
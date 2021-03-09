package com.estazo.project.seeable.app.blind


import android.os.*
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.*
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.device.BPMWorker
import java.util.concurrent.TimeUnit


/**test github*/
class MainBlind: AppCompatActivity() {

//    private lateinit var sharedLocationBtn: Button
//    private lateinit var selfNavBtn: Button
//    private lateinit var emergencyCallBtn: Button
//    private lateinit var careNavBtn: Button
//    private lateinit var fab: FloatingActionButton
//    private lateinit var  mAlertDialog : AlertDialog
//    private lateinit var mGoogleSignInClient: GoogleSignInClient

//    private lateinit var sharedPrefLanguage: SharedPreferences
//    private lateinit var sharedPrefPhone: SharedPreferences
//    private lateinit var sharedPrefPassword: SharedPreferences
//    private lateinit var sharedPrefID: SharedPreferences
//    private lateinit var sharedPrefDisplayName: SharedPreferences
//    private lateinit var sharedPrefUserType : SharedPreferences

//    private lateinit var sharedPrefHomeLocation : SharedPreferences
//    private lateinit var sharedPrefNameHelper: SharedPreferences
//    private lateinit var sharedPrefGoogle : SharedPreferences
//    private lateinit var sharedPrefPhoneHelper: SharedPreferences
//    private lateinit var sharedPrefSex: SharedPreferences
//    private lateinit var sharedGooglePrefUserType : SharedPreferences

//    //Declaring the needed Variables
//    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//    val PERMISSION_ID = 1010
//    var textToSpeech: TextToSpeech? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blind)
        Log.i("MainActivity", "onCreate called")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        hideSystemUI()
//        CheckPermission()

//        sharedPrefLanguage = getSharedPreferences("value", 0)
//        sharedPrefPhone= getSharedPreferences("value", 0)
//        sharedPrefPassword= getSharedPreferences("value", 0)
//        sharedPrefID = getSharedPreferences("value", 0)
//        sharedPrefDisplayName = getSharedPreferences("value", 0)
//        sharedPrefUserType = getSharedPreferences("value", 0)
//
//
//        selfNavBtn = findViewById(R.id.selfNavButton)
//        careNavBtn = findViewById(R.id.careNavButton)
//        emergencyCallBtn = findViewById(R.id.callEmergency)
//        sharedLocationBtn = findViewById(R.id.sendLocation)
//        fab = findViewById(R.id.floating_action_button)


        // Retrieve NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_blind_fragment) as NavHostFragment

        val navController: NavController = navHostFragment.navController

//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        RequestPermission()

//        val displayName = sharedPrefDisplayName.getString("stringKeyDisplayName", "not found!")
//        if(displayName == "-"){
//            alertDialogSetName()
//        }

//        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
//            if (status != TextToSpeech.ERROR) {
//                textToSpeech!!.language = Locale.US
//            }
//        })
//        textToSpeech!!.setSpeechRate(0.9f)


//        selfNavBtn.setOnVeryLongClickListener{
//            vibrate()
//            textToSpeech!!.speak("Self-Navigation Activate", TextToSpeech.QUEUE_FLUSH, null)
//            navigation()
//            Toast.makeText(this, getString(R.string.button_self_navigation), Toast.LENGTH_SHORT).show()
//        }
//        careNavBtn.setOnVeryLongClickListener{
//            vibrate()
//            textToSpeech!!.speak("Caretaker-Navigation Activate", TextToSpeech.QUEUE_FLUSH, null)
//            Toast.makeText(this, getString(R.string.button_caretaker_navigation), Toast.LENGTH_SHORT).show()
//        }
//        emergencyCallBtn.setOnVeryLongClickListener{
//            vibrate()
//           textToSpeech!!.speak("Call Emergency Activate", TextToSpeech.QUEUE_FLUSH, null)
//            emergencyCall()
//            Toast.makeText(this, getString(R.string.button_emergency_call), Toast.LENGTH_SHORT).show()
//        }
//        sharedLocationBtn.setOnVeryLongClickListener{
//            vibrate()
//            Log.d("Debug:","sharedLocationBtn -> CheckPermission : "  + CheckPermission().toString())
//            Log.d("Debug:", "sharedLocationBtn -> isLocationEnabled : " +  isLocationEnabled().toString())
//            textToSpeech!!.speak("send Location Activate", TextToSpeech.QUEUE_FLUSH, null)
//            getLastLocation()
//            sendLocation()
//        }
//
//        fab.setOnClickListener {
//            val intent = Intent(this,
//                SettingBlind::class.java)
//                startActivity(intent)
//        }


        val bpmValue = workDataOf("bpm" to "no-value")
        val constraint = Constraints.Builder().apply { setRequiredNetworkType(NetworkType.CONNECTED) }.build()
        //one time
        val request = PeriodicWorkRequestBuilder<BPMWorker>(10, TimeUnit.SECONDS).apply {
            setInputData(bpmValue)
            setConstraints(constraint)
        }.build()

        //Period
//        val request = OneTimeWorkRequestBuilder<BPMWorker>().apply {
//        val bpmValue = workDataOf(
//            "bpm" to "no-value"
//        )
//
//        val constraint = Constraints.Builder().apply {
//            setRequiredNetworkType(NetworkType.CONNECTED)
//        }.build()
//
//        //one time
//        val request = PeriodicWorkRequestBuilder<BPMWorker>(10, TimeUnit.SECONDS).apply {
//            setInputData(bpmValue)
//            setConstraints(constraint)
//        }.build()
//
//        //Period
////        val request = OneTimeWorkRequestBuilder<BPMWorker>().apply {
////            setConstraints(constraint)
////            setInitialDelay(10, TimeUnit.SECONDS)
////        }.build()
//
//        WorkManager.getInstance().enqueue(request)



    }



//    @SuppressLint("MissingPermission", "DefaultLocale")
//    private fun sendLocation(){
//        Log.d("Debug_sendLocation:" ,"sendLocation call" )
//        fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
//            var location:Location? = task.result
//            NewLocationData()
//            if(location == null){
//                Log.i("Debug_sendLocation","call if")
//                NewLocationData()
//            }else{
//                val link = java.lang.String.format("%f,%f", location.latitude,location.longitude)
//                Log.i("Debug_sendLocation","call else")
//                Log.d("Debug_sendLocation:" ,"link : $link" )
//                Log.d("Debug_sendLocation:" ,"location : $location" )
//                val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
//                val currentPassword = sharedPrefPassword.getString("stringKeyPassword", "not found!")
//                val currentID = sharedPrefID.getString("stringKey2", "not found!")
//                val currentDisplayName = sharedPrefDisplayName.getString("stringKeyDisplayName", "not found!")
//
//                Log.d("Debug_sendLocation","$currentPhone, $currentPassword ,$currentID, $currentDisplayName")
//
//                val ref = FirebaseDatabase.getInstance().reference
//                val postLocation =  Locations(location.latitude,location.longitude)
//                val postValues = postLocation.toMap()
//                val childUpdates = hashMapOf<String, Any>("users_blind/$currentPhone/Location" to postValues)
//                ref.updateChildren(childUpdates)
//
//            }
//        }
//
//    }

//    /** Config location */
//    @SuppressLint("MissingPermission")
//    private fun getLastLocation(){
//        Log.d("Debug:" ,"getLastLocation call" )
//        if(CheckPermission()){
//            if(isLocationEnabled()){
//                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
//                    var location:Location? = task.result
//                    if(location == null){
//                        NewLocationData()
//                    }else{
//                        Log.d("Debug:" ,"getLastLocation() -> Your Location : Long: "+ location.longitude + " , Lat: " + location.latitude )
//                        val text = "You Current Location is : Long: "+ location.longitude + " , Lat: " + location.latitude + "\n"
//                        Toast.makeText(this,"$text",Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }else{
//                Toast.makeText(this,"Please Turn on Your device Location",Toast.LENGTH_SHORT).show()
//            }
//        }else{
//            RequestPermission()
//        }
//    }
//    private fun NewLocationData(){
//        var locationRequest =  LocationRequest()
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        locationRequest.interval = 0
//        locationRequest.fastestInterval = 0
//        locationRequest.numUpdates = 1
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        fusedLocationProviderClient.requestLocationUpdates(
//            locationRequest,locationCallback,Looper.myLooper()
//        )
//    }
//    private val locationCallback = object : LocationCallback(){
//        override fun onLocationResult(locationResult: LocationResult) {
//            var lastLocation: Location = locationResult.lastLocation
//            Log.d("Debug:","onLocationResult -> your last location: latitude = "+ lastLocation.latitude.toString()+", longitude = "+ lastLocation.longitude.toString())
//            val text = "You Last Location is : Long: "+ lastLocation.longitude + " , Lat: " + lastLocation.latitude + "\n"
//
//            Toast.makeText(this@MainActivity,"$text",Toast.LENGTH_SHORT).show()
//        }
//    }
//    private fun CheckPermission():Boolean{
//        //this function will return a boolean
//        //true: if we have permission
//        //false if not
//        if(
//            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
//            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED||
//            ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
//        ){
//            return true
//        }
//        return false
//    }
//    private fun RequestPermission(){
//        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
//        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE), PERMISSION_ID)
//    }
//    private fun isLocationEnabled():Boolean{
//        //this function will return to us the state of the location service
//        //if the gps or the network provider is enabled then it will return true otherwise it will return false
//        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        if(requestCode == PERMISSION_ID){
//            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                Log.d("Debug:","onRequestPermissionsResult -> You have the Permission")
//            }
//        }
//    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "onStart called")
    }

    override fun onResume(){
        super.onResume()
        updateUI()
        Log.i("MainActivity", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.i("MainActivity", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.i("MainActivity", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MainActivity", "onDestroy called")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        Log.i("MainActivity", "onBackPressed called")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
        Log.i("MainActivity", "onWindowFocusChanged called")
    }

//    private fun navigation() {
//        val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
//        val query = FirebaseDatabase.getInstance().getReference("users_blind").child("$currentPhone/Navigation")
//        query.addListenerForSingleValueEvent(valueEventListenerNavigation)
//    }

//    private fun emergencyCall(){
//        val phone = "1112"
//        val intent = Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phone, null))
//        startActivity(intent)
//    }


    /** hide navigation and status bar in each activity */
    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

//    /** function press and hold button for few seconds */
//    private fun View.setOnVeryLongClickListener(listener: () -> Unit) {
//        setOnTouchListener(object : View.OnTouchListener {
//            private val longClickDuration = 2000L
//            private val handler = Handler()
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                if (event?.action == MotionEvent.ACTION_DOWN) {
//                    handler.postDelayed({ listener.invoke() }, longClickDuration)
//                } else if (event?.action == MotionEvent.ACTION_UP) {
//                    handler.removeCallbacksAndMessages(null)
//                }
//                return true
//            }
//        })
//    }
//
//    /** function vibrations */
//    private fun vibrate(){
//        // Vibrate for 300 milliseconds
//        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
//        }
//        else {
//            //deprecated in API 26
//            v.vibrate(300)
//        }
//    }


//    /** AlertDialog to set DisplayName in user_bind  */
//    private fun alertDialogSetName() {
//        //Inflate the dialog with custom view
//        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_set_name,null)
//        //AlertDialogBuilder
//        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
//        //show dialog
//        mAlertDialog  = mBuilder.show()
//        mAlertDialog.setCanceledOnTouchOutside(false)
//        mAlertDialog.setCancelable(false)
//        mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//        val subHeader : TextView = mDialogView.findViewById(R.id.info1)
//        val txtEN = "Or you can Change it later."
//        val txtTH = "หรือคุณสามารถเปลี่ยนชื่อในภายหลัง"
//        val ssbEN = SpannableStringBuilder(txtEN)
//        val ssbTH = SpannableStringBuilder(txtTH)
//        val fcsSky = ForegroundColorSpan(resources.getColor(R.color.txtDisplay))
//        val fcsGray = ForegroundColorSpan(GRAY)
//        val language = sharedPrefLanguage.getString("stringKey", "not found!")
//        if(language == "en"){
//            ssbEN.setSpan(fcsGray, 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            ssbEN.setSpan(fcsSky, 11, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            subHeader.text = ssbEN
//        }
//        else if(language =="th"){
//            ssbTH.setSpan(fcsGray, 0, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            ssbTH.setSpan(fcsSky, 13, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            subHeader.text = ssbTH
//        }
//
//        //confirm button click of custom layout
//        mDialogView.dialogConfirmBtn.setOnClickListener {
//            val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
//            val editName : EditText = mDialogView.findViewById(R.id.addDisplayName)
//            val name = editName.text.toString()
//
//            if(name.isEmpty() || name.isBlank() || name=="-"){
//                Toast.makeText(this,
//                    R.string.check_setName,Toast.LENGTH_SHORT).show()
//            }
//            else{
//                val ref = FirebaseDatabase.getInstance().reference
//                ref.child("users_blind/$currentPhone/displayName").setValue(name)
//                val editor = sharedPrefDisplayName.edit()
//                editor.putString("stringKeyDisplayName", name)
//                editor.apply()
//                mAlertDialog.dismiss()
//            }
//        }
//        mDialogView.info1.setOnClickListener {
//            mAlertDialog.dismiss()
//        }
//    }




//    /** Navigation to home in Google Map  */
//    private var valueEventListenerNavigation: ValueEventListener = object : ValueEventListener {
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            if (dataSnapshot.exists()) {
//                val location = dataSnapshot.child("Self_Navigate_bindUser").value.toString()
////                val latitude = dataSnapshot.child("Latitude").value.toString()
////                val longitude = dataSnapshot.child("Longitude").value.toString()
////                val locationNavigate = "$latitude,$longitude"
//                Log.d("test_locationNavigate ","Navigate to location : $location")
//                if(location =="null"){
//                    Toast.makeText(this@MainActivity,
//                        R.string.locatoin_null,Toast.LENGTH_SHORT).show()
//                }
//                else{
//                    // Navigation : current place direct to gmmIntentUri
//                    val gmmIntentUri = Uri.parse("google.navigation:q=$location&mode=w&avoid=thf")
//                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//                    mapIntent.setPackage("com.google.android.apps.maps")
//                    mapIntent.resolveActivity(packageManager)?.let {
//                        startActivity(mapIntent)
//                    }
//
//                }
//
//            }
//        }
//        override fun onCancelled(databaseError: DatabaseError) {}
//    }

    private fun updateUI() {
        val decorView = window.decorView
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }
        }
    }
}


package com.estazo.project.seeable.app


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationManager
import android.os.*
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import com.estazo.project.seeable.app.HelperClass.UserBlinderHelperClass
import com.estazo.project.seeable.app.Login.LoginScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.alert_dialog_profile.view.*
import java.util.*


class MainActivity : AppCompatActivity(){

    private lateinit var sharedLocationBtn: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var fab: FloatingActionButton
    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefFullName: SharedPreferences
    private lateinit var sharedPrefNameHelper: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPhoneHelper: SharedPreferences
    private lateinit var sharedPrefUsername: SharedPreferences

    private lateinit var  mAlertDialog : AlertDialog

    private lateinit var sharedPrefGoogle : SharedPreferences
    private lateinit var sharedPrefUserType : SharedPreferences


    //Declaring the needed Variables
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val PERMISSION_ID = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideSystemUI()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        CheckPermission()
        sharedPrefGoogle  = getSharedPreferences("value", 0)
        sharedPrefLanguage = getSharedPreferences("value", 0)
        sharedPrefID = getSharedPreferences("value", 0)
        sharedPrefUsername= getSharedPreferences("value", 0)
        sharedPrefPassword= getSharedPreferences("value", 0)
        sharedPrefFullName= getSharedPreferences("value", 0)
        sharedPrefNameHelper= getSharedPreferences("value", 0)
        sharedPrefPhone= getSharedPreferences("value", 0)
        sharedPrefPhoneHelper= getSharedPreferences("value", 0)
        sharedPrefUserType = getSharedPreferences("value", 0)


        val stringValue = sharedPrefLanguage.getString("stringKey", "not found!")
        val currentUser = sharedPrefID.getString("stringKey2", "not found!")


       Log.i("CheckUserID_MainBlind", "Current User ID : $currentUser")
       Log.i("SplashScreenMain", "LoginScreen now language : $stringValue")

        sharedLocationBtn = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)
        fab = findViewById(R.id.floating_action_button)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        RequestPermission()

        sharedLocationBtn.setOnVeryLongClickListener{
            vibrate()
            //create method
 //           Toast.makeText(this,getString(R.string.button_shared_locatoin), Toast.LENGTH_SHORT).show()
            Log.d("Debug:","sharedLocationBtn -> CheckPermission : "  + CheckPermission().toString())
            Log.d("Debug:", "sharedLocationBtn -> isLocationEnabled : " +  isLocationEnabled().toString())
//            RequestPermission()
            getLastLocation()
            sendLocation()

        }
        button2.setOnVeryLongClickListener{
            vibrate()
            Toast.makeText(this, getString(R.string.button_navigation), Toast.LENGTH_SHORT).show()
        }
        button3.setOnVeryLongClickListener{
            vibrate()
            Toast.makeText(this, getString(R.string.button_emergency_call), Toast.LENGTH_SHORT).show()
        }
        button4.setOnVeryLongClickListener{
            vibrate()
         Toast.makeText(this, getString(R.string.button_main_4), Toast.LENGTH_SHORT).show()

        }
        fab.setOnClickListener {
            /** PopupMenu dropdown */
            val popupMenu = PopupMenu(this, fab, Gravity.CENTER)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.menu.findItem(R.id.action_profile).isVisible = true
            popupMenu.menu.findItem(R.id.action_logout).isVisible = true
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_about -> gotoAbout()
                    R.id.action_change_language -> changeLanguage()
                    R.id.action_settings -> gotoSetting()
                    R.id.action_profile -> alertDialogProfile()
                    R.id.action_logout -> gotoLogout()
                }
                hideSystemUI()
                true
            }
            popupMenu.show()
        }


    }

    @SuppressLint("MissingPermission", "DefaultLocale")
    private fun sendLocation(){
        Log.d("Debug_sendLocation:" ,"sendLocation call" )
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
            var location:Location? = task.result
            NewLocationData()
            if(location == null){
                Log.i("Debug_sendLocation","call if")
                NewLocationData()
            }else{
                val link = java.lang.String.format("%f,%f", location.latitude,location.longitude)
                Log.i("Debug_sendLocation","call else")
                Log.d("Debug_sendLocation:" ,"$link" )
                val currentID = sharedPrefID.getString("stringKey2", "not found!")
                val currentUsername = sharedPrefUsername.getString("stringKeyUsername", "not found!")
                val currentPassword = sharedPrefPassword.getString("stringKeyPassword", "not found!")
                val currentFullName = sharedPrefFullName.getString("stringKeyFullName", "not found!")
                val currentNameHelper = sharedPrefNameHelper.getString("stringKeyNameHelper", "not found!")
                val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
                val currentPhoneHelper = sharedPrefPhoneHelper.getString("stringKeyPhoneHelper", "not found!")
                Log.d("Debug_sendLocation","$currentID, $currentUsername, $currentPassword , $currentFullName,$currentPhone,$currentNameHelper,$currentPhoneHelper")
                val ref = FirebaseDatabase.getInstance().reference

                val post = UserBlinderHelperClass("$currentID", "$currentUsername", "$currentPassword",
                    "$currentFullName","$currentPhone","$currentNameHelper",
                    "$currentPhoneHelper",location.latitude,location.longitude)
                val postValues = post.toMap()
                val childUpdates = hashMapOf<String, Any>("users_blind/$currentID" to postValues)
                ref.updateChildren(childUpdates)

            }
        }

    }

    /** Config location */
    @SuppressLint("MissingPermission")
    private fun getLastLocation(){
        Log.d("Debug:" ,"getLastLocation call" )
        if(CheckPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
                    var location:Location? = task.result
                    if(location == null){
                        NewLocationData()
                    }else{
                        Log.d("Debug:" ,"getLastLocation() -> Your Location : Long: "+ location.longitude + " , Lat: " + location.latitude )
                        val text = "You Current Location is : Long: "+ location.longitude + " , Lat: " + location.latitude + "\n"
                        Toast.makeText(this,"$text",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Please Turn on Your device Location",Toast.LENGTH_SHORT).show()
            }
        }else{
            RequestPermission()
        }
    }
    private fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
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
            Log.d("Debug:","onLocationResult -> your last last location: "+ lastLocation.longitude.toString())
            val text = "You Last Location is : Long: "+ lastLocation.longitude + " , Lat: " + lastLocation.latitude + "\n"

            Toast.makeText(this@MainActivity,"$text",Toast.LENGTH_SHORT).show()
        }
    }
    private fun CheckPermission():Boolean{
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if(
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
        return false
    }
    private fun RequestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
    }
    private fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug:","onRequestPermissionsResult -> You have the Permission")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "onStart called")
    }

    override fun onResume(){
        super.onResume()
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

    /** change Language TH and EN*/
    private fun changeLanguage(){
        val language = sharedPrefLanguage.getString("stringKey", "not found!")
        Log.i("SplashScreenMain", "Now Language is :$language ")
        var locale: Locale? = null
        var editor = sharedPrefLanguage.edit()
        if (language=="en") {
            locale = Locale("th")
            editor.putString("stringKey", "th")
            editor.apply()
        } else if (language =="th") {
            locale = Locale("en")
            editor.putString("stringKey", "en")
            editor.apply()
        }
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, null)
        val intent = Intent(this,SplashScreen::class.java)
        startActivity(intent)
//        recreate()
    }

    private fun gotoAbout(){
        val intent = Intent(this,AboutScreen::class.java)
        startActivity(intent)
    }

    private fun gotoSetting(){
        val intent = Intent(this,SettingScreen::class.java)
        startActivity(intent)
    }

//    private fun gotoViewProfile(){
//        alertDialogProfile()
//    }

    private fun gotoLogout(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        Log.i("testusergoogle1","$acct")
        if(acct != null){
            mGoogleSignInClient.signOut()
            Toast.makeText(this, getString(R.string.action_logout), Toast.LENGTH_SHORT).show()
            Log.i("testusergoogle2","$acct")
        }

        FirebaseAuth.getInstance().signOut()

        var editorID = sharedPrefID.edit()
        var editorUsername = sharedPrefUsername.edit()
        var editorPassword = sharedPrefPassword.edit()
        var editorFullName = sharedPrefFullName.edit()
        var editorNameHelper = sharedPrefNameHelper.edit()
        var editorPhone = sharedPrefPhone.edit()
        var editorPhoneHelper = sharedPrefPhoneHelper.edit()
        var editorGoogleUser = sharedPrefGoogle.edit()
        var editorUserType = sharedPrefUserType.edit()

        editorID.putString("stringKey2", "not found!")
        editorUsername.putString("stringKeyUsername", "not found!")
        editorPassword.putString("stringKeyPassword", "not found!")
        editorFullName.putString("stringKeyFullName", "not found!")
        editorNameHelper.putString("stringKeyNameHelper", "not found!")
        editorPhone.putString("stringKeyPhone", "not found!")
        editorPhoneHelper.putString("stringKeyPhoneHelper", "not found!")
        editorGoogleUser.putString("stringKeyGoogle", "not found!")
        editorUserType.putString("stringKeyType", "not found!")

        editorID.apply()
        editorUsername.apply()
        editorPassword.apply()
        editorFullName.apply()
        editorNameHelper.apply()
        editorPhone.apply()
        editorPhoneHelper.apply()
        editorGoogleUser.apply()
        editorUserType.apply()

        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
     }

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
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        }
        else {
            //deprecated in API 26
            v.vibrate(300)
        }
    }

    /** AlertDialog to view profile users_blind  */
    private fun alertDialogProfile() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_profile, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        //show dialog
        mAlertDialog  = mBuilder.show()
        Log.i("test","mAlertDialog.show() call")

        var idText : TextView =  mDialogView.findViewById(R.id.blinderID)
        var usernameText : TextView =  mDialogView.findViewById(R.id.blinderUsername)
        var fullNameText : TextView =  mDialogView.findViewById(R.id.blinderFullName)
        var phoneText : TextView =  mDialogView.findViewById(R.id.blinderPhone)
        var nameHelperText : TextView =  mDialogView.findViewById(R.id.blinderNameHelper)
        var phoneHelperText : TextView =  mDialogView.findViewById(R.id.blinderPhoneHelper)

        val id = sharedPrefID.getString("stringKey2", "not found!")
        val username =  sharedPrefUsername.getString("stringKeyUsername", "not found!")
        val fullName = sharedPrefFullName.getString("stringKeyFullName", "not found!")
        val phone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
        val nameHelper = sharedPrefNameHelper.getString("stringKeyNameHelper", "not found!")
        val phoneHelper = sharedPrefPhoneHelper.getString("stringKeyPhoneHelper", "not found!")

        idText.text = getString(R.string.main_blind_id)+" $id "
        usernameText.text = getString(R.string.main_blind_username)+"$username "
        phoneText.text = getString(R.string.main_blind_fullName)+"$fullName "
        fullNameText.text = getString(R.string.main_blind_phone)+"$phone "
        nameHelperText.text =getString(R.string.main_blind_name_helper)+ "$nameHelper "
        phoneHelperText.text = getString(R.string.main_blind_phone_helper)+"$phoneHelper "

        //login button click of custom layout
        mDialogView.dialogCloseBtn.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }
}



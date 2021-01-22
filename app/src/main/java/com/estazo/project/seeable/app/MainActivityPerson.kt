package com.estazo.project.seeable.app


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.estazo.project.seeable.app.Login.LoginScreen
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivityPerson : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton
    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefDisplayName: SharedPreferences
    private lateinit var sharedPrefUserType : SharedPreferences

    private lateinit var language : String
    private lateinit var phone : String
    private lateinit var password : String
    private lateinit var id : String
    private lateinit var displayName : String
    private lateinit var userType: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_person)
        Log.i("MainActivityPerson", "onCreate called")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        hideSystemUI()

        sharedPrefLanguage = getSharedPreferences("value", 0)
        sharedPrefPhone= getSharedPreferences("value", 0)
        sharedPrefID = getSharedPreferences("value", 0)
        sharedPrefPassword= getSharedPreferences("value", 0)
        sharedPrefDisplayName= getSharedPreferences("value", 0)
        sharedPrefUserType = getSharedPreferences("value", 0)

        language = sharedPrefLanguage.getString("stringKey", "not found!").toString()
        phone  = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
        password  = sharedPrefPassword.getString("stringKeyPassword", "not found!").toString()
        id  = sharedPrefID.getString("stringKey2", "not found!").toString()
        displayName  = sharedPrefDisplayName.getString("stringKeyDisplayName", "not found!").toString()
        userType  = sharedPrefUserType.getString("stringKeyType", "not found!").toString()

        Log.i("MainActivityPerson", "sharedPref -> Language : $language ," +
                "Phone : $phone , ID : $id, Password : $password," +
                " DisplayName : $displayName, UserType : $userType ")
    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivityPerson", "onStart called")
    }
    override fun onResume() {
        super.onResume()
        Log.i("MainActivityPerson", "onResume called")
        updateUI()
    }
    override fun onPause() {
        super.onPause()
        Log.i("MainActivityPerson", "onPause called")
    }
    override fun onStop() {
        super.onStop()
        Log.i("MainActivityPerson", "onStop called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("MainActivityPerson", "onDestroy called")
    }
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

        override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
        Log.i("MainActivityPerson", "onWindowFocusChanged called")
    }

    /** change Language TH and EN*/
    fun changeLanguage(){
//        val language = sharedPrefLanguage.getString("stringKey", "not found!")
        Log.i("CheckLanguage", "Now Language is :$language")
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
    }

    fun gotoLogout(){
        val editorID = sharedPrefID.edit()
        val editorPhone = sharedPrefPhone.edit()
        val editorPassword = sharedPrefPassword.edit()
        val editorDisplayName = sharedPrefDisplayName.edit()
        val editorUserType = sharedPrefUserType.edit()

        editorPhone.putString("stringKeyPhone", "not found!")
        editorPassword.putString("stringKeyPassword", "not found!")
        editorID.putString("stringKey2", "not found!")
        editorDisplayName.putString("stringKeyDisplayName","not found!")
        editorUserType.putString("stringKeyType", "not found!")

        editorPhone.apply()
        editorPassword.apply()
        editorID.apply()
        editorUserType.apply()
        editorDisplayName.apply()

        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

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


//    private lateinit var mGoogleSignInClient: GoogleSignInClient
//    private lateinit var mapButton : Button

//    private lateinit var fab: FloatingActionButton
//    private lateinit var sharedPrefLanguage: SharedPreferences
//    private lateinit var sharedPrefPhone: SharedPreferences
//    private lateinit var sharedPrefPassword: SharedPreferences
//    private lateinit var sharedPrefID: SharedPreferences
//    private lateinit var sharedPrefDisplayName: SharedPreferences
//    private lateinit var sharedPrefUserType : SharedPreferences
//
//
//    private lateinit var checkPartnerID : String
//    private lateinit var  mAlertDialog : AlertDialog
//
//    private lateinit var setting : View
//    private lateinit var notify : View
//    private lateinit var activity_walking : ImageButton
//    private lateinit var health_status : ImageButton
//    private lateinit var heart : ImageButton
//    private lateinit var bpm_number : TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main_person)
//        hideSystemUI()
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
//
//        sharedPrefLanguage = getSharedPreferences("value", 0)
//        sharedPrefPhone= getSharedPreferences("value", 0)
//        sharedPrefID = getSharedPreferences("value", 0)
//        sharedPrefPassword= getSharedPreferences("value", 0)
//        sharedPrefDisplayName= getSharedPreferences("value", 0)
//        sharedPrefUserType = getSharedPreferences("value", 0)
//
////        sharedPrefPartnerID = getSharedPreferences("value", 0)
//
//
////        val partnerID = sharedPrefPartnerID.getString("stringKeyPartnerID","not found!")
////        Log.d("checkPairing_MainPerson","$partnerID")
////        if(partnerID=="no-pairing"){
////            alertDialogPairing()
////        }
//
//        //Initializing Views
//        setting = findViewById(R.id.setting)
//        setting.setOnClickListener{
//            /** PopupMenu dropdown */
//            val popupMenu = PopupMenu(this, setting, Gravity.CENTER)
//            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
//            popupMenu.menu.findItem(R.id.action_delete_partnerID).isVisible = true
//            popupMenu.menu.findItem(R.id.action_logout).isVisible = true
//            popupMenu.setOnMenuItemClickListener { item ->
//                when (item.itemId) {
//                    R.id.action_about -> gotoAbout()
//                    R.id.action_change_language -> changeLanguage()
//                    R.id.action_settings -> gotoSetting()
////                    R.id.action_delete_partnerID -> gotoChangePartnerID()
//                    R.id.action_logout -> gotoLogout()
//                }
//                hideSystemUI()
//                true
//            }
//            popupMenu.show()
//        }
//
//        notify = findViewById(R.id.notify)
//        notify.setOnClickListener{
//            Toast.makeText(this," Notification not available",Toast.LENGTH_SHORT).show()
//        }
//
//        heart = findViewById(R.id.heart)
//        heart.setOnClickListener{
//            Toast.makeText(this," This Function not available",Toast.LENGTH_SHORT).show()
//        }
//
//        health_status = findViewById(R.id.health_status)
//        health_status.setOnClickListener{
//            Toast.makeText(this," Healthy Status not available",Toast.LENGTH_SHORT).show()
//        }
//
//        activity_walking = findViewById(R.id.activity_walking)
//        activity_walking.setOnClickListener{
////            Log.i("partnerID_main","$partnerID")
////            val query = FirebaseDatabase.getInstance().getReference("users_blind").child("$partnerID").orderByChild("id")
////            query.addListenerForSingleValueEvent(valueEventListenerDirectMap)
////            alertDialogLoading()
//        }
//
//        bpm_number = findViewById(R.id.bpm_number)
//
//        val bpmValue = workDataOf(
//            "ฺBPM" to "no-value"
//        )
//
//        val constraint = Constraints.Builder().apply {
//            setRequiredNetworkType(NetworkType.CONNECTED)
//        }.build()
//
//        //one time use
////        val request = PeriodicWorkRequestBuilder<BPMWorker>(10, TimeUnit.SECONDS).apply {
//////            setInputData(bpmValue)
////            setConstraints(constraint)
////        }.build()
//
//        val request = OneTimeWorkRequestBuilder<BPMWorker>().apply {
//            setConstraints(constraint)
//            setInitialDelay(10, TimeUnit.SECONDS)
//        }.build()
//
//        WorkManager.getInstance().enqueue(request)
//
//
//        WorkManager.getInstance().getWorkInfoByIdLiveData(request.id)
//            .observe(this, Observer { info: WorkInfo ->
//                Log.d("bpm_worker_observer","State :" + info.state.toString())
////                bpm_number.setText("BPM")
//                if (info.state == WorkInfo.State.ENQUEUED) {
//                    val bpm = info.outputData.getString("bpm")
//                    Log.d("bpm_worker_mainAc", " BPM in ENQUEUED : $bpm")
//                    if(bpm != null){
//                        bpm_number.setText( "$bpm")
////                        recreate()
//                    }
//                }
//                else if (info.state == WorkInfo.State.RUNNING){
//                    val bpm = info.outputData.getString("bpm")
//                    Log.d("bpm_worker_mainAc", " BPM in RUNNING : $bpm")
//                    if(bpm != null){
//                        bpm_number.setText( "$bpm")
////                        recreate()
//                    }
//                }
//            })
////        bpm_number.setText("BPM")
//
//
//    }
//
//
//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        super.onWindowFocusChanged(hasFocus)
//        hideSystemUI()
//        Log.d("MainActivity", "onWindowFocusChanged called")
//    }
//
//    /** change Language TH and EN*/
//    private fun changeLanguage(){
//        val language = sharedPrefLanguage.getString("stringKey", "not found!")
//        Log.i("CheckLanguage", "Now Language is :$language ")
//        var locale: Locale? = null
//        var editor = sharedPrefLanguage.edit()
//        if (language=="en") {
//            locale = Locale("th")
//            editor.putString("stringKey", "th")
//            editor.apply()
//        } else if (language =="th") {
//            locale = Locale("en")
//            editor.putString("stringKey", "en")
//            editor.apply()
//        }
//        Locale.setDefault(locale)
//        val config = Configuration()
//        config.locale = locale
//        baseContext.resources.updateConfiguration(config, null)
//        val intent = Intent(this,SplashScreen::class.java)
//        startActivity(intent)
//    }
//
//    private fun gotoAbout(){
////        val intent = Intent(this,AboutScreen::class.java)
////        startActivity(intent)
//    }
//
//    private fun gotoSetting(){
////        val intent = Intent(this,SettingScreen::class.java)
////        startActivity(intent)
//    }
//
////    private fun gotoChangePartnerID(){
////
////        val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
////        val currentPassword = sharedPrefPassword.getString("stringKeyPassword", "not found!")
////        val currentID = sharedPrefID.getString("stringKey2", "not found!")
////        val currentDisplay = sharedPrefDisplay.getString("stringKeyDisplayName", "not found!")
//////        val currentSex = sharedPrefSex.getString("stringKeySex", "not found!")
////
////
////        val ref = FirebaseDatabase.getInstance().reference
////
////        val post = UserPersonHelperClassNew("$currentID", "$currentPhone", "$currentPassword",
////            "$currentDisplay","-","-")
////        val postValues = post.toMap()
////        val childUpdates = hashMapOf<String, Any>("users_caretaker/$currentID" to postValues)
////        ref.updateChildren(childUpdates)
////
////        val editorPartnerID = sharedPrefPartnerID.edit()
////        editorPartnerID.putString("stringKeyPartnerID", "no-pairing")
////        editorPartnerID.apply()
////
////        alertDialogPairing()
////
////    }
//
//    private fun gotoLogout(){
////        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
////        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
////        val acct = GoogleSignIn.getLastSignedInAccount(this)
////        if(acct != null){
////            mGoogleSignInClient.signOut()
////            Toast.makeText(this, getString(R.string.action_logout), Toast.LENGTH_SHORT).show()
////        }
////        FirebaseAuth.getInstance().signOut()
//
//        val editorID = sharedPrefID.edit()
//        val editorPhone = sharedPrefPhone.edit()
//        val editorPassword = sharedPrefPassword.edit()
//        val editorDisplayName = sharedPrefDisplayName.edit()
//        val editorUserType = sharedPrefUserType.edit()
////        val editorPartnerID = sharedPrefPartnerID.edit()
////        val editorSex = sharedPrefSex.edit()
////        val editorNameHelper = sharedPrefNameHelper.edit()
////        val editorPhoneHelper = sharedPrefPhoneHelper.edit()
////        val editorGoogleUser = sharedPrefGoogle.edit()
////        val editorGoogleUserType = sharedGooglePrefUserType.edit()
//
//        editorPhone.putString("stringKeyPhone", "not found!")
//        editorPassword.putString("stringKeyPassword", "not found!")
//        editorID.putString("stringKey2", "not found!")
//        editorDisplayName.putString("stringKeyDisplayName","not found!")
//        editorUserType.putString("stringKeyType", "not found!")
//
////        editorPartnerID.putString("stringKeyPartnerID", "not found!")
////        editorSex.putString("stringKeySex", "not found!")
////        editorNameHelper.putString("stringKeyNameHelper", "not found!")
////        editorPhoneHelper.putString("stringKeyPhoneHelper", "not found!")
////        editorGoogleUser.putString("stringKeyGoogle", "not found!")
////        editorGoogleUserType.putString("stringKeyGoogleType", "not found!")
//
//        editorPhone.apply()
//        editorPassword.apply()
//        editorID.apply()
//        editorUserType.apply()
//        editorDisplayName.apply()
//
////        editorPartnerID.apply()
////        editorSex.apply()
////        editorNameHelper.apply()
////        editorPhoneHelper.apply()
////        editorGoogleUser.apply()
////        editorGoogleUserType.apply()
//
//        val intent = Intent(this, LoginScreen::class.java)
//        startActivity(intent)
//    }
//
//    override fun onBackPressed() {
//        super.onBackPressed()
//        finishAffinity()
//        Log.d("MainActivity", "onBackPressed called")
//    }
//
//    /** hide navigation and status bar in each activity */
//    private fun hideSystemUI() {
//        // Enables regular immersive mode.
//        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
//        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                // Set the content to appear under the system bars so that the
//                // content doesn't resize when the system bars hide and show.
//                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                // Hide the nav bar and status bar
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_FULLSCREEN)
//    }
//
//
//    /** AlertDialog to pairing user with partner ID  */
//    private fun alertDialogPairing() {
////        //Inflate the dialog with custom view
////        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_pairing, null)
////        //AlertDialogBuilder
////        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
////        //show dialog
////        mAlertDialog  = mBuilder.show()
////        mAlertDialog.setCanceledOnTouchOutside(false)
////        mAlertDialog.setCancelable(false)
////        mDialogView.dialogSummitBtn.setOnClickListener {
////            val query = FirebaseDatabase.getInstance().getReference("users_blind").orderByChild("id")
////            query.addListenerForSingleValueEvent(valueEventListener)
////            val partnerIDBox = mDialogView.dialogPartnerID.text.toString()
////            checkPartnerID = partnerIDBox
////        }
////        //logout button click of custom layout
////        mDialogView.dialogLogoutBtn.setOnClickListener {
////            gotoLogout()
////        }
////        //exit button click of custom layout
////        mDialogView.dialogExitBtn.setOnClickListener {
////            finishAffinity()
////        }
//
//        val nag = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
//        nag.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        nag.setCancelable(false)
//        nag.setContentView(R.layout.alert_dialog_pairing)
//        nag.setCanceledOnTouchOutside(false)
//
//       val btnYES = nag.findViewById(R.id.dialogSummitBtn) as Button
//       val btnLogout = nag.findViewById(R.id.dialogLogoutBtn) as Button
//
//        val header = nag.findViewById(R.id.header) as TextView
//        header.setTextColor(Color.WHITE)
//        val subHeader = nag.findViewById(R.id.subHeader) as TextView
//        subHeader.setTextColor(Color.BLACK)
//
//        btnYES.dialogSummitBtn.setOnClickListener {
////            val query = FirebaseDatabase.getInstance().getReference("users_blind").orderByChild("id")
////            query.addListenerForSingleValueEvent(valueEventListener)
////            val partnerIDBox = mDialogView.dialogPartnerID.text.toString()
////            checkPartnerID = partnerIDBox
//        }
//        //logout button click of custom layout
//        btnLogout.dialogLogoutBtn.setOnClickListener {
//            gotoLogout()
//        }
////        //exit button click of custom layout
////        btnExit.dialogExitBtn.setOnClickListener {
////            finishAffinity()
////        }
//        nag.show()
//
//    }
//
//    /** AlertDialog to loading  */
//    private fun alertDialogLoading() {
//        //Inflate the dialog with custom view
//        val mDialogView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null)
//        //AlertDialogBuilder
//        val mBuilder = AlertDialog.Builder(this)
//            .setView(mDialogView)
//        //show dialog
//        mAlertDialog  = mBuilder.show()
//        mAlertDialog.window!!.setLayout(400,300)
//        mAlertDialog.setCanceledOnTouchOutside(false)
//        mAlertDialog.setCancelable(false)
//    }
//
//    private fun dismissAlertDialogLoading() {
//        //Inflate the dialog with custom view
//        val mDialogView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null)
//        //AlertDialogBuilder
//        val mBuilder = AlertDialog.Builder(this)
//            .setView(mDialogView)
//        //show dialog
//        mAlertDialog.dismiss()
//    }
//
//    /** Direction in Google Map  */
//    private var valueEventListenerDirectMap: ValueEventListener = object : ValueEventListener {
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
////                Toast.makeText(this@MainActivityPerson," Activity walking activate",Toast.LENGTH_SHORT).show()
////                val partnerID = sharedPrefPartnerID.getString("stringKeyPartnerID","not found!")
////                if (dataSnapshot.exists()) {
////                        val id = dataSnapshot.child("id").value.toString()
////                        val latitude = dataSnapshot.child("latitude").value.toString()
////                        val longtitude = dataSnapshot.child("longitude").value.toString()
////                        Log.d("Direction","partnerID  = $partnerID , id =$id")
////                        if (partnerID == id) {
////                            Log.i("position_latitude", "$latitude")
////                            Log.i("position_longitude", "$longtitude")
////                            // Navigation : current place direct to gmmIntentUri
////                            val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longtitude&mode=w&avoid=thf")
////                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
////                            mapIntent.setPackage("com.google.android.apps.maps")
////                            mapIntent.resolveActivity(packageManager)?.let {
////                                startActivity(mapIntent)
////                            }
////                        }
////                }
////            dismissAlertDialogLoading()
//        }
//        override fun onCancelled(databaseError: DatabaseError) {
//            Toast.makeText(this@MainActivityPerson," Activity walking failed",Toast.LENGTH_SHORT).show()
//            dismissAlertDialogLoading() }
//    }
//
//    /** check users_blind to pairing */
//    private var valueEventListener: ValueEventListener = object : ValueEventListener {
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            var count = 0
//            Log.i("MainPerson_count","Before adding listener, count=$count")
//            if (dataSnapshot.exists()) {
//                for (snapshot in dataSnapshot.children) {
//                    val id = snapshot.child("id").value.toString()
//                    Log.i("MainPerson_count","In onDataChange, count=$count")
//                    Log.i("MainPerson_count", " ID in checkbox : $id")
//                    Log.i("MainPerson_count", "Database info :  $id")
//                    if (checkPartnerID == id){
//                        val currentID = sharedPrefID.getString("stringKey2","not found!")
//                        Log.i("test_sharedPrefID ","$currentID")
//                        val query = FirebaseDatabase.getInstance().getReference("users_caretaker").child("$currentID").orderByChild("id")
//                        query.addListenerForSingleValueEvent(valueEventListenerInsertPartnerID)
//                        break
//                    }
//                    ++count
//                }
//                Log.i("MainPerson_count","After adding listener, count=$count")
//                val countDatabase = dataSnapshot.childrenCount.toInt()
//                Log.i("MainPerson_count","After adding listener -> count=$count ,countDatabase=$countDatabase")
//                if(count==countDatabase){
//                    mAlertDialog.show()
//                    Toast.makeText(this@MainActivityPerson, getString(R.string.main_toast_failed), Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        }
//        override fun onCancelled(databaseError: DatabaseError) {}
//    }
//
//    /** insert partner id to users_caretaker */
//    private var valueEventListenerInsertPartnerID: ValueEventListener = object : ValueEventListener {
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            if (dataSnapshot.exists()) {
//                    val id = dataSnapshot.child("id").value.toString()
//                    val phone = dataSnapshot.child("phone").value.toString()
//                    val password = dataSnapshot.child("password").value.toString()
//                    val fullname = dataSnapshot.child("fullName").value.toString()
//                    val sex = dataSnapshot.child("sex").value.toString()
//                    Log.i("MainPerson_count", " ID in checkbox : $id")
//                    Log.i("MainPerson_count", "Database info :  $id ,$password,$fullname,$phone,$sex")
//                        val ref = FirebaseDatabase.getInstance().reference
//                        val post = UserPersonHelperClassNew("$id" ,"$phone","$password","$fullname","$sex","$checkPartnerID")
//                        val postValues = post.toMap()
//                        val childUpdates = hashMapOf<String, Any>("users_caretaker/$id" to postValues)
//                        ref.updateChildren(childUpdates)
//                        mAlertDialog.dismiss()
//                Toast.makeText(this@MainActivityPerson, getString(R.string.main_toast_success), Toast.LENGTH_SHORT).show()
//            }
//        }
//        override fun onCancelled(databaseError: DatabaseError) {}
//    }
//
//
//}
//
//

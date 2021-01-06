package com.estazo.project.seeable.app


import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.estazo.project.seeable.app.HelperClass.UserPersonHelperClass
import com.estazo.project.seeable.app.HelperClass.UserPersonHelperClassNew
import com.estazo.project.seeable.app.Login.LoginScreen
import com.estazo.project.seeable.app.Register.BPMWorker
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.alert_dialog_home_location.view.*
import kotlinx.android.synthetic.main.alert_dialog_pairing.view.*
import kotlinx.android.synthetic.main.alert_dialog_pairing.view.dialogLogoutBtn
import kotlinx.android.synthetic.main.alert_dialog_pairing.view.dialogSummitBtn
import java.util.*
import java.util.concurrent.TimeUnit
import androidx.lifecycle.Observer

class MainActivityPerson : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton
    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mapButton : Button
    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefFullName: SharedPreferences
    private lateinit var sharedPrefNameHelper: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPhoneHelper: SharedPreferences
    private lateinit var sharedPrefSex: SharedPreferences

    private lateinit var sharedPrefPartnerID: SharedPreferences

    private lateinit var checkPartnerID : String
    private lateinit var  mAlertDialog : AlertDialog

    private lateinit var sharedPrefGoogle : SharedPreferences
    private lateinit var sharedPrefUserType : SharedPreferences
    private lateinit var sharedGooglePrefUserType : SharedPreferences

    private lateinit var setting : View
    private lateinit var notify : View
    private lateinit var activity_walking : ImageButton
    private lateinit var health_status : ImageButton
    private lateinit var heart : ImageButton
    private lateinit var bpm_number : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_person)
        hideSystemUI()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        sharedPrefLanguage = getSharedPreferences("value", 0)
        sharedPrefID = getSharedPreferences("value", 0)
        sharedPrefSex= getSharedPreferences("value", 0)
        sharedPrefPassword= getSharedPreferences("value", 0)
        sharedPrefFullName= getSharedPreferences("value", 0)
        sharedPrefNameHelper= getSharedPreferences("value", 0)
        sharedPrefPhone= getSharedPreferences("value", 0)
        sharedPrefPhoneHelper= getSharedPreferences("value", 0)
        sharedPrefPartnerID = getSharedPreferences("value", 0)
        sharedPrefGoogle  = getSharedPreferences("value", 0)
        sharedPrefUserType = getSharedPreferences("value", 0)
        sharedGooglePrefUserType = getSharedPreferences("value", 0)


        val partnerID = sharedPrefPartnerID.getString("stringKeyPartnerID","not found!")
        Log.d("checkPairing_MainPerson","$partnerID")
//        comment for development
//        if(partnerID=="no-pairing"){
//            alertDialogPairing()
//        }

        //Initializing Views
        setting = findViewById(R.id.setting)
        setting.setOnClickListener{
            /** PopupMenu dropdown */
            val popupMenu = PopupMenu(this, setting, Gravity.CENTER)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.menu.findItem(R.id.action_delete_partnerID).isVisible = true
            popupMenu.menu.findItem(R.id.action_logout).isVisible = true
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_about -> gotoAbout()
                    R.id.action_change_language -> changeLanguage()
                    R.id.action_settings -> gotoSetting()
                    R.id.action_delete_partnerID -> gotoChangePartnerID()
                    R.id.action_logout -> gotoLogout()
                }
                hideSystemUI()
                true
            }
            popupMenu.show()
        }

        val bpmValue = workDataOf(
            "ฺBPM" to "no-value"
        )

        val constraint = Constraints.Builder().apply {
            setRequiredNetworkType(NetworkType.CONNECTED)
        }.build()

        //one time use
        val request = PeriodicWorkRequestBuilder<BPMWorker>(10, TimeUnit.SECONDS).apply {
//            setInputData(bpmValue)
            setConstraints(constraint)
        }.build()

        WorkManager.getInstance().enqueue(request)
        notify = findViewById(R.id.notify)
        notify.setOnClickListener{
            Toast.makeText(this," Notification not available",Toast.LENGTH_SHORT).show()
        }

        heart = findViewById(R.id.heart)
        heart.setOnClickListener{
            Toast.makeText(this," This Function not available",Toast.LENGTH_SHORT).show()
        }

        health_status = findViewById(R.id.health_status)
        health_status.setOnClickListener{
            Toast.makeText(this," Healthy Status not available",Toast.LENGTH_SHORT).show()
        }

        activity_walking = findViewById(R.id.activity_walking)
        activity_walking.setOnClickListener{
            Log.i("partnerID_main","$partnerID")
            val query = FirebaseDatabase.getInstance().getReference("users_blind").child("$partnerID").orderByChild("id")
            query.addListenerForSingleValueEvent(valueEventListenerDirectMap)
            alertDialogLoading()
        }

        bpm_number = findViewById(R.id.bpm_number)
        WorkManager.getInstance().getWorkInfoByIdLiveData(request.id)
            .observe(this, Observer { info: WorkInfo ->
                Log.d("BPM worker observe", info.state.toString())
                bpm_number.setText("BPM")
                if (info.state == WorkInfo.State.SUCCEEDED) {
                    val bpm = info.outputData.getString("BPM")
                    Log.d("BPM in activity : ", "$bpm")
                    bpm_number.setText("BPM")
                }
            })
//        bpm_number.setText("BPM")
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
        Log.d("MainActivity", "onWindowFocusChanged called")
    }

    /** change Language TH and EN*/
    private fun changeLanguage(){
        val language = sharedPrefLanguage.getString("stringKey", "not found!")
        Log.i("CheckLanguage", "Now Language is :$language ")
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

    private fun gotoAbout(){
        val intent = Intent(this,AboutScreen::class.java)
        startActivity(intent)
    }

    private fun gotoSetting(){
        val intent = Intent(this,SettingScreen::class.java)
        startActivity(intent)
    }

    private fun gotoChangePartnerID(){

        val currentID = sharedPrefID.getString("stringKey2", "not found!")
        val currentSex = sharedPrefSex.getString("stringKeySex", "not found!")
        val currentPassword = sharedPrefPassword.getString("stringKeyPassword", "not found!")
        val currentFullName = sharedPrefFullName.getString("stringKeyFullName", "not found!")
        val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")

        val ref = FirebaseDatabase.getInstance().reference

        val post = UserPersonHelperClass("$currentID", "$currentSex", "$currentPassword",
            "$currentFullName","$currentPhone","no-pairing")
        val postValues = post.toMap()
        val childUpdates = hashMapOf<String, Any>("users_caretaker/$currentID" to postValues)
        ref.updateChildren(childUpdates)

        val editorPartnerID = sharedPrefPartnerID.edit()
        editorPartnerID.putString("stringKeyPartnerID", "no-pairing")
        editorPartnerID.apply()

        alertDialogPairing()

    }

    private fun gotoLogout(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if(acct != null){
            mGoogleSignInClient.signOut()
            Toast.makeText(this, getString(R.string.action_logout), Toast.LENGTH_SHORT).show()
        }
        FirebaseAuth.getInstance().signOut()

        val editorID = sharedPrefID.edit()
        val editorPhone = sharedPrefPhone.edit()
        val editorPassword = sharedPrefPassword.edit()
        val editorPartnerID = sharedPrefPartnerID.edit()
        val editorSex = sharedPrefSex.edit()
        val editorFullName = sharedPrefFullName.edit()
        val editorNameHelper = sharedPrefNameHelper.edit()
        val editorPhoneHelper = sharedPrefPhoneHelper.edit()
        val editorGoogleUser = sharedPrefGoogle.edit()
        val editorUserType = sharedPrefUserType.edit()
        val editorGoogleUserType = sharedGooglePrefUserType.edit()

        editorID.putString("stringKey2", "not found!")
        editorPhone.putString("stringKeyPhone", "not found!")
        editorPassword.putString("stringKeyPassword", "not found!")
        editorPartnerID.putString("stringKeyPartnerID", "not found!")
        editorSex.putString("stringKeySex", "not found!")
        editorFullName.putString("stringKeyFullName", "not found!")
        editorNameHelper.putString("stringKeyNameHelper", "not found!")
        editorPhoneHelper.putString("stringKeyPhoneHelper", "not found!")
        editorGoogleUser.putString("stringKeyGoogle", "not found!")
        editorUserType.putString("stringKeyType", "not found!")
        editorGoogleUserType.putString("stringKeyGoogleType", "not found!")

        editorID.apply()
        editorPhone.apply()
        editorPassword.apply()
        editorPartnerID.apply()
        editorSex.apply()
        editorFullName.apply()
        editorNameHelper.apply()
        editorPhoneHelper.apply()
        editorGoogleUser.apply()
        editorUserType.apply()
        editorGoogleUserType.apply()

        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        Log.d("MainActivity", "onBackPressed called")
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


    /** AlertDialog to pairing user with partner ID  */
    private fun alertDialogPairing() {
//        //Inflate the dialog with custom view
//        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_pairing, null)
//        //AlertDialogBuilder
//        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
//        //show dialog
//        mAlertDialog  = mBuilder.show()
//        mAlertDialog.setCanceledOnTouchOutside(false)
//        mAlertDialog.setCancelable(false)
//        mDialogView.dialogSummitBtn.setOnClickListener {
//            val query = FirebaseDatabase.getInstance().getReference("users_blind").orderByChild("id")
//            query.addListenerForSingleValueEvent(valueEventListener)
//            val partnerIDBox = mDialogView.dialogPartnerID.text.toString()
//            checkPartnerID = partnerIDBox
//        }
//        //logout button click of custom layout
//        mDialogView.dialogLogoutBtn.setOnClickListener {
//            gotoLogout()
//        }
//        //exit button click of custom layout
//        mDialogView.dialogExitBtn.setOnClickListener {
//            finishAffinity()
//        }

        val nag = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        nag.requestWindowFeature(Window.FEATURE_NO_TITLE)
        nag.setCancelable(false)
        nag.setContentView(R.layout.alert_dialog_pairing)
        nag.setCanceledOnTouchOutside(false)

       val btnYES = nag.findViewById(R.id.dialogSummitBtn) as Button
       val btnLogout = nag.findViewById(R.id.dialogLogoutBtn) as Button

        val header = nag.findViewById(R.id.header) as TextView
        header.setTextColor(Color.WHITE)
        val subHeader = nag.findViewById(R.id.subHeader) as TextView
        subHeader.setTextColor(Color.BLACK)

        btnYES.dialogSummitBtn.setOnClickListener {
//            val query = FirebaseDatabase.getInstance().getReference("users_blind").orderByChild("id")
//            query.addListenerForSingleValueEvent(valueEventListener)
//            val partnerIDBox = mDialogView.dialogPartnerID.text.toString()
//            checkPartnerID = partnerIDBox
        }
        //logout button click of custom layout
        btnLogout.dialogLogoutBtn.setOnClickListener {
            gotoLogout()
        }
//        //exit button click of custom layout
//        btnExit.dialogExitBtn.setOnClickListener {
//            finishAffinity()
//        }
        nag.show()

    }

    /** AlertDialog to loading  */
    private fun alertDialogLoading() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        //show dialog
        mAlertDialog  = mBuilder.show()
        mAlertDialog.window!!.setLayout(400,300)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)
    }

    private fun dismissAlertDialogLoading() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        //show dialog
        mAlertDialog.dismiss()
    }

    /** Direction in Google Map  */
    private var valueEventListenerDirectMap: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
                Toast.makeText(this@MainActivityPerson," Activity walking activate",Toast.LENGTH_SHORT).show()
                val partnerID = sharedPrefPartnerID.getString("stringKeyPartnerID","not found!")
                if (dataSnapshot.exists()) {
                        val id = dataSnapshot.child("id").value.toString()
                        val latitude = dataSnapshot.child("latitude").value.toString()
                        val longtitude = dataSnapshot.child("longitude").value.toString()
                        Log.d("Direction","partnerID  = $partnerID , id =$id")
                        if (partnerID == id) {
                            Log.i("position_latitude", "$latitude")
                            Log.i("position_longitude", "$longtitude")
                            // Navigation : current place direct to gmmIntentUri
                            val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longtitude&mode=w&avoid=thf")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            mapIntent.resolveActivity(packageManager)?.let {
                                startActivity(mapIntent)
                            }
                        }
                }
            dismissAlertDialogLoading()
        }
        override fun onCancelled(databaseError: DatabaseError) {
            Toast.makeText(this@MainActivityPerson," Activity walking failed",Toast.LENGTH_SHORT).show()
            dismissAlertDialogLoading() }
    }

    /** check users_blind to pairing */
    private var valueEventListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            var count = 0
            Log.i("MainPerson_count","Before adding listener, count=$count")
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
                    val id = snapshot.child("id").value.toString()
                    Log.i("MainPerson_count","In onDataChange, count=$count")
                    Log.i("MainPerson_count", " ID in checkbox : $id")
                    Log.i("MainPerson_count", "Database info :  $id")
                    if (checkPartnerID == id){
                        val currentID = sharedPrefID.getString("stringKey2","not found!")
                        Log.i("test_sharedPrefID ","$currentID")
                        val query = FirebaseDatabase.getInstance().getReference("users_caretaker").child("$currentID").orderByChild("id")
                        query.addListenerForSingleValueEvent(valueEventListenerInsertPartnerID)
                        break
                    }
                    ++count
                }
                Log.i("MainPerson_count","After adding listener, count=$count")
                val countDatabase = dataSnapshot.childrenCount.toInt()
                Log.i("MainPerson_count","After adding listener -> count=$count ,countDatabase=$countDatabase")
                if(count==countDatabase){
                    mAlertDialog.show()
                    Toast.makeText(this@MainActivityPerson, getString(R.string.main_toast_failed), Toast.LENGTH_SHORT).show()
                }

            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

    /** insert partner id to users_caretaker */
    private var valueEventListenerInsertPartnerID: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                    val id = dataSnapshot.child("id").value.toString()
                    val phone = dataSnapshot.child("phone").value.toString()
                    val password = dataSnapshot.child("password").value.toString()
                    val fullname = dataSnapshot.child("fullName").value.toString()
                    val sex = dataSnapshot.child("sex").value.toString()
                    Log.i("MainPerson_count", " ID in checkbox : $id")
                    Log.i("MainPerson_count", "Database info :  $id ,$password,$fullname,$phone,$sex")
                        val ref = FirebaseDatabase.getInstance().reference
                        val post = UserPersonHelperClassNew("$id" ,"$phone","$password","$fullname","$sex","$checkPartnerID")
                        val postValues = post.toMap()
                        val childUpdates = hashMapOf<String, Any>("users_caretaker/$id" to postValues)
                        ref.updateChildren(childUpdates)
                        mAlertDialog.dismiss()
                Toast.makeText(this@MainActivityPerson, getString(R.string.main_toast_success), Toast.LENGTH_SHORT).show()
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }


}



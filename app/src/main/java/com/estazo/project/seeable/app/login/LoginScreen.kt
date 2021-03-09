package com.estazo.project.seeable.app.login

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.estazo.project.seeable.app.blind.MainBlind
import com.estazo.project.seeable.app.caretaker.MainCaretaker
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.register.SendOTP
import com.estazo.project.seeable.app.SplashScreen
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class LoginScreen : AppCompatActivity() {


    private lateinit var telBox: EditText
    private lateinit var passwordBox: EditText
    private lateinit var finish: Button
    private lateinit var register: Button
//    private var RC_SIGN_IN = 0
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var auth: FirebaseAuth

    private lateinit var sharedPrefIntroApp: SharedPreferences
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefDisplayName: SharedPreferences
    private lateinit var sharedPrefUserType: SharedPreferences
    private lateinit var sharedPrefCaretakerUser: SharedPreferences


    private lateinit var  mAlertDialog : AlertDialog
    private lateinit var changeLang : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login_screen)
        hideSystemUI()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Initializing Views
        telBox = findViewById(R.id.tel_box)
        passwordBox = findViewById(R.id.password_box)
        finish = findViewById(R.id.login_finish_button)
        register = findViewById(R.id.regis_button)
        auth = FirebaseAuth.getInstance()
        changeLang = findViewById(R.id.en_th)

        sharedPrefIntroApp = getSharedPreferences("value", 0)
        val introApp = sharedPrefIntroApp.getString("stringKeyIntro", "No")
        if(introApp =="No"){
            val editor = sharedPrefIntroApp.edit()
            editor.putString("stringKeyIntro","YES")
            editor.apply()
        }


        sharedPrefLanguage = getSharedPreferences("value", 0)

        sharedPrefPhone= getSharedPreferences("value", 0)
        sharedPrefPassword= getSharedPreferences("value", 0)
        sharedPrefID = getSharedPreferences("value", 0)
        sharedPrefDisplayName= getSharedPreferences("value", 0)
        sharedPrefUserType = getSharedPreferences("value", 0)
        sharedPrefCaretakerUser = getSharedPreferences("value", 0)


        val text = "EN|TH"
        val ssb = SpannableStringBuilder(text)
        val fcsWhite = ForegroundColorSpan(Color.WHITE)
        val fcsGreen = ForegroundColorSpan(Color.GREEN)

        val language = sharedPrefLanguage.getString("stringKey", "not found!")
        if(language == "en"){
            ssb.setSpan(fcsWhite, 3, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ssb.setSpan(fcsGreen, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            changeLang.text = ssb

        }
        else if(language =="th"){
            ssb.setSpan(fcsWhite, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ssb.setSpan(fcsGreen, 3, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            changeLang.text = ssb
        }

        val stringValue = sharedPrefLanguage.getString("stringKey", "not found!")
        val stringValue2 = sharedPrefID.getString("stringKey2", "not found!")

        Log.i("CheckUserID_login", "Current User ID  : $stringValue2")
        Log.i("CheckLanguage_splash", "LoginScreen now language : $stringValue")

        finish.setOnClickListener(View.OnClickListener { login() })
        register.setOnClickListener(View.OnClickListener { register() })
        changeLang.setOnClickListener(View.OnClickListener { changeLanguage() })

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        Log.d("LoginScreen", "onStart currentUser is :$currentUser ")
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        Log.d("LoginScreen", "onBackPressed called")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
        Log.d("LoginScreen", "onWindowFocusChanged called")
    }


    private fun register() {
//       val intent = Intent(this, SelectRegister::class.java)
       val intent = Intent(this, SendOTP::class.java)
        startActivity(intent)
    }

    private fun login() {
        closeKeyboard()
        alertDialogLoading()
        val query = FirebaseDatabase.getInstance().getReference("users_caretaker").orderByChild("phone")
        query.addListenerForSingleValueEvent(valueEventListener)
    }


    /** change Language TH and EN  */
    private fun changeLanguage(){
        val language = sharedPrefLanguage.getString("stringKey", "not found!")
        Log.i("CheckLanguage", "Now Language is :$language ")
        var locale: Locale? = null
        val editor = sharedPrefLanguage.edit()
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
        val intent = Intent(this, SplashScreen::class.java)
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
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        //show dialog
        mAlertDialog.dismiss()
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**receive value from realtime database (user_person) and check Login */
    private var valueEventListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val loginTel = telBox.text.toString()
            val loginPassword = passwordBox.text.toString()
            var count = 0
            Log.i("LoginScreen_count","Before adding listener, count=$count")
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
                    val id = snapshot.child("id").value.toString()
                    val phone = snapshot.child("phone").value.toString()
                    val password = snapshot.child("password").value.toString()
                    val displayName = snapshot.child("displayName").value.toString()


//                    val sex = snapshot.child("sex").value.toString()
//                    val partnerIDFirebase = snapshot.child("partner_id").value.toString()

                    Log.i("LoginScreen_count","In onDataChange, count=$count")
                    Log.i("LoginScreen_count", "Username : $loginTel , Password : $loginPassword")
                    Log.i("LoginScreen_count", "Database info :  $id,$password,$displayName,$phone ")

                    if (loginTel.equals(phone) && loginPassword.equals(password)){
                        var editorID = sharedPrefID.edit()
                        val editorPhone = sharedPrefPhone.edit()
                        val editorPassword = sharedPrefPassword.edit()
                        val editorDisplayName = sharedPrefDisplayName.edit()
                        var editorUserType = sharedPrefUserType.edit()
//                        val editorSex = sharedPrefSex.edit()
//                        val editorPartnerID = sharedPrefPartnerID.edit()

                        editorID.putString("stringKey2", id)
                        editorPhone.putString("stringKeyPhone", phone)
                        editorPassword.putString("stringKeyPassword", password)
                        editorDisplayName.putString("stringKeyDisplayName", displayName)
                        editorUserType.putString("stringKeyType", "caretaker")
//                        editorSex.putString("stringKeySex", sex)
//                        editorPartnerID.putString("stringKeyPartnerID", "$partnerIDFirebase")

                        editorID.apply()
                        editorPhone.apply()
                        editorPassword.apply()
                        editorDisplayName.apply()
                        editorUserType.apply()
//                        editorSex.apply()
//                        editorPartnerID.apply()

                        dismissAlertDialogLoading()

                        val intent = Intent(this@LoginScreen, MainCaretaker::class.java)
                        startActivity(intent)
                        dismissAlertDialogLoading()
                        finishAffinity()
                        break
                    }
                    else if (loginTel.isEmpty()  || loginPassword.isEmpty() ) {
                        dismissAlertDialogLoading()
                        Toast.makeText(applicationContext, getString(R.string.login_empty), Toast.LENGTH_SHORT).show()
                        break
                    }
                    ++count
                }
                Log.i("login_page_count","After adding listener, count=$count")
                val countDatabase = dataSnapshot.childrenCount.toInt()
                if(count==countDatabase){
                    /**if not found user in user_person -> find in users_blind */
                    val query2 = FirebaseDatabase.getInstance().getReference("users_blind").orderByChild("id")
                    query2.addListenerForSingleValueEvent(valueEventListener2)
                }
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

    /**receive value from realtime database (users_blind) and check Login */
    private var valueEventListener2: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val loginTel = telBox.text.toString()
            val loginPassword = passwordBox.text.toString()
            var count = 0
            Log.i("LoginScreen_checkLogin","Before adding listener, count=$count")
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
                    val id = snapshot.child("id").value.toString()
                    val phone = snapshot.child("phone").value.toString()
                    val password = snapshot.child("password").value.toString()
                    val displayName = snapshot.child("displayName").value.toString()
                    val user1 = snapshot.child("Caretaker/user1").value.toString()
                    val user2 = snapshot.child("Caretaker/user2").value.toString()
                    val user3 = snapshot.child("Caretaker/user3").value.toString()
                    val user4 = snapshot.child("Caretaker/user4").value.toString()

                    Log.i("LoginScreen_checkLogin","In onDataChange, count=$count")
                    Log.i("LoginScreen_checkLogin", "Username : $loginTel , Password : $loginPassword")
                    Log.i("LoginScreen_checkLogin", "Database info :  $id,$password,$displayName,$phone ,$user1,$user2,$user3,$user4 ")

                    if (loginTel.equals(phone) && loginPassword.equals(password)){
                        Toast.makeText(applicationContext, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                        val editorID = sharedPrefID.edit()
                        val editorPhone = sharedPrefPhone.edit()
                        val editorPassword = sharedPrefPassword.edit()
                        val editorDisplayName = sharedPrefDisplayName.edit()
                        val editorUserType = sharedPrefUserType.edit()
                        val editorCaretakerUser = sharedPrefCaretakerUser.edit()

                        editorID.putString("stringKey2", id)
                        editorPhone.putString("stringKeyPhone", phone)
                        editorPassword.putString("stringKeyPassword", password)
                        editorDisplayName.putString("stringKeyDisplayName", displayName)
                        editorUserType.putString("stringKeyType", "blind")
                        editorCaretakerUser.putString("stringKeyCaretakerUser", "$user1,$user2,$user3,$user4")

                        editorPhone.apply()
                        editorPassword.apply()
                        editorID.apply()
                        editorDisplayName.apply()
                        editorUserType.apply()
                        editorCaretakerUser.apply()

                        val test = sharedPrefCaretakerUser.getString("stringKeyCaretakerUser","not found!")
                        Log.d("wtfArray","test : $test ")
                        if(test != null){
                            val yourArray: List<String> = test.split(",")
                            Log.d("wtfArray","yourArray : $yourArray ")
                            val user1 = yourArray[0]
                            val user2 = yourArray[1]
                            val user3 = yourArray[2]
                            val user4 = yourArray[3]

                            Log.d("wtfArray","After split -> user1 : $user1 ,  user2 : $user2 , user3 : $user3 , user4 : $user4")
                        }

                        val intent = Intent(this@LoginScreen, MainBlind::class.java)
                        startActivity(intent)
                        dismissAlertDialogLoading()
                        finishAffinity()
                        break
                    }
                    else if (loginTel.isEmpty()  || loginPassword.isEmpty() ) {
                        Toast.makeText(applicationContext, getString(R.string.login_empty), Toast.LENGTH_SHORT).show()
                        dismissAlertDialogLoading()
                        break
                    }
                    ++count
                }
                Log.i("LoginScreen_checkLogin","After adding listener, count=$count")
                val countDatabase = dataSnapshot.childrenCount.toInt()
                if(count==countDatabase){
                    Log.i("LoginScreen_checkLogin","check count database, count=$countDatabase")
                    Log.i("LoginScreen_checkLogin","check count for loop, count=$count")
                    Toast.makeText(applicationContext, getString(R.string.login_incorrect), Toast.LENGTH_SHORT).show()
                    dismissAlertDialogLoading()
                }
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

}


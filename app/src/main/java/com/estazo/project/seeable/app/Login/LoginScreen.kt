package com.estazo.project.seeable.app.Login

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
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.estazo.project.seeable.app.*
import com.estazo.project.seeable.app.Register.SelectRegister
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class LoginScreen : AppCompatActivity() {

    private lateinit var userNameBox: EditText
    private lateinit var passwordBox: EditText
    private lateinit var finish: Button
    private lateinit var register: Button
    private var RC_SIGN_IN = 0
    private lateinit var signInButton: SignInButton
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var fab: FloatingActionButton
    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var auth: FirebaseAuth

    private lateinit var sharedPrefIntroApp: SharedPreferences
    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefFullName: SharedPreferences
    private lateinit var sharedPrefNameHelper: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPhoneHelper: SharedPreferences
    private lateinit var sharedPrefUsername: SharedPreferences
    private lateinit var sharedPrefHomeLocation: SharedPreferences
    private lateinit var sharedPrefPartnerID: SharedPreferences

    private lateinit var sharedPrefUserType: SharedPreferences
    private lateinit var sharedPrefGoogle : SharedPreferences
    private lateinit var sharedGooglePrefUserType : SharedPreferences

    private lateinit var  mAlertDialog : AlertDialog
    private lateinit var UID : String
    private lateinit var changeLang : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        hideSystemUI()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Initializing Views
        signInButton = findViewById(R.id.sign_in_button)
        userNameBox = findViewById(R.id.username_box)
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
        sharedPrefID = getSharedPreferences("value", 0)
        sharedPrefUsername= getSharedPreferences("value", 0)
        sharedPrefPassword= getSharedPreferences("value", 0)
        sharedPrefFullName= getSharedPreferences("value", 0)
        sharedPrefNameHelper= getSharedPreferences("value", 0)
        sharedPrefPhone= getSharedPreferences("value", 0)
        sharedPrefPhoneHelper= getSharedPreferences("value", 0)
        sharedPrefUserType = getSharedPreferences("value", 0)
        sharedPrefGoogle  = getSharedPreferences("value", 0)
        sharedGooglePrefUserType = getSharedPreferences("value", 0)
        sharedPrefHomeLocation = getSharedPreferences("value", 0)
        sharedPrefPartnerID = getSharedPreferences("value", 0)


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

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        finish.setOnClickListener(View.OnClickListener { login() })
        signInButton.setOnClickListener(View.OnClickListener { signIn() })
        register.setOnClickListener(View.OnClickListener { register() })
        changeLang.setOnClickListener(View.OnClickListener { changeLanguage() })
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
//        updateUI(currentUser)
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


    private fun gotoAbout(){
        val intent = Intent(this, AboutScreen::class.java)
        startActivity(intent)
    }

    private fun gotoSetting(){
        val intent = Intent(this, SettingScreen::class.java)
        startActivity(intent)
    }

    private fun register() {
        val intent = Intent(this, SelectRegister::class.java)
        startActivity(intent)
    }

    private fun login() {
        closeKeyboard()
        alertDialogLoading()
        val query = FirebaseDatabase.getInstance().getReference("users_person").orderByChild("id")
        query.addListenerForSingleValueEvent(valueEventListener)
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.i("LoginScreen_onActResult", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("LoginScreen_onActResult", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        alertDialogLoading()
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.i("LoginScreen_fbAuth", "signInWithCredential:success")
//                    val user = auth.currentUser
                    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                    Log.i("testFirebaseUser1","$user")
                    val userGoogle = sharedPrefGoogle.getString("stringKeyGoogle","not found!")
                    val userType = sharedPrefUserType.getString("stringKeyType","not found!")

                    val googleUserType = sharedGooglePrefUserType.getString("stringKeyGoogleType","not found!")
                    val editorGoogleUser  = sharedPrefGoogle.edit()
                    val editorGoogleUserType  = sharedGooglePrefUserType.edit()

                    Log.i("checkloginlast","user : $user , userGoogle : $userGoogle , userType : $userType")

                    if (user != null && userGoogle == "not found!" && googleUserType == "not found!") {
                        val uid = user.uid
                        Log.i("testFirebaseUser2","$uid")
                        UID = uid
                        val queryUserPerson = FirebaseDatabase.getInstance().getReference("users_person").orderByChild("id")
                        queryUserPerson.addListenerForSingleValueEvent(valueEventListenerCheckGoogleUserPerson)

                        editorGoogleUser.putString("stringKeyGoogle","$UID")
                        editorGoogleUserType.putString("stringKeyGoogleType","noRegister")
                        editorGoogleUser.apply()
                        editorGoogleUserType.apply()
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LoginScreen_fbAuth", "signInWithCredential:failure", task.exception)
                    Toast.makeText(applicationContext, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
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
//        recreate()
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
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
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
            val loginName = userNameBox.text.toString()
            val loginPassword = passwordBox.text.toString()
            var count = 0
            Log.i("LoginScreen_count","Before adding listener, count=$count")
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
                    val fullname = snapshot.child("fullName").value.toString()
                    val id = snapshot.child("id").value.toString()
                    val password = snapshot.child("password").value.toString()
                    val phone = snapshot.child("phone").value.toString()
                    val username = snapshot.child("username").value.toString()
                    val partnerIDFirebase = snapshot.child("partner_id").value.toString()

                    Log.i("LoginScreen_count","In onDataChange, count=$count")
                    Log.i("LoginScreen_count", "Username : $loginName , Password : $loginPassword")
                    Log.i("LoginScreen_count", "Database info :  $id,$password,$username,$fullname,$phone ,$partnerIDFirebase")

                    if (loginName.equals(username) && loginPassword.equals(password)){
                        var editorID = sharedPrefID.edit()
                        val editorUsername = sharedPrefUsername.edit()
                        val editorPassword = sharedPrefPassword.edit()
                        val editorFullName = sharedPrefFullName.edit()
                        val editorPhone = sharedPrefPhone.edit()
                        var editorUserType = sharedPrefUserType.edit()
                        val editorPartnerID = sharedPrefPartnerID.edit()

                        editorID.putString("stringKey2", id)
                        editorUsername.putString("stringKeyUsername", username)
                        editorPassword.putString("stringKeyPassword", password)
                        editorFullName.putString("stringKeyFullName", fullname)
                        editorPhone.putString("stringKeyPhone", phone)
                        editorUserType.putString("stringKeyType", "person")
                        editorPartnerID.putString("stringKeyPartnerID", "$partnerIDFirebase")

                        editorID.apply()
                        editorUsername.apply()
                        editorPassword.apply()
                        editorFullName.apply()
                        editorPhone.apply()
                        editorUserType.apply()
                        editorPartnerID.apply()

                        dismissAlertDialogLoading()

                        val intent = Intent(this@LoginScreen, MainActivityPerson::class.java)
                        startActivity(intent)
                        dismissAlertDialogLoading()

//                        /** Check user pair with blinder */
//                        val query = FirebaseDatabase.getInstance().getReference("users_person").child("$id").orderByChild("partner_id")
//                        query.addListenerForSingleValueEvent(valueEventListenerCheckUserPairing)
                        break
                    }
                    else if (loginName.isEmpty()  || loginPassword.isEmpty() ) {
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
            val loginName = userNameBox.text.toString()
            val loginPassword = passwordBox.text.toString()
            var count = 0
            Log.i("LoginScreen_checkLogin","Before adding listener, count=$count")
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
                    val fullname = snapshot.child("fullName").value.toString()
                    val id = snapshot.child("id").value.toString()
                    val password = snapshot.child("password").value.toString()
                    val phone = snapshot.child("phone").value.toString()
                    val username = snapshot.child("username").value.toString()
                    val nameHelper = snapshot.child("nameHelper").value.toString()
                    val phoneHelper = snapshot.child("phoneHelper").value.toString()
                    val homeLocation = snapshot.child("homeLocation").value.toString()

                    Log.i("LoginScreen_checkLogin","In onDataChange, count=$count")
                    Log.i("LoginScreen_checkLogin", "Username : $loginName , Password : $loginPassword")
                    Log.i("LoginScreen_checkLogin", "Database info :  $id,$password,$username,$fullname,$phone,$nameHelper,$phoneHelper")

                    if (loginName.equals(username) && loginPassword.equals(password)){
                        Toast.makeText(applicationContext, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                        val editorID = sharedPrefID.edit()
                        val editorUsername = sharedPrefUsername.edit()
                        val editorPassword = sharedPrefPassword.edit()
                        val editorFullName = sharedPrefFullName.edit()
                        val editorNameHelper = sharedPrefNameHelper.edit()
                        val editorPhone = sharedPrefPhone.edit()
                        val editorPhoneHelper = sharedPrefPhoneHelper.edit()
                        val editorUserType = sharedPrefUserType.edit()
                        val editorHomeLocation = sharedPrefHomeLocation.edit()

                        editorID.putString("stringKey2", id)
                        editorUsername.putString("stringKeyUsername", username)
                        editorPassword.putString("stringKeyPassword", password)
                        editorFullName.putString("stringKeyFullName", fullname)
                        editorNameHelper.putString("stringKeyNameHelper", nameHelper)
                        editorPhone.putString("stringKeyPhone", phone)
                        editorPhoneHelper.putString("stringKeyPhoneHelper", phoneHelper)
                        editorUserType.putString("stringKeyType", "blind")
                        editorHomeLocation.putString("stringKeyHomeLocation", homeLocation)

                        editorID.apply()
                        editorUsername.apply()
                        editorPassword.apply()
                        editorFullName.apply()
                        editorNameHelper.apply()
                        editorPhone.apply()
                        editorPhoneHelper.apply()
                        editorUserType.apply()
                        editorHomeLocation.apply()

                        val intent = Intent(this@LoginScreen, MainActivity::class.java)
                        startActivity(intent)
                        dismissAlertDialogLoading()
                        break
                    }
                    else if (loginName.isEmpty()  || loginPassword.isEmpty() ) {
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


    /**receive value from realtime database (user_person) and check Login Google User*/
    private var valueEventListenerCheckGoogleUserPerson : ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
                var count = 0
            Log.i("LoginScreen_countG","Before adding listener, count=$count")
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
                    val id = snapshot.child("id").value.toString()
                    val partnerIDFirebase = snapshot.child("partner_id").value.toString()
                    Log.i("LoginScreen_countG","In onDataChange, count=$count")
                    if (UID == id){
                        Toast.makeText(applicationContext, getString(R.string.login_success), Toast.LENGTH_SHORT).show()

                        val editorGoogleUser = sharedPrefGoogle.edit()
                        val editorGoogleUserType = sharedGooglePrefUserType.edit()
                        val editorPartnerID = sharedPrefPartnerID.edit()

                        editorGoogleUser.putString("stringKeyGoogle","$UID")
                        editorGoogleUserType.putString("stringKeyGoogleType", "person")
                        editorPartnerID.putString("stringKeyPartnerID", "$partnerIDFirebase")

                        editorGoogleUser.apply()
                        editorGoogleUserType.apply()
                        editorPartnerID.apply()

                    val intent = Intent(this@LoginScreen, MainActivityPerson::class.java)
                    startActivity(intent)
                    dismissAlertDialogLoading()

                    break
                    }
                    ++count
                }
                Log.i("login_page_count","After adding listener, count=$count")
                val countDatabase = dataSnapshot.childrenCount.toInt()
                if(count==countDatabase){
                    /**if not found user in user_person -> find in users_blind */
                    val queryUserBlinder = FirebaseDatabase.getInstance().getReference("users_blind").orderByChild("id")
                    queryUserBlinder.addListenerForSingleValueEvent(valueEventListenerCheckGoogleUserBlinder)
                }
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

    /**receive value from realtime database (user_blind) and check Login Google User*/
    private var valueEventListenerCheckGoogleUserBlinder : ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            var count = 0
            Log.i("LoginScreen_checkLogin","Before adding listener, count=$count")
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
                    val id = snapshot.child("id").value.toString()
                    val fullname = snapshot.child("fullName").value.toString()
                    val phone = snapshot.child("phone").value.toString()
                    val username = snapshot.child("username").value.toString()
                    val nameHelper = snapshot.child("nameHelper").value.toString()
                    val phoneHelper = snapshot.child("phoneHelper").value.toString()
                    val homeLocation = snapshot.child("homeLocation").value.toString()

                    Log.i("LoginScreen_checkLogin","In onDataChange, count=$count")
                    if (UID == id){
                        Toast.makeText(applicationContext, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                        val editorID = sharedPrefID.edit()
                        val editorFullName = sharedPrefFullName.edit()
                        val editorNameHelper = sharedPrefNameHelper.edit()
                        val editorPhone = sharedPrefPhone.edit()
                        val editorPhoneHelper = sharedPrefPhoneHelper.edit()
                        val editorGoogleUser = sharedPrefGoogle.edit()
                        val editorGoogleUserType = sharedGooglePrefUserType.edit()
                        val editorHomeLocation = sharedPrefHomeLocation.edit()

                        editorID.putString("stringKey2", id)
                        editorFullName.putString("stringKeyFullName", fullname)
                        editorNameHelper.putString("stringKeyNameHelper", nameHelper)
                        editorPhone.putString("stringKeyPhone", phone)
                        editorPhoneHelper.putString("stringKeyPhoneHelper", phoneHelper)
                        editorGoogleUser.putString("stringKeyGoogle","$UID")
                        editorGoogleUserType.putString("stringKeyGoogleType", "blind")
                        editorHomeLocation.putString("stringKeyHomeLocation", homeLocation)

                        editorID.apply()
                        editorFullName.apply()
                        editorNameHelper.apply()
                        editorPhone.apply()
                        editorPhoneHelper.apply()
                        editorGoogleUser.apply()
                        editorGoogleUserType.apply()
                        editorHomeLocation.apply()

                        val intent = Intent(this@LoginScreen, MainActivity::class.java)
                        startActivity(intent)
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
                    val intent = Intent(this@LoginScreen, SelectRegister::class.java)
                    startActivity(intent)
                    dismissAlertDialogLoading()
                }
            }

        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }


    /** Check user pair with blinder */
//    val query = FirebaseDatabase.getInstance().getReference("users_person").child("$id").orderByChild("partner_id")
//    query.addListenerForSingleValueEvent(valueEventListenerCheckUserPairing)

    /** Check User pair with blinder ( check at only one user not all ) */
//    private var valueEventListenerCheckUserPairing: ValueEventListener = object : ValueEventListener {
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            if (dataSnapshot.exists()) {
//                val partnerIDFirebase = dataSnapshot.child("partner_id").value.toString()
//                Log.d("checkPairing_login","$partnerIDFirebase")
//                val sharedPrefPartnerID = getSharedPreferences("value", 0)
//                val editorPartnerID = sharedPrefPartnerID.edit()
//
//                if (partnerIDFirebase != "no-pairing") {
//                    Log.d("checkPairing_login","$partnerIDFirebase")
//                    Toast.makeText(applicationContext, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
//                    editorPartnerID.putString("stringKeyPartnerID", "$partnerIDFirebase")
//                    editorPartnerID.apply()
//                    val intent = Intent(this@LoginScreen, MainActivityPerson::class.java)
//                    startActivity(intent)
//                    dismissAlertDialogLoading()
//                }
//                else if(partnerIDFirebase== "no-pairing"){
//                    Log.d("checkPairing_login","$partnerIDFirebase")
//                    Toast.makeText(applicationContext, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
//                    editorPartnerID.putString("stringKeyPartnerID", "no-pairing")
//                    editorPartnerID.apply()
//                    val intent = Intent(this@LoginScreen, MainActivityPerson::class.java)
//                    startActivity(intent)
//                    dismissAlertDialogLoading()
//                }
//            }
//        }
//        override fun onCancelled(databaseError: DatabaseError) {}
//    }

}


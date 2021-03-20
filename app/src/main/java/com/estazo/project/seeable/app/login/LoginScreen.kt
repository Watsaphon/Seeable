package com.estazo.project.seeable.app.login

import android.app.AlertDialog
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
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.MainActivity
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.SplashScreen
import com.estazo.project.seeable.app.databinding.FragmentBlindBinding
import com.estazo.project.seeable.app.databinding.FragmentLoginScreenBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class LoginScreen : Fragment() {

    private lateinit var binding: FragmentLoginScreenBinding


//    private lateinit var telBox: EditText
//    private lateinit var passwordBox: EditText
//    private lateinit var finish: Button
//    private lateinit var register: Button
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
//    private lateinit var changeLang : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                requireActivity().finishAffinity()
            }
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_screen, container, false)

        auth = FirebaseAuth.getInstance()

        /** check first time use app */
        sharedPrefIntroApp = requireActivity().getSharedPreferences("value", 0)
        val introApp = sharedPrefIntroApp.getString("stringKeyIntro", "No")
        if(introApp =="No"){
            val editor = sharedPrefIntroApp.edit()
            editor.putString("stringKeyIntro","YES")
            editor.apply()
        }

        sharedPrefLanguage = requireActivity().getSharedPreferences("value", 0)
        sharedPrefPhone= requireActivity().getSharedPreferences("value", 0)
        sharedPrefPassword= requireActivity().getSharedPreferences("value", 0)
        sharedPrefID = requireActivity().getSharedPreferences("value", 0)
        sharedPrefDisplayName= requireActivity().getSharedPreferences("value", 0)
        sharedPrefUserType = requireActivity().getSharedPreferences("value", 0)
        sharedPrefCaretakerUser = requireActivity().getSharedPreferences("value", 0)

        val text = "EN|TH"
        val ssb = SpannableStringBuilder(text)
        val fcsWhite = ForegroundColorSpan(Color.WHITE)
        val fcsGreen = ForegroundColorSpan(Color.GREEN)

        val language = sharedPrefLanguage.getString("stringKey", "not found!")
        if(language == "en"){
            ssb.setSpan(fcsWhite, 3, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ssb.setSpan(fcsGreen, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.enTh.text = ssb

        }
        else if(language =="th"){
            ssb.setSpan(fcsWhite, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ssb.setSpan(fcsGreen, 3, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.enTh.text = ssb
        }


        /** check status */
        val stringValue = sharedPrefLanguage.getString("stringKey", "not found!")
        val stringValue2 = sharedPrefID.getString("stringKey2", "not found!")
        Log.i("CheckUserID_login", "Current User ID  : $stringValue2")
        Log.i("CheckLanguage_splash", "LoginScreen now language : $stringValue")

        binding.loginFinishButton.setOnClickListener(View.OnClickListener { login() })
        binding.regisButton.setOnClickListener(View.OnClickListener { register() })
        binding.enTh.setOnClickListener(View.OnClickListener { changeLanguage() })

        return binding.root
        
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        Log.d("LoginScreen", "onStart currentUser is :$currentUser ")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LoginScreen", " onResumed ")

    }


    private fun register() {
        findNavController().navigate(R.id.action_loginScreen_to_sendOTP)
//       val intent = Intent(this, SendOTP::class.java)
//        startActivity(intent)
    }

    private fun login() {
        (activity as MainActivity).closeKeyboard()
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
        requireActivity().baseContext.resources.updateConfiguration(config, null)
        findNavController().navigate(R.id.loginScreen)
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
//        mAlertDialog.window!!.setLayout(400,300)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)
    }




    /**receive value from realtime database (user_person) and check Login */
    private var valueEventListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val loginTel = binding.telBox.text.toString()
            val loginPassword = binding.passwordBox.text.toString()
            var count = 0
            Log.i("LoginScreen_count","Before adding listener, count=$count")
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
//                    val id = snapshot.child("id").value.toString()
                    val phone = snapshot.child("phone").value.toString()
                    val password = snapshot.child("password").value.toString()
//                    val displayName = snapshot.child("displayName").value.toString()

//                    val sex = snapshot.child("sex").value.toString()
//                    val partnerIDFirebase = snapshot.child("partner_id").value.toString()

                    Log.i("LoginScreen_count","In onDataChange, count=$count")
                    Log.i("LoginScreen_count", "Username : $loginTel , Password : $loginPassword")
                    Log.i("LoginScreen_count", "Database info :$phone,$password ")

                    if (loginTel == phone && loginPassword == password){
//                        val editorID = sharedPrefID.edit()
                        val editorPhone = sharedPrefPhone.edit()
                        val editorPassword = sharedPrefPassword.edit()
//                        val editorDisplayName = sharedPrefDisplayName.edit()
                        val editorUserType = sharedPrefUserType.edit()
//                        val editorSex = sharedPrefSex.edit()
//                        val editorPartnerID = sharedPrefPartnerID.edit()

//                        editorID.putString("stringKey2", id)
                        editorPhone.putString("stringKeyPhone", phone)
                        editorPassword.putString("stringKeyPassword", password)
//                        editorDisplayName.putString("stringKeyDisplayName", displayName)
                        editorUserType.putString("stringKeyType", "caretaker")
//                        editorSex.putString("stringKeySex", sex)
//                        editorPartnerID.putString("stringKeyPartnerID", "$partnerIDFirebase")

//                        editorID.apply()
                        editorPhone.apply()
                        editorPassword.apply()
//                        editorDisplayName.apply()
                        editorUserType.apply()
//                        editorSex.apply()
//                        editorPartnerID.apply()

                        mAlertDialog.dismiss()

//                        val intent = Intent(this@LoginScreen, MainCaretaker::class.java)
//                        startActivity(intent)
//                        view?.findNavController()?.navigate(R.id.action_loginScreen_to_caretakerFragment)
                        findNavController().navigate(R.id.action_loginScreen_to_caretakerFragment, null,
                            NavOptions.Builder().setPopUpTo(R.id.loginScreen, true).build())
                        mAlertDialog.dismiss()
                        break
                    }
                    else if (loginTel.isEmpty()  || loginPassword.isEmpty() ) {
                        mAlertDialog.dismiss()
                        Toast.makeText(activity, getString(R.string.login_empty), Toast.LENGTH_SHORT).show()
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
            val loginTel = binding.telBox.text.toString()
            val loginPassword = binding.passwordBox.text.toString()
            var count = 0
            Log.i("LoginScreen_checkLogin","Before adding listener, count=$count")
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
//                    val id = snapshot.child("id").value.toString()
                    val phone = snapshot.child("phone").value.toString()
                    val password = snapshot.child("password").value.toString()
                    val displayName = snapshot.child("displayName").value.toString()
//                    val user1 = snapshot.child("Caretaker/user1").value.toString()
//                    val user2 = snapshot.child("Caretaker/user2").value.toString()
//                    val user3 = snapshot.child("Caretaker/user3").value.toString()
//                    val user4 = snapshot.child("Caretaker/user4").value.toString()

                    Log.i("LoginScreen_checkLogin","In onDataChange, count=$count")
                    Log.i("LoginScreen_checkLogin", "Username : $loginTel , Password : $loginPassword")
                    Log.i("LoginScreen_checkLogin", "Database info :  $phone,$password ")

                    if (loginTel == phone && loginPassword == password){
                        Toast.makeText(activity, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
//                        val editorID = sharedPrefID.edit()
                        val editorPhone = sharedPrefPhone.edit()
                        val editorPassword = sharedPrefPassword.edit()
                        val editorDisplayName = sharedPrefDisplayName.edit()
                        val editorUserType = sharedPrefUserType.edit()
//                        val editorCaretakerUser = sharedPrefCaretakerUser.edit()

//                        editorID.putString("stringKey2", id)
                        editorPhone.putString("stringKeyPhone", phone)
                        editorPassword.putString("stringKeyPassword", password)
                        editorDisplayName.putString("stringKeyDisplayName", displayName)
                        editorUserType.putString("stringKeyType", "blind")
//                        editorCaretakerUser.putString("stringKeyCaretakerUser", "$user1,$user2,$user3,$user4")

                        editorPhone.apply()
                        editorPassword.apply()
//                        editorID.apply()
                        editorDisplayName.apply()
                        editorUserType.apply()
//                        editorCaretakerUser.apply()

//                        val test = sharedPrefCaretakerUser.getString("stringKeyCaretakerUser","not found!")
//                        Log.d("wtfArray","test : $test ")
//                        if(test != null){
//                            val yourArray: List<String> = test.split(",")
//                            Log.d("wtfArray","yourArray : $yourArray ")
//                            val user1 = yourArray[0]
//                            val user2 = yourArray[1]
//                            val user3 = yourArray[2]
//                            val user4 = yourArray[3]
//
//                            Log.d("wtfArray","After split -> user1 : $user1 ,  user2 : $user2 , user3 : $user3 , user4 : $user4")
//                        }

//                        view?.findNavController()?.navigate(R.id.action_loginScreen_to_blindFragment)
                       findNavController().navigate(R.id.action_loginScreen_to_blindFragment, null,
                            NavOptions.Builder().setPopUpTo(R.id.loginScreen, true).build())
//                        val intent = Intent(this@LoginScreen, MainBlind::class.java)
//                        startActivity(intent)
                        mAlertDialog.dismiss()
                        break
                    }
                    else if (loginTel.isEmpty()  || loginPassword.isEmpty() ) {
                        Toast.makeText(activity, getString(R.string.login_empty), Toast.LENGTH_SHORT).show()
                        mAlertDialog.dismiss()
                        break
                    }
                    ++count
                }
                Log.i("LoginScreen_checkLogin","After adding listener, count=$count")
                val countDatabase = dataSnapshot.childrenCount.toInt()
                if(count==countDatabase){
                    Log.i("LoginScreen_checkLogin","check count database, count=$countDatabase")
                    Log.i("LoginScreen_checkLogin","check count for loop, count=$count")
                    Toast.makeText(activity, getString(R.string.login_incorrect), Toast.LENGTH_SHORT).show()
                    mAlertDialog.dismiss()
                }
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }


}


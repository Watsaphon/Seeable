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
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.MainActivity
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.UserTypeViewModel
import com.estazo.project.seeable.app.databinding.FragmentLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class LoginScreen : Fragment() {

    private lateinit var binding: FragmentLoginScreenBinding

    private lateinit var sharedPrefIntroApp: SharedPreferences
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefDisplayName: SharedPreferences
    private lateinit var sharedPrefUserType: SharedPreferences
    private lateinit var sharedPrefCaretakerUser: SharedPreferences
    private lateinit var sharedPrefEmptyList: SharedPreferences
    private lateinit var sharedPrefLanguage: SharedPreferences

    private lateinit var auth: FirebaseAuth

    private lateinit var  mAlertDialog : AlertDialog

    private val viewModel : UserTypeViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("LoginScreen", "call onCreate")
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
        Log.i("LoginScreen", "call onCreateView")

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
        sharedPrefEmptyList = requireActivity().getSharedPreferences("value", 0)

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

        binding.loginFinishButton.setOnClickListener { login() }
        binding.regisButton.setOnClickListener { register() }
        binding.enTh.setOnClickListener { changeLanguage() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("LoginScreen", "call onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        Log.i("LoginScreen", "call -> onStart currentUser is :$currentUser ")
    }

    override fun onResume() {
        super.onResume()
        Log.i("LoginScreen", "call onResumed")
    }

    private fun register() {
        findNavController().navigate(R.id.action_loginScreen_to_sendOTP)
    }

    private fun login() {
        (activity as MainActivity).closeKeyboard()
        alertDialogLoading()
        val query = FirebaseDatabase.getInstance().getReference("users_caretaker").orderByChild("phone")
        query.addListenerForSingleValueEvent(valueEventListenerCaretaker)
    }


    /** change Language TH and EN  */
    private fun changeLanguage(){
        val language = sharedPrefLanguage.getString("stringKey", "not found!")
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
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)
    }


    /**receive value from realtime database (users_caretaker) and check Login */
    private var valueEventListenerCaretaker: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val loginTel = binding.telBox.text.toString()
            val loginPassword = binding.passwordBox.text.toString()
            var count = 0
            Log.d("LoginScreen_count","Before adding listener, count=$count")
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
                    val phone = snapshot.child("phone").value.toString()
                    val password = snapshot.child("password").value.toString()

                    Log.d("LoginScreen_count","In onDataChange, count=$count")
                    Log.d("LoginScreen_count", "Username : $loginTel , Password : $loginPassword")
                    Log.d("LoginScreen_count", "Database info :$phone,$password ")

                    if (loginTel == phone && loginPassword == password){
                        val editorPhone = sharedPrefPhone.edit()
                        val editorPassword = sharedPrefPassword.edit()
                        val editorUserType = sharedPrefUserType.edit()
                        editorPhone.putString("stringKeyPhone", phone)
                        editorPassword.putString("stringKeyPassword", password)
                        editorUserType.putString("stringKeyType", "caretaker")
                        editorPhone.apply()
                        editorPassword.apply()
                        editorUserType.apply()

                        viewModel.userType.value = "caretaker"

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
                Log.d("LoginScreen_count","After adding listener, count=$count")
                val countDatabase = dataSnapshot.childrenCount.toInt()
                if(count==countDatabase){
                    /**if not found user in user_person -> find in users_blind */
                    val query2 = FirebaseDatabase.getInstance().getReference("users_blind").orderByChild("phone")
                    query2.addListenerForSingleValueEvent(valueEventListenerBlind)
                }
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

    /**receive value from realtime database (users_blind) and check Login */
    private var valueEventListenerBlind: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val loginTel = binding.telBox.text.toString()
            val loginPassword = binding.passwordBox.text.toString()
            var count = 0
            Log.d("LoginScreen_count","Before adding listener, count=$count")
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
                    val phone = snapshot.child("phone").value.toString()
                    val password = snapshot.child("password").value.toString()
                    val displayName = snapshot.child("displayName").value.toString()

                    Log.d("LoginScreen_count","In onDataChange, count=$count")
                    Log.d("LoginScreen_count", "Username : $loginTel , Password : $loginPassword")
                    Log.d("LoginScreen_count", "Database info :  $phone,$password ")

                    if (loginTel == phone && loginPassword == password){
                        Toast.makeText(activity, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                        val editorPhone = sharedPrefPhone.edit()
                        val editorPassword = sharedPrefPassword.edit()
                        val editorDisplayName = sharedPrefDisplayName.edit()
                        val editorUserType = sharedPrefUserType.edit()
                        editorPhone.putString("stringKeyPhone", phone)
                        editorPassword.putString("stringKeyPassword", password)
                        editorDisplayName.putString("stringKeyDisplayName", displayName)
                        editorUserType.putString("stringKeyType", "blind")
                        editorPhone.apply()
                        editorPassword.apply()
                        editorDisplayName.apply()
                        editorUserType.apply()

                        viewModel.userType.value = "blind"

                       findNavController().navigate(R.id.action_loginScreen_to_blindFragment, null,
                            NavOptions.Builder().setPopUpTo(R.id.loginScreen, true).build())
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
                Log.i("LoginScreen_count","After adding listener, count=$count")
                val countDatabase = dataSnapshot.childrenCount.toInt()
                if(count==countDatabase){
                    Log.d("LoginScreen_count","check count database, count=$countDatabase")
                    Log.d("LoginScreen_count","check count for loop, count=$count")
                    Toast.makeText(activity, getString(R.string.login_incorrect), Toast.LENGTH_SHORT).show()
                    mAlertDialog.dismiss()
                }
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

}


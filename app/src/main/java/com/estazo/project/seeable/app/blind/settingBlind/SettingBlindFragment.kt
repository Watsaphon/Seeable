package com.estazo.project.seeable.app.blind.settingBlind

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import java.util.*
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.login.LoginScreen
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentSettingBlindBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class SettingBlindFragment : Fragment() {

    private lateinit var backButton: ImageButton
    private lateinit var lang: Button
    private lateinit var account: Button
    private lateinit var setLocation: Button
    private lateinit var manageCare: Button
    private lateinit var logout: Button

    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefDisplayName: SharedPreferences
    private lateinit var sharedPrefUserType : SharedPreferences
    private lateinit var sharedPrefCaretakerUser : SharedPreferences

    private lateinit var  mAlertDialog : AlertDialog

    private lateinit var binding : FragmentSettingBlindBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_setting_blind)
//
//        backButton = findViewById(R.id.back_button)
//        lang = findViewById(R.id.langBtn)
//        account = findViewById(R.id.accountBtn)
//        setLocation = findViewById(R.id.locationBtn)
//        manageCare = findViewById(R.id.manageUserBtn)
//        logout = findViewById(R.id.logoutBtn)

//        sharedPrefLanguage = getSharedPreferences("value", 0)
//
//        sharedPrefID = getSharedPreferences("value", 0)
//        sharedPrefPhone= getSharedPreferences("value", 0)
//        sharedPrefPassword= getSharedPreferences("value", 0)
//        sharedPrefDisplayName= getSharedPreferences("value", 0)
//        sharedPrefUserType = getSharedPreferences("value", 0)
//        sharedPrefCaretakerUser = getSharedPreferences("value", 0)
//
//        lang.setOnClickListener {
//            changeLanguage()
//        }
//        account.setOnClickListener {
//            account()
//        }
//        setLocation.setOnClickListener {
//            searchLocation()
//        }
//        manageCare.setOnClickListener {
////            manageCaretaker()
//            Toast.makeText(this@SettingBlind," manage Caretaker User",Toast.LENGTH_SHORT).show()
//        }
//        logout.setOnClickListener {
//            gotoLogout()
//        }
//        backButton.setOnClickListener {
//            onBackPressed()
//        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_blind, container, false)

        sharedPrefLanguage = requireActivity().getSharedPreferences("value", 0)

        sharedPrefID = requireActivity().getSharedPreferences("value", 0)
        sharedPrefPhone= requireActivity().getSharedPreferences("value", 0)
        sharedPrefPassword= requireActivity().getSharedPreferences("value", 0)
        sharedPrefDisplayName= requireActivity().getSharedPreferences("value", 0)
        sharedPrefUserType = requireActivity().getSharedPreferences("value", 0)
        sharedPrefCaretakerUser = requireActivity().getSharedPreferences("value", 0)

        binding.langBtn.setOnClickListener {
            changeLanguage()
        }
        binding.accountBtn.setOnClickListener {
            account()
        }
        binding.locationBtn.setOnClickListener {
            searchLocation()
        }
        binding.manageUserBtn.setOnClickListener {
//            manageCaretaker()
            Toast.makeText(activity," manage Caretaker User",Toast.LENGTH_SHORT).show()
        }
        binding.logoutBtn.setOnClickListener {
            gotoLogout()
        }
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

    return binding.root
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
        requireActivity().baseContext.resources.updateConfiguration(config, null)
        requireActivity().onBackPressed()

    }

    private fun account(){
        findNavController().navigate(R.id.action_settingBlindFragment2_to_accountBlindFragment)
    }

    private fun searchLocation(){
        alertDialogLoading()
        findNavController().navigate(R.id.action_settingBlindFragment2_to_searchLocationFragment)
    }

    private fun manageCaretaker(){

    }

    private fun gotoLogout(){

        val editorID = sharedPrefID.edit()
        val editorPhone = sharedPrefPhone.edit()
        val editorPassword = sharedPrefPassword.edit()
        val editorDisplay = sharedPrefDisplayName.edit()
        val editorUserType = sharedPrefUserType.edit()
        val editorCaretakerUser = sharedPrefCaretakerUser.edit()

        editorID.putString("stringKey2", "not found!")
        editorPhone.putString("stringKeyPhone", "not found!")
        editorPassword.putString("stringKeyPassword", "not found!")
        editorDisplay.putString("stringKeyDisplayName", "not found!")
        editorUserType.putString("stringKeyType", "not found!")
        editorCaretakerUser.putString("stringKeyCaretakerUser", "not found!")

        editorID.apply()
        editorPhone.apply()
        editorPassword.apply()
        editorDisplay.apply()
        editorUserType.apply()
        editorCaretakerUser.apply()

        findNavController().navigate(R.id.action_settingBlindFragment2_to_splashScreen, null,
            NavOptions.Builder().setPopUpTo(R.id.loginScreen, false).build())

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


    override fun onPause() {
        super.onPause()
        Log.i("SettingBlindFragment", "onPause call")
    }

    override fun onStop() {
        super.onStop()
        Log.i("SettingBlindFragment", "onStop call")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("SettingBlindFragment", "onDestroyView call")
        mAlertDialog.dismiss()

    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("SettingBlindFragment", "onDestroy call")
    }
    override fun onDetach() {
        super.onDetach()
        Log.i("SettingBlindFragment", "onDetach call")
    }

}
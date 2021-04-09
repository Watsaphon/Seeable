package com.estazo.project.seeable.app

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.databinding.FragmentSplashScreenBinding
import java.util.*


var checkSuccess : Boolean = false

class SplashScreen : Fragment() {

//    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var binding : FragmentSplashScreenBinding
    private val viewModel : UserTypeViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash_screen, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**Shared Preferences เป็นคลาสที่ใช้สำหรับเก็บข้อมูลถาวรที่เป็นค่าของตัวแปรธรรมดาๆ อย่างเช่น Boolean,Int,Float*/
        val sharedPrefIntroApp = requireActivity().getSharedPreferences("value", 0)
        val introApp = sharedPrefIntroApp.getString("stringKeyIntro", "No")

        val sharedPrefLanguage = requireActivity().getSharedPreferences("value", 0)
        val language = sharedPrefLanguage.getString("stringKey", "en")
        val editor = sharedPrefLanguage.edit()

        val sharedPrefPhone = requireActivity().getSharedPreferences("value", 0)
        val login = sharedPrefPhone.getString("stringKeyPhone","not found!")
        val editor2 = sharedPrefPhone.edit()

        val sharedPrefUserType = requireActivity().getSharedPreferences("value", 0)
        val userType = sharedPrefUserType.getString("stringKeyType","not found!")
        val editor3 = sharedPrefUserType.edit()

        var locale: Locale? = null

        Log.i("CheckUserID_splash", "First User ID  : $login")
        Log.i("CheckLanguage_splash", "First Language is :$language")

        /** Check Language in app*/
        if(language=="en"){
            locale = Locale("en")
            Locale.setDefault(locale)
            editor.putString("stringKey", "en")
            Log.i("CheckLanguage_splash", "  check if :  Language is$language")
        }
        else if(language=="th"){
            locale = Locale("th")
            Locale.setDefault(locale)
            editor.putString("stringKey", "th")
            Log.i("CheckLanguage_splash", " check else-if :  Language is $language")
        }
        editor.apply()
        editor2.apply()
        editor3.apply()

        val config = Configuration()
        config.locale = locale
        requireActivity().baseContext.resources.updateConfiguration(config, null)

        Log.i("CheckLanguage_splash", "Now Language is :$language")
        Log.i("CheckUserTypes_plash", "Now User Type is :$userType")

        Log.i("checkStatus"," login : $login , userType : $userType")
        Log.i("checkStatus", "First Login :$introApp")

        /** Check user status in app*/
        if(introApp == "No"){
            findNavController().navigate(R.id.action_splashScreen_to_introduceAppFragment, null,
                NavOptions.Builder().setPopUpTo(R.id.splashScreen, true).build())
            Log.i("testss","intro app ja")
        }
        else{
            if(login != "not found!" && userType != "not found!" ){
                when (userType) {
                    "caretaker" -> {
                        viewModel.userType.value = "caretaker"
                        findNavController().navigate(R.id.action_splashScreen_to_caretakerFragment, null,
                            NavOptions.Builder().setPopUpTo(R.id.splashScreen, true).build())
                        Log.i("testss","caretker ja")
                    }
                    "blind" -> {
                        viewModel.userType.value = "blind"
                        findNavController().navigate(R.id.action_splashScreen_to_blindFragment, null,
                            NavOptions.Builder().setPopUpTo(R.id.splashScreen, true).build())
                        Log.i("testss","blind ja")
                    }
                }
            }
            else{
                Log.i("testss","no user ja")
                viewModel.userType.value = "not found!"
                findNavController().navigate(R.id.action_splashScreen_to_loginScreen, null,
                    NavOptions.Builder().setPopUpTo(R.id.splashScreen, true).build())
            }
        }
    }


}

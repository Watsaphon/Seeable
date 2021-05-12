package com.estazo.project.seeable.app.blind.settingBlind

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.UserTypeViewModel
import com.estazo.project.seeable.app.databinding.FragmentSettingBlindBinding
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class SettingBlindFragment : Fragment() {

    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefDisplayName: SharedPreferences
    private lateinit var sharedPrefUserType : SharedPreferences
    private lateinit var sharedPrefCaretakerUser : SharedPreferences

    private lateinit var  mAlertDialog : AlertDialog

    private lateinit var binding : FragmentSettingBlindBinding

    private val viewModel : UserTypeViewModel by activityViewModels()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("SettingBlind", "onAttach call")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("SettingBlind", "onCreate call")
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

        viewModel.userType.observe(viewLifecycleOwner, Observer { typeView ->
            Log.d("checkViewModel","type : $typeView")
        })

    return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.langBtn.setOnClickListener {
            changeLanguage()
        }
        binding.accountBtn.setOnClickListener {
            account()
        }
        binding.locationBtn.setOnClickListener {
            searchLocation()
        }
        binding.logoutBtn.setOnClickListener {
            gotoLogout()
        }
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    /** change Language TH and EN*/
    private fun changeLanguage(){
        val language = sharedPrefLanguage.getString("stringKey", "not found!")
        Log.i("SplashScreenMain", "Now Language is :$language ")
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
        viewModel.userType.value = "not found!"

        val phone = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
        Log.i("kubkub", "phone : $phone")
        val ref = FirebaseDatabase.getInstance().reference
        val childUpdates = hashMapOf<String, Any>("users_blind/$phone/FCM" to "-")
        ref.updateChildren(childUpdates)

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

        findNavController().navigate(R.id.action_settingBlindFragment2_to_loginScreen, null,
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
//        mAlertDialog.dismiss()
        if (this::mAlertDialog.isInitialized) {
            if (mAlertDialog.isShowing) {
                Log.i("pppp", "alert is showing in if")
                mAlertDialog.dismiss()
            }
        }
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
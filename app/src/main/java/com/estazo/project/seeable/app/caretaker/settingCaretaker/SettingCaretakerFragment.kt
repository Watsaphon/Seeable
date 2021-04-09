package com.estazo.project.seeable.app.caretaker.settingCaretaker

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
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.UserTypeViewModel
import com.estazo.project.seeable.app.databinding.FragmentSettingCaretakerBinding
import java.util.*
import androidx.lifecycle.Observer


class SettingCaretakerFragment : Fragment() {

    private lateinit var binding : FragmentSettingCaretakerBinding

    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var language : String

    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefDisplayName: SharedPreferences
    private lateinit var sharedPrefUserType : SharedPreferences
    private lateinit var sharedPrefBlindId : SharedPreferences

    private val viewModel : UserTypeViewModel by activityViewModels()
//    private val caretakerViewModel: CaretakerViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("SettingCaretaker", "onAttach call")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("SettingCaretaker", "onCreate call")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_caretaker, container, false)
//        val fragmentBinding = FragmentSettingCaretakerBinding.inflate(inflater, container, false)
//        binding = fragmentBinding
        Log.i("SettingCaretaker", "onCreateView call")

        sharedPrefLanguage = requireActivity().getSharedPreferences("value", 0)
        language = sharedPrefLanguage.getString("stringKey", "not found!").toString()

        sharedPrefPhone= requireActivity().getSharedPreferences("value", 0)
        sharedPrefID = requireActivity().getSharedPreferences("value", 0)
        sharedPrefPassword= requireActivity().getSharedPreferences("value", 0)
        sharedPrefDisplayName= requireActivity().getSharedPreferences("value", 0)
        sharedPrefUserType = requireActivity().getSharedPreferences("value", 0)
        sharedPrefBlindId = requireActivity().getSharedPreferences("value", 0)

        Log.i("SettingCaretaker", " language : $language")

        viewModel.userType.observe(viewLifecycleOwner, Observer { typeView ->
            Log.d("eieiei_care","type : $typeView")
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener{
            requireActivity().onBackPressed()
        }

        binding.langBtn.setOnClickListener{
//        (activity as MainActivityPerson).changeLanguage()
            changeLanguage()
        }
        
        binding.blindlistBtn.setOnClickListener{view : View  ->
            view.findNavController().navigate(R.id.action_settingCaretakerFragment_to_blindListFragment)
        }

        binding.accountBtn.setOnClickListener{view : View  ->
            view.findNavController().navigate(R.id.action_settingCaretakerFragment_to_accountSettingFragment)
        }

        binding.logoutBtn.setOnClickListener{
        logout()
        }
    }


    /** change Language TH and EN*/
    private fun changeLanguage(){
        Log.i("CheckLanguageSetting", "Now Language is :$language")
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("SettingCaretaker", "onActivityCreated call")
    }

    private fun logout(){
        viewModel.userType.value = "not found!"

        val editorID = sharedPrefID.edit()
        val editorPhone = sharedPrefPhone.edit()
        val editorPassword = sharedPrefPassword.edit()
        val editorDisplayName = sharedPrefDisplayName.edit()
        val editorUserType = sharedPrefUserType.edit()
        val editorBlindId = sharedPrefBlindId.edit()

        editorPhone.putString("stringKeyPhone", "not found!")
        editorPassword.putString("stringKeyPassword", "not found!")
        editorID.putString("stringKey2", "not found!")
        editorDisplayName.putString("stringKeyDisplayName","not found!")
        editorUserType.putString("stringKeyType", "not found!")
        editorBlindId.putString("stringKeyBlindId", "not found!")

        editorPhone.apply()
        editorPassword.apply()
        editorID.apply()
        editorUserType.apply()
        editorDisplayName.apply()
        editorBlindId.apply()
                findNavController().navigate(R.id.action_settingCaretakerFragment_to_loginScreen, null,
                NavOptions.Builder().setPopUpTo(R.id.loginScreen, true).build())
    }

    override fun onStart() {
        super.onStart()
        Log.i("SettingCaretaker", "onStart call")
    }
    override fun onResume() {
        super.onResume()
        Log.i("SettingCaretaker", "onResume call")
    }
    override fun onPause() {
        super.onPause()
        Log.i("SettingCaretaker", "onPause call")
    }
    override fun onStop() {
        super.onStop()
        Log.i("SettingCaretaker", "onStop call")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("SettingCaretaker", "onDestroyView call")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("SettingCaretaker", "onDestroy call")
    }
    override fun onDetach() {
        super.onDetach()
        Log.i("SettingCaretaker", "onDetach call")
    }


}
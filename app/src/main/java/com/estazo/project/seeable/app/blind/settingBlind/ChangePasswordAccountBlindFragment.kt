package com.estazo.project.seeable.app.blind.settingBlind

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentChangePasswordAccountBlindBinding
import com.google.firebase.database.FirebaseDatabase

class ChangePasswordAccountBlindFragment : Fragment() {

    private lateinit var binding : FragmentChangePasswordAccountBlindBinding

    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("CPAccountBlindFragment", "onCreate call")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password_account_blind, container, false)
        Log.i("CPAccountBlindFragment", "onCreateView call")

        sharedPrefPhone = requireActivity().getSharedPreferences("value", 0)
        sharedPrefPassword = requireActivity().getSharedPreferences("value", 0)
        binding.backButton.setOnClickListener{
            requireActivity().onBackPressed()
        }
        binding.saveChangePassword.setOnClickListener{
            saveChangePassword()
        }
        return binding.root
    }

    private fun saveChangePassword() {
        val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
        val password = sharedPrefPassword.getString("stringKeyPassword","not found!")
        val editCurrent = binding.editCurrentPassword.text.toString()
        val editNew = binding.editNewPassword.text.toString()

        Log.d("savePassword","phone :$currentPhone , password : $password , editCurrent :$editCurrent , editNew :$editNew ")

        if( password == editCurrent && password != editNew && editCurrent != editNew && editNew.isNotBlank() && editCurrent.isNotBlank() ){
            val ref = FirebaseDatabase.getInstance().reference
            ref.child("users_blind/$currentPhone/password").setValue(editNew)
            val editor = sharedPrefPassword.edit()
            editor.putString("stringKeyPassword", editNew)
            editor.apply()
            findNavController().navigate(R.id.action_changePasswordAccountBlindFragment_to_blindFragment)
        }
        else if(editCurrent.isBlank()){
            Toast.makeText(activity,R.string.checkNull_current_ChangePassword, Toast.LENGTH_SHORT).show()
        }
        else if( editNew.isBlank()){
            Toast.makeText(activity,R.string.checkNull_new_ChangePassword, Toast.LENGTH_SHORT).show()
        }
        else if( password != editCurrent){
            Toast.makeText(activity,R.string.check_current_ChangePassword, Toast.LENGTH_SHORT).show()
        }
        else if( editNew == editCurrent){
            Toast.makeText(activity,R.string.check_new_ChangePassword, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        binding.editCurrentPassword.text.clear()
        binding.editNewPassword.text.clear()
    }

}
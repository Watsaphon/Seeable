package com.estazo.project.seeable.app.blind.settingBlind

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.login.LoginScreen
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentDeleteAccountBlindBinding
import com.google.firebase.database.FirebaseDatabase

class DeleteAccountBlindFragment : Fragment() {

    private lateinit var binding : FragmentDeleteAccountBlindBinding

//    private lateinit var back : View
//    private lateinit var editPassword : EditText
//    private lateinit var delete : Button

    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefDisplayName: SharedPreferences
    private lateinit var sharedPrefUserType : SharedPreferences
    private lateinit var sharedPrefCaretakerUser : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_delete_account_blind)

//        back = findViewById(R.id.back_button)
//        editPassword = findViewById(R.id.editPassword)
//        delete = findViewById(R.id.delete)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_delete_account_blind, container, false)

        sharedPrefID = requireActivity().getSharedPreferences("value", 0)
        sharedPrefPhone= requireActivity().getSharedPreferences("value", 0)
        sharedPrefPassword= requireActivity().getSharedPreferences("value", 0)
        sharedPrefDisplayName= requireActivity().getSharedPreferences("value", 0)
        sharedPrefUserType = requireActivity().getSharedPreferences("value", 0)
        sharedPrefCaretakerUser = requireActivity().getSharedPreferences("value", 0)

        binding.backButton.setOnClickListener{
            requireActivity().onBackPressed()
        }
        binding.delete.setOnClickListener{
            deleteAccount()
        }

         return binding.root
    }

        private fun deleteAccount(){
        val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
        val password = sharedPrefPassword.getString("stringKeyPassword","not found!")
        val confirmPassword = binding.editPassword.text.toString()

        Log.d("deleteAccount"," password : $password , confirmPassword :$confirmPassword ")

        when {
            password == confirmPassword -> {
                val ref = FirebaseDatabase.getInstance().reference
                ref.child("users_blind/$currentPhone").removeValue()

                val editorPhone = sharedPrefPhone.edit()
                val editorPassword = sharedPrefPassword.edit()
                val editorID = sharedPrefID.edit()
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

                findNavController().navigate(R.id.action_deleteAccountBlindFragment_to_loginScreen)
                
            }
            confirmPassword.isEmpty() -> {
                Toast.makeText(activity,R.string.checkNull_current_ChangePassword, Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(activity,R.string.checkPassword_DeleteAccountBlind, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onStop() {
        super.onStop()
        binding.editPassword.text.clear()
    }

}
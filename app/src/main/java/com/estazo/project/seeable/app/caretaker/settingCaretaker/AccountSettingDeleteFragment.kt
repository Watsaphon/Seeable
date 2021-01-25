package com.estazo.project.seeable.app.caretaker.settingCaretaker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentAccountSettingDeleteBinding
import com.estazo.project.seeable.app.login.LoginScreen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AccountSettingDeleteFragment : Fragment() {

    private lateinit var binding : FragmentAccountSettingDeleteBinding

    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private lateinit var sharedPrefID: SharedPreferences
    private lateinit var sharedPrefDisplayName: SharedPreferences
    private lateinit var sharedPrefUserType : SharedPreferences

    private lateinit var phone  : String
    private lateinit var password  : String
    private lateinit var confirmPassword  : String
    private lateinit var id : String
    private lateinit var displayName : String
    private lateinit var userType: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_setting_delete, container, false)

        sharedPrefPhone = requireActivity().getSharedPreferences("value", 0)
        sharedPrefPassword = requireActivity().getSharedPreferences("value", 0)
        sharedPrefID = requireActivity().getSharedPreferences("value", 0)
        sharedPrefDisplayName= requireActivity().getSharedPreferences("value", 0)
        sharedPrefUserType = requireActivity().getSharedPreferences("value", 0)

        phone = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
        password = sharedPrefPhone.getString("stringKeyPassword", "not found!").toString()
        id  = sharedPrefID.getString("stringKey2", "not found!").toString()
        displayName  = sharedPrefDisplayName.getString("stringKeyDisplayName", "not found!").toString()
        userType  = sharedPrefUserType.getString("stringKeyType", "not found!").toString()

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.delete.setOnClickListener {
        confirmPassword = binding.confirmPassword.text.toString()
            when {
                confirmPassword.isEmpty() -> {
                    Toast.makeText(context, getString(R.string. empty_password_AccountSettingEditCaretaker), Toast.LENGTH_SHORT).show()
                }
                password == confirmPassword -> {
                    val query = FirebaseDatabase.getInstance().getReference("users_caretaker").child("$phone")
                    query.addListenerForSingleValueEvent(valueEventListener)
                }
                else -> {
                    Toast.makeText(context, getString(R.string.wrong_password_AccountSettingEditCaretaker), Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }


    /**remove user from realtime database (users_caretaker) */
    private var valueEventListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                dataSnapshot.ref.removeValue()
                logout()
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

    private fun logout() {
        val editorID = sharedPrefID.edit()
        val editorPhone = sharedPrefPhone.edit()
        val editorPassword = sharedPrefPassword.edit()
        val editorDisplay = sharedPrefDisplayName.edit()
        val editorUserType = sharedPrefUserType.edit()

        editorID.putString("stringKey2", "not found!")
        editorPhone.putString("stringKeyPhone", "not found!")
        editorPassword.putString("stringKeyPassword", "not found!")
        editorDisplay.putString("stringKeyDisplayName", "not found!")
        editorUserType.putString("stringKeyType", "not found!")

        editorID.apply()
        editorPhone.apply()
        editorPassword.apply()
        editorDisplay.apply()
        editorUserType.apply()

        val intent = Intent(activity, LoginScreen::class.java)
        startActivity(intent)

    }

}
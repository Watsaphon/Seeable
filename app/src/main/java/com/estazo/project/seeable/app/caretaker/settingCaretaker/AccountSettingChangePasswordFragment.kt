package com.estazo.project.seeable.app.caretaker.settingCaretaker

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentAccountSettingChangePasswordBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AccountSettingChangePasswordFragment : Fragment() {

    private lateinit var binding : FragmentAccountSettingChangePasswordBinding
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences

    private lateinit var phone  : String
    private lateinit var passwordPref  : String
    private lateinit var oldPassword  : String
    private lateinit var newPassword  : String
    private lateinit var confirmPassword  : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_setting_change_password ,container, false)

        sharedPrefPhone = requireActivity().getSharedPreferences("value", 0)
        sharedPrefPassword = requireActivity().getSharedPreferences("value", 0)

        phone = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
        passwordPref = sharedPrefPhone.getString("stringKeyPassword", "not found!").toString()

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.saveBtn.setOnClickListener{
            oldPassword = binding.editOldPassword.text.toString()
            newPassword = binding.editNewPassword.text.toString()
            confirmPassword = binding.editConfirmPassword.text.toString()
            Log.i("changePassword","oldPassword : $oldPassword , newPassword : $newPassword ," +
                    " confirmPassword :$confirmPassword , passwordPref : $passwordPref  ")
            when {
                oldPassword.isEmpty() -> {
                    Toast.makeText(activity,R.string.old_empty_ChangePassword, Toast.LENGTH_SHORT).show()
                    binding.editOldPassword.error = getString(R.string.old_empty_ChangePassword)
                }
                 newPassword.isEmpty() ->{
                     Toast.makeText(activity,R.string.new_empty_ChangePassword, Toast.LENGTH_SHORT).show()
                     binding.editNewPassword.error = getString(R.string.new_empty_ChangePassword)
                 }
                confirmPassword.isEmpty() ->{
                    Toast.makeText(activity,R.string.confirm_empty_ChangePassword, Toast.LENGTH_SHORT).show()
                    binding.editConfirmPassword.error = getString(R.string.confirm_empty_ChangePassword)
                }
                oldPassword != passwordPref  ->{
                    Toast.makeText(activity,R.string.old_incorrect_ChangePassword, Toast.LENGTH_SHORT).show()
                    binding.editOldPassword.error = getString(R.string.old_incorrect_ChangePassword)
                }
                oldPassword == passwordPref && newPassword != confirmPassword  ->{
                    Toast.makeText(activity,R.string.newPassword_incorrect_ChangePassword, Toast.LENGTH_SHORT).show()
                    binding.editNewPassword.error = getString(R.string.newPassword_incorrect_ChangePassword)
                    binding.editConfirmPassword.error = getString(R.string.newPassword_incorrect_ChangePassword)
                }
                oldPassword == passwordPref && newPassword == confirmPassword && newPassword == oldPassword ->{
                    Toast.makeText(activity,R.string.password_same_ChangePassword, Toast.LENGTH_SHORT).show()
                    binding.editNewPassword.error = getString(R.string.password_same_ChangePassword)
                    binding.editConfirmPassword.error = getString(R.string.password_same_ChangePassword)
                }
                oldPassword == passwordPref && newPassword == confirmPassword && newPassword != oldPassword ->{
                    val editor = sharedPrefPassword.edit()
                    editor.putString("stringKeyPassword",newPassword )
                    editor.apply()
                    val query = FirebaseDatabase.getInstance().getReference("users_caretaker").child("$phone/password")
                    query.addListenerForSingleValueEvent(valueEventListener)
                    Toast.makeText(activity,R.string.successfully_AccountSettingEditCaretaker, Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                }

            }
        }

        return binding.root
    }

    /**receive value from realtime database (users_blind) and check Login */
    private var valueEventListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                passwordPref = sharedPrefPhone.getString("stringKeyPassword", "not found!").toString()
                newPassword = binding.editNewPassword.text.toString()
                val ref = FirebaseDatabase.getInstance().reference
                val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/password" to "$newPassword")
                ref.updateChildren(childUpdates)

            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

}
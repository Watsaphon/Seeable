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
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.MainActivity
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.caretaker.CaretakerViewModel
import com.estazo.project.seeable.app.databinding.FragmentAccountBlindBinding
import com.google.firebase.database.FirebaseDatabase

class AccountBlindFragment : Fragment() {

    private lateinit var binding : FragmentAccountBlindBinding

    private lateinit var viewModel: AccountBlindViewModel

//    private lateinit var back : View
//    private lateinit var displayName : TextView
//    private lateinit var phone : TextView
//    private lateinit var editName : Button
//    private lateinit var changePass : Button
//    private lateinit var deleteUser : Button

    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefDisplayName: SharedPreferences

    private lateinit var displayName : String
    private lateinit var phone : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_account_blind)

//        back = findViewById(R.id.back_button)
//        displayName = findViewById(R.id.displayName)
//        phone = findViewById(R.id.phone)
//        editName = findViewById(R.id.editName)
//        changePass = findViewById(R.id.changePassword)
//        deleteUser = findViewById(R.id.deleteUser)



    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_blind, container, false)

        sharedPrefLanguage = requireActivity().getSharedPreferences("value", 0)
        sharedPrefPhone = requireActivity().getSharedPreferences("value", 0)
        sharedPrefDisplayName = requireActivity().getSharedPreferences("value", 0)

        val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
        phone = currentPhone.toString()
        val currentDisplay = sharedPrefDisplayName.getString("stringKeyDisplayName","not found!")
        val language = sharedPrefLanguage.getString("stringKey", "not found!")

        if(language == "en"){
            binding.phone.text = "Phone : $currentPhone"
            binding.displayName.text = currentDisplay
        }
        else if(language =="th"){
            binding.phone.text = "เบอร์โทรศัพท์ : $currentPhone"
            binding.displayName.text = currentDisplay
        }

        Log.d("accountB","currentPhone :$currentPhone , currentDisplay : $currentDisplay")


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener{
            requireActivity().onBackPressed()
        }
        binding.editNameButton.setOnClickListener {
            updateDisplayName()
        }
        binding.saveBtn.setOnClickListener {
            (activity as MainActivity).closeKeyboard()
            addDisplayName()
        }
        binding.changePassword.setOnClickListener{
            findNavController().navigate(R.id.action_accountBlindFragment_to_changePasswordAccountBlindFragment)
        }
        binding.deleteUser.setOnClickListener{
            findNavController().navigate(R.id.action_accountBlindFragment_to_deleteAccountBlindFragment)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(AccountBlindViewModel::class.java)
//        val currentPhone = sharedPrefPhone.getString("stringKeyPhone", "not found!")
        if (phone.isNotEmpty()) {
            viewModel.getDisplayName("$phone")
        }


        viewModel.userDisplay.observe(viewLifecycleOwner, Observer<String>{user ->
            Log.i("resume","user : $user")
            binding.displayName.text = user
            displayName = user
        })

    }

    private fun updateDisplayName() {
        binding.editName.visibility = View.VISIBLE
        binding.saveBtn.visibility = View.VISIBLE

        binding.displayName.visibility = View.GONE
        binding.editNameButton.visibility = View.GONE

        // Set the focus to the edit text.
        binding.editName.requestFocus()

    }

    private fun addDisplayName() {
        var updateName : String  = ""
        updateName =  binding.editName.text.toString()

        when {
            updateName.isEmpty() -> {
                Toast.makeText(activity,R.string.update_empty_BlindInformationFragment, Toast.LENGTH_LONG).show()
            }
            updateName == displayName -> {
                Toast.makeText(activity,R.string.update_same_BlindInformationFragment, Toast.LENGTH_LONG).show()
            }
            else -> {
                displayName = updateName
                binding.displayName.text = updateName

                val ref = FirebaseDatabase.getInstance().reference
                val childUpdates = hashMapOf<String,Any>("users_blind/$phone/displayName" to updateName)
                ref.updateChildren(childUpdates)
//                val childUpdates2 = hashMapOf<String, Any>("users_caretaker/$phoneCaretaker/Blind/user$positionBlindUser" to "$selectPhoneBlind/$updateName")
//                ref.updateChildren(childUpdates2)

                binding.editName.visibility = View.GONE
                binding.saveBtn.visibility = View.GONE

                binding.displayName.visibility = View.VISIBLE
                binding.editNameButton.visibility = View.VISIBLE


                binding.editName.text.clear()

                Toast.makeText(activity,R.string.update_success_BlindInformationFragment, Toast.LENGTH_LONG).show()

            }
        }
    }

}
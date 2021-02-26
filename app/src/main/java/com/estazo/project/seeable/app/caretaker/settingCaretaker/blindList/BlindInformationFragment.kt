package com.estazo.project.seeable.app.caretaker.settingCaretaker.blindList

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.caretaker.MainActivityPerson
import com.estazo.project.seeable.app.databinding.FragmentBlindInformationBinding
import com.estazo.project.seeable.app.helperClass.Locations
import com.google.firebase.database.FirebaseDatabase


class BlindInformationFragment : Fragment() {

    private lateinit var binding : FragmentBlindInformationBinding

    private val blindInfoViewModel : BlindInformationViewModel by activityViewModels()

    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var phoneCaretaker  : String
    private lateinit var language : String

    private lateinit var selectPhoneBlind: String
    private lateinit var selectUsernameBlind: String
    private lateinit var  positionBlindUser : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("BlindInformation", "onCreate call")
        sharedPrefLanguage = requireActivity().getSharedPreferences("value", 0)
        sharedPrefPhone= requireActivity().getSharedPreferences("value", 0)

        language = sharedPrefLanguage.getString("stringKey", "not found!").toString()
        phoneCaretaker  = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blind_information, container, false)
        Log.i("BlindInformation", "onCreateView call")

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("BlindInformation", "onViewCreated call")

        binding.editNameButton.setOnClickListener {
            updateDisplayName()
        }
        binding.saveBtn.setOnClickListener {
            addDisplayName()
        }

        blindInfoViewModel.user.observe(viewLifecycleOwner, Observer<String> { user ->
            selectUsernameBlind = user.toString()
            binding.displayName.text = selectUsernameBlind
        })
        blindInfoViewModel.phone.observe(viewLifecycleOwner, Observer<String> { phone ->
            selectPhoneBlind = phone.toString()
            if(language == "en"){
                binding.phone.text = "Phone : $selectPhoneBlind"
            }
            else if(language =="th"){
                binding.phone.text = "เบอร์โทรศัพท์ : $selectPhoneBlind"
            }

        })
        blindInfoViewModel.position.observe(viewLifecycleOwner, Observer<Int> { position ->
            positionBlindUser = position.toString()
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("BlindInformation", "onActivityCreated call")
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
        val oldName : String = selectUsernameBlind
        var updateName : String  = ""
        updateName =  binding.editName.text.toString()

        when {
            updateName.isEmpty() -> {
                Toast.makeText(activity,"Please fill your new name", Toast.LENGTH_LONG).show()
            }
            updateName == selectUsernameBlind -> {
                Toast.makeText(activity,"The new name can't be the same as the old name.", Toast.LENGTH_LONG).show()
            }
            else -> {
                binding.displayName.text = updateName

                val ref = FirebaseDatabase.getInstance().reference
                val childUpdates = hashMapOf<String, Any>("users_blind/$selectPhoneBlind/displayName" to updateName)
                ref.updateChildren(childUpdates)
                val childUpdates2 = hashMapOf<String, Any>("users_caretaker/$phoneCaretaker/Blind/user$positionBlindUser" to "$selectPhoneBlind/$updateName")
                ref.updateChildren(childUpdates2)


                binding.editName.visibility = View.GONE
                binding.saveBtn.visibility = View.GONE

                binding.displayName.visibility = View.VISIBLE
                binding.editNameButton.visibility = View.VISIBLE

                (activity as MainActivityPerson).closeKeyboard()
                Toast.makeText(activity,"Update name successfully.", Toast.LENGTH_LONG).show()

            }
        }
    }

}
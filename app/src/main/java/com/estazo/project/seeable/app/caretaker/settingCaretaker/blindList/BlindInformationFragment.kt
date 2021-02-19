package com.estazo.project.seeable.app.caretaker.settingCaretaker.blindList

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentBlindInformationBinding


class BlindInformationFragment : Fragment() {

    private lateinit var binding : FragmentBlindInformationBinding

    private val blindInfoViewModel : BlindInformationViewModel by activityViewModels()

    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var language : String

    private lateinit var selectPhone: String
    private lateinit var selectUsername: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("BlindInformation", "onCreate call")
        sharedPrefLanguage = requireActivity().getSharedPreferences("value", 0)
        language = sharedPrefLanguage.getString("stringKey", "not found!").toString()
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


        blindInfoViewModel.user.observe(viewLifecycleOwner, Observer<String> { user ->
            selectUsername = user.toString()
            binding.displayName.text = selectUsername
        })
        blindInfoViewModel.phone.observe(viewLifecycleOwner, Observer<String> { phone ->
            selectPhone = phone.toString()
            if(language == "en"){
                binding.phone.text = "Phone : $selectPhone"
            }
            else if(language =="th"){
                binding.phone.text = "เบอร์โทรศัพท์ : $selectPhone"
            }

        })


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("BlindInformation", "onActivityCreated call")
    }

}
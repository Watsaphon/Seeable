package com.estazo.project.seeable.app.Caretaker.Setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentAccountSettingDeleteBinding


class AccountSettingDeleteFragment : Fragment() {

    private lateinit var binding : FragmentAccountSettingDeleteBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_setting_delete, container, false)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

}
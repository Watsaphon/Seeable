package com.estazo.project.seeable.app.caretaker.settingCaretaker.accoutSetting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentAccountSettingBinding


class AccountSettingFragment : Fragment() {

    private lateinit var binding : FragmentAccountSettingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_setting, container,false)

        binding.backButton.setOnClickListener{
            requireActivity().onBackPressed()
        }
        binding.editBtn.setOnClickListener{view : View ->
            view.findNavController().navigate(R.id.action_accountSettingFragment_to_accountSettingEditFragment)
        }
        binding.changePasswordBtn.setOnClickListener{view : View ->
            view.findNavController().navigate(R.id.action_accountSettingFragment_to_accountSettingChangePasswordFragment)
        }
        binding.deleteBtn.setOnClickListener{view : View ->
            view.findNavController().navigate(R.id.action_accountSettingFragment_to_accountSettingDeleteFragment)
        }
        return binding.root
    }


}
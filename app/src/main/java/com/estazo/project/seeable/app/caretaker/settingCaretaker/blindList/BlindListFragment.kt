package com.estazo.project.seeable.app.caretaker.settingCaretaker.blindList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.caretaker.CaretakerViewModel
import com.estazo.project.seeable.app.databinding.FragmentBlindListBinding


class BlindListFragment : Fragment() {

    private lateinit var binding : FragmentBlindListBinding

    private val blindListViewModel : BlindListViewModel by activityViewModels()

//    private val caretakerViewModel: CaretakerViewModel by activityViewModels()

    private val blindInfoViewModel : BlindInformationViewModel by activityViewModels()

//    private var caretakerViewModel: CaretakerViewModel

    private lateinit var selectPhone : String
    private lateinit var selectUser : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("BlindListFragment", "onCreate call")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blind_list, container, false)
//        val fragmentBinding = FragmentBlindListBinding.inflate(inflater, container, false)
//        binding = fragmentBinding
        Log.i("BlindListFragment", "onCreateView call")
        binding.backButton.setOnClickListener{
            requireActivity().onBackPressed()
        }
        binding.addButton.setOnClickListener{ view ->
            view.findNavController().navigate(R.id.action_blindListFragment_to_addBlindUserFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("BlindListFragment", "onViewCreated call")
//        binding.apply {
//            // Specify the fragment as the lifecycle owner
//            lifecycleOwner = viewLifecycleOwner
//            // Assign the view model to a property in the binding class
//            viewModel = caretakerViewModel
//            // Assign the fragment
//            blindListFragment = this@BlindListFragment
//        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("BlindListFragment", "onActivityCreated call")
        blindListViewModel.listUser.observe(viewLifecycleOwner, Observer{user ->
            var numUser : Int = 0
            val size = user.size
                if(size >= 1){
                    binding.user1.visibility = View.VISIBLE
                    binding.nameUser1.text = user[0]
                    ++numUser
                }
                if(size >= 2){
                    binding.user2.visibility = View.VISIBLE
                    binding.nameUser2.text = user[1]
                    ++numUser
                }
                if(size >= 3){
                    binding.user3.visibility = View.VISIBLE
                    binding.nameUser3.text = user[2]
                    ++numUser
                }
                if(size == 4){
                    binding.user4.visibility = View.VISIBLE
                    binding.nameUser4.text = user[3]
                    ++numUser
                }
                if(numUser == 4){
                binding.addButton.visibility = View.GONE
                }
            Log.i("ViewModel","size : $size , user : $user , numUser : $numUser")
        })
        blindListViewModel.listUserPhone.observe(viewLifecycleOwner, Observer{phone ->
            selectPhone = phone.toString()
            Log.i("ViewModelBLF","selectPhone : $selectPhone , phone : $phone")
        })

        binding.editUser1.setOnClickListener{view ->
            val phoneList = selectPhone.split(",".toRegex()).toTypedArray()
            val phone1 = phoneList[0].substring(1)
            blindInfoViewModel.userNameBlind(binding.nameUser1.text.toString(),phone1)
            Log.i("ViewModelBLF","phone1 : $phone1")
            view.findNavController().navigate(R.id.action_blindListFragment_to_blindInformationFragment)
        }
        binding.editUser2.setOnClickListener{view ->
            val phoneList = selectPhone.split(",".toRegex()).toTypedArray()
            val phone2 = phoneList[1]
            blindInfoViewModel.userNameBlind(binding.nameUser2.text.toString(),phone2)
            view.findNavController().navigate(R.id.action_blindListFragment_to_blindInformationFragment)
            Log.i("ViewModelBLF","phone2 : $phone2")
        }
        binding.editUser3.setOnClickListener{view ->
            val phoneList = selectPhone.split(",".toRegex()).toTypedArray()
            val phone3 = phoneList[2]
            blindInfoViewModel.userNameBlind(binding.nameUser3.text.toString(),phone3)
            view.findNavController().navigate(R.id.action_blindListFragment_to_blindInformationFragment)
            Log.i("ViewModelBLF","phone3 : $phone3")
        }
        binding.editUser4.setOnClickListener{view ->
            val phoneList = selectPhone.split(",".toRegex()).toTypedArray()
            val phone4 = phoneList[3].substring(0,11)
            blindInfoViewModel.userNameBlind(binding.nameUser4.text.toString(),phone4)
            view.findNavController().navigate(R.id.action_blindListFragment_to_blindInformationFragment)
            Log.i("ViewModelBLF","phone4 : $phone4")
        }
    }
    override fun onStart() {
        super.onStart()
        Log.i("BlindListFragment", "onStart call")
    }
    override fun onResume() {
        super.onResume()
        Log.i("BlindListFragment", "onResume call")
    }
    override fun onPause() {
        super.onPause()
        Log.i("BlindListFragment", "onPause call")
    }
    override fun onStop() {
        super.onStop()
        Log.i("BlindListFragment", "onStop call")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("BlindListFragment", "onDestroyView call")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("BlindListFragment", "onDestroy call")
    }
    override fun onDetach() {
        super.onDetach()
        Log.i("BlindListFragment", "onDetach call")
    }
}
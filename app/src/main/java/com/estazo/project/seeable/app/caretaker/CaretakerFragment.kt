 package com.estazo.project.seeable.app.caretaker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.caretaker.settingCaretaker.blindList.BlindListViewModel
import com.estazo.project.seeable.app.databinding.FragmentCaretakerBinding
import com.estazo.project.seeable.app.device.BPMRunnable


 class CaretakerFragment : Fragment() {
//     private lateinit var binding : FragmentCaretakerBinding
//
//    private lateinit var viewModel: CaretakerViewModel

     // Binding object instance corresponding to the fragment_start.xml layout
     // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
     // when the view hierarchy is attached to the fragment.
    // private var binding: FragmentCaretakerBinding? = null
     private lateinit var binding: FragmentCaretakerBinding

     // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
//     private val viewModel : CaretakerViewModel by activityViewModels()


    private val blindListViewModel : BlindListViewModel by activityViewModels()

        private lateinit var viewModel : CaretakerViewModel


    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var phone : String

    private var displayUser1 : String = "-"
    private var displayUser2 : String = "-"
    private var displayUser3 : String = "-"
    private var displayUser4 : String = "-"
    private var phoneUser1 : String = "-"
    private var phoneUser2 : String = "-"
    private var phoneUser3 : String = "-"
    private var phoneUser4 : String = "-"

     private lateinit var sharedPrefBlindId : SharedPreferences
     private lateinit var currentBlindId : String

//     private var currentBlindPhone : String = "-"

     override fun onAttach(context: Context) {
         super.onAttach(context)
         Log.i("CaretakerFragment", "onAttach call")
     }

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         Log.i("CaretakerFragment", "onCreate call")
     }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_caretaker, container, false)
        Log.i("CaretakerFragment", "onCreateView call")

        val fragmentBinding = FragmentCaretakerBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        sharedPrefPhone = requireActivity().getSharedPreferences("value", 0)
        phone = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()

        sharedPrefBlindId = requireActivity().getSharedPreferences("value", 0)
        currentBlindId = sharedPrefBlindId.getString("stringKeyBlindId", "not found!").toString()

        return fragmentBinding.root

    }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         Log.i("CaretakerFragment", "onViewCreated call")
         binding.caretakerFragment = this@CaretakerFragment

         binding.setting.setOnClickListener{view : View  ->
             view.findNavController().navigate(R.id.action_caretakerFragment_to_settingCaretakerFragment)
         }

//         queryBlindUser()
     }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("CaretakerFragment", "onActivityCreated call")

//        activity?.let { activity ->
//            blindListViewModel = ViewModelProviders.of(activity).get(BlindListViewModel::class.java)
//        blindListViewModel = ViewModelProvider(this).get(BlindListViewModel::class.java)
//        }

        viewModel = ViewModelProvider(this).get(CaretakerViewModel::class.java)

        viewModel.fetchSpinnerItems().observe(viewLifecycleOwner, Observer<List<Any>> { user ->
            val spinner = binding.spinner
            val arrayAdapter  = ArrayAdapter(activity?.applicationContext!!, R.layout.list_name_blind, user)
            spinner.adapter = arrayAdapter

            /**for store current Blind Id when view destroyed or close app */
            if(currentBlindId != "not found!"){
                    val itemPosition = currentBlindId.toInt()
                    Log.i("currentBlindId"," currentBlindId : $itemPosition")
                    spinner.setSelection(itemPosition)
            }
            /** if it already query then  don't show Loading...  */
            if(phoneUser1 != "-"){
                binding.loading.visibility = View.GONE
            }

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(activity,"Not Selection", Toast.LENGTH_LONG).show()
                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val itemPo = parent?.getItemAtPosition(position).toString()
                    val selectItemPo   = parent?.selectedItemPosition.toString()
                    /** for store data in ViewModel*/
                    viewModel._currentUser.value = selectItemPo.toInt()
                    /** for store data in SharedPreferences*/
                    val itemPosition = selectItemPo.toInt()
                    val editorBlindId = sharedPrefBlindId.edit()
                    editorBlindId.putString("stringKeyBlindId", "$itemPosition")
                    editorBlindId.apply()
                    viewModel._currentBlindPhone.value = viewModel.userTel.value?.get(itemPosition).toString()
                    val bpmThread = Thread(BPMRunnable(binding.bpmNumber,viewModel._currentBlindPhone.value.toString()))
                    bpmThread.start()
                    Log.i("selectItem","itemPo: $itemPo , selectItemPo: $selectItemPo , itemPosition : $itemPosition")
                }
            }

        })

    }

     override fun onStart() {
         super.onStart()
         Log.i("CaretakerFragment", "onStart call")
     }

     override fun onResume() {
         super.onResume()
         Log.i("CaretakerFragment", "onResume call")
         sharedPrefPhone= requireActivity().getSharedPreferences("value", 0)
         phone  = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
         viewModel.queryUser("$phone")

         viewModel.userDisplay.observe(viewLifecycleOwner, Observer<List<String>>{user ->
             Log.i("resume"," userDisplay -> user : $user")
             if(user.isNotEmpty()) {
                 Log.i("resume","userDisplay not empty ja")
                 updateUserNameToBlindList(user)
                 binding.loading.visibility = View.GONE
             }
         })

         viewModel.userTel.observe(viewLifecycleOwner, Observer<List<String>>{user ->
             Log.i("resume","userTel -> user : $user")
             if(user.isNotEmpty()) {
                 Log.i("resume","userList not empty ja")
                 updateUserPhoneToBlindList(user)
                 val updateListThread = Thread(UpdateListBlindUserRunnable(phone))
                 updateListThread.start()
             }
         })

         viewModel.userList.observe(viewLifecycleOwner, Observer<List<String>>{list ->
             Log.i("resume","userList -> list : $list")
             if(list.isNotEmpty()) {
                 Log.i("resume","userList not empty ja")
                 val displayNameThread = Thread(DisplayNameRunnable(phone,list))
                 displayNameThread.start()
             }
         })

     }

    override fun onPause() {
        super.onPause()
        Log.i("CaretakerFragment", "onPause call")
    }


    override fun onStop() {
        super.onStop()
        Log.i("CaretakerFragment", "onStop call")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("CaretakerFragment", "onDestroyView call")
        viewModel.getPhoneUser().removeObservers(this)
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("CaretakerFragment", "onDestroy call")
    }
    override fun onDetach() {
        super.onDetach()
        Log.i("CaretakerFragment", "onDetach call")
    }

     private fun updateUserNameToBlindList(user : List<String>) {
         blindListViewModel.updateUserName(user)
     }

     private fun updateUserPhoneToBlindList(phone : List<String>) {
         blindListViewModel.updateUserPhone(phone)
     }


 }
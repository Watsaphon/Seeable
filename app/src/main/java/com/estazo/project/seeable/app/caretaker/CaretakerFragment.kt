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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentCaretakerBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


 class CaretakerFragment : Fragment() {

    private lateinit var binding : FragmentCaretakerBinding

    private lateinit var viewModel: CaretakerViewModel

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

    lateinit var userList : List<String>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        userList  = ArrayList()
//        queryBlindUser()

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_caretaker, container, false)

        Log.i("CaretakerFragment", "onCreateView call")

//        viewModel = ViewModelProvider(this).get(CaretakerViewModel::class.java)

        sharedPrefPhone= requireActivity().getSharedPreferences("value", 0)
        phone  = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()

//        val adapter: ArrayAdapter<String> = ArrayAdapter(activity?.applicationContext!!, R.layout.list_name_blind, userList)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.spinner.setAdapter(adapter)

//        val bpmThread = Thread(BPMRunnable(binding.bpmNumber))
//        bpmThread.start()

//        Log.i("checkRun", "out of if -> phoneUser1 : $phoneUser1 , displayUser1 : $displayUser1")
//        if (phoneUser1 != "-" && displayUser1 !="-" ){
//            Log.i("checkRun", "phoneUser1 : $phoneUser1 , displayUser1 : $displayUser1")
//            val displayNameThread = Thread(DisplayNameRunnable(phone,phoneUser1,phoneUser2,phoneUser3,phoneUser4
//                ,displayUser1,displayUser2,displayUser3,displayUser4))
//            displayNameThread.start()
//        }

        binding.setting.setOnClickListener{view : View  ->
            view.findNavController().navigate(R.id.action_caretakerFragment_to_settingCaretakerFragment)
        }

//        viewModel.fetchSpinnerItems().observe(viewLifecycleOwner, Observer<List<Any>> { user ->
//            val spinner = binding.spinner
//            val arrayAdapter  = ArrayAdapter(activity?.applicationContext!!, R.layout.list_name_blind, user)
//            spinner.adapter = arrayAdapter
//            if(viewModel.currentUser.value != -1){
//                viewModel.currentUser.value?.toInt()?.let { spinner.setSelection(it) }
//            }
//            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                    println("Hello world")
//                }
//                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                    val itemPo = parent?.getItemAtPosition(position).toString()
//                    val itemIdPO = parent?.getItemIdAtPosition(position).toString()
//                    val selectItemPo   = parent?.getSelectedItemPosition().toString()
//                    val selectItemId   = parent?.getSelectedItemId().toString()
//                    val getCount   = parent?.getCount().toString()
//                    viewModel._currentUser.value = selectItemPo.toInt()
//                    Log.i("selectItem","itemPo: $itemPo , itemIdPO: $itemIdPO , selectItemPo: $selectItemPo " +
//                            ",selectItemId :$selectItemId , getCount : $getCount")
//                    Toast.makeText(activity,itemPo, Toast.LENGTH_LONG).show()
//                }
//            }
////            arrayAdapter.notifyDataSetChanged()
//        })
        return binding.root
    }

    private fun queryBlindUser(){
        sharedPrefPhone= requireActivity().getSharedPreferences("value", 0)
        phone  = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
        var firebaseRef = FirebaseDatabase.getInstance().getReference("users_caretaker/$phone/Blind")
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                displayUser1 = snapshot.child("user1").value.toString()
                displayUser2 = snapshot.child("user2").value.toString()
                displayUser3 = snapshot.child("user3").value.toString()
                displayUser4 = snapshot.child("user4").value.toString()
                var size = viewModel.userTel.value!!.size
                val splitFBUser1 = displayUser1.split("/".toRegex()).toTypedArray()
                val phoneFBUser1 = splitFBUser1[0]
                val nameFBUser1 = splitFBUser1[1]
                val splitFBUser2 = displayUser2.split("/".toRegex()).toTypedArray()
                val phoneFBUser2 = splitFBUser2[0]
                val nameFBUser2 = splitFBUser2[1]
                val splitFBUser3 = displayUser3.split("/".toRegex()).toTypedArray()
                val phoneFBUser3 = splitFBUser3[0]
                val nameFBUser3 = splitFBUser3[1]
                val splitFBUser4 = displayUser4.split("/".toRegex()).toTypedArray()
                val phoneFBUser4 = splitFBUser4[0]
                val nameFBUser4 = splitFBUser4[1]
                /**add user and set view on UI */
                when{
                    /** set 1 user */
                    displayUser4 == "-/-" && displayUser3 == "-/-" && displayUser2 == "-/-" && displayUser1 != "-/-" ->{
                        size = viewModel.userTel.value!!.size
                        viewModel.userDisplay.value = listOf(nameFBUser1)
                        viewModel.userTel.value = listOf(phoneFBUser1)
                        viewModel.getPhoneUser().observe(viewLifecycleOwner, Observer<List<Any>>{phone ->
                            phoneUser1 = phone[0].toString()
                        })
                        Log.i("testListOf()1","size : $size")
                    }
                    /** set 2 user */
                    displayUser4 == "-/-" && displayUser3 == "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-"->{
                        size = viewModel.userTel.value!!.size
                        viewModel.userDisplay.value = listOf(nameFBUser1,nameFBUser2)
                        viewModel.userTel.value = listOf(phoneFBUser1,phoneFBUser2)
                        viewModel.getPhoneUser().observe(viewLifecycleOwner, Observer<List<Any>>{phone ->
                            phoneUser1 = phone[0].toString()
                            if( size == 2) {
                                phoneUser2 = phone[1].toString()
                            }
                        })
                        Log.i("testListOf()2","size : $size")
                    }
                    /** set 3 user */
                    displayUser4 == "-/-" && displayUser3 != "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-" ->{
                        size = viewModel.userTel.value!!.size
                        viewModel.userDisplay.value = listOf(nameFBUser1,nameFBUser2,nameFBUser3)
                        viewModel.userTel.value = listOf(phoneFBUser1,phoneFBUser2,phoneFBUser3)
                            viewModel.getPhoneUser().observe(viewLifecycleOwner, Observer<List<Any>>{ phone ->
                                phoneUser1 = phone[0].toString()
                                if( size == 2) {
                                    phoneUser2 = phone[1].toString()
                                }
                                if( size == 3){
                                    phoneUser3 = phone[2].toString()
                                }
                            })
                        Log.i("testListOf()3","size : $size ")
                    }
                    /** set 4 user */
                    displayUser4 != "-/-" && displayUser3 != "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-"  ->{
                        size = viewModel.userTel.value!!.size
                        viewModel.userDisplay.value = listOf(nameFBUser1,nameFBUser2,nameFBUser3,nameFBUser4)
                        viewModel.userTel.value = listOf(phoneFBUser1,phoneFBUser2,phoneFBUser3,phoneFBUser4)
                            viewModel.getPhoneUser().observe(viewLifecycleOwner, Observer<List<Any>>{ phone ->
                                phoneUser1 = phone[0].toString()
                                if( size == 2) {
                                    phoneUser2 = phone[1].toString()
                                }
                                if( size == 3) {
                                    phoneUser3 = phone[2].toString()
                                }
                                if( size == 4) {
                                    phoneUser4 = phone[3].toString()
                                }
                            })
                        Log.i("testListOf()4","size : $size")
                    }
                }
//                Log.d("queryBlindUser","displayUser1 : $displayUser1 ,displayUser2 : $displayUser2 , displayUser3 : $displayUser3  , displayUser4 : $displayUser4")
//                Log.d("queryBlindUser","nameFBUser1 : $nameFBUser1 ,nameFBUser2 : $nameFBUser2" +
//                        " , nameFBUser3 : $nameFBUser3  , nameFBUser4 : $nameFBUser4")
//                Log.d("queryBlindUser","phoneFBUser1 : $phoneFBUser1 ,phoneFBUser2 : $phoneFBUser2 ," +
//                        " phoneFBUser3 : $phoneFBUser3  , phoneFBUser4 : $phoneFBUser4")
                val displayNameThread = Thread(DisplayNameRunnable(phone,phoneFBUser1,phoneFBUser2,phoneFBUser3,phoneFBUser4
                    ,nameFBUser1,nameFBUser2,nameFBUser3,nameFBUser4))
                displayNameThread.start()
                val updateListThread = Thread(UpdateListBlindUserRunnable(phone))
                updateListThread.start()
                Log.i("testListOf()last","size : $size")
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("CaretakerFragment", "onAttach call")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("CaretakerFragment", "onCreate call")
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("CaretakerFragment", "onActivityCreated call")
//        viewModel = ViewModelProvider(this).get(CaretakerViewModel::class.java)
        viewModel  = ViewModelProviders.of(this).get(CaretakerViewModel::class.java)

        viewModel.fetchSpinnerItems().observe(viewLifecycleOwner, Observer<List<Any>> { user ->
            val spinner = binding.spinner
            val arrayAdapter  = ArrayAdapter(activity?.applicationContext!!, R.layout.list_name_blind, user)
            spinner.adapter = arrayAdapter
            if(viewModel.currentUser.value != -1){
                viewModel.currentUser.value?.toInt()?.let { spinner.setSelection(it) }
            }
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    println("Hello world")
                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val itemPo = parent?.getItemAtPosition(position).toString()
                    val itemIdPO = parent?.getItemIdAtPosition(position).toString()
                    val selectItemPo   = parent?.getSelectedItemPosition().toString()
                    val selectItemId   = parent?.getSelectedItemId().toString()
                    val getCount   = parent?.getCount().toString()
                    viewModel._currentUser.value = selectItemPo.toInt()
                    Log.i("selectItem","itemPo: $itemPo , itemIdPO: $itemIdPO , selectItemPo: $selectItemPo " +
                            ",selectItemId :$selectItemId , getCount : $getCount")
                    Toast.makeText(activity,itemPo, Toast.LENGTH_LONG).show()
                }
            }
//            arrayAdapter.notifyDataSetChanged()
        })
    }
    override fun onStart() {
        super.onStart()
        Log.i("CaretakerFragment", "onStart call")
//        viewModel = ViewModelProvider(this).get(CaretakerViewModel::class.java)
    }
     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         Log.i("CaretakerFragment", "onViewCreated call")
         queryBlindUser()
     }
    override fun onResume() {
        super.onResume()
        Log.i("CaretakerFragment", "onResume call")
    }
    override fun onPause() {
        super.onPause()
        Log.i("CaretakerFragment", "onPause call")
        getViewLifecycleOwnerLiveData()
//        viewLifecycleOwner
    }
    override fun onStop() {
        super.onStop()
        Log.i("CaretakerFragment", "onStop call")
//        viewLifecycleOwner
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("CaretakerFragment", "onDestroyView call")
        viewModel.getPhoneUser().removeObservers(this)
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("CaretakerFragment", "onDestroy call")
//        viewLifecycleOwner
    }
    override fun onDetach() {
        super.onDetach()
        Log.i("CaretakerFragment", "onDetach call")
    }


}




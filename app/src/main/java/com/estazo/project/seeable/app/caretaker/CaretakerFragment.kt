package com.estazo.project.seeable.app.caretaker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentCaretakerBinding
import com.estazo.project.seeable.app.device.BPMRunnable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import java.util.*


class CaretakerFragment : Fragment() {

    private lateinit var binding : FragmentCaretakerBinding

    private lateinit var viewModel: CaretakerViewModel

    private lateinit var sharedPrefPhone: SharedPreferences

    private lateinit var phone : String

    private lateinit var user1 : String
    private lateinit var user2 : String
    private lateinit var user3 : String
    private lateinit var user4 : String

    lateinit var userList : List<String>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        userList  = ArrayList()
        queryBlindUser()
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_caretaker, container, false)
        Log.i("CaretakerFragment", "Called ViewModelProvider.get")
        Log.i("CaretakerFragment", "onCreateView call")

        viewModel = ViewModelProvider(this).get(CaretakerViewModel::class.java)

        sharedPrefPhone= requireActivity().getSharedPreferences("value", 0)
        phone  = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()

//        val adapter: ArrayAdapter<String> = ArrayAdapter(activity?.applicationContext!!, R.layout.list_name_blind, userList)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.spinner.setAdapter(adapter)

//        val bpmThread = Thread(BPMRunnable(binding.bpmNumber))
//        bpmThread.start()

        binding.setting.setOnClickListener{view : View  ->
            view.findNavController().navigate(R.id.action_caretakerFragment_to_settingCaretakerFragment)
        }

        viewModel.fetchSpinnerItems().observe(viewLifecycleOwner, Observer<List<Any>> { user ->
            val spinner = binding.spinner
            val arrayAdapter  = ArrayAdapter(activity?.applicationContext!!, R.layout.list_name_blind, user)
            spinner.adapter = arrayAdapter
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
                    Log.i("selectItem","itemPo: $itemPo , itemIdPO: $itemIdPO , selectItemPo: $selectItemPo " +
                            ",selectItemId :$selectItemId , getCount : $getCount")
                    Toast.makeText(activity,itemPo, Toast.LENGTH_LONG).show()
                }
            }
//            arrayAdapter.notifyDataSetChanged()
        })

        binding.healthStatus.setOnClickListener{
            Log.i("user_worker_Thread", "user1 : $user1 , user2 : $user2 " +
                    ", user3 : $user3 , user4 : $user4 , userList : $userList")
        }


        return binding.root
    }

    private fun queryBlindUser(){
        sharedPrefPhone= requireActivity().getSharedPreferences("value", 0)
        phone  = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
        var firebaseRef = FirebaseDatabase.getInstance().getReference("users_caretaker/$phone/Blind")
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user1 = snapshot.child("user1").value.toString()
                user2 = snapshot.child("user2").value.toString()
                user3 = snapshot.child("user3").value.toString()
                user4 = snapshot.child("user4").value.toString()
                when{
                    user4 == "null" ->{ viewModel.user.value = listOf(user1,user2,user3)
                   user(user1,user2,user3)
                    }
                    user4 == "null" && user3 == "null" -> viewModel.user.value = listOf(user1,user2)
                    user4 == "null" && user3 == "null" && user2 == "null" -> viewModel.user.value = listOf(user1)
                    else -> viewModel.user.value = listOf(user1,user2,user3,user4)
                }
                Log.d("dataclass","user1 : $user1 , user4 : $user4 , userList : $userList")
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun user(user1 : String ,user2 : String , user3 : String){
        lateinit var displayName1 : String
        lateinit var displayName2 : String
        lateinit var displayName3 : String
        var successChanged1 = false
        var successChanged2 = false
        var successChanged3 = false
        sharedPrefPhone= requireActivity().getSharedPreferences("value", 0)
        phone  = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
        var firebaseRef1 = FirebaseDatabase.getInstance().getReference("users_blind/$user1")
        var firebaseRef2 = FirebaseDatabase.getInstance().getReference("users_blind/$user2")
        var firebaseRef3 = FirebaseDatabase.getInstance().getReference("users_blind/$user3")
        firebaseRef1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                 displayName1 = snapshot.child("displayName").value.toString()
                successChanged1 = true
                Log.i("userblind", "displayName1 :$displayName1 ")
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
        firebaseRef2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                 displayName2 = snapshot.child("displayName").value.toString()
                successChanged2 = true
                Log.i("userblind", "displayName2  :$displayName2")
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
        firebaseRef3.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                displayName3 = snapshot.child("displayName").value.toString()
                successChanged3 = true
                Log.i("userblind", "displayName3  :$displayName3")
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
        Handler().postDelayed({
            viewModel.user.value = listOf(displayName1,displayName2,displayName3)
        }, 3000)
        Log.i("userblind", "set listOf() already")
//        for(successChanged1 == true && successChanged2 == true && successChanged3 == true){
//            viewModel.user.value = listOf(displayName1,displayName2,displayName3)
//        }

    }

    private fun setUserUI(){

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
    }
    override fun onStart() {
        super.onStart()
        Log.i("CaretakerFragment", "onStart call")
    }
    override fun onResume() {
        super.onResume()
        Log.i("CaretakerFragment", "onResume call")
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
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("CaretakerFragment", "onDestroy call")
    }
    override fun onDetach() {
        super.onDetach()
        Log.i("CaretakerFragment", "onDetach call")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("CaretakerFragment", "onViewCreated call")
//        viewModel = activity?.run { ViewModelProviders.of(this).get(CaretakerViewModel::class.java) }
//            ?: throw Exception("Invalid Activity")
    }
//    private fun clearBackStack() {
//        val manager: FragmentManager = requireActivity().supportFragmentManager
//        Log.d("clearBackStack", "manager : " + manager.backStackEntryCount)
//        if (manager.backStackEntryCount > 0) {
//            val first = manager.getBackStackEntryAt(0)
//            manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//        }
//    }

}




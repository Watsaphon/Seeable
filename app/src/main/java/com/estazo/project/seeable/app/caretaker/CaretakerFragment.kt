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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.caretaker.settingCaretaker.BlindListViewModel
import com.estazo.project.seeable.app.databinding.FragmentCaretakerBinding
import com.estazo.project.seeable.app.device.BPMRunnable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


 class CaretakerFragment : Fragment() {
//     private lateinit var binding : FragmentCaretakerBinding
//
//    private lateinit var viewModel: CaretakerViewModel


     // Binding object instance corresponding to the fragment_start.xml layout
     // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
     // when the view hierarchy is attached to the fragment.
//     private var binding: FragmentCaretakerBinding? = null
     private lateinit var binding: FragmentCaretakerBinding

     // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
//     private val viewModel : CaretakerViewModel by activityViewModels()


    private val blindListViewModel : BlindListViewModel  by activityViewModels()

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
             Log.i("resume","user : $user")
             if(user.isNotEmpty()) {
                 Log.i("ViewModel","userDisplay not empty ja")
                 updateUserNameToBlindList(user)
                 binding.loading.visibility = View.GONE
             }
         })

         viewModel.userList.observe(viewLifecycleOwner, Observer<List<String>>{user ->
             Log.i("resume","user : $user")
             if(user.isNotEmpty()) {
                 Log.i("ViewModel","userList not empty ja")
//                 val displayNameThread = Thread(DisplayNameRunnable(phone,user))
//                 displayNameThread.start()
                 val updateListThread = Thread(UpdateListBlindUserRunnable(phone))
                 updateListThread.start()
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


//     private fun queryBlindUser(){
////         sharedPrefPhone= requireActivity().getSharedPreferences("value", 0)
////         phone  = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
////
////         var firebaseRef = FirebaseDatabase.getInstance().getReference("users_caretaker/$phone/Blind")
////         firebaseRef.addValueEventListener(object : ValueEventListener {
////             override fun onDataChange(snapshot: DataSnapshot) {
////                 displayUser1 = snapshot.child("user1").value.toString()
////                 displayUser2 = snapshot.child("user2").value.toString()
////                 displayUser3 = snapshot.child("user3").value.toString()
////                 displayUser4 = snapshot.child("user4").value.toString()
////                 var size = viewModel.userTel.value!!.size
////                 val splitFBUser1 = displayUser1.split("/".toRegex()).toTypedArray()
////                 val phoneFBUser1 = splitFBUser1[0]
////                 val nameFBUser1 = splitFBUser1[1]
////                 val splitFBUser2 = displayUser2.split("/".toRegex()).toTypedArray()
////                 val phoneFBUser2 = splitFBUser2[0]
////                 val nameFBUser2 = splitFBUser2[1]
////                 val splitFBUser3 = displayUser3.split("/".toRegex()).toTypedArray()
////                 val phoneFBUser3 = splitFBUser3[0]
////                 val nameFBUser3 = splitFBUser3[1]
////                 val splitFBUser4 = displayUser4.split("/".toRegex()).toTypedArray()
////                 val phoneFBUser4 = splitFBUser4[0]
////                 val nameFBUser4 = splitFBUser4[1]
////                 /**add user and set view on UI */
////                 when{
////                     /** set 1 user */
////                     displayUser4 == "-/-" && displayUser3 == "-/-" && displayUser2 == "-/-" && displayUser1 != "-/-" ->{
////                         binding.loading.visibility = View.GONE
////                         viewModel.userDisplay.value = listOf(nameFBUser1)
////                         viewModel.userTel.value = listOf(phoneFBUser1)
////                         size = viewModel.userTel.value!!.size
////                         viewModel.getPhoneUser().observe(viewLifecycleOwner, Observer<List<Any>>{phone ->
////                             if(size == 1){
////                                 phoneUser1 = phone[0].toString()
////                             }
////                         })
////                         updateUserNameToBlindList(listOf(nameFBUser1))
////                         viewModel.getPhoneUser().removeObservers(viewLifecycleOwner)
////                         Log.i("testListOf()1","size : $size  , user1 :$phoneUser1")
////                     }
////                     /** set 2 user */
////                     displayUser4 == "-/-" && displayUser3 == "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-"->{
////                         binding.loading.visibility = View.GONE
////                         viewModel.userDisplay.value = listOf(nameFBUser1,nameFBUser2)
////                         viewModel.userTel.value = listOf(phoneFBUser1,phoneFBUser2)
////                         size = viewModel.userTel.value!!.size
////                         viewModel.getPhoneUser().observe(viewLifecycleOwner, Observer<List<Any>>{phone ->
////                             if(size == 2){
////                                 phoneUser1 = phone[0].toString()
////                                 phoneUser2 = phone[1].toString()
////                             }
////                         })
////                         updateUserNameToBlindList(listOf(nameFBUser1,nameFBUser2))
////                         viewModel.getPhoneUser().removeObservers(viewLifecycleOwner)
////                         Log.i("testListOf()2","size : $size  , user1 :$phoneUser1 , user2 :$phoneUser2")
////                     }
////                     /** set 3 user */
////                     displayUser4 == "-/-" && displayUser3 != "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-" ->{
////                         binding.loading.visibility = View.GONE
////                         viewModel.userDisplay.value = listOf(nameFBUser1,nameFBUser2,nameFBUser3)
////                         viewModel.userTel.value = listOf(phoneFBUser1,phoneFBUser2,phoneFBUser3)
////                         size = viewModel.userTel.value!!.size
////                         viewModel.getPhoneUser().observe(viewLifecycleOwner, Observer<List<Any>>{ phone ->
////                             if(size == 3 ) {
////                                 phoneUser1 = phone[0].toString()
////                                 phoneUser2 = phone[1].toString()
////                                 phoneUser3 = phone[2].toString()
////                             }
////                         })
////                         updateUserNameToBlindList(listOf(nameFBUser1,nameFBUser2,nameFBUser3))
////                         viewModel.getPhoneUser().removeObservers(viewLifecycleOwner)
//////                         viewModel.getPhoneUser().removeObservers(context as AppCompatActivity)
////                         Log.i("testListOf()3 case3","size : $size , user1 :$phoneUser1 , user2 :$phoneUser2 , user3 :$phoneUser3")
////                     }
////                     /** set 4 user */
////                     displayUser4 != "-/-" && displayUser3 != "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-"  ->{
////                         binding.loading.visibility = View.GONE
////                         viewModel.userDisplay.value = listOf(nameFBUser1,nameFBUser2,nameFBUser3,nameFBUser4)
////                         viewModel.userTel.value = listOf(phoneFBUser1,phoneFBUser2,phoneFBUser3,phoneFBUser4)
//////                         blindListViewModel.userDisplay2.value = listOf(nameFBUser1,nameFBUser2,nameFBUser3,nameFBUser4)
////                         size = viewModel.userTel.value!!.size
////                         viewModel.getPhoneUser().observe(viewLifecycleOwner, Observer<List<Any>>{ phone ->
////                             if(size == 4 ) {
////                                 phoneUser1 = phone[0].toString()
////                                 phoneUser2 = phone[1].toString()
////                                 phoneUser3 = phone[2].toString()
////                                 phoneUser4 = phone[3].toString()
////                             }
////                         })
////                         updateUserNameToBlindList(listOf(nameFBUser1,nameFBUser2,nameFBUser3,nameFBUser4))
////                         viewModel.sendMessage("pass data complete")
////                         viewModel.getPhoneUser().removeObservers(viewLifecycleOwner)
//////                         viewModel.getPhoneUser().removeObservers(context as AppCompatActivity)
////                         Log.i("testListOf()4 case4","size : $size , user1 :$phoneUser1 , user2 :$phoneUser2 , user3 :$phoneUser3 , user4 :$phoneUser4 ")
////
////                     }
////                 }
////                 val displayNameThread = Thread(DisplayNameRunnable(phone,phoneFBUser1,phoneFBUser2,phoneFBUser3,phoneFBUser4,nameFBUser1,nameFBUser2,nameFBUser3,nameFBUser4))
////                 displayNameThread.start()
////                 val updateListThread = Thread(UpdateListBlindUserRunnable(phone))
////                 updateListThread.start()
////                 Log.i("testListOf()final","size : $size")
////             }
////             override fun onCancelled(databaseError: DatabaseError) {
////             }
////         })
//     }

 }
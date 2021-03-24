 package com.estazo.project.seeable.app.caretaker

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.caretaker.settingCaretaker.blindList.BlindListViewModel
import com.estazo.project.seeable.app.databinding.FragmentCaretakerBinding
import com.estazo.project.seeable.app.device.BPMRunnable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.alert_dialog_pairing.view.*
import kotlinx.android.synthetic.main.alert_dialog_set_name.view.*


 class CaretakerFragment : Fragment() {

    private lateinit var binding: FragmentCaretakerBinding

     private lateinit var viewModel : CaretakerViewModel
     private val blindListViewModel : BlindListViewModel by activityViewModels()

     private lateinit var sharedPrefPhone: SharedPreferences
     private lateinit var phone : String
     private lateinit var sharedPrefBlindId : SharedPreferences
     private lateinit var currentBlindId : String

     private var phoneUser1 : String = "-"

     private lateinit var  mAlertDialog : AlertDialog


     override fun onAttach(context: Context) {
         super.onAttach(context)
         Log.i("CaretakerFragment", "onAttach call")
     }

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         Log.i("CaretakerFragment", "onCreate call")
         sharedPrefPhone = requireActivity().getSharedPreferences("value", 0)
         phone = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
         queryUser("$phone")
     }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_caretaker, container, false)
        Log.i("CaretakerFragment", "onCreateView call")

        val fragmentBinding = FragmentCaretakerBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        binding.setting.isEnabled = false

//        sharedPrefPhone = requireActivity().getSharedPreferences("value", 0)
//        phone = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
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

     }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("CaretakerFragment", "onActivityCreated call")

//        activity?.let { activity ->
//            blindListViewModel = ViewModelProviders.of(activity).get(BlindListViewModel::class.java)
//        blindListViewModel = ViewModelProvider(this).get(BlindListViewModel::class.java)
//        }

        viewModel = ViewModelProviders.of(this).get(CaretakerViewModel::class.java)

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

//        viewModel.queryUser("$phone")

        viewModel.userDisplay.observe(viewLifecycleOwner, Observer<List<String>>{user ->
            Log.i("resume1"," userDisplay -> user : $user")
            if(user.isNotEmpty()) {
                Log.i("resume1","userDisplay not empty ja")
                updateUserNameToBlindList(user)
                binding.loading.visibility = View.GONE
            }
        })

        viewModel.userTel.observe(viewLifecycleOwner, Observer<List<String>>{user ->
            Log.i("resume2","userTel -> user : $user")
            if(user.isNotEmpty()) {
                Log.i("resume2","userList not empty ja")
                updateUserPhoneToBlindList(user)
                val updateListThread = Thread(UpdateListBlindUserRunnable(phone))
                updateListThread.start()
            }
        })

        viewModel.userList.observe(viewLifecycleOwner, Observer<List<String>>{list ->
            Log.i("resume33","userList -> list : $list")
            val checkList = list.toString().split(",".toRegex()).toTypedArray()

            val user1 = checkList[0].substring(1)
             if(list.isNotEmpty() && user1 != "-/-" ) {
                 Log.i("resume33","userList not empty ja" +
                         " , checkList : $checkList , user1 : $user1")
                 binding.setting.isEnabled = true
                 val displayNameThread = Thread(DisplayNameRunnable(phone,list))
                 displayNameThread.start()
             }
             else if (list.isNotEmpty() && user1 == "-/-"  ){
                 Log.i("resume33","resume empty")
                 alertDialogPairing()
//                 binding.setting.isEnabled = true
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

     }

     /** AlertDialog to loading  */
     private fun alertDialogPairing() {
         //Inflate the dialog with custom view
         val mDialogView = LayoutInflater.from(activity).inflate(R.layout.alert_dialog_pairing, null)
         //AlertDialogBuilder
         val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
         //show dialog
         mAlertDialog  = mBuilder.show()
         mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
         mAlertDialog.setCanceledOnTouchOutside(false)
         mAlertDialog.setCancelable(false)

         mDialogView.startPair.setOnClickListener {
             findNavController().navigate(R.id.action_caretakerFragment_to_addBlindUserFragment)
             mAlertDialog.dismiss()
             }

     }

     private fun queryUser(phone: String) {
         var displayUser1 = "-"
         var displayUser2 = "-"
         var displayUser3 = "-"
         var displayUser4 = "-"
         Log.i("test","phone : $phone")
         val firebaseRef = FirebaseDatabase.getInstance().getReference("users_caretaker/$phone/Blind")
         firebaseRef.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 displayUser1 = snapshot.child("user1").value.toString()
                 displayUser2 = snapshot.child("user2").value.toString()
                 displayUser3 = snapshot.child("user3").value.toString()
                 displayUser4 = snapshot.child("user4").value.toString()
                 viewModel.userList.value = listOf(displayUser1,displayUser2,displayUser3,displayUser4)

                 Log.i("testListOf()","size : $ , displayUser1 :$displayUser1 , displayUser :$displayUser2 , displayUser3 :$displayUser3 , displayUser4 :$displayUser4")
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
                 Log.i("testListOf()","size : $ , nameFBUser1 :$nameFBUser1 , nameFBUser2 :$nameFBUser2 , nameFBUser3 :$nameFBUser3 , nameFBUser4 :$nameFBUser4")
                 /**add user and set view on UI */
                 when{
                     /** set 1 user */
                     displayUser4 == "-/-" && displayUser3 == "-/-" && displayUser2 == "-/-" && displayUser1 != "-/-" ->{
                         viewModel.userDisplay.value = listOf(nameFBUser1)
                         viewModel.userTel.value = listOf(phoneFBUser1)
                         Log.i("testListOf()1","displayUser1 :$displayUser1")
                     }
                     /** set 2 user */
                     displayUser4 == "-/-" && displayUser3 == "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-"->{
                         viewModel.userDisplay.value = listOf(nameFBUser1,nameFBUser2)
                         viewModel.userTel.value = listOf(phoneFBUser1,phoneFBUser2)
                         Log.i("testListOf()2","displayUser1 :$displayUser1 , displayUser2 :$displayUser2")
                     }
                     /** set 3 user */
                     displayUser4 == "-/-" && displayUser3 != "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-" ->{
                         viewModel.userDisplay.value = listOf(nameFBUser1 , nameFBUser2 , nameFBUser3)
                         viewModel.userTel.value = listOf(phoneFBUser1 , phoneFBUser2 , phoneFBUser3)
                         Log.i("testListOf()3","displayUser1 :$displayUser1 , displayUser :$displayUser2 , displayUser3 :$displayUser3")
                     }
                     /** set 4 user */
                     displayUser4 != "-/-" && displayUser3 != "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-"  ->{
                         viewModel.userDisplay.value = listOf(nameFBUser1,nameFBUser2,nameFBUser3,nameFBUser4)
                         viewModel.userTel.value = listOf(phoneFBUser1,phoneFBUser2,phoneFBUser3,phoneFBUser4)
                         Log.i("testListOf()4","displayUser1 :$displayUser1 , displayUser :$displayUser2 , displayUser3 :$displayUser3 , displayUser4 :$displayUser4")
                     }
                 }

             }
             override fun onCancelled(databaseError: DatabaseError) {
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
//        viewModel.userList.removeObservers(viewLifecycleOwner)

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
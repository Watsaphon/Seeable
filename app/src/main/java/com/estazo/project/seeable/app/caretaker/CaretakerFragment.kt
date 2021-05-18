 package com.estazo.project.seeable.app.caretaker

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.caretaker.settingCaretaker.blindList.BlindListViewModel
import com.estazo.project.seeable.app.databinding.FragmentCaretakerBinding
import com.estazo.project.seeable.app.device.ActivityRunnable
import com.estazo.project.seeable.app.device.BPMRunnable
import com.estazo.project.seeable.app.device.HealthStatusRunnable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.alert_dialog_pairing.view.*


 class CaretakerFragment : Fragment() {

     private lateinit var binding: FragmentCaretakerBinding

     private lateinit var viewModel : CaretakerViewModel
//     private val viewModel : CaretakerViewModel by activityViewModels()
     private val blindListViewModel : BlindListViewModel by activityViewModels()

     private lateinit var sharedPrefPhone: SharedPreferences
     private lateinit var phone : String
     private lateinit var sharedPrefBlindId : SharedPreferences
     private lateinit var currentBlindId : String
     private lateinit var previousBlindId : String

     private var phoneUser1 : String = "-"

     private lateinit var  mAlertDialog : AlertDialog

     private lateinit var database: DatabaseReference
     private lateinit var listener: ValueEventListener

     val PERMISSION_ID = 1010

     override fun onAttach(context: Context) {
         super.onAttach(context)
         Log.i("CaretakerFragment", "onAttach call")
     }

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         Log.i("CaretakerFragment", "onCreate call")
         sharedPrefPhone = requireActivity().getSharedPreferences("value", 0)
         phone = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
                 override fun handleOnBackPressed() {
                     // in here you can do logic when backPress is clicked
                     requireActivity().finishAffinity()
                 }
        })
     }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Log.i("CaretakerFragment", "onCreateView call")
        val fragmentBinding = FragmentCaretakerBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        binding.setting.isEnabled = false
        checkPermission()
        requestPermission()
        sharedPrefBlindId = requireActivity().getSharedPreferences("value", 0)
        currentBlindId = sharedPrefBlindId.getString("stringKeyBlindId", "not found!").toString()
        queryUser("$phone")

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

        viewModel = ViewModelProviders.of(this).get(CaretakerViewModel::class.java)

        viewModel.fetchSpinnerItems().observe(viewLifecycleOwner, Observer<List<Any>> { user ->
            val spinner = binding.spinner
            val arrayAdapter  = ArrayAdapter(activity?.applicationContext!!, R.layout.list_name_blind, user)
            spinner.adapter = arrayAdapter
            /**for store current Blind Id when view destroyed or close app */
            if(currentBlindId != "not found!"){
                    val itemPosition = currentBlindId.toInt()
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
                    database = Firebase.database.reference
                    database.child("users_blind/$currentBlindId/Device/activity").removeEventListener(listener)
                    database.child("users_blind/$currentBlindId/Device/health_Status").removeEventListener(listener)

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
                    val healthStatusThread = Thread(HealthStatusRunnable(binding.healthStatusText,viewModel._currentBlindPhone.value.toString()))
                    healthStatusThread.start()
                    val activityThread = Thread(ActivityRunnable(binding.activityWalkingText,viewModel._currentBlindPhone.value.toString()))
                    activityThread.start()

//                    database = Firebase.database.reference
                    val blind = viewModel._currentBlindPhone.value.toString()
                    listener = database.child("users_blind/$blind/Device").addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    val activity = dataSnapshot.child("activity").value.toString()
                                    val status = dataSnapshot.child("health_Status").value.toString()
                                    viewModel.activity.value = activity
                                    viewModel.healthStatus.value = status
                                    Log.d(" testja", "activity : $activity , status : $status")
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                    Log.d("selectItem","itemPo: $itemPo , selectItemPo: $selectItemPo , itemPosition : $itemPosition")

                }
            }
        })

        viewModel.userDisplay.observe(viewLifecycleOwner, Observer<List<String>>{user ->
            Log.d("checkViewModel_D"," userDisplay -> user : $user")
            if(user.isNotEmpty()) {
                Log.d("checkViewModel_D","userDisplay not empty")
                updateUserNameToBlindList(user)
                binding.loading.visibility = View.GONE
            }
        })

        viewModel.userTel.observe(viewLifecycleOwner, Observer<List<String>>{user ->
            Log.d("checkViewModel_T","userTel -> user : $user")
            if(user.isNotEmpty()) {
                Log.d("checkViewModel_T","userList not empty")
                updateUserPhoneToBlindList(user)
                val updateListThread = Thread(UpdateListBlindUserRunnable(phone))
                updateListThread.start()
            }
        })

        viewModel.userList.observe(viewLifecycleOwner, Observer<List<String>>{list ->
            Log.d("checkViewModel_L","userList -> list : $list")
            val checkList = list.toString().split(",".toRegex()).toTypedArray()
            val user1 = checkList[0].substring(1)
             if(list.isNotEmpty() && user1 != "-/-" ) {
                 Log.d("checkViewModel_L","userList not empty ja , checkList : $checkList , user1 : $user1")
                 binding.setting.isEnabled = true
                 if (this::mAlertDialog.isInitialized){
                     if (mAlertDialog.isShowing){
                         mAlertDialog.dismiss()
                     }
                 }
                 val displayNameThread = Thread(DisplayNameRunnable(phone,list))
                 displayNameThread.start()
             }
             else if (list.isNotEmpty() && user1 == "-/-"  ){
                 Log.d("checkViewModel_L","User Empty ")
                 alertDialogPairing()
             }
        })

        viewModel.healthStatus.observe(viewLifecycleOwner, Observer<String>{status ->
            var buttonDrawable: Drawable = binding.healthStatus.background
            buttonDrawable = DrawableCompat.wrap(buttonDrawable)
            when(status){
                "Tired" -> { /**ส้มขี้*/
                    Log.i("selectItem","status : $status")
                    DrawableCompat.setTint(buttonDrawable, Color.rgb(200, 145, 80))
                    binding.healthStatus.background = buttonDrawable
                }
                "Exhausted" -> { /**ม่วง*/
                    Log.i("selectItem","status : $status")
                    DrawableCompat.setTint(buttonDrawable, Color.rgb(144, 77, 255))
                    binding.healthStatus.background = buttonDrawable
                }
                "Normal" -> { /**เขียว*/
                    Log.i("selectItem","status : $status")
                    DrawableCompat.setTint(buttonDrawable, Color.rgb(84, 201, 129))
                    binding.healthStatus.background = buttonDrawable
                }
                "Danger" -> { /**แดง*/
                    Log.i("selectItem","status : $status")
                    DrawableCompat.setTint(buttonDrawable, Color.rgb(255, 87, 77))
                    binding.healthStatus.background = buttonDrawable
                }
            }
        })
        viewModel.activity.observe(viewLifecycleOwner, Observer<String>{activity ->
            var buttonDrawable: Drawable = binding.activityWalking.background
            buttonDrawable = DrawableCompat.wrap(buttonDrawable)
            when(activity){
                "Idle" -> {
                    /**ฟ้า*/
                    Log.i("selectItem","activity : $activity")
                    DrawableCompat.setTint(buttonDrawable, Color.rgb(61, 173, 255))
                    binding.activityWalking.background = buttonDrawable
                }
                "Walking" -> {
                    /**เหลือง*/
                    Log.i("selectItem","activity : $activity")
                    DrawableCompat.setTint(buttonDrawable, Color.rgb(239, 202, 67))
                    binding.activityWalking.background = buttonDrawable
                }
            }
        })
    }

     private fun checkPermission():Boolean {
         if(ActivityCompat.checkSelfPermission(requireActivity(),
                 Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
             return true
         }
         return false
     }
     private fun requestPermission(){
         ActivityCompat.requestPermissions(requireActivity(), arrayOf(
             Manifest.permission.CALL_PHONE), PERMISSION_ID)
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
         database = Firebase.database.reference
         listener = database.child("users_caretaker/$phone/Blind").addValueEventListener(object : ValueEventListener {
             override fun onDataChange(dataSnapshot: DataSnapshot) {
                 if (dataSnapshot.exists()) {
                     displayUser1 = dataSnapshot.child("user1").value.toString()
                     displayUser2 = dataSnapshot.child("user2").value.toString()
                     displayUser3 = dataSnapshot.child("user3").value.toString()
                     displayUser4 = dataSnapshot.child("user4").value.toString()
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
                             Log.d("testListOf()1","displayUser1 :$displayUser1")
                         }
                         /** set 2 user */
                         displayUser4 == "-/-" && displayUser3 == "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-"->{
                             viewModel.userDisplay.value = listOf(nameFBUser1,nameFBUser2)
                             viewModel.userTel.value = listOf(phoneFBUser1,phoneFBUser2)
                             Log.d("testListOf()2","displayUser1 :$displayUser1 , displayUser2 :$displayUser2")
                         }
                         /** set 3 user */
                         displayUser4 == "-/-" && displayUser3 != "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-" ->{
                             viewModel.userDisplay.value = listOf(nameFBUser1 , nameFBUser2 , nameFBUser3)
                             viewModel.userTel.value = listOf(phoneFBUser1 , phoneFBUser2 , phoneFBUser3)
                             Log.d("testListOf()3","displayUser1 :$displayUser1 , displayUser :$displayUser2 , displayUser3 :$displayUser3")
                         }
                         /** set 4 user */
                         displayUser4 != "-/-" && displayUser3 != "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-"  ->{
                             viewModel.userDisplay.value = listOf(nameFBUser1,nameFBUser2,nameFBUser3,nameFBUser4)
                             viewModel.userTel.value = listOf(phoneFBUser1,phoneFBUser2,phoneFBUser3,phoneFBUser4)
                             Log.d("testListOf()4","displayUser1 :$displayUser1 , displayUser :$displayUser2 , displayUser3 :$displayUser3 , displayUser4 :$displayUser4")
                         }
                     }
                 }
             }
             override fun onCancelled(error: DatabaseError) {
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
        database.child("users_caretaker/$phone/Blind").removeEventListener(listener)
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
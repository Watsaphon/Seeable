package com.estazo.project.seeable.app.caretaker.settingCaretaker.blindList

import android.app.AlertDialog
import android.content.SharedPreferences
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
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentBlindListBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.alert_dialog_delete_blind_user.view.*
import kotlinx.android.synthetic.main.alert_dialog_set_name.view.dialogConfirmBtn


class BlindListFragment : Fragment() {

    private lateinit var binding : FragmentBlindListBinding

    private val blindListViewModel : BlindListViewModel by activityViewModels()
    private val blindInfoViewModel : BlindInformationViewModel by activityViewModels()

    private lateinit var  mAlertDialog : AlertDialog

    private lateinit var sharedPrefPhone: SharedPreferences

    private lateinit var selectPhone : String
    private lateinit var phone : String

    private var lastUser : Boolean = false

    private lateinit var database: DatabaseReference
    private lateinit var listener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("BlindListFragment", "onCreate call")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blind_list, container, false)
        Log.i("BlindListFragment", "onCreateView call")

        sharedPrefPhone = requireActivity().getSharedPreferences("value", 0)
        phone = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()

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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("BlindListFragment", "onActivityCreated call")
        updateBlindList()
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
            Log.d("ViewModel","size : $size , user : $user , numUser : $numUser")
        })
        blindListViewModel.listUserPhone.observe(viewLifecycleOwner, Observer{phone ->
            selectPhone = phone.toString().replace(", ",",")
            Log.d("ViewModelBLF","selectPhone : $selectPhone , phone : $phone")
        })
        binding.editUser1.setOnClickListener{
            val phoneList = selectPhone.split(",".toRegex()).toTypedArray()
            val phone1 = phoneList[0].substring(1,11)
            blindInfoViewModel.userNameBlind(binding.nameUser1.text.toString(),phone1,1)
            Log.d("ViewModelBLF","phone1 :$phone1")
            val action = BlindListFragmentDirections.actionBlindListFragmentToBlindInformationFragment(phone1)
            findNavController().navigate(action)
        }
        binding.editUser2.setOnClickListener{
            val phoneList = selectPhone.split(",".toRegex()).toTypedArray()
            val phone2 = phoneList[1].substring(0,10)
            blindInfoViewModel.userNameBlind(binding.nameUser2.text.toString(),phone2,2)

            val action = BlindListFragmentDirections.actionBlindListFragmentToBlindInformationFragment(phone2)
            findNavController().navigate(action)
            Log.d("ViewModelBLF","phone2 :$phone2")
        }
        binding.editUser3.setOnClickListener{
            val phoneList = selectPhone.split(",".toRegex()).toTypedArray()
            val phone3 = phoneList[2].substring(0,10)
            blindInfoViewModel.userNameBlind(binding.nameUser3.text.toString(),phone3,3)

            val action = BlindListFragmentDirections.actionBlindListFragmentToBlindInformationFragment(phone3)
            findNavController().navigate(action)
            Log.d("ViewModelBLF","phone3 :$phone3")
        }
        binding.editUser4.setOnClickListener{
            val phoneList = selectPhone.split(",".toRegex()).toTypedArray()
            val phone4 = phoneList[3].substring(0,10)
            blindInfoViewModel.userNameBlind(binding.nameUser4.text.toString(),phone4,4)

            val action = BlindListFragmentDirections.actionBlindListFragmentToBlindInformationFragment(phone4)
            findNavController().navigate(action)
            Log.i("ViewModelBLF","phone4 :$phone4")
        }
        binding.deleteUser1.setOnClickListener{
            alertDialogDelete(1)
        }
        binding.deleteUser2.setOnClickListener{
            alertDialogDelete(2)
        }
        binding.deleteUser3.setOnClickListener{
            alertDialogDelete(3)
        }
        binding.deleteUser4.setOnClickListener{
            alertDialogDelete(4)
        }
    }

    /** AlertDialog to delete blind user  */
    private fun alertDialogDelete(user : Int) {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.alert_dialog_delete_blind_user, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
        //show dialog
        mAlertDialog  = mBuilder.show()
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        mDialogView.dialogConfirmBtn.setOnClickListener {
            when (user) {
                1 -> {
                    if (lastUser){
                        mAlertDialog.dismiss()
                        alertDialogLastUser()
                    }
                    else if (!lastUser){
                        val ref = FirebaseDatabase.getInstance().reference
                        val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind/user$user" to "-/-")
                        ref.updateChildren(childUpdates)
                        binding.user1.visibility = View.GONE
                        binding.addButton.visibility = View.VISIBLE
                        updateBlindList()
                        mAlertDialog.dismiss()
                    }
                }
                2 -> {
                    lastUser = false
                    val ref = FirebaseDatabase.getInstance().reference
                    val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind/user$user" to "-/-")
                    ref.updateChildren(childUpdates)
                    binding.user2.visibility = View.GONE
                    binding.addButton.visibility = View.VISIBLE
                    updateBlindList()
                    mAlertDialog.dismiss()
                }
                3 -> {
                    lastUser = false
                    val ref = FirebaseDatabase.getInstance().reference
                    val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind/user$user" to "-/-")
                    ref.updateChildren(childUpdates)
                    binding.user3.visibility = View.GONE
                    binding.addButton.visibility = View.VISIBLE
                    updateBlindList()
                    mAlertDialog.dismiss()
                }
                4 -> {
                    lastUser = false
                    val ref = FirebaseDatabase.getInstance().reference
                    val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind/user$user" to "-/-")
                    ref.updateChildren(childUpdates)
                    binding.user4.visibility = View.GONE
                    binding.addButton.visibility = View.VISIBLE
                    updateBlindList()
                    mAlertDialog.dismiss()
                }
            }
        }
        mDialogView.dialogDeleteBtn.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    /** AlertDialog warn cant delete last user*/
    private fun alertDialogLastUser() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.alert_dialog_last_user, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
        //show dialog
        mAlertDialog  = mBuilder.show()
        mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun updateBlindList(){
        database = Firebase.database.reference
        listener = database.child("users_caretaker/$phone/Blind").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val displayUser1 = dataSnapshot.child("user1").value.toString()
                    val displayUser2 = dataSnapshot.child("user2").value.toString()
                    val displayUser3 = dataSnapshot.child("user3").value.toString()
                    val displayUser4 = dataSnapshot.child("user4").value.toString()

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
                        /** 4 user */
                        displayUser4 != "-/-" && displayUser3 != "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-" ->{
                            blindListViewModel.updateUserName(listOf(nameFBUser1,nameFBUser2,nameFBUser3,nameFBUser4))
                            blindListViewModel.updateUserPhone(listOf(phoneFBUser1,phoneFBUser2,phoneFBUser3,phoneFBUser4))
                        }
                        /** delete user from 4 user */
                        displayUser4 == "-/-" && displayUser3 != "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-" ->{
                            blindListViewModel.updateUserName(listOf(nameFBUser1,nameFBUser2,nameFBUser3))
                            blindListViewModel.updateUserPhone(listOf(phoneFBUser1,phoneFBUser2,phoneFBUser3))
                            binding.user4.visibility = View.GONE
                        }
                        /** delete user from 3 user */
                        displayUser4 == "-/-" && displayUser3 == "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-" ->{
                            blindListViewModel.updateUserName(listOf(nameFBUser1,nameFBUser2))
                            blindListViewModel.updateUserPhone(listOf(phoneFBUser1,phoneFBUser2))
                            binding.user3.visibility = View.GONE
                        }
                        /** delete user from 2 user */
                        displayUser4 == "-/-" && displayUser3 == "-/-" && displayUser2 == "-/-" && displayUser1 != "-/-" ->{
                            blindListViewModel.updateUserName(listOf(nameFBUser1))
                            blindListViewModel.updateUserPhone(listOf(phoneFBUser1))
                            binding.user2.visibility = View.GONE
                            lastUser = true
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
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
        database.child("users_caretaker/$phone/Blind").removeEventListener(listener)
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
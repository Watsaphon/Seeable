package com.estazo.project.seeable.app.caretaker.settingCaretaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.blind.MainActivity
import com.estazo.project.seeable.app.caretaker.CaretakerFragment
import com.estazo.project.seeable.app.caretaker.MainActivityPerson
import com.estazo.project.seeable.app.databinding.FragmentAccountSettingEditBinding
import com.estazo.project.seeable.app.login.LoginScreen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AccountSettingEditFragment : Fragment() {

    private lateinit var binding : FragmentAccountSettingEditBinding
    private lateinit var editname : String

    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var sharedPrefDisplayName: SharedPreferences
    private lateinit var displayName : String
    private lateinit var phone  : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
      binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_setting_edit ,container, false)
        Log.i("AccountSettingEdit", "onCreateView called")
//        val displayName = requireActivity().getPreferences(Context.MODE_PRIVATE)

        sharedPrefPhone= requireActivity().getSharedPreferences("value", 0)
        sharedPrefDisplayName = requireActivity().getSharedPreferences("value", 0)

        displayName  = sharedPrefDisplayName.getString("stringKeyDisplayName", "not found!").toString()
        phone  = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()

        Log.i("test","displayName : $displayName , phone : $phone")

      binding.backButton.setOnClickListener{
            requireActivity().onBackPressed()
      }

      binding.saveButton.setOnClickListener {view : View ->
          editname =  binding.editDisplayName.text.toString()
          Log.i("test","$editname")
          if(editname.isEmpty()){
              binding.editDisplayName.error = getString(R.string.empty_displayName_AccountSettingEditCaretaker)
              Toast.makeText(activity, getString(R.string.empty_displayName_AccountSettingEditCaretaker), Toast.LENGTH_SHORT).show()
          }
          else{
              val editor = sharedPrefDisplayName.edit()
              editor.putString("stringKeyDisplayName", editname)
              editor.apply()
              val query = FirebaseDatabase.getInstance().getReference("users_caretaker").child("$phone/displayName")
              query.addListenerForSingleValueEvent(valueEventListener)
              Toast.makeText(activity,R.string.successfully_ChangePassword, Toast.LENGTH_SHORT).show()
              requireActivity().onBackPressed()
          }
      }

    return binding.root
    }

    /**receive value from realtime database (users_blind) and check Login */
    private var valueEventListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                displayName  = sharedPrefDisplayName.getString("stringKeyDisplayName", "not found!").toString()
                editname =  binding.editDisplayName.text.toString()
                val ref = FirebaseDatabase.getInstance().reference
                val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/displayName" to "$editname")
                ref.updateChildren(childUpdates)

            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("AccountSettingEdit", "onAttach called")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("AccountSettingEdit", "onCreate called")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("AccountSettingEdit", "onActivityCreated called")
    }
    override fun onStart() {
        super.onStart()
        Log.i("AccountSettingEdit", "onStart called")
        binding.editDisplayName.setText("")
    }
    override fun onResume() {
        super.onResume()
        Log.i("AccountSettingEdit", "onResume called")
    }
    override fun onPause() {
        super.onPause()
        Log.i("AccountSettingEdit", "onPause called")
    }
    override fun onStop() {
        super.onStop()
        Log.i("AccountSettingEdit", "onStop called")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("AccountSettingEdit", "onDestroyView called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("AccountSettingEdit", "onDestroy called")

    }
    override fun onDetach() {
        super.onDetach()
        Log.i("AccountSettingEdit", "onDetach called")
    }

}
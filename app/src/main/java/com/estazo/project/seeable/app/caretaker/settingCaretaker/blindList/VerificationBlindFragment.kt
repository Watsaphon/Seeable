package com.estazo.project.seeable.app.caretaker.settingCaretaker.blindList

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.caretaker.CaretakerViewModel
import com.estazo.project.seeable.app.caretaker.MainActivityPerson
import com.estazo.project.seeable.app.caretaker.UpdateListBlindUserRunnable
import com.estazo.project.seeable.app.databinding.FragmentVerificationBlindBinding
import com.estazo.project.seeable.app.register.LittleMore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class VerificationBlindFragment : Fragment() {

    private lateinit var binding : FragmentVerificationBlindBinding

    private val verificationViewModel: VerificationViewModel by activityViewModels()

    private lateinit var mobile : String
    private lateinit var codeOTP : String

    private lateinit var auth: FirebaseAuth
    private lateinit var  mAlertDialog : AlertDialog

    private lateinit var checkOTP : String

    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var phoneCaretaker : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("VerificationBlind", "onCreate call")
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verification_blind, container, false)
        Log.i("VerificationBlind", "onCreateView call")

        binding.backButton.setOnClickListener{
            requireActivity().onBackPressed()
        }

        sharedPrefPhone = requireActivity().getSharedPreferences("value", 0)
        phoneCaretaker  = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()

        binding.digit1.addTextChangedListener(digit1TextWatcher)
        binding.digit2.addTextChangedListener(digit2TextWatcher)
        binding.digit3.addTextChangedListener(digit3TextWatcher)
        binding.digit4.addTextChangedListener(digit4TextWatcher)
        binding.digit5.addTextChangedListener(digit5TextWatcher)
        binding.digit6.addTextChangedListener(digit6TextWatcher)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("VerificationBlind", "onViewCreated call")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("VerificationBlind", "onActivityCreated call")
        verificationViewModel.mobile.observe(viewLifecycleOwner, Observer{phone ->
            mobile = phone.toString()
            binding.phone.text = "+66-$mobile"
//            phoneNext = phone.toString()
            Log.i("VerificationBlind","mobile :$mobile")
        })
        verificationViewModel.otp.observe(viewLifecycleOwner, Observer{code ->
            codeOTP = code.toString()
            Log.i("VerificationBlind","codeOTP :$codeOTP")
        })

        binding.verifyOtpButton.setOnClickListener{
            val c1 = binding.digit1.text.toString()
            val c2 = binding.digit2.text.toString()
            val c3 = binding.digit3.text.toString()
            val c4 = binding.digit4.text.toString()
            val c5 = binding.digit5.text.toString()
            val c6 = binding.digit6.text.toString()
            checkOTP = "$c1$c2$c3$c4$c5$c6"
            val credential = PhoneAuthProvider.getCredential(codeOTP, checkOTP)
            Log.i("VerificationBlind","codeOTP : $codeOTP , checkOTP : $checkOTP")
            signInWithPhone(credential,mobile )
        }

    }
    override fun onStart() {
        super.onStart()
        Log.i("VerificationBlind", "onStart call")
    }

    override fun onResume() {
        super.onResume()
        Log.i("VerificationBlind", "onResume call")
    }

    override fun onPause() {
        super.onPause()
        Log.i("VerificationBlind", "onPause call")
    }
    override fun onStop() {
        super.onStop()
        Log.i("VerificationBlind", "onStop call")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("VerificationBlind", "onDestroyView call")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("VerificationBlind", "onDestroy call")
    }
    override fun onDetach() {
        super.onDetach()
        Log.i("VerificationBlind", "onDetach call")
    }


    private fun signInWithPhone(credential: PhoneAuthCredential , phoneBlind :String) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    alertDialogLoading()
//                    val intent = Intent(this@VerificationOTP, LittleMore::class.java)
//                    Log.i("mobile2",phoneNext)
//                    intent.putExtra("mobile2", phoneNext)
//                    startActivity(intent)
                    queryBlind(phoneCaretaker,phoneBlind)
                    dismissAlertDialogLoading()
                    findNavController().navigate(R.id.action_verificationBlindFragment_to_caretakerFragment)
                    (activity as MainActivityPerson).closeKeyboard()
                    Toast.makeText(activity, "Correct OTP", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(activity, "Incorrect OTP", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun queryBlind(phoneCaretaker : String , phoneBlind : String  ){
        var firebaseRef = FirebaseDatabase.getInstance().getReference("users_blind/$phoneBlind")
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val phone = snapshot.child("phone").value.toString()
                val name = snapshot.child("displayName").value.toString()
                addBlindUser(phoneCaretaker,phone,name)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun addBlindUser(phoneCaretaker : String , phoneBlind : String , name : String ) {
        var firebaseRef = FirebaseDatabase.getInstance().getReference("users_caretaker/$phoneCaretaker/Blind")
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val displayUser1 = snapshot.child("user1").value.toString()
                val displayUser2 = snapshot.child("user2").value.toString()
                val displayUser3 = snapshot.child("user3").value.toString()
                val displayUser4 = snapshot.child("user4").value.toString()
                val splitFBUser1 = displayUser1.split("/".toRegex()).toTypedArray()
                val phoneFBUser1 = splitFBUser1[0]
                val splitFBUser2 = displayUser2.split("/".toRegex()).toTypedArray()
                val phoneFBUser2 = splitFBUser2[0]
                val splitFBUser3 = displayUser3.split("/".toRegex()).toTypedArray()
                val phoneFBUser3 = splitFBUser3[0]
                /**add user and set view on UI */
                when {
                    phoneFBUser1 == phoneBlind ||  phoneFBUser2 == phoneBlind ||  phoneFBUser3 == phoneBlind -> {

                        Log.i("12345","user already match.")
                    }
                    displayUser2 == "-/-" -> {
                        val ref = FirebaseDatabase.getInstance().reference
                        val childUpdates = hashMapOf<String, Any>("users_caretaker/$phoneCaretaker/Blind/user2" to "$phoneBlind/$name")
                        ref.updateChildren(childUpdates)
                    }
                    displayUser3 == "-/-" -> {
                        val ref = FirebaseDatabase.getInstance().reference
                        val childUpdates = hashMapOf<String, Any>("users_caretaker/$phoneCaretaker/Blind/user3" to "$phoneBlind/$name")
                        ref.updateChildren(childUpdates)
                    }
                    displayUser4 == "-/-" -> {
                        val ref = FirebaseDatabase.getInstance().reference
                        val childUpdates = hashMapOf<String, Any>("users_caretaker/$phoneCaretaker/Blind/user4" to "$phoneBlind/$name")
                        ref.updateChildren(childUpdates)
                    }
                    else -> {
                        Log.i("12345","User Not Found.")
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
    private val digit1TextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if(s.toString().trim().isEmpty()){
                binding.digit1.requestFocus()
            }
        }
        override fun afterTextChanged(s: Editable) {}
    }
    private val digit2TextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if(s.toString().trim().isEmpty()){
                binding.digit2.requestFocus()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
    private val digit3TextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if(s.toString().trim().isEmpty()){
                binding.digit3.requestFocus()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
    private val digit4TextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if(s.toString().trim().isEmpty()){
                binding.digit4.requestFocus()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
    private val digit5TextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if(s.toString().trim().isEmpty()){
                binding.digit5.requestFocus()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
    private val digit6TextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if(s.toString().trim().isEmpty()){
                binding.digit6.requestFocus()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }

    /** AlertDialog to loading  */
    private fun alertDialogLoading() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.loading_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
        //show dialog
        mAlertDialog  = mBuilder.show()
        mAlertDialog.window!!.setLayout(400,300)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)
    }

    private fun dismissAlertDialogLoading() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.loading_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
        //show dialog
        mAlertDialog.dismiss()
    }



}
package com.estazo.project.seeable.app.caretaker.settingCaretaker.blindList

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentAddBlindUserBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class AddBlindUserFragment : Fragment() {

    private lateinit var binding : FragmentAddBlindUserBinding

    private val verificationViewModel : VerificationViewModel by activityViewModels()

    private lateinit var auth: FirebaseAuth

    private var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null

    private lateinit var verificationCode: String

    private lateinit var  mAlertDialog : AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("AddBlindUserFragment", "onCreate call")
        auth = FirebaseAuth.getInstance()
        startFirebaseLogin()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_blind_user, container, false)
        Log.i("AddBlindUserFragment", "onCreateView call")

        binding.backButton.setOnClickListener{
            requireActivity().onBackPressed()
        }

        binding.telOTP.addTextChangedListener(phoneTextWatcher)

        binding.sendOtpButton.setOnClickListener {
            alertDialogLoading()
            if(binding.telOTP.text.toString().trim().isEmpty()) {
                Toast.makeText(activity, R.string.empty_AddBlindUser,Toast.LENGTH_SHORT).show()
                dismissAlertDialogLoading()
            }
            else{
                val phone : String = "+66"+binding.telOTP.text.toString()
                activity?.let { activity ->
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phone,  // Phone number to verify
                        60,  // Timeout duration
                        TimeUnit.SECONDS,  // Unit of timeout
                        activity,  // Activity (for callback binding)
                        mCallback!!
                    )
                } // OnVerificationStateChangedCallbacks
            }
        }
        return binding.root
    }

    private val phoneTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val phone : String =  binding.telOTP.text.toString().trim()
            if(phone.isNotEmpty()){
                binding.clearButton.visibility = View.VISIBLE
                binding.clearButton.setOnClickListener {
                    binding.telOTP.text?.clear()
                }
            }
            else if(phone.isEmpty()){
                binding.clearButton.visibility = View.GONE
            }
        }
        override fun afterTextChanged(s: Editable) {}
    }

    private fun startFirebaseLogin() {
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                dismissAlertDialogLoading()
                Toast.makeText(activity,  R.string.completed_SendOTP, Toast.LENGTH_SHORT).show()
            }
            override fun onVerificationFailed(e: FirebaseException) {
                dismissAlertDialogLoading()
                Toast.makeText(activity, R.string.failed_SendOTP, Toast.LENGTH_SHORT).show()
            }
            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationCode = s
                dismissAlertDialogLoading()
                Toast.makeText(activity,  R.string.sent_SendOTP, Toast.LENGTH_SHORT).show()
                verificationViewModel.verifyOTP(binding.telOTP.text.toString(), s)
                view!!.findNavController().navigate(R.id.action_addBlindUserFragment_to_verificationBlindFragment)
                Log.d("Verification","verificationCode : $verificationCode , s : $s")
            }
        }
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
        mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
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
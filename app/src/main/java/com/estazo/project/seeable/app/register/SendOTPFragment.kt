package com.estazo.project.seeable.app.register

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
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.MainActivity
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentSendOTPBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class SendOTPFragment : Fragment() {

    private lateinit var binding : FragmentSendOTPBinding

    private lateinit var auth: FirebaseAuth

    private var mCallback: OnVerificationStateChangedCallbacks? = null
    private var verificationCode: String? = null

    private lateinit var  mAlertDialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("SendOTPFragment","call onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_send_o_t_p, container, false)
        Log.i("SendOTPFragment","call onCreateView")
        auth = FirebaseAuth.getInstance()
        startFirebaseLogin()

        binding.sendOtpButton.setOnClickListener {
            alertDialogLoading()
            if(binding.telOTP.text.toString().trim().isEmpty()) {
                Toast.makeText(activity,R.string.empty_SendOTP,Toast.LENGTH_SHORT).show()
                mAlertDialog.dismiss()
            }
            else{
                val phone : String = "+66"+binding.telOTP.text.toString()
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone,  // Phone number to verify
                    60,  // Timeout duration
                    TimeUnit.SECONDS,  // Unit of timeout
                    requireActivity(),  // Activity (for callback binding)
                    mCallback!!
                ) // OnVerificationStateChangedCallbacks
            }
        }

        binding.telOTP.addTextChangedListener(phoneTextWatcher)

        return binding.root
    }

    private fun startFirebaseLogin() {
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                mAlertDialog.dismiss()
                Toast.makeText(activity, R.string.completed_SendOTP, Toast.LENGTH_SHORT).show()
            }
            override fun onVerificationFailed(e: FirebaseException) {
                mAlertDialog.dismiss()
                Toast.makeText(activity, R.string.failed_SendOTP, Toast.LENGTH_SHORT).show()
            }
            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationCode = s

                val action = SendOTPFragmentDirections.actionSendOTPToVerificationOTP(binding.telOTP.text.toString(), verificationCode.toString())
                    findNavController().navigate(action)

                (activity as MainActivity).closeKeyboard()
                binding.telOTP.text?.clear()

                mAlertDialog.dismiss()

                Toast.makeText(activity, R.string.sent_SendOTP, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val phoneTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val phone : String = binding.telOTP.text.toString().trim()
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

}
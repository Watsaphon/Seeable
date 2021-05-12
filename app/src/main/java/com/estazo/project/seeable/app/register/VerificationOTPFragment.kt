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
import com.estazo.project.seeable.app.databinding.FragmentVerificationOTPBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerificationOTPFragment : Fragment() {

    private lateinit var binding : FragmentVerificationOTPBinding

    private lateinit var auth: FirebaseAuth

    private lateinit var  mAlertDialog : AlertDialog

    private lateinit var codeOTP : String
    private lateinit var  phoneNext : String
    private var verificationCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("VerificationOTPFragment","call onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verification_o_t_p, container, false)
        Log.i("VerificationOTPFragment","call onCreateView")

        arguments?.let {
            val otp = VerificationOTPFragmentArgs.fromBundle(it).OTP
            verificationCode = otp
            val phone = VerificationOTPFragmentArgs.fromBundle(it).mobile
            binding.phone.text = "+66-$phone"
            phoneNext = phone
        }

        auth = FirebaseAuth.getInstance()

        binding.digit1.addTextChangedListener(digit1TextWatcher)
        binding.digit2.addTextChangedListener(digit2TextWatcher)
        binding.digit3.addTextChangedListener(digit3TextWatcher)
        binding.digit4.addTextChangedListener(digit4TextWatcher)
        binding.digit5.addTextChangedListener(digit5TextWatcher)
        binding.digit6.addTextChangedListener(digit6TextWatcher)

        binding.verifyOTPButton.setOnClickListener{
            alertDialogLoading()
            val c1 = binding.digit1.text.toString()
            val c2 = binding.digit2.text.toString()
            val c3 = binding.digit3.text.toString()
            val c4 = binding.digit4.text.toString()
            val c5 = binding.digit5.text.toString()
            val c6 = binding.digit6.text.toString()

            if (c1.isNotEmpty() && c2.isNotEmpty() && c3.isNotEmpty() && c4.isNotEmpty() && c5.isNotEmpty() &&c6.isNotEmpty()){
                codeOTP = "$c1$c2$c3$c4$c5$c6"
                val credential = PhoneAuthProvider.getCredential(verificationCode!!, codeOTP)
                Log.d("checkData","codeOTP : $codeOTP , verificationCode : $verificationCode")
                signInWithPhone(credential)
                mAlertDialog.dismiss()
            }
            else{
                Toast.makeText(activity, R.string.empty_VerifyOTP,Toast.LENGTH_SHORT).show()
                mAlertDialog.dismiss()
            }
            (activity as MainActivity).closeKeyboard()
        }
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    private fun signInWithPhone(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val action = VerificationOTPFragmentDirections.actionVerificationOTPToLittleMore(phoneNext)
                    findNavController().navigate(action)
                    mAlertDialog.dismiss()
                } else {
                    mAlertDialog.dismiss()
                    Toast.makeText(activity, R.string.incorrect_VerifyOTP, Toast.LENGTH_SHORT).show()
                }
            }
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
        mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)
    }

}
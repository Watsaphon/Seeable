package com.estazo.project.seeable.app.register


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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

//    private lateinit var digit_1: EditText
//    private lateinit var digit_2: EditText
//    private lateinit var digit_3: EditText
//    private lateinit var digit_4: EditText
//    private lateinit var digit_5: EditText
//    private lateinit var digit_6: EditText

//    private lateinit var tel: TextView

//    private lateinit var backButton: ImageButton
//    private lateinit var verifyButton: Button
    private lateinit var codeOTP : String
    private lateinit var  phoneNext : String

    private lateinit var auth: FirebaseAuth
    private var verificationCode: String? = null

    private lateinit var  mAlertDialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_verification_o_t_p)

//        tel= findViewById(R.id.phone)
//        backButton = findViewById(R.id.back_button)
//        verifyButton = findViewById(R.id.veify_otp_button)
//        digit_1 = findViewById(R.id.digit_1)
//        digit_2 = findViewById(R.id.digit_2)
//        digit_3 = findViewById(R.id.digit_3)
//        digit_4 = findViewById(R.id.digit_4)
//        digit_5 = findViewById(R.id.digit_5)
//        digit_6 = findViewById(R.id.digit_6)

//        val phone = intent.getStringExtra("mobile")
//        verificationCode = intent.getStringExtra("OTP")
//        val phone = "test ja"
//        verificationCode = "test ja"
//        binding.phone.text = "+66-$phone"
//        phoneNext = phone.toString()
//
//        auth = FirebaseAuth.getInstance()
//
//        binding.digit1.addTextChangedListener(digit1TextWatcher)
//        binding.digit2.addTextChangedListener(digit2TextWatcher)
//        binding.digit3.addTextChangedListener(digit3TextWatcher)
//        binding.digit4.addTextChangedListener(digit4TextWatcher)
//        binding.digit5.addTextChangedListener(digit5TextWatcher)
//        binding.digit6.addTextChangedListener(digit6TextWatcher)
//
//        binding.verifyOTPButton.setOnClickListener{
//            val c1 = binding.digit1.text.toString()
//            val c2 = binding.digit2.text.toString()
//            val c3 = binding.digit3.text.toString()
//            val c4 = binding.digit4.text.toString()
//            val c5 = binding.digit5.text.toString()
//            val c6 = binding.digit6.text.toString()
//                codeOTP = "$c1$c2$c3$c4$c5$c6"
//                val credential = PhoneAuthProvider.getCredential(verificationCode!!, codeOTP)
//            Log.i("eieiei","$codeOTP , $verificationCode")
//            signInWithPhone(credential)
//            }
//
//        binding.backButton.setOnClickListener {
////           finish()
//        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verification_o_t_p, container, false)

        arguments?.let {

            val otp = VerificationOTPFragmentArgs.fromBundle(it).OTP
            verificationCode = otp

            val phone = VerificationOTPFragmentArgs.fromBundle(it).mobile
            binding.phone.text = "+66-$phone"
            phoneNext = phone

        }
//        val phone = intent.getStringExtra("mobile")
//        verificationCode = intent.getStringExtra("OTP")
//        val phone = "test ja"
//        verificationCode = "test ja"
//        binding.phone.text = "+66-$phone"

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

//            codeOTP = "$c1$c2$c3$c4$c5$c6"
//            val credential = PhoneAuthProvider.getCredential(verificationCode!!, codeOTP)
//            Log.i("eieiei","$codeOTP , $verificationCode")
//            signInWithPhone(credential)

            if (c1.isNotEmpty() && c2.isNotEmpty() && c3.isNotEmpty() && c4.isNotEmpty() && c5.isNotEmpty() &&c6.isNotEmpty()){
                codeOTP = "$c1$c2$c3$c4$c5$c6"
                val credential = PhoneAuthProvider.getCredential(verificationCode!!, codeOTP)
                Log.i("eieiei","$codeOTP , $verificationCode")
                signInWithPhone(credential)
            }
            else{
                Toast.makeText(activity, R.string.empty_VerifyOTP,Toast.LENGTH_SHORT).show()
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
//                    val intent = Intent(this@VerificationOTP, LittleMore::class.java)
                    Log.i("mobile2",phoneNext)
//                    intent.putExtra("mobile2", phoneNext)
//                    startActivity(intent)
                    mAlertDialog.dismiss()
//                    finish()
                } else {
                    mAlertDialog.dismiss()
                    Toast.makeText(activity, "Incorrect OTP", Toast.LENGTH_SHORT).show()
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
//        mAlertDialog.window!!.setLayout(400,300)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)
    }

//    private fun dismissAlertDialogLoading() {
//        //Inflate the dialog with custom view
//        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.loading_dialog, null)
//        //AlertDialogBuilder
//        val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
//        //show dialog
//        mAlertDialog.dismiss()
//    }

}
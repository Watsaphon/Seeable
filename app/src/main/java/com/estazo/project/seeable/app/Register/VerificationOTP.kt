package com.estazo.project.seeable.app.Register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.estazo.project.seeable.app.Login.SignoutByGmail
import com.estazo.project.seeable.app.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class VerificationOTP : AppCompatActivity() {

    private lateinit var etTelOTP : TextInputEditText
    private lateinit var sendButton: Button
    private lateinit var telOTP  : String

    private lateinit var etcodeOTP : TextInputEditText
    private lateinit var veriftButton: Button
    private lateinit var codeOTP : String

//    private lateinit var mCallback: OnVerificationStateChangedCallbacks
    private var mCallback: OnVerificationStateChangedCallbacks? = null
    private lateinit var auth: FirebaseAuth
//    private lateinit var verificationCode: String
    private var verificationCode: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_o_t_p)

        etTelOTP = findViewById(R.id.telOTP)
        sendButton = findViewById(R.id.send_otp_button)
        etcodeOTP = findViewById(R.id.codeOTP)
        veriftButton = findViewById(R.id.verify_otp_button)
//        auth = FirebaseAuth.getInstance()

        StartFirebaseLogin()

        sendButton.setOnClickListener {
            telOTP = etTelOTP.text.toString()
            if(telOTP != null) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    telOTP,  // Phone number to verify
                    60,  // Timeout duration
                    TimeUnit.SECONDS,  // Unit of timeout
                    this@VerificationOTP,  // Activity (for callback binding)
                    mCallback!!
                ) // OnVerificationStateChangedCallbacks
            }
            else{
                Toast.makeText(this@VerificationOTP, "Please fill Tel",Toast.LENGTH_SHORT).show()
            }
        }

        veriftButton.setOnClickListener{
                codeOTP = etcodeOTP.text.toString()
                val credential =
                    PhoneAuthProvider.getCredential(verificationCode!!, codeOTP)
                SigninWithPhone(credential)
            }
        }

    private fun StartFirebaseLogin() {
        auth = FirebaseAuth.getInstance()
        mCallback = object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Toast.makeText(this@VerificationOTP, "verification completed", Toast.LENGTH_SHORT).show()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@VerificationOTP, "verification fialed", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                s: String,
                forceResendingToken: ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                verificationCode = s
                Toast.makeText(this@VerificationOTP, "Code sent", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun SigninWithPhone(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this@VerificationOTP, SignedIn::class.java))
                    finish()
                } else {
                    Toast.makeText(this@VerificationOTP, "Incorrect OTP", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }


}
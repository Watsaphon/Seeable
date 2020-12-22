package com.estazo.project.seeable.app.Register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.estazo.project.seeable.app.Login.SignoutByGmail
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.SplashScreen
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks

class SendOTP : AppCompatActivity() {

    private lateinit var etTelOTP : EditText
    private lateinit var sendButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var clearButton: ImageButton

    private var mCallback: OnVerificationStateChangedCallbacks? = null
    private var verificationCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_o_t_p)

        etTelOTP = findViewById(R.id.telOTP)
        sendButton = findViewById(R.id.send_otp_button)
        clearButton = findViewById(R.id.clear_button)

        auth = FirebaseAuth.getInstance()
        StartFirebaseLogin()

        sendButton.setOnClickListener {
            if(etTelOTP.text.toString().trim().isEmpty()) {
               Toast.makeText(this@SendOTP, "Enter Phone",Toast.LENGTH_SHORT).show()
            }
            else{
                val phone : String = "+66"+etTelOTP.text.toString()
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone,  // Phone number to verify
                    60,  // Timeout duration
                    TimeUnit.SECONDS,  // Unit of timeout
                    this@SendOTP,  // Activity (for callback binding)
                    mCallback!!
                ) // OnVerificationStateChangedCallbacks
                Log.i("eieiei","$phone")
            }
        }

        etTelOTP.addTextChangedListener(phoneTextWatcher)


    }

    private fun StartFirebaseLogin() {
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Toast.makeText(this@SendOTP, "verification completed", Toast.LENGTH_SHORT).show()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@SendOTP, "verification failed", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationCode = s
                val intent = Intent(this@SendOTP, VerificationOTP::class.java)
                intent.putExtra("mobile",etTelOTP.text.toString())
                intent.putExtra("OTP",verificationCode)
                startActivity(intent)
                Toast.makeText(this@SendOTP, "Code sent", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val phoneTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val phone : String = etTelOTP.text.toString().trim()
            if(phone.isNotEmpty()){
                clearButton.visibility = View.VISIBLE
                clearButton.setOnClickListener {
                    etTelOTP.text.clear()
                }
            }
            else if(phone.isEmpty()){
                clearButton.visibility = View.GONE
            }
        }
        override fun afterTextChanged(s: Editable) {}
    }


}
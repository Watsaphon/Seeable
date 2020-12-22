package com.estazo.project.seeable.app.Register


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.estazo.project.seeable.app.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import kotlinx.android.synthetic.main.activity_verification_o_t_p.*
import java.util.concurrent.TimeUnit

class VerificationOTP : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var verifyButton: Button
    private lateinit var digit_1: EditText
    private lateinit var digit_2: EditText
    private lateinit var digit_3: EditText
    private lateinit var digit_4: EditText
    private lateinit var digit_5: EditText
    private lateinit var digit_6: EditText
    private lateinit var tel: TextView
    private lateinit var codeOTP : String
    private lateinit var  phoneNext : String


    private lateinit var auth: FirebaseAuth
    private var verificationCode: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_o_t_p)

        tel= findViewById(R.id.phone)
        backButton = findViewById(R.id.back_button)
        verifyButton = findViewById(R.id.veify_otp_button)
        digit_1 = findViewById(R.id.digit_1)
        digit_2 = findViewById(R.id.digit_2)
        digit_3 = findViewById(R.id.digit_3)
        digit_4 = findViewById(R.id.digit_4)
        digit_5 = findViewById(R.id.digit_5)
        digit_6 = findViewById(R.id.digit_6)

        val phone = intent.getStringExtra("mobile")
        verificationCode = intent.getStringExtra("OTP")
        tel.text = "+66-$phone"
        phoneNext = phone.toString()

        auth = FirebaseAuth.getInstance()

        digit_1.addTextChangedListener(digit1TextWatcher)
        digit_2.addTextChangedListener(digit2TextWatcher)
        digit_3.addTextChangedListener(digit3TextWatcher)
        digit_4.addTextChangedListener(digit4TextWatcher)
        digit_5.addTextChangedListener(digit5TextWatcher)
        digit_6.addTextChangedListener(digit6TextWatcher)

        verifyButton.setOnClickListener{
            val c1 = digit_1.text.toString()
            val c2 = digit_2.text.toString()
            val c3 = digit_3.text.toString()
            val c4 = digit_4.text.toString()
            val c5 = digit_5.text.toString()
            val c6 = digit_6.text.toString()
                codeOTP = "$c1$c2$c3$c4$c5$c6"
                val credential = PhoneAuthProvider.getCredential(verificationCode!!, codeOTP)
            Log.i("eieiei","$codeOTP , $verificationCode")
                SigninWithPhone(credential)
            }

        backButton.setOnClickListener {
           finish()
        }

    }

    private fun SigninWithPhone(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@VerificationOTP, LittleMore::class.java)
                    Log.i("mobile2",phoneNext)
                    intent.putExtra("mobile2", phoneNext)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@VerificationOTP, "Incorrect OTP", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private val digit1TextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if(s.toString().trim().isEmpty()){
            digit_1.requestFocus()
            }
        }
        override fun afterTextChanged(s: Editable) {}
    }
    private val digit2TextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if(s.toString().trim().isEmpty()){
                digit_2.requestFocus()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
    private val digit3TextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if(s.toString().trim().isEmpty()){
                digit_3.requestFocus()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
    private val digit4TextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if(s.toString().trim().isEmpty()){
                digit_4.requestFocus()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
    private val digit5TextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if(s.toString().trim().isEmpty()){
                digit_5.requestFocus()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
    private val digit6TextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if(s.toString().trim().isEmpty()){
                digit_6.requestFocus()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }

}
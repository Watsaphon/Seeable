package com.estazo.project.seeable.app.Register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.estazo.project.seeable.app.R
import com.google.android.material.textfield.TextInputEditText

class VerificationOTP : AppCompatActivity() {

    private lateinit var telOTP : TextInputEditText
    private lateinit var sendButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_o_t_p)

        telOTP = findViewById(R.id.telOTP)
        sendButton = findViewById(R.id.send_otp_button)
    }

}
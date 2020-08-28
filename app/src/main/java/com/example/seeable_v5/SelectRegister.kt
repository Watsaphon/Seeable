package com.example.seeable_v5

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class SelectRegister : AppCompatActivity() {
    private lateinit var blindButton: Button
    private lateinit var personButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_register)

        blindButton = findViewById(R.id.blinder_btn)
        personButton = findViewById(R.id.person_btn)

        blindButton.setOnClickListener {
            val i = Intent(this@SelectRegister, RegisterBlind::class.java)
            startActivity(i)
        }
        personButton.setOnClickListener {
            val i = Intent(this@SelectRegister, RegisterPerson::class.java)
            startActivity(i)
        }
    }

}
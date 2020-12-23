package com.estazo.project.seeable.app.Register

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.estazo.project.seeable.app.HelperClass.UserBlinderHelperClassNew
import com.estazo.project.seeable.app.HelperClass.UserPersonHelperClassNew
import com.estazo.project.seeable.app.Login.LoginScreen
import com.estazo.project.seeable.app.R
import com.google.firebase.database.FirebaseDatabase


class LittleMore : AppCompatActivity() {

    private lateinit var blindBtn: RadioButton
    private lateinit var careBtn: RadioButton
    private lateinit var clearBtn: ImageButton
    private lateinit var registerBtn: Button
    private lateinit var setPassword: EditText
    private lateinit var radioGroup: RadioGroup

    private lateinit var  mAlertDialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_little_more)

        blindBtn = findViewById(R.id.select_blind)
        careBtn = findViewById(R.id.select_care)
        clearBtn = findViewById(R.id.clear_button)
        registerBtn = findViewById(R.id.register_button)
        setPassword = findViewById(R.id.setPassword)
        radioGroup = findViewById(R.id.select_mode)

        val tel = intent.getStringExtra("mobile2")
        Log.i("LittleMore", "phone : $tel")

        val phone : String = tel.toString()

        // Get radio group selected status and text using button click event
        registerBtn.setOnClickListener {

            val password : String = setPassword.text.toString()


            // Get the checked radio button id from radio group
            var id: Int = radioGroup.checkedRadioButtonId
            if (id != -1) {
                // If any radio button checked from radio group
                // Get the instance of radio button using id
                val radio: RadioButton = findViewById(id)
                if (radio.text == "Blind" && password.isNotEmpty() ) {
                    alertDialogLoading()
//                    Toast.makeText(applicationContext, "On button click :" + " ${radio.text}", Toast.LENGTH_SHORT).show()
                    val ref = FirebaseDatabase.getInstance().getReference("users_blind")
                    val ID = ref.push().key
                    val test = UserBlinderHelperClassNew(
                            ID.toString(),
                            phone,
                            "$password",
                            "-",
                            "-",
                            "-",
                            "-",
                            "-")
                    ref.child(phone).setValue(test).addOnCompleteListener { saveRegister() }
                }
                else if (radio.text == "Caretaker" && password.isNotEmpty()) {
                    alertDialogLoading()
//                    Toast.makeText(applicationContext, "On button click :" + " ${radio.text}", Toast.LENGTH_SHORT).show()
                    val ref = FirebaseDatabase.getInstance().getReference("users_caretaker")
                    val ID = ref.push().key
                    val test = UserPersonHelperClassNew(
                        ID.toString(),
                        phone,
                        "$password",
                        "-",
                        "-",
                        "-")
                    ref.child(phone).setValue(test).addOnCompleteListener { saveRegister() }
                }
                else if (password.isEmpty()) {
                    Toast.makeText(applicationContext, "Please fill your password", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                // If no radio button checked in this radio group
                Toast.makeText(applicationContext, "Please Select mode", Toast.LENGTH_SHORT).show()
            }
        }

        setPassword.addTextChangedListener(phoneTextWatcher)

    }

    private fun saveRegister(){
        dismissAlertDialogLoading()
        val i = Intent(this, LoginScreen::class.java)
        startActivity(i)
        finish()
    }

    private val phoneTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val password : String = setPassword.text.toString().trim()
            if(password.isNotEmpty()){
                clearBtn.visibility = View.VISIBLE
                clearBtn.setOnClickListener {
                    setPassword.text.clear()
                }
            }
            else if(password.isEmpty()){
                clearBtn.visibility = View.GONE
            }
        }
        override fun afterTextChanged(s: Editable) {}
    }

    /** AlertDialog to loading  */
    private fun alertDialogLoading() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        //show dialog
        mAlertDialog  = mBuilder.show()
        mAlertDialog.window!!.setLayout(400,300)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)
    }

    private fun dismissAlertDialogLoading() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        //show dialog
        mAlertDialog.dismiss()
    }
}


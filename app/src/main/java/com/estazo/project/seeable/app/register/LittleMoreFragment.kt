package com.estazo.project.seeable.app.register

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.MainActivity
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentLittleMoreBinding
import com.estazo.project.seeable.app.helperClass.*
import com.google.firebase.database.FirebaseDatabase


class LittleMoreFragment : Fragment() {

    private lateinit var binding : FragmentLittleMoreBinding

    private lateinit var radioGroup: RadioGroup

    private lateinit var phone: String

    private lateinit var  mAlertDialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        radioGroup = findViewById(R.id.select_mode)

    }

//    fun addListenerOnButton() {
//        radioGroup = findViewById(R.id.radio) as RadioGroup
//        btnDisplay = findViewById(R.id.btnDisplay) as Button
//        btnDisplay.setOnClickListener(object : OnClickListener() {
//            fun onClick(v: View?) {
//
//                // get selected radio button from radioGroup
//                val selectedId: Int = radioGroup.getCheckedRadioButtonId()
//
//                // find the radiobutton by returned id
//                radioButton = findViewById(selectedId) as RadioButton
//                Toast.makeText(
//                    this@MyAndroidAppActivity,
//                    radioButton.getText(), Toast.LENGTH_SHORT
//                ).show()
//            }
//        })
//    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_little_more, container, false)

        arguments?.let {
            val mobile = LittleMoreFragmentArgs.fromBundle(it).mobile
            phone = mobile
        }


//        Log.i("LittleMore", "phone : $phone")

        // Get radio group selected status and text using button click event
        binding.registerButton.setOnClickListener() {

            (activity as MainActivity).closeKeyboard()

           val password : String = binding.setPassword.text.toString()

            // Get the checked radio button id from radio group
            var id: Int = binding.selectMode.checkedRadioButtonId

//            var radioButton  = id as RadioButton
//            var radioButton : RadioButton  = id

            if (id != -1) {

                if (binding.selectBlind.isChecked && password.isNotEmpty() ) {
                    alertDialogLoading()
                    val rootRef = FirebaseDatabase.getInstance().getReference("users_blind")
                    val ID = rootRef.push().key

                    val location = Locations(0.00000000, 0.00000000)
                    val caretaker = Caretaker("0812345678", "-","-", "-")
                    val device = DeviceBlind("-","-",false,"-", "-")
                    val navigation = Navigation("-", "-", "-")

                    val valueRef = FirebaseDatabase.getInstance().getReference("users_blind/$phone")
                    val rootData = UserBlinderHelperClassNew(ID.toString(), phone, "$password", "-")


                    rootRef.child(phone).setValue(rootData).addOnCompleteListener {
                        valueRef.child("Location").setValue(location).addOnCompleteListener {
                            valueRef.child("Caretaker").setValue(caretaker).addOnCompleteListener {
                                valueRef.child("Device").setValue(device).addOnCompleteListener {
                                    valueRef.child("Navigation").setValue(navigation).addOnCompleteListener {
                                        Toast.makeText(activity,getString(R.string.success_regis),Toast.LENGTH_SHORT).show()
                                        saveRegister()
                                    }
                                }
                            }
                        }

                    }
                }
                else if (binding.selectCare.isChecked && password.isNotEmpty()) {
                    alertDialogLoading()
                    val rootRef = FirebaseDatabase.getInstance().getReference("users_caretaker")
                    val ID = rootRef.push().key
                    val blind = Blind("0876543210", "-/-","-/-", "-/-")
                    val device = DeviceCaretaker("-","-", "-")
                    val valueRef = FirebaseDatabase.getInstance().getReference("users_caretaker/$phone")
                    val rootData = UserPersonHelperClassNew(
                        ID.toString(),
                        phone,
                        "$password",
                        "-",
                        "-",
                        "-")
                    rootRef.child(phone).setValue(rootData).addOnCompleteListener {
                        valueRef.child("Blind").setValue(blind).addOnCompleteListener {
                            valueRef.child("Device").setValue(device).addOnCompleteListener {
                                Toast.makeText(activity, getString(R.string.success_regis), Toast.LENGTH_SHORT).show()
                                saveRegister()
                            }
                        }
                    }
                }
                else if (password.isEmpty()) {
                    Toast.makeText(activity, "Please fill your password", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                // If no radio button checked in this radio group
                Toast.makeText(activity, "Please Select mode", Toast.LENGTH_SHORT).show()
            }
        }

        binding.setPassword.addTextChangedListener(phoneTextWatcher)

        fun onRadioButtonClicked(view: View) {
            if (view is RadioButton) {
                // Is the button now checked?
                val checked = view.isChecked
                // Check which radio button was clicked
                when (view.getId()) {
                    R.id.select_blind ->
                        if (checked) {
                            // Pirates are the best
                        }
                    R.id.select_care ->
                        if (checked) {
                            // Ninjas rule
                        }
                }
            }
        }

        return binding.root
    }

    private fun saveRegister(){
        mAlertDialog.dismiss()
        findNavController().navigate(R.id.action_littleMore_to_loginScreen)
//        val i = Intent(this, LoginScreen::class.java)
//        startActivity(i)
//        finish()
    }

    private val phoneTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val password : String = binding.setPassword.text.toString().trim()
            if(password.isNotEmpty()){
                binding.clearButton.visibility = View.VISIBLE
                binding.clearButton.setOnClickListener {
                    binding.setPassword.text?.clear()
                }
            }
            else if(password.isEmpty()){
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


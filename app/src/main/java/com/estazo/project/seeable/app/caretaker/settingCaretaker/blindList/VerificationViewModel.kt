package com.estazo.project.seeable.app.caretaker.settingCaretaker.blindList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VerificationViewModel : ViewModel() {
    var mobile = MutableLiveData<String>()
    var otp = MutableLiveData<String>()

    init {
        mobile.value = "-"
        otp.value = "-"
        Log.d("VerificationViewModel", "VerificationViewModel created!")
    }

    fun verifyOTP(phone :String , code : String) {
        mobile.value = phone
        otp.value = code
        Log.d("VerificationViewModel"," mobile :$mobile , otp :$otp")
    }


    /**Callback called when the ViewModel is destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.d("VerificationViewModel", "VerificationViewModel destroyed!")
    }

}
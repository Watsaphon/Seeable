package com.estazo.project.seeable.app.caretaker.settingCaretaker.blindList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BlindInformationViewModel : ViewModel() {

    var user = MutableLiveData<String>()
    var phone = MutableLiveData<String>()

    init {
        user.value = "-"
        phone.value = "-"
        Log.i("BlindInformationView", "BlindInformationView created!")
    }

    fun userNameBlind(userBlind : String , phoneBlind : String) {
        user.value = userBlind
        phone.value = phoneBlind
        Log.d("BlindInformationView" ," listUser :$user")
    }

    /**Callback called when the ViewModel is destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.i("BlindInformationView", "BlindInformationView destroyed!")
    }

}
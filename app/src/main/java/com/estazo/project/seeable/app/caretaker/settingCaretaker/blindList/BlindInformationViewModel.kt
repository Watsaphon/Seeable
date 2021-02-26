package com.estazo.project.seeable.app.caretaker.settingCaretaker.blindList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BlindInformationViewModel : ViewModel() {

    var user = MutableLiveData<String>()
    var phone = MutableLiveData<String>()
    var position = MutableLiveData<Int>()


    init {
        user.value = "-"
        phone.value = "-"
        position.value = 0
        Log.i("BlindInformationView", "BlindInformationView created!")
    }

    fun userNameBlind(userBlind : String , phoneBlind : String , num : Int) {
        user.value = userBlind
        phone.value = phoneBlind
        position.value = num
        Log.d("BlindInformationView" ," listUser :$user")
    }

    /**Callback called when the ViewModel is destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.i("BlindInformationView", "BlindInformationView destroyed!")
    }

}
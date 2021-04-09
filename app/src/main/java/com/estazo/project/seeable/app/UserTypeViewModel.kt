package com.estazo.project.seeable.app

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserTypeViewModel : ViewModel() {

    var userType = MutableLiveData<String>()

    init{
        userType.value = "-"
//    Log.i("eieiei","userType : $userType")
    }



    /**Callback called when the ViewModel is destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.i("UserTypeViewModel", "UserTypeViewModel destroyed!")
    }

}
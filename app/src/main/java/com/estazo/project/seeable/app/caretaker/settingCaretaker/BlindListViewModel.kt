package com.estazo.project.seeable.app.caretaker.settingCaretaker

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.estazo.project.seeable.app.caretaker.SingleLiveEvent

class BlindListViewModel : ViewModel() {

    val updateUserNameEvent = SingleLiveEvent<List<String>>()

    var listUser = MutableLiveData<List<String>>()



    init {
        listUser.value = listOf()
        Log.i("BlindListViewModel", "BlindListViewModel created!")
    }

    fun updateUserName(user : List<String>) {
//        updateUserNameEvent.value = user
        listUser.value = user
        Log.d("updateUserNameEvent","updateUserNameEvent : $updateUserNameEvent , listUser :$listUser")
    }

    /**Callback called when the ViewModel is destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.i("BlindListViewModel", "BlindListViewModel destroyed!")
    }

}
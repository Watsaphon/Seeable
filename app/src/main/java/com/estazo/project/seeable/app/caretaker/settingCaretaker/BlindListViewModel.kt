package com.estazo.project.seeable.app.caretaker.settingCaretaker

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BlindListViewModel : ViewModel() {

    var userDisplay2 = MutableLiveData<List<String>>()

    init {
        userDisplay2.value = listOf("1","2","3","4")
    }

    /**Callback called when the ViewModel is destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.i("BlindListViewModel", "BlindListViewModel destroyed!")
    }

}
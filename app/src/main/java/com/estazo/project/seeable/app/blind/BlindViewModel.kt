package com.estazo.project.seeable.app.blind

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BlindViewModel : ViewModel() {

    var titleBlind = MutableLiveData<String>()
    var titleCaretaker = MutableLiveData<String>()

    init{
        titleBlind.value = "-"
        titleCaretaker.value = "-"

    }

    /**Callback called when the ViewModel is destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.i("BlindViewModel", "BlindViewModel destroyed!")
    }

}
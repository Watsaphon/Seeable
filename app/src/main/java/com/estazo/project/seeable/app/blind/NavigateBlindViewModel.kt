package com.estazo.project.seeable.app.blind

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NavigateBlindViewModel : ViewModel() {

    var detect = MutableLiveData<String>()

    init{
        detect.value = "none"
    }

    fun detectDialog(): MutableLiveData<String> {
        return detect
    }

    /**Callback called when the ViewModel is destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.i("NavigateBlindFragment", "NavigateBlindViewModel destroyed!")
    }

}
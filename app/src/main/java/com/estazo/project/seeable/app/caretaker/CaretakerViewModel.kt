package com.estazo.project.seeable.app.caretaker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CaretakerViewModel : ViewModel() {


    val bpm: LiveData<Int>
        get() = _bpm

    // The current bpm
    private val _bpm = MutableLiveData<Int>()


    private  var userList: MutableList<String>


    init {
       _bpm.value = 0
        userList = mutableListOf("Aum", "Keaw", "Safe", "Cat")

        Log.i("CaretakerViewModel", "CaretakerViewModel created!")
    }

    /**
     * Callback called when the ViewModel is destroyed
     */
    override fun onCleared() {
        super.onCleared()
        Log.i("CaretakerViewModel", "CaretakerViewModel destroyed!")
    }

}
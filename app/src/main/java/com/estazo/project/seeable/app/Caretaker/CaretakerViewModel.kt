package com.estazo.project.seeable.app.Caretaker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CaretakerViewModel : ViewModel() {

    // The current bpm
    private val _bpm = MutableLiveData<Int>()

    val bpm: LiveData<Int>
        get() = _bpm

    init {
        _bpm.value = 0
        Log.i("CaretakerViewModel", "CaretakerViewModel created!")

    }

    /**
     * Callback called when the ViewModel is destroyed
     */
    override fun onCleared() {
        super.onCleared()
        Log.i("CaretakerViewModel", "CaretakerViewModel destroyed!")
    }

    fun bpmConnect() {
        _bpm.value = (_bpm.value)?.plus(1)

    }

}
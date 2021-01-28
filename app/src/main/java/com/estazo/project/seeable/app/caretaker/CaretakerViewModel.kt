package com.estazo.project.seeable.app.caretaker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.estazo.project.seeable.app.helperClass.Blind
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class CaretakerViewModel() : ViewModel() {

    var userTel = MutableLiveData<List<String>>()
    var userDisplay = MutableLiveData<List<String>>()


    // The current bpm
    private val _bpm = MutableLiveData<Int>()
    val bpm: LiveData<Int>
        get() = _bpm


    init {
       _bpm.value = 0
        userTel.value = listOf()
        userDisplay.value = listOf()
        Log.i("CaretakerViewModel", "CaretakerViewModel created!")
        Log.i("testPhone", "userTel : $userTel , userDisplay : $userDisplay ")
    }

    fun fetchSpinnerItems(): LiveData<List<String>> {
        userDisplay.value = listOf()
        return userDisplay
    }

    fun getPhoneUser() : LiveData<List<String>>{
        return userTel
    }

    /**Callback called when the ViewModel is destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.i("CaretakerViewModel", "CaretakerViewModel destroyed!")
    }



}



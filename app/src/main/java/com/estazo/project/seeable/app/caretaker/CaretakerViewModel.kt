package com.estazo.project.seeable.app.caretaker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class CaretakerViewModel : ViewModel() {

    var  userList = MutableLiveData<List<String>>()
    var userTel = MutableLiveData<List<String>>()
    var userDisplay = MutableLiveData<List<String>>()

    var healthStatus = MutableLiveData<String>()
    var activity = MutableLiveData<String>()
    // The current user
     val _currentUser = MutableLiveData<Int>()

    // The current phone of blind user
    val _currentBlindPhone = MutableLiveData<String>()

    init {
        userTel.value = listOf()
        _currentUser.value = -1
        _currentBlindPhone.value = "-"
        Log.i("CaretakerViewModel", "CaretakerViewModel created!")
    }

    fun fetchSpinnerItems(): LiveData<List<String>> {
        return userDisplay
    }

    /**Callback called when the ViewModel is destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.i("CaretakerViewModel", "CaretakerViewModel destroyed!")
    }

}



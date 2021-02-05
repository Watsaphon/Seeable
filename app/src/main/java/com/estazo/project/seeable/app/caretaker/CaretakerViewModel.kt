package com.estazo.project.seeable.app.caretaker

import android.util.Log
import android.view.SearchEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.estazo.project.seeable.app.helperClass.Blind
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class CaretakerViewModel() : ViewModel() {

    val message =MutableLiveData<Any>()

    val changeFragmentEvent = SingleLiveEvent<Unit>()
    val updateUserNameEvent = SingleLiveEvent<String>()

    var userTel = MutableLiveData<List<String>>()
    var userDisplay = MutableLiveData<List<String>>()

    // The current user
     val _currentUser = MutableLiveData<Int>()
    val currentUser : LiveData<Int>
        get() = _currentUser

    // The current bpm
    private val _bpm = MutableLiveData<Int>()
    val bpm: LiveData<Int>
        get() = _bpm

    // The current phone of blind user
    val _currentBlindPhone = MutableLiveData<String>()
    val currentBlindPhone : LiveData<String>
        get() = _currentBlindPhone


    val _test =  MutableLiveData<String>()
    val test : LiveData<String>
        get() = _test

    init {
        _test.value = "Test ja"
       _bpm.value = 0
        userTel.value = listOf()
        userDisplay.value = listOf()
        _currentUser.value = -1
        _currentBlindPhone.value = "-"
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

    fun getDisplayUser() : LiveData<List<String>>{
        return userDisplay
    }

    fun changeFragment() {
        changeFragmentEvent.call()
    }

    fun updateUserName(name: String) {
        updateUserNameEvent.value = name
    }

    fun setMsgCommunicator(msg:String){
        message.value = msg
    }

    /**Callback called when the ViewModel is destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.i("CaretakerViewModel", "CaretakerViewModel destroyed!")
    }

}



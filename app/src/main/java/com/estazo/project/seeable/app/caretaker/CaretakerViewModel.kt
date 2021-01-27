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

    var user = MutableLiveData<List<String>>()

     var eiei : String = "5555"

    // The current bpm
    private val _bpm = MutableLiveData<Int>()
    val bpm: LiveData<Int>
        get() = _bpm

    // The current _user1
    private val _user1 = MutableLiveData<String>()
    val user1: LiveData<String>
        get() = _user1

//    val phone = MutableLiveData<String>()
val _phone = MutableLiveData<String>()
    val phone: LiveData<String>
        get() = _phone
    init {
        _phone.value = eiei
        _user1.value = "AAA"
       _bpm.value = 0
        user.value = listOf()
        Log.i("CaretakerViewModel", "CaretakerViewModel created!")
        val test = phone.toString()
        Log.i("testPhone", "phone : $_phone , user : $user , test :$test , eiei : $eiei")
        getValue(phone.toString())
    }

    fun fetchSpinnerItems(): LiveData<List<String>> {

        user.value =  listOf()

        return user
    }

    fun getValue(item : String){
        _phone.value = item
        val gogo = item
        Log.i("testPhone", "gogo : $gogo")
    }

    /**Callback called when the ViewModel is destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.i("CaretakerViewModel", "CaretakerViewModel destroyed!")
    }


    /**receive value from realtime database (users_blind) and check Login */
    private var valueEventListener : ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                val user1 = dataSnapshot.child("user1").value.toString()
                val user2 = dataSnapshot.child("user2").value.toString()
                val user3 = dataSnapshot.child("user3").value.toString()
                val user4 = dataSnapshot.child("user4").value.toString()
                Log.d("test","user1 : $user1 ,  user2 : $user2 , user3 : $user3 , user4 : $user4")
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {

        }
    }

}



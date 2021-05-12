package com.estazo.project.seeable.app.blind.settingBlind

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AccountBlindViewModel : ViewModel() {

    var userDisplay = MutableLiveData<String>()

    init{
        userDisplay.value = "not found!!"
    }

    fun getDisplayName(phone: String) {
        var displayUser = "-"
        Log.d("test","phone : $phone")
        val firebaseRef = FirebaseDatabase.getInstance().getReference("users_blind/$phone")
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                displayUser = snapshot.child("displayName").value.toString()
                userDisplay.value = displayUser
                Log.d("test","displayUser : $displayUser")
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    /**Callback called when the ViewModel is destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.i("AccountBlindView", "AccountBlindView destroyed!")
    }

}
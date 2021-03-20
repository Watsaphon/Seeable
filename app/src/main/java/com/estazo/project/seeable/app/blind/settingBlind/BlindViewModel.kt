package com.estazo.project.seeable.app.blind.settingBlind

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BlindViewModel : ViewModel() {

    var userDisplay = MutableLiveData<String>()

    init {
        userDisplay.value = "not found!"
    }

//    fun queryInfo(phone: String) {
//        var displayUser = "-"
//        var displayUser1 = "-"
//        var displayUser2 = "-"
//        var displayUser3 = "-"
//        var displayUser4 = "-"
//        Log.i("test","phone : $phone")
//        var firebaseRef = FirebaseDatabase.getInstance().getReference("users_blind/$phone")
//        firebaseRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                displayUser = snapshot.child("displayName").value.toString()
//
//                userDisplay.value = displayUser
//
//                displayUser1 = snapshot.child("user1").value.toString()
//                displayUser2 = snapshot.child("user2").value.toString()
//                displayUser3 = snapshot.child("user3").value.toString()
//                displayUser4 = snapshot.child("user4").value.toString()
//
//                userList.value = listOf(displayUser1,displayUser2,displayUser3,displayUser4)
//
//                Log.i("testListOf()","size : $ , displayUser1 :$displayUser1 , displayUser :$displayUser2 , displayUser3 :$displayUser3 , displayUser4 :$displayUser4")
//                val splitFBUser1 = displayUser1.split("/".toRegex()).toTypedArray()
//                val phoneFBUser1 = splitFBUser1[0]
//                val nameFBUser1 = splitFBUser1[1]
//                val splitFBUser2 = displayUser2.split("/".toRegex()).toTypedArray()
//                val phoneFBUser2 = splitFBUser2[0]
//                val nameFBUser2 = splitFBUser2[1]
//                val splitFBUser3 = displayUser3.split("/".toRegex()).toTypedArray()
//                val phoneFBUser3 = splitFBUser3[0]
//                val nameFBUser3 = splitFBUser3[1]
//                val splitFBUser4 = displayUser4.split("/".toRegex()).toTypedArray()
//                val phoneFBUser4 = splitFBUser4[0]
//                val nameFBUser4 = splitFBUser4[1]
//                Log.i("testListOf()","size : $ , nameFBUser1 :$nameFBUser1 , nameFBUser2 :$nameFBUser2 , nameFBUser3 :$nameFBUser3 , nameFBUser4 :$nameFBUser4")
//                /**add user and set view on UI */
//                when{
//                    /** set 1 user */
//                    displayUser4 == "-/-" && displayUser3 == "-/-" && displayUser2 == "-/-" && displayUser1 != "-/-" ->{
//                        userDisplay.value = listOf(nameFBUser1)
//                        userTel.value = listOf(phoneFBUser1)
//                        Log.i("testListOf()1","displayUser1 :$displayUser1")
//                    }
//                    /** set 2 user */
//                    displayUser4 == "-/-" && displayUser3 == "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-"->{
//                        userDisplay.value = listOf(nameFBUser1,nameFBUser2)
//                        userTel.value = listOf(phoneFBUser1,phoneFBUser2)
//                        Log.i("testListOf()2","displayUser1 :$displayUser1 , displayUser2 :$displayUser2")
//                    }
//                    /** set 3 user */
//                    displayUser4 == "-/-" && displayUser3 != "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-" ->{
//                        userDisplay.value = listOf(nameFBUser1 , nameFBUser2 , nameFBUser3)
//                        userTel.value = listOf(phoneFBUser1 , phoneFBUser2 , phoneFBUser3)
//                        Log.i("testListOf()3","displayUser1 :$displayUser1 , displayUser :$displayUser2 , displayUser3 :$displayUser3")
//                    }
//                    /** set 4 user */
//                    displayUser4 != "-/-" && displayUser3 != "-/-" && displayUser2 != "-/-" && displayUser1 != "-/-"  ->{
//                        userDisplay.value = listOf(nameFBUser1,nameFBUser2,nameFBUser3,nameFBUser4)
//                        userTel.value = listOf(phoneFBUser1,phoneFBUser2,phoneFBUser3,phoneFBUser4)
//                        Log.i("testListOf()4","displayUser1 :$displayUser1 , displayUser :$displayUser2 , displayUser3 :$displayUser3 , displayUser4 :$displayUser4")
//                    }
//                }
//            }
//            override fun onCancelled(databaseError: DatabaseError) {
//            }
//        })
//    }

    /**Callback called when the ViewModel is destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.i("BlindViewModel", "BlindViewModel destroyed!")
    }

}
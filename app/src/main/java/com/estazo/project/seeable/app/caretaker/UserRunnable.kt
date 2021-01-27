package com.estazo.project.seeable.app.caretaker

import android.util.Log
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRunnable(val phone : String ,var callbackUser1 : TextView, var callbackUser2 : TextView, var callbackUser3 : TextView, var callbackUser4 : TextView) : Runnable {
    var firebaseRef = FirebaseDatabase.getInstance().getReference("users_caretaker/$phone/Blind")
    override fun run() {
        var user1  = "Running"
        var user2  = "Running"
        var user3  = "Running"
        var user4  = "Running"
//        firebaseRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                user1 = snapshot.child("user1").value.toString()
//                user2 = snapshot.child("user2").value.toString()
//                user3 = snapshot.child("user3").value.toString()
//                user4 = snapshot.child("user4").value.toString()
////                callbackUser1.text = user1
////                callbackUser2.text = user2
////                callbackUser3.text = user3
////                callbackUser4.text = user4
////                val userList : List<String> = ArrayList()
////                val userList : List<String>
////                userList = ArrayList()
////                (userList).add(user1)
////                (userList).add(user3)
////                userList.add(user2)
////                userList.add(user4)
//
//                val userList = UserBlind(user1,user2,user3,user4)
//                Log.d("dataclass","callbackUser1 : $callbackUser1 , callbackUser2 : $callbackUser2 " +
//                        ", user3 : $user3 , user4 : $user4 , userList : $userList")
//            }
//            override fun onCancelled(databaseError: DatabaseError) {
//            }
//        })
    }
}


package com.estazo.project.seeable.app.caretaker

import android.util.Log
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DisplayNameRunnable(val phone : String, val phoneBlindUser1 : String , val phoneBlindUser2 : String , val phoneBlindUser3 : String , val phoneBlindUser4 : String ,
                          val displayBlindUser1 : String, val displayBlindUser2 : String, val displayBlindUser3 : String, val displayBlindUser4 : String ) : Runnable {

    private var firebaseBlind1 = FirebaseDatabase.getInstance().getReference("users_blind/$phoneBlindUser1")
    private var firebaseBlind2 = FirebaseDatabase.getInstance().getReference("users_blind/$phoneBlindUser2")
    private var firebaseBlind3 = FirebaseDatabase.getInstance().getReference("users_blind/$phoneBlindUser3")
    private var firebaseBlind4 = FirebaseDatabase.getInstance().getReference("users_blind/$phoneBlindUser4")

    override fun run() {
        Log.i("checkRunAll","run()-> phone , $phone , phoneBlindUser1 : $phoneBlindUser1 ,displayBlindUser1 : $displayBlindUser1 ")
        firebaseBlind1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var displayName = snapshot.child("displayName").value.toString()
                    Log.i("checkRun1","snapshot.exists() , displayName : $displayName ,displayBlindUser1 : $displayBlindUser1 ")
                    if(displayBlindUser1 != displayName){
                        val ref = FirebaseDatabase.getInstance().reference
                        val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind/user1" to "$phoneBlindUser1/$displayName")
                        ref.updateChildren(childUpdates)
                        Log.i("checkRun1","if call , displayName : $displayName ,displayBlindUser1 : $displayBlindUser1 ")
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
        firebaseBlind2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var displayName = snapshot.child("displayName").value.toString()
                    Log.i("checkRun2","snapshot.exists() , displayName : $displayName ,displayBlindUser2 : $displayBlindUser2 ")
                    if(displayBlindUser2 != displayName){
                        val ref = FirebaseDatabase.getInstance().reference
                        val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind/user2" to "$phoneBlindUser2/$displayName")
                        ref.updateChildren(childUpdates)
                        Log.i("checkRun2","if call , displayName : $displayName ,displayBlindUser1 : $displayBlindUser2 ")
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
        firebaseBlind3.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var displayName = snapshot.child("displayName").value.toString()
                    Log.i("checkRun3","snapshot.exists() , displayName : $displayName ,displayBlindUser3 : $displayBlindUser3 ")
                    if(displayBlindUser3 != displayName){
                        val ref = FirebaseDatabase.getInstance().reference
                        val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind/user3" to "$phoneBlindUser3/$displayName")
                        ref.updateChildren(childUpdates)
                        Log.i("checkRun3","if call , displayName : $displayName ,displayBlindUser3 : $displayBlindUser3 ")
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
        firebaseBlind4.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var displayName = snapshot.child("displayName").value.toString()
                    Log.i("checkRun4","snapshot.exists() , displayName : $displayName ,displayBlindUser4 : $displayBlindUser4 ")
                    if(displayBlindUser4 != displayName){
                        val ref = FirebaseDatabase.getInstance().reference
                        val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind/user4" to "$phoneBlindUser4/$displayName")
                        ref.updateChildren(childUpdates)
                        Log.i("checkRun4","if call , displayName : $displayName ,displayBlindUser4 : $displayBlindUser4 ")
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}


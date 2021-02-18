package com.estazo.project.seeable.app.caretaker

import android.util.Log
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/** When Blind User change display name.Runnable will also change blind name in Caretaker section */
class DisplayNameRunnable(val phone : String, val user : List<String> ) : Runnable {

    private val splitFBUser1 = user[0].split("/".toRegex()).toTypedArray()
    private val phoneFBUser1 = splitFBUser1[0]
    private val nameFBUser1 = splitFBUser1[1]

    private val splitFBUser2 = user[1].split("/".toRegex()).toTypedArray()
    private val phoneFBUser2 = splitFBUser2[0]
    private val nameFBUser2 = splitFBUser2[1]

    private val splitFBUser3 = user[2].split("/".toRegex()).toTypedArray()
    private val phoneFBUser3 = splitFBUser3[0]
    private val nameFBUser3 = splitFBUser3[1]

    private val splitFBUser4 = user[3].split("/".toRegex()).toTypedArray()
    private val phoneFBUser4 = splitFBUser4[0]
    private val nameFBUser4 = splitFBUser4[1]

    private var firebaseBlind1 = FirebaseDatabase.getInstance().getReference("users_blind/$phoneFBUser1")
    private var firebaseBlind2 = FirebaseDatabase.getInstance().getReference("users_blind/$phoneFBUser2")
    private var firebaseBlind3 = FirebaseDatabase.getInstance().getReference("users_blind/$phoneFBUser3")
    private var firebaseBlind4 = FirebaseDatabase.getInstance().getReference("users_blind/$phoneFBUser4")

    override fun run() {
        Log.i("checkRunAll","run()-> phone , $phone , phoneFBUser1 : $phoneFBUser1 ,nameFBUser1 : $nameFBUser1 ")
        firebaseBlind1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var displayName = snapshot.child("displayName").value.toString()
                    Log.i("checkRun1","snapshot.exists() , displayName : $displayName ,nameFBUser1 : $nameFBUser1 ")
                    if(nameFBUser1 != displayName){
                        val ref = FirebaseDatabase.getInstance().reference
                        val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind/user1" to "$phoneFBUser1/$displayName")
                        ref.updateChildren(childUpdates)
                        Log.i("checkRun1","if call , displayName : $displayName ,nameFBUser1 : $nameFBUser1 ")
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
        firebaseBlind2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var displayName = snapshot.child("displayName").value.toString()
                    Log.i("checkRun2","snapshot.exists() , displayName : $displayName ,nameFBUser2 : $nameFBUser2 ")
                    if(nameFBUser2 != displayName){
                        val ref = FirebaseDatabase.getInstance().reference
                        val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind/user2" to "$phoneFBUser2/$displayName")
                        ref.updateChildren(childUpdates)
                        Log.i("checkRun2","if call , displayName : $displayName ,nameFBUser2 : $nameFBUser2 ")
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
        firebaseBlind3.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var displayName = snapshot.child("displayName").value.toString()
                    Log.i("checkRun3","snapshot.exists() , displayName : $displayName ,nameFBUser3 : $nameFBUser3 ")
                    if(nameFBUser3 != displayName){
                        val ref = FirebaseDatabase.getInstance().reference
                        val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind/user3" to "$phoneFBUser3/$displayName")
                        ref.updateChildren(childUpdates)
                        Log.i("checkRun3","if call , displayName : $displayName ,nameFBUser3 : $nameFBUser3 ")
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
        firebaseBlind4.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var displayName = snapshot.child("displayName").value.toString()
                    Log.i("checkRun4","snapshot.exists() , displayName : $displayName ,nameFBUser4 : $nameFBUser4 ")
                    if(nameFBUser4 != displayName){
                        val ref = FirebaseDatabase.getInstance().reference
                        val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind/user4" to "$phoneFBUser4/$displayName")
                        ref.updateChildren(childUpdates)
                        Log.i("checkRun4","if call , displayName : $displayName ,nameFBUser4 : $nameFBUser4 ")
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}


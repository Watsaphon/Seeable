package com.estazo.project.seeable.app.device

import android.util.Log
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BPMRunnable(val callback: TextView, private val currentBlindPhone : String) : Runnable{

    var firebaseRef = FirebaseDatabase.getInstance().getReference("users_blind/$currentBlindPhone/Device/bpm")

    override fun run() {
        Log.d("selectItem","currentBlindPhone : $currentBlindPhone")
        lateinit var bpm : String
        if(currentBlindPhone != "-"){
            firebaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    bpm = snapshot.value.toString()
                    Log.d("bpm_worker","bpm : $bpm")
                    callback.text = bpm
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }
    }
}
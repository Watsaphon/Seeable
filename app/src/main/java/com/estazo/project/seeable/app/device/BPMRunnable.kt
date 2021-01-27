package com.estazo.project.seeable.app.device

import android.content.SharedPreferences
import android.util.Log
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BPMRunnable(val callback: TextView) : Runnable{

    var firebaseRef = FirebaseDatabase.getInstance().getReference("users_caretaker/0863806211/Device/bpm")

    override fun run() {
        var bpm : String = "Running"
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
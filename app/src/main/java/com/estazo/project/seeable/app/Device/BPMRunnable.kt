package com.estazo.project.seeable.app.Device

import android.util.Log
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BPMRunnable(val callback: TextView) : Runnable{
    var firebaseRef = FirebaseDatabase.getInstance().getReference("users_blind/0866283062/Device/bpm")
    override fun run() {
        var text : String = "Running"
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                text = snapshot.value.toString()
                Log.d("bpm_worker","$text")
                callback.setText(text)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}
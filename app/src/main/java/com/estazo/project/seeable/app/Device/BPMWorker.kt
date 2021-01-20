package com.estazo.project.seeable.app.Device

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class BPMWorker (context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    //Please add your google-services.json file to the App directory
    internal var firebaseRef = FirebaseDatabase.getInstance().getReference("users_blind/0866283062/Device/bpm")

    override fun doWork(): Result {
        try {
            var text : String = "Hello"
            firebaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    text = snapshot.value.toString()
                    Log.d("bpm_worker","$text")
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
            val data : Data = workDataOf("BPM" to text)
            Log.d("Run this", data.getString("BPM").toString() + " - " + Calendar.getInstance().time.toString())
            return Result.success(data)
        }
        catch (e: Exception) {
            Log.d("bpm_worker ", "error")
        }
        return Result.failure()
    }

//    override fun doWork(): Result {
//        Log.d("bpm_worker ", "doWork Active ")
//        try {
//            return Result.success(workDataOf("BPM" to "Hello"))
//        }
//        catch (e: Exception) {
//
//        }
//        return Result.failure()
//    }
}




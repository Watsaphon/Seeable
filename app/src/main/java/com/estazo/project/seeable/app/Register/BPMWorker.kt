package com.estazo.project.seeable.app.Register

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.work.ListenableWorker
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
        Log.d("bpm_worker ", "doWork Active ")
        try {
            var text : String = ""
            firebaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    text = snapshot.value.toString()
                    Log.d("bpm_worker","$text")
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
            return Result.success(workDataOf("BPM" to text))
        }
        catch (e: Exception) {
            Log.d("bpm_worker ", "error")
        }
        return Result.failure()
    }
}






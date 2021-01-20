package com.estazo.project.seeable.app.Register

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.work.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class BPMWorker (context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    //Please add your google-services.json file to the App directory
    private var firebaseRef = FirebaseDatabase.getInstance().getReference("users_blind/0866283062/Device/bpm")


    override fun doWork(): Result {
        Log.d("bpm_worker ", "doWork Active ")
        try {
            var bpm : String = ""
            lateinit var outputData : Data
            firebaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    bpm = snapshot.value.toString()
//                    if(bpm != null){
                        outputData = workDataOf(
                            "bpm" to bpm
                        )
//                    }
                    Log.d("bpm_worker","onDataChange : bpm = $bpm")
                } 
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("bpm_worker ", " onCancelled : error")
                }
            })
           return Result.success(outputData)
        }
        catch (e: Exception) {
            Log.d("bpm_worker ", "Exception : error")
            return Result.failure()
        }
    }
}






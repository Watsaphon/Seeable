package com.estazo.project.seeable.app.caretaker

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/** When Blind User change display name.Runnable will also change blind name in Caretaker section */
class DisplayNameRunnable(val phone : String, val list : List<String> ) : Runnable {

    private lateinit var phoneList : String
    private var user1 = "-/-"
    private var user2 = "-/-"
    private var user3 = "-/-"
    private var user4 = "-/-"
    private var phone1 = "-"
    private var phone2 = "-"
    private var phone3 = "-"
    private var phone4 = "-"
    private var name1 = "-"
    private var name2 = "-"
    private var name3 = "-"
    private var name4 = "-"

    override fun run() {

        val size = list.size

        phoneList = list.toString().replace(", ",",")

        val user = phoneList.split(",".toRegex()).toTypedArray()

        if (size >= 1){
            user1 = user[0].substring(1)
            val info1 = user1.split("/".toRegex()).toTypedArray()
            phone1 = info1[0]
            name1 = info1[1]
            Log.d("checkCon","user1 call if").toString()
        }
        if (size >= 2){
            user2 = user[1]
            val info2 = user2.split("/".toRegex()).toTypedArray()
            phone2 = info2[0]
            name2 = info2[1]
            Log.d("checkCon","user2 call if").toString()
        }
        if (size >= 3){
            user3 = user[2]
            val info3 = user3.split("/".toRegex()).toTypedArray()
            phone3 = info3[0]
            name3 = info3[1]
            Log.d("checkCon","user3 call if").toString()
        }
        if (size >= 4){
            user4= user[3].replace("]","")
            val info4 = user4.split("/".toRegex()).toTypedArray()
            phone4 = info4[0]
            name4 = info4[1]
            Log.d("checkCon","user4 call if").toString()
        }

        Log.d("nameRunnable","Runable ->  list : $list  , phoneList : $phoneList")
        Log.d("nameRunnable","Runable -> size : $size , user1 : $user1 , user2 : $user2 , user3 : $user3 , user4 : $user4")
        Log.d("nameRunnable","Runable -> phone1 : $phone1 , phone2 : $phone2 , phone3 : $phone3 , phone4 : $phone4 ")
        Log.d("nameRunnable","Runable -> name1 : $name1 , name2 : $name2 , name3 : $name3 , name4 : $name4 ")

        if(user1 != "-/-"){
            val firebaseBlind1 = FirebaseDatabase.getInstance().getReference("users_blind/$phone1")
            firebaseBlind1.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val displayName = snapshot.child("displayName").value.toString()
                        Log.i("checkRun1","snapshot.exists() , displayName : $displayName , name1 : $name1 ")
                        if(name1 != displayName){
                            val ref = FirebaseDatabase.getInstance().reference
                            val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind/user1" to "$phone1/$displayName")
                            ref.updateChildren(childUpdates)
                            Log.i("checkRun1","if call , displayName : $displayName ,nameFBUser1 : $name1 ")
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
        if(user2 != "-/-"){
            val firebaseBlind2 = FirebaseDatabase.getInstance().getReference("users_blind/$phone2")
            firebaseBlind2.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val displayName = snapshot.child("displayName").value.toString()
                        Log.i("checkRun2","snapshot.exists() , displayName : $displayName , name2 : $name2 ")
                        if(name2 != displayName){
                            val ref = FirebaseDatabase.getInstance().reference
                            val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind/user2" to "$phone2/$displayName")
                            ref.updateChildren(childUpdates)
                            Log.i("checkRun2","if call , displayName : $displayName ,name2 : $name2 ")
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
        if(user3 != "/--"){
            val firebaseBlind3 = FirebaseDatabase.getInstance().getReference("users_blind/$phone3")
            firebaseBlind3.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val displayName = snapshot.child("displayName").value.toString()
                        Log.i("checkRun3","snapshot.exists() , displayName : $displayName , name3 : $name3 ")
                        if(name3 != displayName){
                            val ref = FirebaseDatabase.getInstance().reference
                            val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind/user3" to "$phone3/$displayName")
                            ref.updateChildren(childUpdates)
                            Log.i("checkRun3","if call , displayName : $displayName ,name3 : $name3 ")
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
        if(user4 != "-/-"){
            val firebaseBlind4 = FirebaseDatabase.getInstance().getReference("users_blind/$phone4")
            firebaseBlind4.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val displayName = snapshot.child("displayName").value.toString()
                        Log.i("checkRun4","snapshot.exists() , displayName : $displayName ,name4 : $name4 ")
                        if(name4 != displayName){
                            val ref = FirebaseDatabase.getInstance().reference
                            val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind/user4" to "$phone4/$displayName")
                            ref.updateChildren(childUpdates)
                            Log.i("checkRun4","if call , displayName : $displayName , name4 : $name4 ")
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }
}


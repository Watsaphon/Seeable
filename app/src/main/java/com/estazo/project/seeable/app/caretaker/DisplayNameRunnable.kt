package com.estazo.project.seeable.app.caretaker

import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/** When Blind User change display name.Runnable will also change blind name in Caretaker section */
class DisplayNameRunnable(val phone : String, val list : List<String> ) : Runnable {

    private lateinit var phoneList : String

    override fun run() {

        val size = list.size

        phoneList = list.toString().replace(", ",",")

        val user = phoneList.split(",".toRegex()).toTypedArray()

        var user1 = "-/-"
        var user2 = "-/-"
        var user3 = "-/-"
        var user4 = "-/-"

        var info1 : Array<String>
        var phone1 = ""
        var name1 = ""
        var info2 : Array<String>
        var phone2 = ""
        var name2 = ""
        var info3 : Array<String>
        var phone3 = ""
        var name3 = ""
        var info4 : Array<String>
        var phone4 = ""
        var name4 = ""

        if (size >= 1){
            user1 = user[0].substring(1)
            info1 = user1.split("/".toRegex()).toTypedArray()
            phone1 = info1[0]
            name1 = info1[1]
            Log.i("checkCon","user1 call if").toString()
        } else{
            user1 = "-/-"
            phone1 = ""
            name1 = ""
            Log.i("checkCon","user1 call else").toString()

        }
        if (size >= 2){
            user2 = user[1]
            info2 = user2.split("/".toRegex()).toTypedArray()
            phone2 = info2[0]
            name2 = info2[1]
            Log.i("checkCon","user2 call if").toString()
        } else{
            user2 = "-/-"
            phone2 = ""
            name2 = ""
            Log.i("checkCon","user2 call else").toString()

        }
        if (size >= 3){
            user3 = user[2]
            info3 = user3.split("/".toRegex()).toTypedArray()
            phone3 = info3[0]
            name3 = info3[1]
            Log.i("checkCon","user3 call if").toString()
        } else{
            user3 = "-/-"
            phone3 = ""
            name3 = ""
            Log.i("checkCon","user3 call else").toString()
        }
        if (size >= 4){
            user4= user[3].replace("]","")
            info4 = user4.split("/".toRegex()).toTypedArray()
            phone4 = info4[0]
            name4 = info4[1]
            Log.i("checkCon","user4 call if").toString()
        } else{
            user4 = "-/-"
            phone4 = ""
            name4 = ""
            Log.i("checkCon","user4 call else").toString()
        }


        Log.i("nameRunnable","Runable ->  list : $list  , phoneList : $phoneList")
        Log.i("nameRunnable","Runable -> size : $size , user1 : $user1 , user2 : $user2 , user3 : $user3 , user4 : $user4")
        Log.i("nameRunnable","Runable -> phone1 : $phone1 , phone2 : $phone2 , phone3 : $phone3 , phone4 : $phone4 ")
        Log.i("nameRunnable","Runable -> name1 : $name1 , name2 : $name2 , name3 : $name3 , name4 : $name4 ")


        if(user1 != "-/-"){
            val firebaseBlind1 = FirebaseDatabase.getInstance().getReference("users_blind/$phone1")
            firebaseBlind1.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var displayName = snapshot.child("displayName").value.toString()
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
                        var displayName = snapshot.child("displayName").value.toString()
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
                        var displayName = snapshot.child("displayName").value.toString()
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
                        var displayName = snapshot.child("displayName").value.toString()
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
//        Log.i("checkRunAll","run()-> phone , $phone , phoneFBUser1 : $phoneFBUser1 ,nameFBUser1 : $nameFBUser1 ")
    }
}


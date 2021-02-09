package com.estazo.project.seeable.app.caretaker

import android.util.Log
import com.estazo.project.seeable.app.helperClass.Blind
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/** When Blind User in Caretaker section deleted  Runnable will sort user1-4 */
class UpdateListBlindUserRunnable(val phone : String ) : Runnable {

    var firebaseRef = FirebaseDatabase.getInstance().getReference("users_caretaker/$phone/Blind")

        override fun run() {
            firebaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var user1 = snapshot.child("user1").value.toString()
                        var user2 = snapshot.child("user2").value.toString()
                        var user3 = snapshot.child("user3").value.toString()
                        var user4 = snapshot.child("user4").value.toString()
                        when{
                            /**update blind user for 4 user */
                            user1 == "-/-" && user2 != "-/-" && user3 != "-/-" && user4 != "-/-" -> {
                                Log.i("testUpdate4user1","delete user 1 ")
                                val ref = FirebaseDatabase.getInstance().reference
                                val updateList =  Blind(user2,user3,user4,"-/-")
                                val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind" to updateList)
                                ref.updateChildren(childUpdates)
                            }
                            user1 != "-/-" && user2 == "-/-" && user3 != "-/-" && user4 != "-/-" -> {
                                Log.i("testUpdate4user2","delete user 2 ")
                                val ref = FirebaseDatabase.getInstance().reference
                                val updateList =  Blind(user1,user3,user4,"-/-")
                                val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind" to updateList)
                                ref.updateChildren(childUpdates)
                            }
                            user1 != "-/-" && user2 != "-/-" && user3 == "-/-" && user4 != "-/-"  -> {
                                Log.i("testUpdate4user3","delete user 3 ")
                                val ref = FirebaseDatabase.getInstance().reference
                                val updateList =  Blind(user1,user2,user4,"-/-")
                                val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind" to updateList)
                                ref.updateChildren(childUpdates)
                            }
                            /**update blind user for 3 user */
                            user1 == "-/-" && user2 != "-/-" && user3 != "-/-" && user4 == "-/-"  -> {
                                Log.i("testUpdate3user1","delete user 1 ")
                                val ref = FirebaseDatabase.getInstance().reference
                                val updateList =  Blind(user2,user3,"-/-","-/-")
                                val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind" to updateList)
                                ref.updateChildren(childUpdates)
                            }
                            user1 != "-/-" && user2 == "-/-" && user3 != "-/-" && user4 == "-/-"  -> {
                                Log.i("testUpdate3user2","delete user 2 ")
                                val ref = FirebaseDatabase.getInstance().reference
                                val updateList =  Blind(user1,user3,"-/-","-/-")
                                val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind" to updateList)
                                ref.updateChildren(childUpdates)
                            }
                            /**update blind user for 2 user */
                            user1 == "-/-" && user2 != "-/-" && user3 == "-/-" && user4 == "-/-"  -> {
                                Log.i("testUpdate3user1","delete user 1 ")
                                val ref = FirebaseDatabase.getInstance().reference
                                val updateList =  Blind(user2,"-/-","-/-","-/-")
                                val childUpdates = hashMapOf<String, Any>("users_caretaker/$phone/Blind" to updateList)
                                ref.updateChildren(childUpdates)
                            }
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

        }
    }



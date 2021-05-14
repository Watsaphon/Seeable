package com.estazo.project.seeable.app.helperClass

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


data class UserPersonHelperClassNew(
    val ID : String ,
    val Phone: String,
    val Password: String,
    val DisplayName: String,
    val CriticalEvent: String,
    val Notification : String,
    val FCM : String
){
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to ID,
            "phone" to Phone,
            "password" to Password,
            "displayName" to DisplayName,
            "criticalEvent" to CriticalEvent,
            "notification" to Notification,
            "FCM" to FCM
        )
    }
}

@IgnoreExtraProperties
data class Blind(
    var user1: String ,
    var user2: String ,
    var user3: String ,
    var user4: String
)

@IgnoreExtraProperties
data class DeviceCaretaker(
    var Activity: String ,
    var BPM: String ,
    var Health_Status: String
)




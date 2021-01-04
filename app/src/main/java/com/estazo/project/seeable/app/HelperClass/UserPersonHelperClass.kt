package com.estazo.project.seeable.app.HelperClass

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserPersonHelperClass(
    val ID : String ,
    val Username: String,
    val  Password: String,
    val FullName: String,
    val Phone: String,
    val partner_id : String
){ @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to ID,
            "username" to Username,
            "password" to Password,
            "fullName" to FullName,
            "phone" to Phone,
            "partner_id" to partner_id
        )
    }
}

data class UserPersonHelperClassNew(
    val ID : String ,
    val Phone: String,
    val Password: String,
    val DisplayName: String,
    val CriticalEvent: String,
    val Notification : String
){ @Exclude
fun toMap(): Map<String, Any?> {
    return mapOf(
        "id" to ID,
        "phone" to Phone,
        "password" to Password,
        "displayName" to DisplayName,
        "criticalEvent" to CriticalEvent,
        "notification" to Notification
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
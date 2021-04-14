package com.estazo.project.seeable.app.helperClass

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


data class UserBlinderHelperClassNew(
    val ID: String,
    val Phone: String,
    val Password: String,
    val DisplayName: String,
    val Notification: String
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to ID,
            "phone" to Phone,
            "password" to Password,
            "displayName" to DisplayName,
            "notification" to Notification
        )
    }
}



data class Locations(
    var Latitude: Double ,
    var Longitude: Double
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "latitude" to Latitude,
            "longitude" to Longitude
        )
    }
}

data class Notification(
    var timestamp: String ,
    var type: String
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "timestamp" to timestamp,
            "type" to type
        )
    }
}

@IgnoreExtraProperties
data class Caretaker(
    var user1: String = "-",
    var user2: String = "-",
    var user3: String = "-",
    var user4: String = "-"
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "user1" to user1,
            "user2" to user2,
            "user3" to user3,
            "user4" to user4
        )
    }
}

@IgnoreExtraProperties
data class DeviceBlind(
    var Activity: String = "-",
    var BPM: String = "-",
    var Critical_Condition: Boolean = false,
    var Fall_Detection: String = "no",
    var Health_Status: String = "-"
)

@IgnoreExtraProperties
data class Navigation(
    var Title_Navigate_blindUser: String,
    var Navigate_blindUser: String,
    var Title_Navigate_careUser: String,
    var Navigate_careUser: String
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "navigate_blindUser" to Navigate_blindUser,
            "navigate_careUser" to Navigate_careUser,
            "title_Navigate_blindUser" to Title_Navigate_blindUser,
            "title_Navigate_careUser" to Title_Navigate_careUser
        )
    }
}


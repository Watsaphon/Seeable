package com.estazo.project.seeable.app.helperClass

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

//class UserBlinderHelperClass (val ID : String , val Username: String, val Password: String, val FullName: String, val Phone: String, val NameHelper: String, val PhoneHelper: String,val latitude: Int, val longitude: Int)

@IgnoreExtraProperties
data class UserBlinderHelperClass(
    val ID: String,
    val Username: String,
    val Password: String,
    val FullName: String,
    val Phone: String,
    val NameHelper: String,
    val PhoneHelper: String,
    val latitude: Double,
    val longitude: Double ,
    val homeLocation : String
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to ID,
            "username" to Username,
            "password" to Password,
            "fullName" to FullName,
            "phone" to Phone,
            "nameHelper" to NameHelper,
            "phoneHelper" to PhoneHelper,
            "latitude" to latitude,
            "longitude" to longitude,
            "homeLocation" to homeLocation
        )
    }
}


data class UserBlinderHelperClassNew(
    val ID: String,
    val Phone: String,
    val Password: String,
    val DisplayName: String
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to ID,
            "phone" to Phone,
            "password" to Password,
            "displayName" to DisplayName
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
    var Fall_Detection: String = "-",
    var Health_Status: String = "-"
)

@IgnoreExtraProperties
data class Navigation(
    var Caretaker_Navigate: String ,
    var Self_Navigate_bindUser: String,
    var Self_Navigate_careUser: String
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "caretaker_Navigate" to Caretaker_Navigate,
            "self_Navigate_bindUser" to Self_Navigate_bindUser,
            "self_Navigate_careUser" to Self_Navigate_careUser
        )
    }
}


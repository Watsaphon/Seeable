package com.estazo.project.seeable.app.HelperClass

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
    val longitude: Double
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
            "longitude" to longitude
        )
    }
}

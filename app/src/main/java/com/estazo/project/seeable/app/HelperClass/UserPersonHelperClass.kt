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
    val  Password: String,
    val FullName: String,
    val Sex: String,
    val partner_id : String
){ @Exclude
fun toMap(): Map<String, Any?> {
    return mapOf(
        "id" to ID,
        "phone" to Phone,
        "password" to Password,
        "fullName" to FullName,
        "sex" to Sex,
        "partner_id" to partner_id
    )
}
}
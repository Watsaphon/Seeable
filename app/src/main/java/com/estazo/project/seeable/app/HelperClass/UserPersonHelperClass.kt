package com.estazo.project.seeable.app.HelperClass

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserPersonHelperClass(
    val ID : String ,
    val Username: String,
    val  Password: String,
    val FullName: String,
    val Phone: String,
    val pairingID : String)


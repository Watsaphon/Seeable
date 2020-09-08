package com.estazo.project.seeable.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class SignoutByGmail : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var sign_out: Button
    private lateinit var nameTV: TextView
    private lateinit var emailTV: TextView
    private lateinit var idTV: TextView
    private lateinit var photoIV: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signout_by_gmail)
        sign_out = findViewById(R.id.log_out)
        nameTV = findViewById(R.id.name)
        emailTV = findViewById(R.id.email)
        idTV = findViewById(R.id.id)
        photoIV = findViewById(R.id.photo)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            val personName = acct.displayName
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            val personEmail = acct.email
            val personId = acct.id
            val personPhoto: Uri? = acct.photoUrl
            nameTV.text = "Name: $personName"
            emailTV.text = "Email: $personEmail"
            idTV.text = "ID: $personId"
            Glide.with(this).load(personPhoto).apply(RequestOptions.circleCropTransform()).into(photoIV)



        }
        sign_out.setOnClickListener(View.OnClickListener { signOut() })
    }

    private fun signOut() {
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) {
                Toast.makeText(
                    this,
                    "Successfully signed out",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, SigninByGmail::class.java))
                finish()
            }
    }
}
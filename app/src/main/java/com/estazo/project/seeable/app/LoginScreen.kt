package com.estazo.project.seeable.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*


class LoginScreen : AppCompatActivity() {

    private lateinit var userNameBox : EditText
    private lateinit var passwordBox  : EditText

    private lateinit var finish : Button
    private lateinit var register : Button
    private var RC_SIGN_IN = 0
    private lateinit var signInButton: SignInButton
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var reff: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        //Initializing Views
        signInButton = findViewById(R.id.sign_in_button)
        userNameBox = findViewById(R.id.username_box)
        passwordBox= findViewById(R.id.password_box)
        finish= findViewById(R.id.login_finish_button)
        register= findViewById(R.id.regis_button)


        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        finish.setOnClickListener(View.OnClickListener { login() })
        signInButton.setOnClickListener(View.OnClickListener { signIn() })
        register.setOnClickListener(View.OnClickListener { register() })

    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    private fun register() {
        val intent = Intent(this, SelectRegister::class.java)
        startActivity(intent)
    }

    private fun login() {
       val loginName = userNameBox.text.toString()
       val loginPassword = passwordBox.text.toString()
        reff = FirebaseDatabase.getInstance().reference.child("users_person").child("-MGiPEZ6x6z9_AXOkuzN")

        reff.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val fullname = dataSnapshot.child("fullName").value.toString()
                val password = dataSnapshot.child("password").value.toString()
                val username = dataSnapshot.child("username").value.toString()
                val phone = dataSnapshot.child("phone").value.toString()

                Log.i("login","$loginName,$loginPassword")
                Log.i("login","$fullname,$password,$username,$phone")

                if(loginName.equals(username) && loginPassword.equals(password)){
//                    val intent = Intent(this@LoginScreen,MainActivity::class.java)
//                    startActivity(intent)
                    Toast.makeText(applicationContext,"Login Success",Toast.LENGTH_SHORT).show()
                }
                else if(loginName.equals(username)) {
                    Toast.makeText(applicationContext,"Password Incorrect",Toast.LENGTH_SHORT).show()
                }
                else if(loginPassword.equals(password)){
                    Toast.makeText(applicationContext,"Username Incorrect",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(applicationContext,"Username and Password Incorrect",Toast.LENGTH_SHORT).show()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("login","error")
               // mTextView.setText("Failed: ")
            }
        })


    }



    public override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            startActivity(Intent(this, SignoutByGmail::class.java))
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(
                "Google Sign In Error",
                "signInResult:failed code=" + e.statusCode
            )
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            startActivity(Intent(this, SignoutByGmail::class.java))
        }
        super.onStart()
    }

}

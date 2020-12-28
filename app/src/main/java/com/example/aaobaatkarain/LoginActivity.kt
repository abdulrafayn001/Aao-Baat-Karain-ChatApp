package com.example.aaobaatkarain

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    var fbUser:FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onStart() {
        super.onStart()
        fbUser = FirebaseAuth.getInstance().currentUser
        if(fbUser != null)
        {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}
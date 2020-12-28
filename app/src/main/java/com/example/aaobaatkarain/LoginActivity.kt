package com.example.aaobaatkarain

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



    }

    fun userSignUp(view: View) {
        startActivity(Intent(this,RegisterActivity::class.java))
    }
    fun userLogin(view: View) {

    }
}
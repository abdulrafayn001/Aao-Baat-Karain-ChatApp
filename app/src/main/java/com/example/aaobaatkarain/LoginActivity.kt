package com.example.aaobaatkarain

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private var fbUser:FirebaseUser? = null
    private lateinit var userAuth:FirebaseAuth

    private lateinit var Email:EditText
    private lateinit var Pswd:EditText
    private lateinit var Login_btn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Email = findViewById(R.id.email_login)
        Pswd = findViewById(R.id.password_login)
        Login_btn = findViewById(R.id.btn_login)

        userAuth = FirebaseAuth.getInstance()

        Login_btn.setOnClickListener {
            val email:String = Email.text.toString()
            val pswd:String = Pswd.text.toString()

            if (email == "")
            {
                Toast.makeText(this,"Email Can't be Empty", Toast.LENGTH_LONG).show()
            }
            else if (pswd == "")
            {
                Toast.makeText(this,"Password Can't be Empty", Toast.LENGTH_LONG).show()
            }
            else
            {
                println("--------------------------------------------------------"+email);
                println("================================================="+pswd);
                userAuth.signInWithEmailAndPassword(email,pswd)
                        .addOnCompleteListener { task->
                            if(task.isSuccessful)
                            {
                                Toast.makeText(this,"Login Sucessfull", Toast.LENGTH_LONG).show()
                                startActivity(Intent(this,MainActivity::class.java))
                                finish()
                            }
                            else
                            {
                                Toast.makeText(this,"Invalid Email or Password!", Toast.LENGTH_LONG).show()
                            }
                        }
            }

        }

    }


    fun userSignup(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
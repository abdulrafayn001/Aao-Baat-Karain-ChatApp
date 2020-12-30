package com.example.aaobaatkarain

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var userAuth:FirebaseAuth

    private lateinit var Email:EditText
    private lateinit var Pswd:EditText
    private lateinit var Login_btn:Button

    var user:FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        Email = findViewById(R.id.email_login)
        Pswd = findViewById(R.id.password_login)
        Login_btn = findViewById(R.id.btn_login)

        userAuth = FirebaseAuth.getInstance()

        val data: SharedPreferences = getSharedPreferences("LOG", MODE_PRIVATE)
        val gedit = data.edit()
        if(userAuth.currentUser != null && data.getBoolean("LogSucess",false))
        {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

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
                var waitingBar: ProgressDialog = ProgressDialog(this)
                waitingBar.setMessage("Signing in Please Wait!")
                waitingBar.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                waitingBar.setTitle("Sign In")
                waitingBar.show()


                userAuth.signInWithEmailAndPassword(email,pswd)
                        .addOnCompleteListener { task->
                            if(task.isSuccessful)
                            {
                                waitingBar.dismiss()
                                Toast.makeText(this,"Login Successfull", Toast.LENGTH_LONG).show()
                                gedit.putBoolean("LogSucess",true)
                                gedit.apply()
                                gedit.commit()
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
        finish()
    }

    fun userLogin(view: View) {

    }
}
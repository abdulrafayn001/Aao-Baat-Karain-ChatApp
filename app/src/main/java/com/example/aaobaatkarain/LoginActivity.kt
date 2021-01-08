package com.example.aaobaatkarain

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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

        //Store instance of FirebaseAuth
        userAuth = FirebaseAuth.getInstance()

        //Create a store with the name of "LOG" in shared preference
        val data: SharedPreferences = getSharedPreferences("LOG", MODE_PRIVATE)
        val gedit = data.edit()


        //If user is already logged in go to main activity
        if(userAuth.currentUser != null && data.getBoolean("LogSucess",false))
        {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }




        //If the user click on login button bellow action will take place
        Login_btn.setOnClickListener {
            //Convert email and password from raw to string
            val email:String = Email.text.toString()
            val pswd:String = Pswd.text.toString()


            when {
                //if email input field is empty
                email == "" -> {
                    Toast.makeText(this,"Email Can't be Empty", Toast.LENGTH_LONG).show()
                }
                //if password input filed is empty
                pswd == "" -> {
                    Toast.makeText(this,"Password Can't be Empty", Toast.LENGTH_LONG).show()
                }
                //if both email and password input fields are not empty
                else -> {
                    //Loading Screen
                    val waitingBar: ProgressDialog = ProgressDialog(this)
                    waitingBar.setMessage("Signing in Please Wait!")
                    waitingBar.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                    waitingBar.setTitle("Sign In")
                    waitingBar.show()


                    //Login User
                    userAuth.signInWithEmailAndPassword(email,pswd)
                            .addOnCompleteListener { task->
                                if(task.isSuccessful)
                                {
                                    //dismiss the loading if successfully logged in
                                    waitingBar.dismiss()

                                    //Toast message on successful login
                                    Toast.makeText(this,"Login Successful", Toast.LENGTH_LONG).show()
                                    //Put a variable "LogSucess" with bool value "true" to make sure that if user again open the App he/she will go to main screen
                                    gedit.putBoolean("LogSucess",true)
                                    gedit.apply()
                                    gedit.commit()


                                    //After successfully login move to main activity
                                    startActivity(Intent(this,MainActivity::class.java))
                                    finish()
                                }//If user do not login successfully then print a toast message
                                else
                                {
                                    waitingBar.dismiss()
                                    Toast.makeText(this,"Invalid Email or Password!", Toast.LENGTH_LONG).show()
                                }
                            }
                }
            }

        }

    }

    //If user click on signUp or register button then move to register activity
    fun userSignup(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }

}
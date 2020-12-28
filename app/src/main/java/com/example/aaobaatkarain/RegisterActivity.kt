package com.example.aaobaatkarain

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class RegisterActivity : AppCompatActivity() {

    private lateinit var userAuth:FirebaseAuth
    private lateinit var userRefernc:DatabaseReference

    private lateinit var UserName:EditText;
    private lateinit var Email:EditText;
    private lateinit var Password:EditText;
    private lateinit var C_Parrword:EditText;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        UserName = findViewById(R.id.username_register)
        Email = findViewById(R.id.email_register)
        Password = findViewById(R.id.password_register)
        C_Parrword = findViewById(R.id.password_confirm)

        userAuth = FirebaseAuth.getInstance()
    }

    fun userSignup(view: View)
    {
        registerUser(
                UserName.text.toString(),
                Email.text.toString(),
                Password.text.toString(),
                C_Parrword.text.toString()
        )
    }
    private fun registerUser(User:String,Email:String,Pswd:String,C_Pswd:String)
    {
        if(User.equals(""))
        {
            Toast.makeText(this,"UserName Can't be Empty",Toast.LENGTH_LONG).show()
        }
        else if (Email.equals(""))
        {
            Toast.makeText(this,"Email Can't be Empty",Toast.LENGTH_LONG).show()
        }
        else if (Pswd.equals(""))
        {
            Toast.makeText(this,"Password Can't be Empty",Toast.LENGTH_LONG).show()
        }
        else if(C_Pswd.equals(""))
        {
            Toast.makeText(this,"Password Can't be Empty",Toast.LENGTH_LONG).show()
        }
        else
        {
            
        }
    }
}
package com.example.aaobaatkarain
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.HashMap

class RegisterActivity : AppCompatActivity() {

    private lateinit var userAuth:FirebaseAuth
    private lateinit var userRefernc:DatabaseReference
    private var FireBaseID:String = ""

    private lateinit var UserName:EditText
    private lateinit var Email:EditText
    private lateinit var Password:EditText
    private lateinit var C_Parrword:EditText

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
    private fun registerUser(user:String,email:String,pswd:String,c_pswd:String)
    {
        if(user.equals(""))
        {
            PrintError("UserName Can't be Empty")
        }
        else if (email.equals(""))
        {
            PrintError("Email Can't be Empty")
        }
        else if (pswd.equals(""))
        {
            PrintError("Password Can't be Empty")
        }
        else if(c_pswd.equals(""))
        {
            PrintError("Confirm Password Can't be Empty")
        }
        else
            if(c_pswd!=pswd)
            {
                PrintError("Password does not match")
            }
        else
        {
            if(validateEmail(email) && validatePassword(pswd))
            {
                val waitingBar: ProgressDialog = ProgressDialog(this)
                waitingBar.setMessage("Registering your account. Please Wait!")
                waitingBar.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                waitingBar.setTitle("Register")
                waitingBar.progress = 1
                waitingBar.show()
                userAuth.createUserWithEmailAndPassword(email, pswd).addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        waitingBar.progress = 0
                        FireBaseID = userAuth.currentUser!!.uid
                        userRefernc = FirebaseDatabase.getInstance().reference.child("Users").child(FireBaseID)

                        val UserMap = HashMap<String, Any>()
                        UserMap["uid"] = FireBaseID
                        UserMap["username"] = user
                        UserMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/aao-baat-karain.appspot.com/o/PlaceHolder.png?alt=media&token=9c73c7e0-10e6-4040-9852-45f68073cb27"
                        UserMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/aao-baat-karain.appspot.com/o/cover.jpg?alt=media&token=ca58ac4d-e0e1-48d1-924e-25ba2c22e12e"
                        UserMap["facebook"] = "www.facebook.com"
                        UserMap["instagram"] = "www.instagram.com"
                        UserMap["website"] = "www.google.com"
                        UserMap["status"] = "offline"
                        UserMap["search"] = user.toLowerCase(Locale.ROOT)
                        userRefernc.updateChildren(UserMap).addOnCompleteListener { task ->
                            if (task.isSuccessful)
                            {
                                PrintError("Registered Successfully!")
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                            else
                            {
                                PrintError("Error Occurred! Try Again Later!")
                            }
                        }
                    }
                    else
                    {
                        PrintError("Error Occurred Try Again Later!")
                    }
                }
            }
            else
            {
                PrintError("Invalid Email Type")
            }

        }
    }
    private fun validateEmail(email:String): Boolean {

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
                PrintError("Invalid Email Address")
                return false
        }
        else
        {
                return true
        }
    }
    private fun validatePassword(password: String): Boolean
    {
        if(password.contains(" "))
        {
            PrintError("Password Should not contain spaces. Try Again!")
            return false
        }
        else if(password.length < 8)
        {
            PrintError("Password Should have minimum length of 8 Characters. Try Again!")
            return false
        }
        else
        {
           return true
        }
    }
    private fun PrintError(v:String)
    {
        Toast.makeText(this, v, Toast.LENGTH_SHORT).show()
    }
}
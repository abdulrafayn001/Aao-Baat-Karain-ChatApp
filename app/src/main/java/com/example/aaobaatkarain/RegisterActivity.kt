package com.example.aaobaatkarain
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.utilities.Utilities
import com.google.firebase.installations.Utils
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
        {
            if(validateEmail(email) && validatePassword(pswd))
            {
                userAuth.createUserWithEmailAndPassword(email, pswd).addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        FireBaseID = userAuth.currentUser!!.uid
                        userRefernc = FirebaseDatabase.getInstance().reference.child("Users").child(FireBaseID)

                        val UserMap = HashMap<String, Any>()
                        UserMap["uid"] = FireBaseID
                        UserMap["username"] = user
                        UserMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/aao-baat-karain.appspot.com/o/PlaceHolder.png?alt=media&token=9c73c7e0-10e6-4040-9852-45f68073cb27"
                        UserMap["status"] = "offline"
                        UserMap["search"] = user.toLowerCase(Locale.ROOT)
                        userRefernc.updateChildren(UserMap).addOnCompleteListener { task ->
                            if (task.isSuccessful)
                            {
                                PrintError("Registered Successfully!")
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
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
            return false;
        }
        else if(password.length < 8)
        {
            PrintError("Password Should have minimum length of 8 Characters. Try Again!")
            return false;
        }
        else
        {
           return true;
        }
    }
    private fun PrintError(v:String)
    {
        Toast.makeText(this, v, Toast.LENGTH_SHORT).show()
    }
}
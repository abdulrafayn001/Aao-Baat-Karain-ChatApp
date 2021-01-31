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

    //The bellow function will trigger when user click on SignUp or register button in RegisterActivity
    fun userSignup(view: View)
    {
        //registerUser function will on clicking signUp button
        registerUser(
                UserName.text.toString(),
                Email.text.toString(),
                Password.text.toString(),
                C_Parrword.text.toString()
        )
    }
    private fun registerUser(user:String,email:String,pswd:String,c_pswd:String)
    {
        //if username input filed is empty print an error message
        if(user == "")
        {
            PrintError("UserName Can't be Empty")
        }
        //if email input filed is empty print an error message
        else if (email == "")
        {
            PrintError("Email Can't be Empty")
        }
        //if password input filed is empty print an error message
        else if (pswd == "")
        {
            PrintError("Password Can't be Empty")
        }
        //if confirm password input filed is empty print an error message
        else if(c_pswd == "")
        {
            PrintError("Confirm Password Can't be Empty")
        }
        else //if password and confirm password does not match print an error message
            if(c_pswd!=pswd)
            {
                PrintError("Password does not match")
            }
        else
        {
            //Check the validation of email and password
            if(validateEmail(email) && validatePassword(pswd))
            {
                // Loading screen while register
                val waitingBar: ProgressDialog = ProgressDialog(this,R.style.MyAlertDialogStyle)
                waitingBar.setMessage("Registering your account. Please Wait!")
                waitingBar.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                waitingBar.setTitle("Register")
                waitingBar.show()
                //Create a user with given email and password
                userAuth.createUserWithEmailAndPassword(email, pswd).addOnCompleteListener { task ->
                    //if user account is successfully created
                    if (task.isSuccessful)
                    {
                        //Get a unique id of newly created account
                        FireBaseID = userAuth.currentUser!!.uid

                        //Get the reference of newly created user fro firebase database
                        userRefernc = FirebaseDatabase.getInstance().reference.child("Users").child(FireBaseID)

                        //Create a HashMap and store all the information of user in it.
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

                        //Update the data of newly created account in firebase database
                        userRefernc.updateChildren(UserMap).addOnCompleteListener { task ->
                            //If data is successfully stored in database, stop loading.
                            if (task.isSuccessful)
                            {
                                waitingBar.dismiss()
                                PrintError("Registered Successfully!")
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }//if data is not successfully stored in data base print ab error message.
                            else
                            {
                                waitingBar.dismiss()
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

        return if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            PrintError("Invalid Email Address")
            false
        }
        else
        {
            true
        }
    }
    private fun validatePassword(password: String): Boolean
    {
        return when {
            password.contains(" ") -> {
                PrintError("Password Should not contain spaces. Try Again!")
                false
            }
            password.length < 8 -> {
                PrintError("Password Should have minimum length of 8 Characters. Try Again!")
                false
            }
            else -> {
                true
            }
        }
    }
    private fun PrintError(v:String)
    {
        Toast.makeText(this, v, Toast.LENGTH_SHORT).show()
    }
}
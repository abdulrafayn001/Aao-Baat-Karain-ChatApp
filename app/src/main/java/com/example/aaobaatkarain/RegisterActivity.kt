package com.example.aaobaatkarain
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
               userAuth.createUserWithEmailAndPassword(Email, Pswd).addOnCompleteListener {
                   task->
                   if(task.isSuccessful)
                   {
                       FireBaseID = userAuth.currentUser!!.uid
                       userRefernc = FirebaseDatabase.getInstance().reference.child("Users").child(FireBaseID)

                       val UserMap = HashMap<String,Any>()
                       UserMap["uid"] = FireBaseID
                       UserMap["username"] = User
                       UserMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/aao-baat-karain.appspot.com/o/PlaceHolder.png?alt=media&token=9c73c7e0-10e6-4040-9852-45f68073cb27"
                       UserMap["status"] = "offline"
                       UserMap["search"] = User.toLowerCase(Locale.ROOT)
                       userRefernc.updateChildren(UserMap)
                               .addOnCompleteListener { task->
                                   if(task.isSuccessful)
                                   {
                                       Toast.makeText(this,"Registered Sucessfully!",Toast.LENGTH_LONG).show()
                                       val intent = Intent(this,LoginActivity::class.java)
                                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                       startActivity(intent)
                                       finish()
                                   }
                                   else
                                   {
                                       Toast.makeText(this,"Error Occured Try Again Later!",Toast.LENGTH_LONG).show()
                                   }
                               }
                   }
                   else
                   {
                       Toast.makeText(this,"Error Occured Try Again Later!",Toast.LENGTH_LONG).show()
                   }
               }
        }
    }
}
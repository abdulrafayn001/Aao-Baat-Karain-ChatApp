package com.example.aaobaatkarain.Fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.aaobaatkarain.ModelClasses.Users
import com.example.aaobaatkarain.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class SettingsFragment : Fragment() {

    var userRef:DatabaseReference? = null
    var firebaseUser:FirebaseUser? = null
    private val ReqCode = 898
    private var imageRrl:Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        userRef = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(firebaseUser!!.uid)

        userRef!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val user:Users? = snapshot.getValue(Users::class.java)
                    view.findViewById<TextView>(R.id.usernameProf).text = user!!.getUsername()
                    if(context!=null)
                    {
                        Picasso.get()
                                .load(user.getCover())
                                .into(view.findViewById<ImageView>(R.id.cover_image))
                        Picasso.get()
                                .load(user.getProfile())
                                .into(view.findViewById<ImageView>(R.id.profile_image))

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        view.findViewById<ImageView>(R.id.profile_image).setOnClickListener {
            changeImage()
        }


        return view
    }

    private fun changeImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,ReqCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == ReqCode &&
                resultCode == Activity.RESULT_OK && data!!.data!=null)
        {
            // Pending
        }
    }

}
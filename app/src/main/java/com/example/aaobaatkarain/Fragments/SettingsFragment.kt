package com.example.aaobaatkarain.Fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.aaobaatkarain.ModelClasses.Users
import com.example.aaobaatkarain.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso


class SettingsFragment : Fragment() {

    var userRef:DatabaseReference? = null
    var firebaseUser:FirebaseUser? = null
    private val ReqCode = 898
    private var imageAddress:Uri? = null
    private var storageRef:StorageReference? = null
    private var isCover:Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        userRef = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(firebaseUser!!.uid)
        storageRef = FirebaseStorage.getInstance().reference.child("UserImages")

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
            isCover = true
            changeImage()
        }
        view.findViewById<ImageView>(R.id.cover_image).setOnClickListener {
            isCover = false
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
            imageAddress = data.data
            Toast.makeText (context,"Uploading Image",Toast.LENGTH_SHORT).show()
            uploadImageFirebase()
        }
    }

    private fun uploadImageFirebase() {
        val progress = ProgressDialog(context)
        progress.setTitle("Uploading Image")
        progress.setTitle("Uploading image please wait!")
        progress.show()

        if(imageAddress!=null)
        {
            val fileRef = storageRef?.child(System.currentTimeMillis().toString()+".jpg")
            val uploadTask:StorageTask<*>
            if (fileRef != null)
            {
                uploadTask = fileRef.putFile(imageAddress!!)
                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task->
                    if(!task.isSuccessful)
                    {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener { task->
                    if(task.isSuccessful)
                    {
                        val downloadUrl = task.result
                        val Url = downloadUrl.toString()

                        if(isCover)
                        {
                            val mapCover = HashMap<String,Any>()
                            mapCover["cover"] = Url
                            userRef!!.updateChildren(mapCover)
                            isCover = false
                        }
                        else
                        {
                            val mapProfile = HashMap<String,Any>()
                            mapProfile["profile"] = Url
                            userRef!!.updateChildren(mapProfile)
                        }
                        progress.dismiss()
                    }
                }
            }
        }
    }

}
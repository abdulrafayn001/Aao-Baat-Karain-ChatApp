package com.example.aaobaatkarain.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
    private var socialLink:String? = null


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

        userRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user: Users? = snapshot.getValue(Users::class.java)
                    view.findViewById<TextView>(R.id.usernameProf).text = user!!.getUsername()
                    if (context != null) {
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
        // cover picture changer
        view.findViewById<ImageView>(R.id.profile_image).setOnClickListener {
            val builder = AlertDialog.Builder(context,R.style.MyAlertDialogStyle)
            builder.setTitle("Change Profile Picture")
            builder.setMessage("Are You Sure You Want to Change Your Profile Picture ?")
            builder.setPositiveButton("YES"){ dialog, which ->
                isCover = false
                changeImage()
            }
            builder.setNeutralButton("Cancel"){ dialogInterface: DialogInterface, i: Int -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        // Profile picture changer
        view.findViewById<ImageView>(R.id.cover_image).setOnClickListener {
            val builder = AlertDialog.Builder(context,R.style.MyAlertDialogStyle)
            builder.setTitle("Change Cover Picture")
            builder.setMessage("Are You Sure You Want to Change Your Cover Picture ?")
            builder.setPositiveButton("YES"){ dialog, which ->
                isCover = true
                changeImage()
            }
            builder.setNeutralButton("Cancel"){ dialogInterface: DialogInterface, i: Int -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        // Facebook links
        view.findViewById<ImageView>(R.id.set_fb).setOnClickListener {
            val builder = AlertDialog.Builder(context,R.style.MyAlertDialogStyle)
            builder.setTitle("Change Facebook link")
            builder.setMessage("Are You Sure You Want to Change Your Facebook link?")
            builder.setPositiveButton("YES"){ dialog, which ->
                socialLink = "fb"
                setSocialLinks()
            }
            builder.setNeutralButton("Cancel"){ dialogInterface: DialogInterface, i: Int -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        // Instagram links
        view.findViewById<ImageView>(R.id.set_insta).setOnClickListener {
            val builder = AlertDialog.Builder(context,R.style.MyAlertDialogStyle)
            builder.setTitle("Change Instagram Link")
            builder.setMessage("Are You Sure You Want to Change Your instagram link?")
            builder.setPositiveButton("YES"){ dialog, which ->
                socialLink = "insta"
                setSocialLinks()

            }
            builder.setNeutralButton("Cancel"){ dialogInterface: DialogInterface, i: Int -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        // Website links
        view.findViewById<ImageView>(R.id.set_web).setOnClickListener {
            val builder = AlertDialog.Builder(context,R.style.MyAlertDialogStyle)
            builder.setTitle("Change Website Link")
            builder.setMessage("Are You Sure You Want to Change Your Website link?")
            builder.setPositiveButton("YES"){ dialog, which ->
                socialLink = "web"
                setSocialLinks()

            }
            builder.setNeutralButton("Cancel"){ dialogInterface: DialogInterface, i: Int -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        return view
    }

    private fun setSocialLinks() {
        val builder = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)

        when (socialLink) {
            "fb" -> {
                builder.setTitle("Facebook User Name")
            }
            "insta" -> {
                builder.setTitle("Instagram User Name")
            }
            "web" -> {
                builder.setTitle("Website URL")
            }
        }

        val inputField = EditText(context)

        when (socialLink) {
            "fb" -> {
                inputField.hint = "   Username of Facebook"
            }
            "insta" -> {
                inputField.hint = "   Username of Instagram"
            }
            "web" -> {
                inputField.hint = "   e.g, www.google.com"
            }
        }
        builder.setView(inputField)
        builder.setPositiveButton("Save Changes"){ dialog, which ->

            val input:String = inputField.text.toString()
            if(input == "")
            {
                Toast.makeText(context, "Link Can't be Empty", Toast.LENGTH_SHORT).show()
            }
            else
            {
                saveSocialInDatabase(input)
            }

        }
        builder.setNeutralButton("Cancel"){ dialogInterface: DialogInterface, i: Int -> }
        val dialog: AlertDialog = builder.create()
        dialog.setOnShowListener(OnShowListener
        {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(1)
        })

        dialog.show()


    }

    private fun saveSocialInDatabase(input: String) {
        val Links = HashMap<String, Any>()
        if(input!="")
        {
            when(socialLink) {
                "fb" -> {
                    Links["facebook"] = "https://www.facebook.com/$input"
                }
                "insta" -> {
                    Links["instagram"] = "https://www.instagram.com/$input"
                }
                "web" -> {
                    Links["website"] = "https://$input"
                }
            }
        }
        userRef!!.updateChildren(Links).addOnCompleteListener { task->
            if(task.isSuccessful)
            {
                Toast.makeText(context, "Links Updated Successfully", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(context, "UnSuccessfull ! Try Again Later", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun changeImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, ReqCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == ReqCode &&
                resultCode == Activity.RESULT_OK && data!!.data!=null)
        {
            imageAddress = data.data
            Toast.makeText(context, "Uploading Image", Toast.LENGTH_SHORT).show()
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
            val fileRef = storageRef?.child(System.currentTimeMillis().toString() + ".jpg")
            val uploadTask:StorageTask<*>
            if (fileRef != null)
            {
                uploadTask = fileRef.putFile(imageAddress!!)
                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
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
                            val mapCover = HashMap<String, Any>()
                            mapCover["cover"] = Url
                            userRef!!.updateChildren(mapCover)
                            isCover = false
                        }
                        else
                        {
                            val mapProfile = HashMap<String, Any>()
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

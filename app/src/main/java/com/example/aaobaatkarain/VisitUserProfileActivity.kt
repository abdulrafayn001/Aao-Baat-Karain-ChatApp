package com.example.aaobaatkarain

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aaobaatkarain.ModelClasses.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class VisitUserProfileActivity : AppCompatActivity() {
    private var visitId:String=""
    private lateinit var name:TextView
    private lateinit var profile:CircleImageView
    private lateinit var cover:ImageView
    private lateinit var fb:ImageView
    private lateinit var insta:ImageView
    private lateinit var web:ImageView
    private var user: Users? =null
    private lateinit var send_message_btn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_user_profile)

        fb=findViewById(R.id.fb_visit)
        insta=findViewById(R.id.insta_visit)
        web=findViewById(R.id.web_visit)
        send_message_btn=findViewById(R.id.send_msg_btn)

        visitId= intent.getStringExtra("visit_id").toString()
        val ref=FirebaseDatabase.getInstance().reference.child("Users").child(visitId)
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    user=snapshot.getValue(Users::class.java)
                    name=findViewById(R.id.usernameProf_visit)
                    profile=findViewById(R.id.profile_image_visit)
                    cover=findViewById(R.id.cover_image_visit)

                    name.text=user!!.getUsername()
                    Picasso.get().load(user!!.getProfile()).into(profile)
                    Picasso.get().load(user!!.getCover()).into(cover)


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        fb.setOnClickListener {
            val uri= Uri.parse(user!!.getFacebook())
            val intent= Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }
        insta.setOnClickListener {
            val uri= Uri.parse(user!!.getInstagram())
            val intent= Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }
        web.setOnClickListener {
            val uri= Uri.parse(user!!.getWebsite())
            val intent= Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }

        send_message_btn.setOnClickListener{
            val intent=Intent(this@VisitUserProfileActivity,ChatActivity::class.java)
            intent.putExtra("visit_id",user!!.getUid())
            startActivity(intent)
        }
    }
}
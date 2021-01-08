package com.example.aaobaatkarain

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aaobaatkarain.AdapterClasses.ChatAdapter
import com.example.aaobaatkarain.ModelClasses.Chat
import com.example.aaobaatkarain.ModelClasses.Users
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso

class ChatActivity : AppCompatActivity() {
    lateinit var sendButton: ImageView
    lateinit var message:EditText
    lateinit var userVisitId:String
    lateinit var firebaseUser: FirebaseUser
    lateinit var userName:TextView
    lateinit var profileImage:ImageView
    lateinit var sendImage:ImageView
    lateinit var recycler_view_chat:RecyclerView
    var chatList:List<Chat>?=null
    var chatAdapter:ChatAdapter?=null
    var seenListner:ValueEventListener? = null
    var ref:DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        sendButton=findViewById(R.id.send_msg_btn)
        message=findViewById(R.id.text_msg)
        userName=findViewById(R.id.username_chat)
        profileImage=findViewById(R.id.profile_img_chat)
        sendImage=findViewById(R.id.attach_image_file)

        intent=intent
        userVisitId= intent.getStringExtra("visit_id")!!
        firebaseUser= FirebaseAuth.getInstance().currentUser!!

        recycler_view_chat=findViewById(R.id.chat_list_view)
        recycler_view_chat.setHasFixedSize(true)
        val linearLayoutManager=LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd=true
        recycler_view_chat.layoutManager=linearLayoutManager



        ref=FirebaseDatabase.getInstance()
                .reference
                .child("Users")
                .child(userVisitId)

        ref!!.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user:Users?=snapshot.getValue(Users::class.java)
                userName.text=user!!.getUsername()
                Picasso.get().load(user.getProfile()).into(profileImage)
                retrieveMessages(firebaseUser.uid,userVisitId,user.getProfile())
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        sendButton.setOnClickListener{
            if(message.text.toString()=="")
            {
                Toast.makeText(this, "Message Field is Empty", Toast.LENGTH_SHORT).show()
            }
            else
            {
                sendMessageToUser(firebaseUser.uid,userVisitId,message)
            }
            message.setText("")
        }

        sendImage.setOnClickListener {
            val intent= Intent()
            intent.action=Intent.ACTION_GET_CONTENT
            intent.type="image/*"
            startActivityForResult(Intent.createChooser(intent,"Pick Image"),438)
        }
        seenMsg(userVisitId)

    }

    private fun retrieveMessages(senderId: String, receiverId: String, receiverImageUrl: String) {
        chatList=ArrayList()
        val ref = FirebaseDatabase.getInstance().reference.child("Chats")

        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (chatList as ArrayList<Chat>).clear()
                for( item in snapshot.children)
                {
                    val chat =item.getValue(Chat::class.java)
                    //This condition is to make sure that the messages only belongs to sender and receiver
                    if(chat!!.getReceiver() == senderId && chat.getSender() == receiverId || chat.getReceiver() == receiverId && chat.getSender().equals(senderId))
                    {
                        (chatList as ArrayList<Chat>).add(chat)
                    }
                    chatAdapter= ChatAdapter(this@ChatActivity,(chatList as ArrayList<Chat>),receiverImageUrl)
                    recycler_view_chat.adapter=chatAdapter
                }



            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun sendMessageToUser(senderId: String, receiverId: String, message: EditText?) {
        val ref=FirebaseDatabase.getInstance().reference
        val messageKey=ref.push().key

        val messageHash=HashMap<String,Any?>()
        messageHash["sender"]=senderId
        if (message != null) {
            messageHash["message"]=message.text.toString()
        }
        messageHash["receiver"]=receiverId
        messageHash["isSeen"]=false
        messageHash["url"]=""
        messageHash["messageId"]=messageKey
        ref.child("Chats")
                .child(messageKey!!)
                .setValue(messageHash)
                .addOnCompleteListener {
            task ->
            if(task.isSuccessful)
            {
                val chatsListRef=FirebaseDatabase
                        .getInstance().reference
                        .child("ChatLists")
                        .child(firebaseUser.uid)
                        .child(userVisitId)

                chatsListRef.addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(!snapshot.exists())
                        {
                            chatsListRef.child("id").setValue(userVisitId)
                        }
                        val chatsListReceiverRef=FirebaseDatabase
                                .getInstance().reference
                                .child("ChatLists")
                                .child(userVisitId)
                                .child(firebaseUser.uid)
                        chatsListReceiverRef.child("id").setValue(firebaseUser.uid)
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 438 && resultCode == RESULT_OK && data!=null && data!!.data != null) {
            val progress = ProgressDialog(this)
            progress.setTitle("Uploading Image")
            progress.setTitle("Uploading image please wait!")
            progress.show()
            val storageRef = FirebaseStorage.getInstance().reference.child("Chat Images")
            val ref = FirebaseDatabase.getInstance().reference
            val messageId = ref.push().key
            val filePath = storageRef.child("$messageId.jpg")

            val fileRef = storageRef.child(System.currentTimeMillis().toString() + ".jpg")
            val uploadTask: StorageTask<*>
            uploadTask = filePath.putFile(data.data!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation filePath.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()
                    val messageHash = HashMap<String, Any?>()
                    messageHash["sender"] = firebaseUser.uid
                    messageHash["message"] = "image sent"
                    messageHash["receiver"] = userVisitId
                    messageHash["isSeen"] = false
                    messageHash["url"] = url
                    messageHash["messageId"] = messageId

                    ref.child("Chats").child(messageId!!).setValue(messageHash)
                        .addOnCompleteListener{
                            task->
                            if(task.isSuccessful)
                            {
                                progress.dismiss()
                            }
                            else
                            {
                                Toast.makeText(this,"Connectivity issue",Toast.LENGTH_SHORT).show()
                                progress.dismiss()
                            }
                        }
                 }
            }
        }
    }

    private fun seenMsg(userId:String)
    {
        val ref = FirebaseDatabase.getInstance().reference.child("Chats")
        seenListner = ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(dataOnj in snapshot.children)
                {
                    val chat = dataOnj.getValue(Chat::class.java)
                    if(chat!!.getReceiver().equals(firebaseUser.uid) && chat.getSender().equals(userId))
                    {
                        val map = HashMap<String,Any>()
                        map["isSeen"] = true
                        dataOnj.ref.updateChildren(map)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onPause()
    {
        super.onPause()
        updateStatus("offline")
        ref!!.removeEventListener(seenListner!!)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun updateStatus(status:String)
    {
        val ref=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        val hashMap=HashMap<String,Any>()
        hashMap["status"]=status
        ref!!.updateChildren(hashMap)
    }

    override fun onResume() {
        super.onResume()
        updateStatus("online")
    }


}
package com.example.aaobaatkarain.AdapterClasses

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aaobaatkarain.ChatActivity
import com.example.aaobaatkarain.ModelClasses.Chat
import com.example.aaobaatkarain.ModelClasses.Users
import com.example.aaobaatkarain.R
import com.example.aaobaatkarain.VisitUserProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter (context: Context,users:List<Users>,isChatChecked:Boolean): RecyclerView.Adapter<UserAdapter.ViewHolder?>() {
    private  val context:Context
    private  val users:List<Users>
    private  var isChatChecked:Boolean
    private var lastMessage:String=""

    init {
        this.context=context
        this.isChatChecked=isChatChecked
        this.users=users
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        lateinit var userNameText:TextView
        lateinit var profileImageView:CircleImageView
        lateinit var onlineText:ImageView
        lateinit var offlineText:ImageView
        lateinit var lastMessageText:TextView

        init {
            userNameText=view.findViewById(R.id.username)
            profileImageView=view.findViewById(R.id.profile_image)
            onlineText=view.findViewById(R.id.online_status)
            offlineText=view.findViewById(R.id.offline_status)
            lastMessageText=view.findViewById(R.id.last_message)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View=LayoutInflater.from(context).inflate(R.layout.search_item_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = users[position]
        holder.userNameText.text= item.getUsername()
        Picasso.get().load(item.getProfile()).placeholder(R.drawable.ic_profile).into(holder.profileImageView)

        if(isChatChecked)
        {
            retrieveLastMessage(item.getUid(),holder.lastMessageText)
        }
        else
        {
            holder.lastMessageText.visibility=View.GONE
        }

        if(isChatChecked)
        {
            if(item.getStatus()=="online")
            {
                holder.onlineText.visibility=View.VISIBLE
                holder.offlineText.visibility=View.GONE
            }
            else
            {
                holder.onlineText.visibility=View.GONE
                holder.offlineText.visibility=View.VISIBLE
            }
        }
        else
        {
            holder.onlineText.visibility=View.GONE
            holder.offlineText.visibility=View.GONE
        }
        holder.itemView.setOnClickListener {
            val options= arrayOf<String>("Send Message","Visit Profile")
            val builder: androidx.appcompat.app.AlertDialog.Builder =androidx.appcompat.app.AlertDialog.Builder(context)
            builder.setItems(options,DialogInterface.OnClickListener {
                dialog, which ->
                if(which==0)
                {
                    val intent = Intent(context,ChatActivity::class.java)
                    intent.putExtra("visit_id",item.getUid())
                    context.startActivity(intent)
                }
                if(which==1)
                {
                    val intent = Intent(context, VisitUserProfileActivity::class.java)
                    intent.putExtra("visit_id",item.getUid())
                    context.startActivity(intent)
                }
            })
            builder.show()
        }
    }

    private fun retrieveLastMessage(UserId: String, lastMessageText: TextView) {
        lastMessage="no message"
        val firebaseUser=FirebaseAuth.getInstance().currentUser
        val ref=FirebaseDatabase.getInstance().reference.child("Chats")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children)
                {
                    val chat: Chat? =data.getValue(Chat::class.java)

                    if(firebaseUser!=null && chat!=null)
                    {
                        if(chat.getReceiver()==firebaseUser!!.uid && chat.getSender()==UserId || chat.getReceiver()==UserId && chat.getSender()==firebaseUser!!.uid)
                        {
                            lastMessage=chat.getMessage()
                        }
                    }
                }

                when(lastMessage)
                {
                    "no message"->lastMessageText.text="No Message"
                    "sent you an image"-> lastMessageText.text="image"
                    else -> lastMessageText.text=lastMessage
                }
                lastMessage="no message"
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun getItemCount(): Int {
        return users.size
    }
}
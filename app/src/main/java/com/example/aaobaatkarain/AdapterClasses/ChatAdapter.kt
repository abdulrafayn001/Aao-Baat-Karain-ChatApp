package com.example.aaobaatkarain.AdapterClasses

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.aaobaatkarain.ModelClasses.Chat
import com.example.aaobaatkarain.R
import com.example.aaobaatkarain.ViewFullImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class ChatAdapter (var mContext: Context, var mChatList:List<Chat>, var imageURL:String):RecyclerView.Adapter<ChatAdapter.ViewHolder>(){
    private val firebaseUser:FirebaseUser?=FirebaseAuth.getInstance().currentUser!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(viewType==1)
        {
            val view:View=LayoutInflater.from(mContext).inflate(com.example.aaobaatkarain.R.layout.message_item_right,parent,false)
            ViewHolder(view)
        }
        else
        {
            val view:View=LayoutInflater.from(mContext).inflate(com.example.aaobaatkarain.R.layout.message_item_left,parent,false)
            ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat:Chat=mChatList[position]

        Picasso.get().load(imageURL).into(holder.profile_image)

        //Images Messages

        if(chat.getMessage() == "image sent" && chat.getURL() != "")
        {
            //Image Message at Right Side
            if(chat.getSender() == firebaseUser!!.uid)
            {
                holder.show_text_Message!!.visibility=View.GONE
                holder.right_image_view!!.visibility=View.VISIBLE
                Picasso.get().load(chat.getURL()).into(holder.right_image_view)

                holder.right_image_view!!.setOnClickListener {
                    val options = arrayOf(
                            "View Full Image",
                            "Delete Image",
                            "Cancel"
                    )
                    var builder:AlertDialog.Builder = AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("Choose ?")
                    builder.setItems(options,DialogInterface.OnClickListener{
                        dialog, which ->
                        if(which == 0)
                        {
                            val intent = Intent(mContext,ViewFullImage::class.java)
                            intent.putExtra("url",chat.getURL())
                            mContext.startActivity(intent)
                        }
                        else if(which == 1)
                        {
                            deleteMessage(position,holder)
                        }

                    })
                    builder.show()
                }
            }
            else //Image Message at Left Side
                if(chat.getSender() != firebaseUser.uid)
                {
                    holder.show_text_Message!!.visibility=View.GONE
                    holder.left_image_view!!.visibility=View.VISIBLE
                    Picasso.get().load(chat.getURL()).into(holder.left_image_view)

                    holder.left_image_view!!.setOnClickListener {
                        val options = arrayOf(
                                "View Full Image",
                                "Cancel"
                        )
                        var builder: AlertDialog.Builder = AlertDialog.Builder(holder.itemView.context)
                        builder.setTitle("Choose ?")
                        builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                            if (which == 0) {
                                val intent = Intent(mContext, ViewFullImage::class.java)
                                intent.putExtra("url", chat.getURL())
                                mContext.startActivity(intent)
                            }

                        })
                        builder.show()
                    }
                }
        }
        else//Text Messages
        {
            if (firebaseUser!!.uid == chat.getSender()) {
                holder.show_text_Message!!.text=chat.getMessage()
                holder.show_text_Message!!.setOnClickListener {
                    val options = arrayOf(
                            "Delete Message",
                            "Cancel"
                    )
                    val builder: AlertDialog.Builder = AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("Choose ?")
                    builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                        if (which == 0)
                        {
                            deleteMessage(position, holder)
                        }

                    })
                    builder.show()
                }
            }
            else
            {
                holder.show_text_Message!!.text=chat.getMessage()
            }
        }

        //sent and seen message
        if(position==mChatList.size-1) {

            if(chat.getIsSeen())
            {
                holder.text_Seen!!.text="Seen"
                println(chat.getMessage())
                if(chat.getMessage() == "image sent" && chat.getURL() != "")
                {
                    val lp: RelativeLayout.LayoutParams? =holder.text_Seen!!.layoutParams as RelativeLayout.LayoutParams?
                    lp!!.setMargins(0,245,10,0)
                    holder.text_Seen!!.layoutParams=lp
                }
            }
            else
            {
                holder.text_Seen!!.text="Sent"
                if(chat.getMessage() == "sent you an image" && chat.getMessage() != "")
                {
                    val lp: RelativeLayout.LayoutParams? =holder.text_Seen!!.layoutParams as RelativeLayout.LayoutParams
                    lp!!.setMargins(0,245,10,0)
                    holder.text_Seen!!.layoutParams=lp
                }
            }

        }else
        {
            holder.text_Seen!!.visibility=View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mChatList.size
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        var profile_image:CircleImageView?=null
        var show_text_Message:TextView?=null
        var left_image_view:ImageView?=null
        var text_Seen:TextView?=null
        var right_image_view:ImageView?=null

        init {
            profile_image=view.findViewById(R.id.profile_image)
            show_text_Message=view.findViewById(R.id.show_text_message)
            left_image_view=view.findViewById(R.id.left_image_view)
            text_Seen=view.findViewById(R.id.text_seen)
            right_image_view=view.findViewById(R.id.right_image_view)
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if(mChatList[position].getSender() == firebaseUser!!.uid)
        {
            1
        }
        else
        {
            0
        }
    }
    private fun deleteMessage(position:Int,holder:ChatAdapter.ViewHolder)
    {
        val ref = FirebaseDatabase.getInstance().reference.child("Chats")
                .child(mChatList.get(position).getMessageId())
                .removeValue()
    }

}
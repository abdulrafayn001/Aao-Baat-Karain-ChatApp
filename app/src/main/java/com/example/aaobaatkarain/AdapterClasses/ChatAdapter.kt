package com.example.aaobaatkarain.AdapterClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aaobaatkarain.ModelClasses.Chat
import com.example.aaobaatkarain.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter (var mContext: Context, var mChatList:List<Chat>, var imageURL:String):RecyclerView.Adapter<ChatAdapter.ViewHolder>(){
    private val firebaseUser:FirebaseUser?=null
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        var profile_image:CircleImageView?=null
        var show_text_Message:TextView?=null
        var left_image_view:ImageView?=null
        var text_seen:TextView?=null
        var right_image_view:ImageView?=null
        init {
            profile_image=view.findViewById(R.id.profile_img_chat)
            show_text_Message=view.findViewById(R.id.show_text_message)
            left_image_view=view.findViewById(R.id.text_seen)
            right_image_view=view.findViewById(R.id.right_image_view)
        }
    }

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
        if(chat.getMessage().equals("sent you an image") && !chat.getMessage().equals(""))
        {
            if(chat.getSender().equals(firebaseUser!!.uid))
            {
                holder.show_text_Message!!.visibility=View.GONE
                holder.right_image_view!!.visibility=View.VISIBLE
                Picasso.get().load(chat.getURL()).into(holder.right_image_view)
            }
            else
                if(chat.getSender() != firebaseUser.uid)
                {
                    holder.show_text_Message!!.visibility=View.VISIBLE
                    holder.right_image_view!!.visibility=View.GONE
                    Picasso.get().load(chat.getURL()).into(holder.left_image_view)
                }
        }
        else
        {
            holder.show_text_Message!!.text=chat.getMessage()
        }
    }

    override fun getItemCount(): Int {
        return mChatList.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
        firebaseUser = FirebaseAuth.getInstance().currentUser

        return if(mChatList[position].getSender().equals(firebaseUser!!.uid))
        {
            1
        }
        else
        {
            0
        }
    }

}
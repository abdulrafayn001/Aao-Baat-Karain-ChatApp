package com.example.aaobaatkarain.AdapterClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aaobaatkarain.ModelClasses.Users
import com.example.aaobaatkarain.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter (context: Context,users:List<Users>,isChatChecked:Boolean): RecyclerView.Adapter<UserAdapter.ViewHolder?>() {
    private  val context:Context
    private  val users:List<Users>
    private  var isChatChecked:Boolean

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
        return UserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = users[position]
        for(i in this.users)
            println(i)
        holder.userNameText.text= item.getUsername()
        Picasso.get().load(item.getProfile()).placeholder(R.drawable.ic_profile).into(holder.profileImageView)
        
    }

    override fun getItemCount(): Int {
        return users.size
    }
}
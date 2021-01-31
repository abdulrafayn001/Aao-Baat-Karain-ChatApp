package com.example.aaobaatkarain.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aaobaatkarain.AdapterClasses.UserAdapter
import com.example.aaobaatkarain.ModelClasses.Chatlist
import com.example.aaobaatkarain.ModelClasses.Users
import com.example.aaobaatkarain.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragment : Fragment() {

    private var userAdapter:UserAdapter? = null
    private var mUsers: List<Users>? = null
    private var chatUsersList: List<Chatlist>? = null
    lateinit var recycView:RecyclerView
    private var firebaseUser: FirebaseUser? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val viewRef:View = inflater.inflate(R.layout.fragment_chat, container, false)

        recycView = viewRef.findViewById(R.id.chatList)
        recycView.setHasFixedSize(true)
        recycView.layoutManager = LinearLayoutManager(context)

        firebaseUser = FirebaseAuth.getInstance().currentUser

        chatUsersList = ArrayList()

        val refUser = FirebaseDatabase.getInstance().reference.child("ChatLists").child(firebaseUser!!.uid)
        refUser.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                (chatUsersList as ArrayList).clear()

                for (dataOnj in snapshot.children) {

                    val chatLst = dataOnj.getValue(Chatlist::class.java)

                    (chatUsersList as ArrayList).add(chatLst!!)
                }
                reteiveChatList()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return viewRef

    }

    private fun reteiveChatList()
    {
        mUsers = ArrayList()
        val refUser = FirebaseDatabase.getInstance().reference.child("Users")
        refUser.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                (mUsers as ArrayList).clear()

                for (dataPack in snapshot.children)
                {
                    val userRef = dataPack.getValue(Users::class.java)
                    for( chatItem in chatUsersList!!)
                    {
                        if(userRef!!.getUid().equals(chatItem.getId()))
                        {
                            (mUsers as ArrayList).add(userRef)
                        }
                    }
                }
                for(i in mUsers!!)
                {
                    println("<<<<<<<<<<<<<<<<<<<<<< "+i+" <<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
                }
                userAdapter = UserAdapter(context!!,(mUsers as ArrayList<Users>),true)
                recycView.adapter = userAdapter
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}





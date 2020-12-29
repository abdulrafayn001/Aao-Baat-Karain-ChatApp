package com.example.aaobaatkarain.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aaobaatkarain.AdapterClasses.UserAdapter
import com.example.aaobaatkarain.ModelClasses.Users
import com.example.aaobaatkarain.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : Fragment() {
    private var userAdapter:UserAdapter?=null
    private var mUsers:List<Users>?=null
    private var recyclerView: RecyclerView?=null
    private var searchEditText: EditText?=null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view:View=inflater.inflate(R.layout.fragment_search, container, false)
        mUsers=ArrayList()
        retrieveAllUsers()

        recyclerView=view.findViewById(R.id.search_list_view)
//        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager= LinearLayoutManager(context)

        searchEditText=view.findViewById(R.id.searchUserET)
        searchEditText!! .addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchUser(s.toString().toLowerCase(Locale.ROOT))
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        return view
    }

    private fun retrieveAllUsers() {

        val firebaseUserID=FirebaseAuth.getInstance().currentUser!!.uid
        val refUsers= FirebaseDatabase.getInstance().reference.child("Users")

        refUsers.addValueEventListener(object :ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                (mUsers as ArrayList<Users>).clear()

                if(searchEditText!!.text.toString()=="")
                {
                    for(s in snapshot.children)
                    {
                        val usr:Users?=s.getValue(Users::class.java)
                        if(usr!!.getUid() != firebaseUserID)
                        {
                            (mUsers as ArrayList<Users>).add(usr)
                        }
                    }

                    recyclerView!!.adapter = UserAdapter(context!!, mUsers!!,false)

                    // Use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    recyclerView!!.setHasFixedSize(true)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }
    private fun searchUser(find:String){
        val firebaseUserID=FirebaseAuth.getInstance().currentUser!!.uid
        val queryUsers= FirebaseDatabase.getInstance().reference.child("Users").orderByChild("search").startAt(find).endAt(find+"\uf8ff")

        queryUsers.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                for(s in snapshot.children)
                {
                    val usr:Users?=s.getValue(Users::class.java)
                    if(usr!!.getUid() != firebaseUserID)
                    {
                        (mUsers as ArrayList<Users>).add(usr)
                    }
                }

                recyclerView!!.adapter = UserAdapter(context!!, mUsers!!,false)

                // Use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                recyclerView!!.setHasFixedSize(true)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

}
package com.example.aaobaatkarain.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aaobaatkarain.AdapterClasses.UserAdapter
import com.example.aaobaatkarain.ModelClasses.Users
import com.example.aaobaatkarain.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    private var userAdapter:UserAdapter?=null
    private var mUsers:List<Users>?=null

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

        return view
    }

    private fun retrieveAllUsers() {
        val firebaseUserID=FirebaseAuth.getInstance().currentUser!!.uid
        val refUsers= FirebaseDatabase.getInstance().reference.child("Users")

        refUsers.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                for(i in snapshot.children)
                {
                    val usr:Users?=snapshot.getValue(Users::class.java)
                    if(usr!!.getUid() != firebaseUserID)
                    {
                        (mUsers as ArrayList<Users>).add(usr)
                    }
                }
                userAdapter= UserAdapter(context!!,mUsers!!,false)
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
                for(i in snapshot.children)
                {
                    val usr:Users?=snapshot.getValue(Users::class.java)
                    if(usr!!.getUid() != firebaseUserID)
                    {
                        (mUsers as ArrayList<Users>).add(usr)
                    }
                }
                userAdapter= UserAdapter(context!!,mUsers!!,false)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

}
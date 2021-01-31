package com.example.aaobaatkarain

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.aaobaatkarain.Fragments.ChatFragment
import com.example.aaobaatkarain.Fragments.SearchFragment
import com.example.aaobaatkarain.Fragments.SettingsFragment
import com.example.aaobaatkarain.ModelClasses.Chat
import com.example.aaobaatkarain.ModelClasses.Users
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    var refUsers:DatabaseReference?=null
    var firebaseUser:FirebaseUser?=null
    var userAuth:FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userAuth = FirebaseAuth.getInstance()
        firebaseUser=FirebaseAuth.getInstance().currentUser
        refUsers=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        //Remove the name of the app form Toolbar
        val toolbar : Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.title= ""

        val tabLayout:TabLayout=findViewById(R.id.tab_layout)
        val viewPager:ViewPager=findViewById(R.id.view_pager)

        val ref = FirebaseDatabase.getInstance().reference.child("Chats")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                val viewPagerAdapter=ViewPagerAdapter(supportFragmentManager)
                var countUnreadMsg:Int = 0
                for(data in snapshot.children)
                {
                    val chat = data.getValue(Chat::class.java)
                    if(chat!!.getReceiver().equals(firebaseUser!!.uid) && !chat.getIsSeen())
                    {
                        countUnreadMsg+=1
                    }
                }
                if(countUnreadMsg == 0)
                {
                    viewPagerAdapter.addFragment(ChatFragment(),"Chat")
                }
                else
                {
                    viewPagerAdapter.addFragment(ChatFragment(),"Chat ($countUnreadMsg)")
                }
                viewPagerAdapter.addFragment(SearchFragment(),"Search")
                viewPagerAdapter.addFragment(SettingsFragment(),"Profile")

                viewPager.adapter=viewPagerAdapter
                tabLayout.setupWithViewPager(viewPager)
            }
        })
        startActivity(Intent(this,SplahScreen::class.java))

        //Displaying username and profile picture in main activity
        refUsers!!.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val user: Users?=snapshot.getValue(Users::class.java)
                    findViewById<TextView>(R.id.user_name).text= user!!.getUsername()
                    Picasso.get().load(user.getProfile()).placeholder(R.drawable.ic_profile).into(findViewById<ImageView>(R.id.profile_img))
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId)
        {
            R.id.logout ->
            {
                userAuth?.signOut()
                val data: SharedPreferences = getSharedPreferences("LOG", MODE_PRIVATE)
                val gedit = data.edit()
                gedit.putBoolean("LogSucess",false)
                gedit.apply()
                gedit.commit()
//                var intent=Intent(this@MainActivity, LoginActivity::class.java)
//                startActivity(intent)
//                finish()
                exitProcess(-1)
                return true
            }
        }
        return false
    }

    internal class ViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager)
    {
        private val fragments : ArrayList<Fragment> = ArrayList<Fragment>()
        private val titles : ArrayList<String> = ArrayList<String>()

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        fun addFragment(fragment: Fragment, title: String)
        {
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
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

    override fun onPause() {
        super.onPause()
        updateStatus("offline")
    }


}
package com.example.aaobaatkarain

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.aaobaatkarain.Fragments.ChatFragment
import com.example.aaobaatkarain.Fragments.SearchFragment
import com.example.aaobaatkarain.Fragments.SettingsFragment
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar_main))


        //Remove the name of the app form Toolbar
        val toolbar : Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.title= ""

        val tabLayout:TabLayout=findViewById(R.id.tab_layout)
        val viewPager:ViewPager=findViewById(R.id.view_pager)
        val viewPagerAdapter=ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(ChatFragment(),"Chat")
        viewPagerAdapter.addFragment(SearchFragment(),"Search")
        viewPagerAdapter.addFragment(SettingsFragment(),"Settings")

        viewPager.adapter=viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
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
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    internal class ViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
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
}
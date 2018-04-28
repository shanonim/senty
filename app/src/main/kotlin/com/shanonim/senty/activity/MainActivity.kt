package com.shanonim.senty.activity

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.shanonim.senty.fragment.MyPageFragment
import com.shanonim.senty.R
import com.shanonim.senty.databinding.ActivityMainBinding
import com.shanonim.senty.fragment.MapFragment
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.navigation?.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        binding?.navigation?.findViewById(R.id.navigation_home)?.performClick()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager.beginTransaction().replace(R.id.content, MapFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_my_page -> {
                val myPageFragment = MyPageFragment()
                supportFragmentManager.beginTransaction().replace(R.id.content, myPageFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun <T : Activity> Activity.startActivity(classRef: KClass<T>, bundle: Bundle? = null) {
        val intent = Intent(this, classRef.java).setAction(Intent.ACTION_VIEW)
        bundle?.let {
            intent.putExtra("args", bundle)
        }
        startActivity(intent)
    }
}

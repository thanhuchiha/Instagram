package com.thanhuhiha.instagram.activities

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.thanhuhiha.instagram.*

abstract class BaseActivity(val navNumber : Int) : AppCompatActivity() {
    private val TAG = "BaseActivity"
    fun setupBottomNavigation() {
        val bnv: BottomNavigationViewEx = findViewById(R.id.bottom_navigation_view)
        bnv.setIconSize(29f, 29f)
        bnv.setTextVisibility(false)
        bnv.enableAnimation(false)
        for (i in 0 until bnv.menu.size()) {
            bnv.setIconTintList(i, null)
        }

        bnv.setOnNavigationItemSelectedListener {
            val nextActivity =
                when (it.itemId) {
                    R.id.nav_icon_home -> HomeActivity::class.java
                    R.id.nav_icon_search -> SearchActivity::class.java
                    R.id.nav_icon_share -> ShareActivity::class.java
                    R.id.nav_icon_like -> LikeActivity::class.java
                    R.id.nav_icon_profile -> ProfileActivity::class.java
                    else -> {
                        Log.d(TAG, "UNKNOWN NAV ITEM CLICK $it")
                        null
                    }
                }
            if (nextActivity != null) {
                val intent = Intent(this, nextActivity)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
                overridePendingTransition(0,0)
                true
            } else {
                false
            }
        }
        bnv.menu.getItem(navNumber).isChecked = true
    }
}
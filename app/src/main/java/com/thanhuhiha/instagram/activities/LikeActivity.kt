package com.thanhuhiha.instagram.activities

import android.os.Bundle
import android.util.Log
import com.thanhuhiha.instagram.R

class LikeActivity : BaseActivity(3) {
    private val TAG = "LikeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupBottomNavigation()
        Log.d(TAG,"OnCreate")
    }
}

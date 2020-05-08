package com.thanhuhiha.instagram.ui

import android.os.Bundle
import android.util.Log
import com.thanhuhiha.instagram.R

class LikeActivity : BaseActivity(3) {
    private val TAG = "LikeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        setupBottomNavigation()
        Log.d(TAG,"OnCreate")
    }
}

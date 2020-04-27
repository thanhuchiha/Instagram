package com.thanhuhiha.instagram.activities

import android.os.Bundle
import android.util.Log
import com.thanhuhiha.instagram.R

class SearchActivity : BaseActivity(1) {
    private val TAG = "SearchActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupBottomNavigation()
        Log.d(TAG,"OnCreate")
    }
}

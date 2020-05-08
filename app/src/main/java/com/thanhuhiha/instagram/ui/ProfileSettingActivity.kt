package com.thanhuhiha.instagram.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thanhuhiha.instagram.R
import kotlinx.android.synthetic.main.activity_profile_setting.*

class ProfileSettingActivity : AppCompatActivity() {
    private lateinit var mFirebase: FirebaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_setting)

        mFirebase = FirebaseHelper(this)
        sign_out_text.setOnClickListener { mFirebase.auth.signOut() }
        back_image.setOnClickListener { finish() }
    }
}

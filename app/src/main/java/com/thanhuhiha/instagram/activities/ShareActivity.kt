package com.thanhuhiha.instagram.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.thanhuhiha.instagram.R
import com.thanhuhiha.instagram.utils.CameraPictureTaker
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : BaseActivity(2) {
    private val TAG = "ShareActivity"
    private lateinit var mCameraHelper: CameraPictureTaker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        setupBottomNavigation()
        Log.d(TAG,"OnCreate")

        mCameraHelper = CameraPictureTaker(this)
        mCameraHelper.takeCameraPicture()

        back_image.setOnClickListener{finish()}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == mCameraHelper.REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Glide.with(this).load(mCameraHelper.imageUri).centerCrop().into(post_image)
        }
    }
}

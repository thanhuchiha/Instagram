package com.thanhuhiha.instagram.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thanhuhiha.instagram.R
import com.thanhuhiha.instagram.ui.editprofile.EditProfileActivity
import com.thanhuhiha.instagram.ui.addfriends.AddFriendActivity
import com.thanhuhiha.instagram.ui.common.*
import com.thanhuhiha.instagram.ui.loadUserPhoto
import com.thanhuhiha.instagram.ui.profilesetting.ProfileSettingActivity
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : BaseActivity() {
    private lateinit var imagesAdapter: ImagesAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        Log.d(TAG, "onCreate")

        edit_profile_btn.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
        setting_image.setOnClickListener {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
        }
        add_friend_image.setOnClickListener {
            val intent = Intent(this, AddFriendActivity::class.java)
            startActivity(intent)
        }
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView = findViewById(R.id.image_recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

//        image_recycler.layoutManager = GridLayoutManager(this, 3)
//        mAdapter = ImagesAdapter()
//        image_recycler.adapter = mAdapter

        setupAuthGuard { uid ->
            setupBottomNavigation(uid, 4)
            val viewModel = initViewModel<ProfileViewModel>()
            viewModel.init(uid)
            viewModel.user.observe(this, Observer {
                it?.let {
                    Log.d(TAG, "Photo here : ${it}")
                    if (it.photo != null) {
                        profile_image.loadUserPhoto(it.photo)
                    }
                    if (it.followers.isNotEmpty()) {
                        followers_count_text.text = it.followers.size.toString()
                    }
                    if (it.follows.isNotEmpty()) {
                        following_count_text.text = it.follows.size.toString()
                    }
                    username_text.text = it.username
                }
            })
            viewModel.images.observe(this, Observer {
                it?.let { images ->
                    Log.d(TAG, "Image Here: $images")
                    //mAdapter.updateImages(images)
                    if (images.isNotEmpty()) {
                        imagesAdapter = ImagesAdapter()
                        imagesAdapter.images = images + images + images + images
                        recyclerView.adapter = imagesAdapter
                        posts_count_text.text = images.size.toString()
                    }
                }
            })
        }
    }

    companion object {
        const val TAG = "ProfileActivity"
    }
}


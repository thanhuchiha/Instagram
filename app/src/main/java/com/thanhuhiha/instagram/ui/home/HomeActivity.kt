package com.thanhuhiha.instagram.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.thanhuhiha.instagram.R
import com.thanhuhiha.instagram.data.firebase.common.auth
import com.thanhuhiha.instagram.ui.comment.CommentsActivity
import com.thanhuhiha.instagram.ui.common.BaseActivity
import com.thanhuhiha.instagram.ui.common.setupAuthGuard
import com.thanhuhiha.instagram.ui.common.setupBottomNavigation
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity(), FeedAdapter.Listener {
    private lateinit var mAdapter: FeedAdapter
    private lateinit var mViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAdapter = FeedAdapter(this)
        feed_recycler.adapter = mAdapter
        feed_recycler.layoutManager = LinearLayoutManager(this)

        setupAuthGuard { uid ->
            setupBottomNavigation(uid, 0)
            mViewModel = initViewModel()
            mViewModel.init(uid)
            mViewModel.feedPosts.observe(this, Observer {
                it?.let {
                    mAdapter.updatePosts(it)
                }
            })
            mViewModel.goToCommentsScreen.observe(this, Observer {
                it?.let { postId ->
                    CommentsActivity.start(this, postId)
                }
            })
        }
    }

    override fun toggleLike(postId: String) {
        mViewModel.toggleLike(postId)
    }

    override fun loadLikes(postId: String, position: Int) {
        if (mViewModel.getLikes(postId) == null) {
            mViewModel.loadLikes(postId).observe(this, Observer {
                it?.let { postLikes ->
                    mAdapter.updatePostLikes(position, postLikes)
                }
            })
        }
    }

    override fun openComments(postId: String) {
        mViewModel.openComments(postId)
    }

    override fun deleteFeedPost(postId: String, uid: String) {
        mViewModel.deleteFeedPost(postId, uid)
    }

    companion object {
        const val TAG = "HomeActivity"
    }
}


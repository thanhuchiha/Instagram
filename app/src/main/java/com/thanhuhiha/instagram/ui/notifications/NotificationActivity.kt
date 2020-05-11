package com.thanhuhiha.instagram.ui.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.thanhuhiha.instagram.R
import com.thanhuhiha.instagram.ui.common.BaseActivity
import com.thanhuhiha.instagram.ui.common.setupAuthGuard
import com.thanhuhiha.instagram.ui.common.setupBottomNavigation
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationsActivity : BaseActivity() {
    private lateinit var mAdapter: NotificationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        Log.d(TAG, "onCreate")

        setupAuthGuard { uid ->
            setupBottomNavigation(uid,3)
            mAdapter = NotificationsAdapter()
            notifications_recycler.layoutManager = LinearLayoutManager(this)
            notifications_recycler.adapter = mAdapter

            val viewModel = initViewModel<NotificationsViewModel>()
            viewModel.init(uid)
            viewModel.notifications.observe(this, Observer {
                it?.let {
                    mAdapter.updateNotifications(it)
                    viewModel.setNotificationsRead(it)
                }
            })
        }
    }

    companion object {
        const val TAG = "LikesActivity"
    }
}

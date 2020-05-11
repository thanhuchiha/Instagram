package com.thanhuhiha.instagram.ui

import android.app.Application
import com.thanhuhiha.instagram.data.FirebaseSearchRepository
import com.thanhuhiha.instagram.data.common.firebase.FirebaseAuthManager
import com.thanhuhiha.instagram.data.firebase.FirebaseFeedPostsRepository
import com.thanhuhiha.instagram.data.firebase.FirebaseNotificationsRepository
import com.thanhuhiha.instagram.data.firebase.FirebaseUsersRepository
import com.thanhuhiha.instagram.ui.notifications.NotificationsCreator
import com.thanhuhiha.instagram.ui.search.SearchPostsCreator

class InstagramApp : Application() {
    val usersRepo by lazy { FirebaseUsersRepository() }
    val feedPostsRepo by lazy { FirebaseFeedPostsRepository() }
    val notificationsRepo by lazy { FirebaseNotificationsRepository() }
    val authManager by lazy { FirebaseAuthManager() }
    val searchRepo by lazy { FirebaseSearchRepository() }


    override fun onCreate() {
        super.onCreate()
        NotificationsCreator(notificationsRepo, usersRepo, feedPostsRepo)
        SearchPostsCreator(searchRepo)
    }
}

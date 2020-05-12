package com.thanhuhiha.instagram.ui.home

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.thanhuhiha.instagram.data.FeedPostsRepository
import com.thanhuhiha.instagram.data.common.SingleLiveEvent
import com.thanhuhiha.instagram.data.common.map
import com.thanhuhiha.instagram.models.FeedPost
import com.thanhuhiha.instagram.ui.common.BaseViewModel

class HomeViewModel(onFailureListener: OnFailureListener,
                    private val feedPostsRepo: FeedPostsRepository
) : BaseViewModel(onFailureListener) {
    lateinit var uid: String
    lateinit var feedPosts: LiveData<List<FeedPost>>
    private var loadedLikes = mapOf<String, LiveData<FeedPostLikes>>()
    private val _goToCommentsScreen = SingleLiveEvent<String>()
    val goToCommentsScreen = _goToCommentsScreen

    fun init(uid: String) {
        if (!this::uid.isInitialized) {
            this.uid = uid
            feedPosts = feedPostsRepo.getFeedPosts(uid).map {
                it.sortedByDescending { it.timeStampDate()}
            }
        }
    }

    fun toggleLike(postId: String) {
        feedPostsRepo.toggleLike(postId, uid).addOnFailureListener(onFailureListener)
    }

    fun getLikes(postId: String): LiveData<FeedPostLikes>? = loadedLikes[postId]

    fun loadLikes(postId: String): LiveData<FeedPostLikes> {
        val existingLoadedLikes = loadedLikes[postId]
        if (existingLoadedLikes == null) {
            val liveData = feedPostsRepo.getLikes(postId).map { likes ->
                FeedPostLikes(
                    likesCount = likes.size,
                    likedByUser = likes.find { it.userId == uid } != null)
            }
            loadedLikes += postId to liveData
            return liveData
        } else {
            return existingLoadedLikes
        }
    }

    fun openComments(postId: String) {
        _goToCommentsScreen.value = postId
    }

    fun deleteFeedPost(postId: String, currentUid:String){
        feedPostsRepo.deleteFeedPost(postId, currentUid)
    }
}
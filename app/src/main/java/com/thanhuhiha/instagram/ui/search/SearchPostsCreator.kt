package com.thanhuhiha.instagram.ui.search

import android.util.Log
import androidx.lifecycle.Observer
import com.thanhuhiha.instagram.data.SearchRepository
import com.thanhuhiha.instagram.data.common.BaseEventListener
import com.thanhuhiha.instagram.data.common.Event
import com.thanhuhiha.instagram.data.common.EventBus
import com.thanhuhiha.instagram.models.SearchPost

class SearchPostsCreator(searchRepo: SearchRepository) : BaseEventListener() {
    init {
        EventBus.events.observe(this, Observer {
            it?.let { event ->
                when (event) {
                    is Event.CreateFeedPost -> {
                        val searchPost = with(event.post) {
                            SearchPost(
                                image = image,
                                caption = caption,
                                postId = id)
                        }
                        searchRepo.createPost(searchPost).addOnFailureListener {
                            Log.d(TAG, "Failed to create search post for event: $event", it)
                        }
                    }
                    else -> {
                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "SearchPostsCreator"
    }
}
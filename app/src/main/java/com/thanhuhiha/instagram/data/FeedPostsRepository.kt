package com.thanhuhiha.instagram.data

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.thanhuhiha.instagram.models.Comment
import com.thanhuhiha.instagram.models.FeedPost

interface FeedPostsRepository {
    fun copyFeedPosts(postAuthorUid: String, uid: String): Task<Unit>
    fun deleteFeedPosts(postAuthorUid: String, uid: String): Task<Unit>
    fun deleteFeedPost(postId: String, uid: String): Task<Unit>
    fun getFeedPost(uid: String, postId: String): LiveData<FeedPost>
    fun getFeedPosts(uid: String): LiveData<List<FeedPost>>
    fun toggleLike(postId: String, uid: String): Task<Unit>
    fun getLikes(postId: String): LiveData<List<FeedPostLike>>
    fun getComments(postId: String): LiveData<List<Comment>>
    fun createComment(postId: String, comment: Comment): Task<Unit>
    fun createFeedPost(uid: String, feedPost: FeedPost): Task<Unit>
}
data class FeedPostLike(val userId: String)
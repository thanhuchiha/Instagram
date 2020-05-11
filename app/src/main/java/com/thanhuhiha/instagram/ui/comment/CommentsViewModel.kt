package com.thanhuhiha.instagram.ui.comment

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.thanhuhiha.instagram.data.FeedPostsRepository
import com.thanhuhiha.instagram.data.UsersRepository
import com.thanhuhiha.instagram.models.Comment
import com.thanhuhiha.instagram.models.User
import com.thanhuhiha.instagram.ui.common.BaseViewModel

class CommentsViewModel(private val feedPostsRepo: FeedPostsRepository,
                        usersRepo: UsersRepository,
                        onFailureListener: OnFailureListener
) :
    BaseViewModel(onFailureListener) {
    lateinit var comments: LiveData<List<Comment>>
    private lateinit var postId: String
    val user: LiveData<User> = usersRepo.getUser()

    fun init(postId: String) {
        this.postId = postId
        comments = feedPostsRepo.getComments(postId)
    }

    fun createComment(text: String, user: User) {
        val comment = Comment(
            uid = user.uid,
            username = user.username,
            photo = user.photo,
            text = text)
        feedPostsRepo.createComment(postId, comment).addOnFailureListener(onFailureListener)
    }
}
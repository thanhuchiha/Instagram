package com.thanhuhiha.instagram.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.thanhuhiha.instagram.R
import com.thanhuhiha.instagram.models.FeedPost
import com.thanhuhiha.instagram.ui.common.SimpleCallback
import com.thanhuhiha.instagram.ui.common.loadImage
import com.thanhuhiha.instagram.ui.common.loadUserPhoto
import com.thanhuhiha.instagram.ui.common.setCaptionText
import kotlinx.android.synthetic.main.feed_item.view.*

class FeedAdapter(private val listener: Listener) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    interface Listener {
        fun toggleLike(postId: String)
        fun loadLikes(postId: String, position: Int)
        fun openComments(postId: String)
        fun deleteFeedPost(postId: String, uid: String)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    private var posts = listOf<FeedPost>()
    private var postLikes: Map<Int, FeedPostLikes> = emptyMap()
    private val defaultPostLikes = FeedPostLikes(0, false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_item, parent, false)
        return ViewHolder(view)
    }

    fun updatePostLikes(position: Int, likes: FeedPostLikes) {
        postLikes += (position to likes)
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        val likes = postLikes[position] ?: defaultPostLikes
        val myMessagingService: MyMessagingService = MyMessagingService()
        with(holder.view) {
            user_photo_image.loadUserPhoto(post.photo)
            username_text.text = post.username
            post_image.loadImage(post.image)
            if (likes.likesCount == 0) {
                likes_text.visibility = View.GONE
            } else {
                likes_text.visibility = View.VISIBLE
                val likesCountText = holder.view.context.resources.getQuantityString(
                    R.plurals.likes_count, likes.likesCount, likes.likesCount
                )
                likes_text.text = likesCountText
            }
            caption_text.setCaptionText(post.username, post.caption)
            like_image.setOnClickListener {
                if (!likes.likedByUser)
                    myMessagingService.showNotification(
                        "Instagram",
                        "${username_text.text.toString()} liked feed for you!",
                        this.context
                    )
                listener.toggleLike(post.id)

            }
            like_image.setImageResource(
                if (likes.likedByUser) {
                    R.drawable.ic_likes_actived_foreground
                } else R.drawable.ic_likes_border
            )
            comment_image.setOnClickListener { listener.openComments(post.id) }
            more_image.setOnClickListener {
                Log.d("ID", "ID:${post}")
                val postId = post.id
                val uid = post.uid
                //deleteFeedPost(postId, uid)
                listener.deleteFeedPost(postId, uid)
            }
            listener.loadLikes(post.id, position)
        }
    }

    override fun getItemCount() = posts.size

    fun updatePosts(newPosts: List<FeedPost>) {
        val diffResult = DiffUtil.calculateDiff(SimpleCallback(this.posts, newPosts) { it.id })
        this.posts = newPosts
        diffResult.dispatchUpdatesTo(this)
    }
}
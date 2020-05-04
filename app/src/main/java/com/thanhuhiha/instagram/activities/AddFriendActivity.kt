package com.thanhuhiha.instagram.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.thanhuhiha.instagram.R
import com.thanhuhiha.instagram.models.User
import com.thanhuhiha.instagram.utils.TaskSourceOnCompleteListener
import com.thanhuhiha.instagram.utils.ValueEventListenerAdapter
import kotlinx.android.synthetic.main.activity_add_friend.*
import kotlinx.android.synthetic.main.add_friends_item.view.*

class AddFriendActivity : AppCompatActivity(), FriendsAdapter.Listener {
    private lateinit var mFirebase: FirebaseHelper
    private lateinit var mUser: User
    private lateinit var mUsers: List<User>
    private lateinit var mAdapter: FriendsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        mFirebase = FirebaseHelper(this)
        mAdapter = FriendsAdapter(this)

        val uid = mFirebase.auth.currentUser!!.uid

        back_image.setOnClickListener{finish()}

        add_friend_recycler.adapter = mAdapter
        add_friend_recycler.layoutManager = LinearLayoutManager(this)

        mFirebase.database.child("users").addValueEventListener(ValueEventListenerAdapter {
            val allUsers = it.children.map { it.asUser()}
            val (userList, otherUserList) = allUsers.partition { it?.uid == uid }
            mUser = userList.first()!!
            mUsers = otherUserList as List<User>


            mAdapter.update(mUsers, mUser.follows)
        })
    }

    override fun follow(uid: String) {
        setFollow(uid, true) {
            mAdapter.followed(uid)
        }
    }

    override fun unfollow(uid: String) {
        setFollow(uid, false) {
            mAdapter.unfollowed(uid)
        }
    }

    private fun setFollow(uid: String, follow: Boolean, onSuccess: () -> Unit) {


        val followsTask = mFirebase.database.child("users").child(mUser.uid!!).child("follows")
            .child(uid).setValueTrueOrRemove(follow)
        val followersTask = mFirebase.database.child("users").child(uid).child("followers")
            .child(mUser.uid!!).setValueTrueOrRemove(follow)



        val taskSource = TaskCompletionSource<Void>()
        val feedPostsTask = task<Void> {taskSource ->
            mFirebase.database.child("feed-posts").child(uid)
                .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                    val postsMap = if (follow) {
                        it.children.map { it.key to it.value }.toMap()
                    } else {
                        it.children.map { it.key to null }.toMap()
                    }
                    mFirebase.database.child("feed-posts").child(mUser.uid!!)
                        .updateChildren(postsMap)
                        .addOnCompleteListener(TaskSourceOnCompleteListener(taskSource))
                })
        }
        Tasks.whenAll(followsTask, followersTask, feedPostsTask).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                showToast(it.exception!!.message!!)
            }
        }
    }
}


class FriendsAdapter(private val listener: Listener) :
    RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface Listener {
        fun follow(uid: String)
        fun unfollow(uid: String)
    }

    private val TAG = "AddFriendActivity"
    private var mUsers = listOf<User>()
    private var mFollows = mapOf<String, Boolean>()
    private var mPositions = mapOf<String, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_friends_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val user = mUsers[position]
            view.photo_image.loadImage(user.photo!!)
            view.username_text.text = user.username
            view.name_text.text = user.name
            view.follow_btn.setOnClickListener { listener.follow(user.uid) }
            view.unfollow_btn.setOnClickListener { listener.unfollow(user.uid) }

            val follows = mFollows[user.uid] ?: false
            if (follows) {
                view.follow_btn.visibility = View.GONE
                view.unfollow_btn.visibility = View.VISIBLE
            } else {
                view.follow_btn.visibility = View.VISIBLE
                view.unfollow_btn.visibility = View.GONE
            }
        }
    }

    override fun getItemCount() = mUsers.size

    fun update(
        users: List<User>,
        follows: Map<String, Boolean>
    ) {
        mUsers = users
        mPositions = users.withIndex().map { (idx, user) -> user.uid to idx }.toMap()
        mFollows = follows
        notifyDataSetChanged()
    }



    fun followed(uid: String) {
        mFollows += (uid to true)
        notifyItemChanged(mPositions[uid]!!)

    }

    fun unfollowed(uid: String) {
        mFollows -= uid
        notifyItemChanged(mPositions[uid]!!)
    }
}
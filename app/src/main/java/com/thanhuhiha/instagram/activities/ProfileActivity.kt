package com.thanhuhiha.instagram.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.thanhuhiha.instagram.R
import com.thanhuhiha.instagram.models.User
import com.thanhuhiha.instagram.utils.ValueEventListenerAdapter
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.profile_image

class ProfileActivity : BaseActivity(4) {
    private val TAG = "ProfileActivity"
    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var mUser: User
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupBottomNavigation()
        Log.d(TAG, "OnCreate")

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

        mFirebaseHelper = FirebaseHelper(this)
        mFirebaseHelper.currentUserReference().addValueEventListener(ValueEventListenerAdapter {
            mUser = it.getValue(User::class.java)!!
            profile_image.loadUserPhoto(mUser.photo)
            username_text.text = mUser.username
        })

        val layoutManager = GridLayoutManager(this, 2);
        recyclerView = findViewById(R.id.image_recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        //imageGalleryAdapter = ImageGalleryAdapter(this, )
        mFirebaseHelper.database.child("images").child(mFirebaseHelper.auth.currentUser!!.uid)
            .addValueEventListener(ValueEventListenerAdapter {
                val images = it.children.map { it.getValue(String::class.java) }
                recyclerView.adapter = ImageGalleryAdapter(
                    this,
                    (images + images + images + images) as List<String>
                )
            })
    }

    fun passDataCom(image: String) {
        val bundle = Bundle()
        bundle.putString("img", image)
        val transaction = this.supportFragmentManager.beginTransaction()
        val fragment = ShowImageFragment()
        fragment.arguments = bundle

        transaction.replace(R.id.profile_layout, fragment)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }

    private inner class ImageGalleryAdapter(
        val context: Context,
        private val images: List<String>
    ) : RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ImageGalleryAdapter.MyViewHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val photoView = inflater.inflate(R.layout.image_item, parent, false)
            return MyViewHolder(photoView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val image = images[position]
            val imageView = holder.photoImageView
            Picasso.get().load(image)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error).fit().into(imageView)
        }

        override fun getItemCount() = images.size

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
            var photoImageView: ImageView = itemView.findViewById(R.id.image_item)

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val image = images[position]
//                    val intent = Intent(context, ShowPhotoActivity::class.java)
//                        .apply {
//                            putExtra(ShowPhotoActivity.EXTRA_PHOTO, image)
//                        }
//                    startActivity(intent)
                    //passDataCom(image)
                }
            }
        }

    }
}

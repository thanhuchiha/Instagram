package com.thanhuhiha.instagram.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.thanhuhiha.instagram.models.Communicator
import com.thanhuhiha.instagram.models.User
import com.thanhuhiha.instagram.utils.ValueEventListenerAdapter
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.profile_image

class ProfileActivity : BaseActivity(4), Communicator {
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


        val layoutManager = GridLayoutManager(this, 2)
        recyclerView = findViewById(R.id.image_recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        mFirebaseHelper.database.child("images").child(mFirebaseHelper.auth.currentUser!!.uid)
            .addValueEventListener(ValueEventListenerAdapter {
                val images = it.children.map { it.getValue(String::class.java)!! }
                Log.d(TAG, "imagesABC: $images")
                recyclerView.adapter = ImageGalleryAdapter(this, images)
            })
    }

    //Pass data to fragment
    override fun passDataCom(image: String) {
        val bundle = Bundle()
        bundle.putString("img", image)

        val transaction = this.supportFragmentManager.beginTransaction()
        val frag = Fragment()
        frag.arguments = bundle

        transaction.replace(R.id.content_id, frag).addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    //PICASSO
    private inner class ImageGalleryAdapter(val context: Context, val images: List<String>) :
        RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>() {

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
            var photoImageView: ImageView = itemView.findViewById(R.id.iv_photo)

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(view: View?) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    Toast.makeText(context, "oke", Toast.LENGTH_SHORT).show()
                    passDataCom(images[position])
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ImageGalleryAdapter.MyViewHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val photoView = inflater.inflate(R.layout.item_image, parent, false)
            return MyViewHolder(photoView)
        }

        override fun getItemCount() = images.size

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val photo = images[position]
            val imageView = holder.photoImageView
            Picasso.get().load(photo)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error).fit()
                .into(imageView)
        }
    }
}


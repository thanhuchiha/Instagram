package com.thanhuhiha.instagram.activities

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.google.firebase.database.ServerValue
import com.thanhuhiha.instagram.R
import com.thanhuhiha.instagram.models.User
import com.thanhuhiha.instagram.utils.CameraPictureTaker
import com.thanhuhiha.instagram.utils.ValueEventListenerAdapter
import kotlinx.android.synthetic.main.activity_share.*
import java.util.*

class ShareActivity : BaseActivity(2) {
    private val TAG = "ShareActivity"
    private lateinit var mCameraHelper: CameraPictureTaker
    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    private lateinit var mUser: User

    private lateinit var mFirebase: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        setupBottomNavigation()
        Log.d(TAG, "OnCreate")

        mFirebase = FirebaseHelper(this)

        //if system os is Marshmallow or Above, we need to request runtime permission
        takeCameraPicture()

        back_image.setOnClickListener { finish() }
        share_text.setOnClickListener { share() }

        mFirebase.currentUserReference().addValueEventListener(ValueEventListenerAdapter{
            mUser = it.getValue(User::class.java)!!
        })
    }

    private fun takeCameraPicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED
            ) {
                //permission was not enabled
                val permission = arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                //show popup to request permission
                requestPermissions(permission, PERMISSION_CODE)
            } else {
                //permission already granted
                openCamera()
            }
        } else {
            //system os is < marshmallow
            openCamera()
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Glide.with(this).load(image_uri).centerCrop().into(post_image)
        } else {
            finish()
        }
    }

    private fun share() {
        if (image_uri != null) {
            //upload image to user folder -> storage
            //add images to user images <- db
            val uid = mFirebase.auth.currentUser!!.uid
            image_uri!!.lastPathSegment?.let {
                mFirebase.storage.child("users").child(uid).child("images")
                    .child(it).putFile(image_uri!!).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val imageDownloadUrl = image_uri.toString()
                            mFirebase.database.child("images").child(uid).push()
                            .setValue(imageDownloadUrl)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        //ctrl + alt + v : extra fun
                                        mFirebase.database.child("feed-posts").child(uid).push()
                                            .setValue(mkFeedPost(uid, imageDownloadUrl)).addOnCompleteListener{
                                                if(it.isSuccessful){
                                                    startActivity(Intent(this, ProfileActivity::class.java))
                                                    finish()
                                                }
                                            }

                                    } else {
                                        showToast(it.exception!!.message!!)
                                    }
                                }
                        } else {
                            showToast(it.exception!!.message!!)
                        }
                    }
            }

        }
    }
    private fun mkFeedPost(uid: String, imageDownloadUrl:String): FeedPost{
        return FeedPost(uid = uid,
            username = mUser.username,
            image =imageDownloadUrl,
            caption = caption_input.text.toString(),
            photo = mUser.photo)
    }
}

data class FeedPost(
    val uid: String = "",
    val username: String = "",
    val image: String = "",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val caption: String = "",
    val comments: List<Comment> = emptyList(),
    val timestamp: Any = ServerValue.TIMESTAMP,
    val photo: String? = null
) {
    // save -> firebase puts Long value
    //get <- Long
    fun timeStampDate(): Date = Date(timestamp as Long)
}

data class Comment(val uid: String, val username: String, val text: String) {

}

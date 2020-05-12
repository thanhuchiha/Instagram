package com.thanhuhiha.instagram.ui.editprofile

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
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.firebase.auth.EmailAuthProvider
import com.thanhuhiha.instagram.R
import com.thanhuhiha.instagram.data.firebase.common.FirebaseHelper
import com.thanhuhiha.instagram.models.User
import com.thanhuhiha.instagram.ui.common.*
import com.thanhuhiha.instagram.utils.CameraPictureTaker
import com.thanhuhiha.instagram.utils.ValueEventListenerAdapter
import com.thanhuhiha.instagram.views.PasswordDialog
import kotlinx.android.synthetic.main.activity_edit_profile.*


class EditProfileActivity : BaseActivity(), PasswordDialog.Listener {
    private lateinit var mUser: User
    private lateinit var mPendingUser: User
    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var cameraPictureTaker: CameraPictureTaker
    private lateinit var mCamera: CameraHelper
    private lateinit var mViewModel: EditProfileViewModel
    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        Log.d(TAG, "OnCreate")
        mFirebaseHelper = FirebaseHelper(this)

        close_image.setOnClickListener { finish() }
        save_image.setOnClickListener { updateProfile() }

        change_photo_text.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            takeCameraPicture()
        }

        setupAuthGuard {
            mViewModel = initViewModel()

            mViewModel.user.observe(this, Observer {
                it?.let {
                    mUser = it
                    name_input.setText(mUser.name)
                    username_input.setText(mUser.username)
                    website_input.setText(mUser.website)
                    bio_input.setText(mUser.bio)
                    email_input.setText(mUser.email)
                    phone_input.setText(mUser.phone?.toString())
                    profile_image.loadUserPhoto(mUser.photo)
                    Log.d(TAG, "Image1: ${mUser.photo}")
                }
            })
        }
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
        Log.d(TAG, "Image1 here: $image_uri ")
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup was granted
                    takeCameraPicture()
                } else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //set image captured to image view
            //update image to firebase storage
            Log.d(TAG, "IMAGE1: $image_uri")
            mFirebaseHelper.uploadUserPhoto(image_uri!!) {
                val photoUrl = it.storage?.downloadUrl.toString()
                mFirebaseHelper.updateUserPhoto(image_uri.toString()) {
                    mUser = mUser.copy(photo = image_uri.toString())
                    Log.d(TAG, "Photo here: ${image_uri.toString()}")
                    profile_image.loadUserPhoto(mUser.photo)
                }
            }
        }
    }

    //save image to database user.photo
    private fun updateProfile() {
        //get user from input
        //validate
        mPendingUser = readInputs()
        val error = validate(mPendingUser)
        if (error == null) {
            //save
            if (mPendingUser.email == mUser.email) {
                //update user
                updateUser(mPendingUser)
            } else {
                //show dialog
                PasswordDialog().show(supportFragmentManager, "password_dialog")//????
            }
        } else {
            //don't save
            showToast(error)
        }
    }

    private fun readInputs(): User {
        return User(
            name = name_input.text.toString(),
            username = username_input.text.toString(),
            website = website_input.text.toStringOrNull(),
            bio = bio_input.text.toStringOrNull(),
            email = email_input.text.toString(),
            phone = phone_input.text.toString().toLongOrNull()
        )
    }

    override fun onPasswordConfirm(password: String) {
        if (password.isNotEmpty()) {
            mViewModel.updateEmail(
                currentEmail = mUser.email,
                newEmail = mPendingUser.email,
                password = password
            )
                .addOnSuccessListener { updateUser(mPendingUser) }
        } else {
            showToast(getString(R.string.you_should_enter_your_password))
        }
    }

    private fun updateUser(user: User) {
        mViewModel.updateUserProfile(currentUser = mUser, newUser = user)
            .addOnSuccessListener {
                showToast(getString(R.string.profile_saved))
                finish()
            }
    }

    private fun validate(user: User): String? = when {
        user.name.isEmpty() -> "Please enter name"
        user.username.isEmpty() -> "Please enter username"
        user.email.isEmpty() -> "Please enter email"
        else -> null
    }

    companion object {
        const val TAG = "EditProfileActivity"
    }
}

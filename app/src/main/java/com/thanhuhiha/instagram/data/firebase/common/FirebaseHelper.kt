package com.thanhuhiha.instagram.data.firebase.common

import android.app.Activity
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.thanhuhiha.instagram.ui.common.showToast

class FirebaseHelper(private val activity: Activity) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val storage: StorageReference = FirebaseStorage.getInstance().reference

    fun currentUserReference(): DatabaseReference =
        database.child("users").child(currentUid()!!)

    fun currentUid(): String? = auth.currentUser?.uid

    fun uploadUserPhoto(
        photo: Uri,
        onSuccess: (UploadTask.TaskSnapshot) -> Unit
    ) {
        storage.child("users/${auth.currentUser!!.uid}/photo").putFile(photo)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess(it.result!!)
                } else {
                    activity.showToast(it.exception!!.message!!)
                }
            }
    }

    fun updateUserPhoto(
        photoUrl: String,
        onSuccess: () -> Unit
    ) {
        database.child("users/${auth.currentUser!!.uid}/photo").setValue(photoUrl)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    activity.showToast(it.exception!!.message!!)
                }
            }
    }
}
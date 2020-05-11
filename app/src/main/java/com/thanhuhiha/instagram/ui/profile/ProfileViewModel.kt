package com.thanhuhiha.instagram.ui.profile

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.thanhuhiha.instagram.data.UsersRepository
import com.thanhuhiha.instagram.ui.common.BaseViewModel

class ProfileViewModel(private val usersRepo: UsersRepository, onFailureListener: OnFailureListener)
    : BaseViewModel(onFailureListener) {
    val user = usersRepo.getUser()
    lateinit var images: LiveData<List<String>>

    fun init(uid: String) {
        if (!this::images.isInitialized) {
            images = usersRepo.getImages(uid)
        }
    }
}
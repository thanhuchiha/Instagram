package com.thanhuhiha.instagram.data

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.thanhuhiha.instagram.models.SearchPost

interface SearchRepository {
    fun searchPosts(text: String): LiveData<List<SearchPost>>
    fun createPost(post: SearchPost): Task<Unit>
}
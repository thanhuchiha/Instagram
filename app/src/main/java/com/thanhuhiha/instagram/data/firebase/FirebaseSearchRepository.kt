package com.thanhuhiha.instagram.data.firebase

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.thanhuhiha.instagram.data.SearchRepository
import com.thanhuhiha.instagram.data.common.map
import com.thanhuhiha.instagram.data.common.toUnit
import com.thanhuhiha.instagram.data.firebase.common.FirebaseLiveData
import com.thanhuhiha.instagram.data.firebase.common.database
import com.thanhuhiha.instagram.models.SearchPost

class FirebaseSearchRepository : SearchRepository {
    override fun searchPosts(text: String): LiveData<List<SearchPost>> {
        val reference = database.child("search-posts")
        val query = if (text.isEmpty()) {
            reference
        } else {
            reference.orderByChild("caption")
                .startAt(text.toLowerCase()).endAt("${text.toLowerCase()}\\uf8ff")
        }
        return FirebaseLiveData(query).map {
            it.children.map { it.asSearchPost()!! }
        }
    }

    override fun createPost(post: SearchPost): Task<Unit> =
        database.child("search-posts").push()
            .setValue(post.copy(caption = post.caption.toLowerCase())).toUnit()
}

private fun DataSnapshot.asSearchPost(): SearchPost? =
    getValue(SearchPost::class.java)?.copy(id = key!!)
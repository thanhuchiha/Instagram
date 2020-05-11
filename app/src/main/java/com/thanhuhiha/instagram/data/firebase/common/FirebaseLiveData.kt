package com.thanhuhiha.instagram.data.firebase.common

import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query
import com.thanhuhiha.instagram.utils.ValueEventListenerAdapter

class FirebaseLiveData(private val query: Query) : LiveData<DataSnapshot>() {
    private val listener = ValueEventListenerAdapter {
        value = it
    }

    override fun onActive() {
        super.onActive()
        query.addValueEventListener(listener)
    }

    override fun onInactive() {
        super.onInactive()
        query.removeEventListener(listener)
    }
}
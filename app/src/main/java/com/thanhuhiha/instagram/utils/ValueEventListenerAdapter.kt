package com.thanhuhiha.instagram.utils

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ValueEventListenerAdapter(val handler: (DataSnapshot) -> Unit) :
    ValueEventListener {
    private val TAG = "ValueEventListenerAdapter"

    override fun onDataChange(data: DataSnapshot) {
        handler(data)
    }

    @SuppressLint("LongLogTag")
    override fun onCancelled(error: DatabaseError) {
        Log.e(TAG, "OnCancelled", error.toException())
    }

}
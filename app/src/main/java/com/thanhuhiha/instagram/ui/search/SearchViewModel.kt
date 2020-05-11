package com.thanhuhiha.instagram.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.android.gms.tasks.OnFailureListener
import com.thanhuhiha.instagram.data.SearchRepository
import com.thanhuhiha.instagram.models.SearchPost
import com.thanhuhiha.instagram.ui.common.BaseViewModel

class SearchViewModel(searchRepo: SearchRepository,
                      onFailureListener: OnFailureListener
) : BaseViewModel(onFailureListener) {
    private val searchText = MutableLiveData<String>()

    val posts: LiveData<List<SearchPost>> = Transformations.switchMap(searchText) { text ->
        searchRepo.searchPosts(text)
    }

    fun setSearchText(text: String) {
        searchText.value = text
    }
}
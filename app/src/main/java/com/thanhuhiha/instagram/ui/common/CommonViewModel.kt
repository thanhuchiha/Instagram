package com.thanhuhiha.instagram.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnFailureListener
import java.lang.Exception

class CommonViewModel : ViewModel(), OnFailureListener {
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    override fun onFailure(ex: Exception) {
        setErrorMessage(ex.message)
    }

    fun setErrorMessage(message: String?) {
        message?.let { _errorMessage.value = it }
    }
}
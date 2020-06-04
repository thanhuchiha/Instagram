package com.thanhuhiha.instagram.ui.register

import android.app.Application
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.thanhuhiha.instagram.R
import com.thanhuhiha.instagram.data.UsersRepository
import com.thanhuhiha.instagram.data.common.SingleLiveEvent
import com.thanhuhiha.instagram.models.User
import com.thanhuhiha.instagram.ui.common.BaseViewModel
import com.thanhuhiha.instagram.ui.common.CommonViewModel

class RegisterViewModel(
    private val commonViewModel: CommonViewModel,
    private val app: Application,
    onFailureListener: OnFailureListener,
    private val usersRepo: UsersRepository
) : BaseViewModel(onFailureListener) {
    private var email: String? = null
    private val _goToNamePassScreen = SingleLiveEvent<Unit>()
    private val _goToHomeScreen = SingleLiveEvent<Unit>()
    private val _goBackToEmailScreen = SingleLiveEvent<Unit>()
    val goToNamePassScreen = _goToNamePassScreen
    val goToHomeScreen = _goToHomeScreen
    val goBackToEmailScreen = _goBackToEmailScreen

    fun onEmailEntered(email: String) {
        if (email.isNotEmpty()) {
            this.email = email
            usersRepo.isUserExistsForEmail(email).addOnSuccessListener { exists ->
                if (!exists) {
                    _goToNamePassScreen.call()
                } else {
                    commonViewModel.setErrorMessage(app.getString(R.string.this_email_already_exists))
                }
            }.addOnFailureListener(onFailureListener)
        } else {
            commonViewModel.setErrorMessage(app.getString(R.string.please_enter_email))
        }
    }

    fun onRegister(fullName: String, password: String) {
        if (fullName.isNotEmpty() && password.isNotEmpty()) {
            val localEmail = email
            if (localEmail != null) {
                usersRepo.createUser(mkUser(fullName, localEmail), password).addOnSuccessListener {
                    _goToHomeScreen.call()
                }.addOnFailureListener(onFailureListener)
            } else {
                Log.e(RegisterActivity.TAG, "onRegister: email is null")
                commonViewModel.setErrorMessage(app.getString(R.string.please_enter_email))
                _goBackToEmailScreen.call()
            }
        } else {
            commonViewModel.setErrorMessage(app.getString(R.string.please_enter_fullname_and_password))
        }
    }

    private fun mkUser(fullName: String, email: String): User {
        val username = mkUsername(fullName)
        return User(name = fullName, username = username, email = email)
    }

    private fun mkUsername(fullName: String) =
        fullName.toLowerCase().replace(" ", ".")
}
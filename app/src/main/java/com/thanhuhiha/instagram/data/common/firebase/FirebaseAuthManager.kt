package com.thanhuhiha.instagram.data.common.firebase

import com.google.android.gms.tasks.Task
import com.thanhuhiha.instagram.data.common.AuthManager
import com.thanhuhiha.instagram.data.common.toUnit
import com.thanhuhiha.instagram.data.firebase.common.auth

class FirebaseAuthManager : AuthManager {
    override fun signOut() {
        auth.signOut()
    }

    override fun signIn(email: String, password: String): Task<Unit> =
        auth.signInWithEmailAndPassword(email, password).toUnit()


}
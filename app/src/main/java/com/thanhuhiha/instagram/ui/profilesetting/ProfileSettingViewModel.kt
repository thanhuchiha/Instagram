package com.thanhuhiha.instagram.ui.profilesetting

import com.google.android.gms.tasks.OnFailureListener
import com.thanhuhiha.instagram.data.common.AuthManager
import com.thanhuhiha.instagram.ui.common.BaseViewModel

class ProfileSettingsViewModel(
    private val authManager: AuthManager,
    onFailureListener: OnFailureListener
) :
    BaseViewModel(onFailureListener),
    AuthManager by authManager
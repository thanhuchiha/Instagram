package com.thanhuhiha.instagram.ui.profilesetting

import android.os.Bundle
import com.thanhuhiha.instagram.R
import com.thanhuhiha.instagram.ui.common.BaseActivity
import com.thanhuhiha.instagram.ui.common.setupAuthGuard
import kotlinx.android.synthetic.main.activity_profile_setting.*

class ProfileSettingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_setting)

        setupAuthGuard {
            val viewModel = initViewModel<ProfileSettingsViewModel>()
            sign_out_text.setOnClickListener { viewModel.signOut() }
            back_image.setOnClickListener { finish() }
        }
    }
}

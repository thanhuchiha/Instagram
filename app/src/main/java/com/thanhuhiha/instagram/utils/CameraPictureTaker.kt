package com.thanhuhiha.instagram.utils

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraPictureTaker(private val activity: Activity) {
    val simpleDateFormat = SimpleDateFormat(
        "yyyyMMdd_HHmmss",
        Locale.US
    ).format(Date())
}
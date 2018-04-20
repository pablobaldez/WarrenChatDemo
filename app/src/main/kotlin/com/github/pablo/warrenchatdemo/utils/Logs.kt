package com.github.pablo.warrenchatdemo.utils

import android.util.Log
import com.github.pablo.warrenchatdemo.BuildConfig

fun logD(message: String) {
    if(BuildConfig.DEBUG) {
        Log.d("warrenTag#", message)
    }
}
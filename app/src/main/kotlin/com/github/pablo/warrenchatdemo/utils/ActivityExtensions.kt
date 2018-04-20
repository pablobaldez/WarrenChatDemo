package com.github.pablo.warrenchatdemo.utils

import android.app.Activity
import android.content.Intent
import com.github.pablo.warrenchatdemo.WarrenDemoApp

val Activity.warrenDemoApp get() = application as WarrenDemoApp

inline fun <reified T : Activity> Activity.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}
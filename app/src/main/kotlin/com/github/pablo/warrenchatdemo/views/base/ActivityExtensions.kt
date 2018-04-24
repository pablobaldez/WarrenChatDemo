package com.github.pablo.warrenchatdemo.views.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import com.github.pablo.warrenchatdemo.WarrenDemoApp


val Activity.warrenDemoApp get() = application as WarrenDemoApp

inline fun <reified T : Activity> Activity.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
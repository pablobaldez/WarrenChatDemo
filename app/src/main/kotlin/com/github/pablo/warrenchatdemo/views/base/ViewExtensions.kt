package com.github.pablo.warrenchatdemo.views.base

import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.EditText
import android.widget.TextView
import com.github.pablo.warrenchatdemo.R

fun EditText.setupNameMask() {
    inputType = InputType.TYPE_CLASS_TEXT or
            InputType.TYPE_TEXT_VARIATION_PERSON_NAME or
            InputType.TYPE_TEXT_FLAG_CAP_WORDS
    setHint(R.string.your_name)
}

fun EditText.setupEmailMask() {
    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    setHint(R.string.your_email)
}

fun EditText.setupCurrencyMask() {
    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
    setHint(R.string.your_salary)
}

fun EditText.setupIntegerMask() {
    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
    setHint(R.string.your_age)
}

fun TextView.setOnClickImeOptionsClickListener(imeId: Int, onClick: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if(imeId == actionId) {
            onClick()
            true
        } else false
    }
}

fun RecyclerView.scrollToBottom() {
    adapter?.let { smoothScrollToPosition(adapter.itemCount) }
}

val TextView.string get() = text.toString()

fun View.blink() {
    val animation = AlphaAnimation(1f, .3f)
    animation.duration = 1000
    animation.interpolator = LinearInterpolator()
    animation.repeatCount = Animation.INFINITE
    animation.repeatMode = Animation.REVERSE
    startAnimation(animation)
}
package com.github.pablo.warrenchatdemo.views.base

import android.databinding.BindingAdapter
import android.support.v4.view.GravityCompat
import android.support.v7.widget.CardView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.github.pablo.warrenchatdemo.R

@BindingAdapter("isUserChatTextColor")
fun TextView.setUserChatTextColor(chatMessageFromUser: Boolean) {
    val colorRes = if(chatMessageFromUser) {
        R.color.black
    } else {
        R.color.white
    }
    val color = resources.getColorCodeFromRes(colorRes)
    setTextColor(color)
}

@BindingAdapter("isUserChatBackground")
fun View.setUserChatBackground(chatMessageFromUser: Boolean) {
    val colorRes = if(chatMessageFromUser) {
        R.color.light_gray
    } else {
        R.color.colorPrimary
    }
    val color = resources.getColorCodeFromRes(colorRes)
    if(this is CardView) {
        setCardBackgroundColor(color)
    } else {
        setBackgroundColor(color)
    }
}

@BindingAdapter("isUserChatMargin")
fun View.setUserChatMargin(chatMessageFromUser: Boolean) {
    val margin = resources.getDimensionPixelSize(R.dimen.chat_message_horizontal_margin)
    val newLayoutParams = layoutParams
    if(newLayoutParams is ViewGroup.MarginLayoutParams) {
        if(chatMessageFromUser) {
            newLayoutParams.marginStart += margin
        } else {
            newLayoutParams.marginEnd += margin
        }
    }
}

@BindingAdapter("isUserChatGravity")
fun LinearLayout.setUserChatGravity(chatMessageFromUser: Boolean) {
    gravity = if(chatMessageFromUser) {
        GravityCompat.END or Gravity.TOP
    } else {
        GravityCompat.START or Gravity.TOP
    }
}

fun EditText.setupNameMask() {
    inputType = EditorInfo.TYPE_TEXT_FLAG_CAP_WORDS or
            EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME
    setHint(R.string.your_name)
}

fun EditText.setupEmailMask() {
    inputType = EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    setHint(R.string.your_email)
}

fun EditText.setupCurrencyMask() {
    inputType = EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
    setHint(R.string.your_email)
}

fun EditText.setupIntegerMask() {
    inputType = EditorInfo.TYPE_NUMBER_VARIATION_NORMAL
    hint = ""
}

fun TextView.setOnClickImeOptionsClickListener(imeId: Int, onClick: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if(imeId == actionId) {
            onClick()
            true
        } else false
    }
}

val TextView.string get() = text.toString()
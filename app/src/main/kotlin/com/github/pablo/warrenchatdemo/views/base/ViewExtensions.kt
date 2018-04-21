package com.github.pablo.warrenchatdemo.views.base

import android.databinding.BindingAdapter
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.GravityCompat
import android.support.v7.widget.CardView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.pablo.warrenchatdemo.R

@BindingAdapter("isUserChatBackground")
fun View.setUserChatBackground(chatMessageFromUser: Boolean) {
    val colorRes = if(chatMessageFromUser) {
        android.R.color.darker_gray
    } else {
        android.R.color.white
    }
    val color = ResourcesCompat.getColor(resources, colorRes, null)
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
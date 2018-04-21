package com.github.pablo.warrenchatdemo.views.base

import android.content.res.Resources
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.res.ResourcesCompat

@ColorInt
fun Resources.getColorCodeFromRes(@ColorRes colorResId: Int): Int {
    return ResourcesCompat.getColor(this, colorResId, null)
}
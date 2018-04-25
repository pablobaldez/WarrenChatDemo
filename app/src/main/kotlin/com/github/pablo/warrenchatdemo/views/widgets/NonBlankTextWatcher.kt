package com.github.pablo.warrenchatdemo.views.widgets

import android.text.Editable
import android.text.TextWatcher
import android.view.View

class NonBlankTextWatcher(private val viewToControl: View) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        viewToControl.isEnabled = s?.isNotBlank() ?: false
    }
}
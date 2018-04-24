package com.github.pablo.warrenchatdemo.views.base

import android.text.InputType
import android.widget.EditText
import android.widget.TextView
import com.github.pablo.warrenchatdemo.R

fun EditText.setupNameMask() {
    inputType = InputType.TYPE_CLASS_TEXT or
            InputType.TYPE_TEXT_FLAG_CAP_WORDS or
            InputType.TYPE_TEXT_VARIATION_PERSON_NAME
    setHint(R.string.your_name)
}

fun EditText.setupEmailMask() {
    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    setHint(R.string.your_email)
}

fun EditText.setupCurrencyMask() {
    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
    setHint(R.string.your_email)
}

fun EditText.setupIntegerMask() {
    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
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
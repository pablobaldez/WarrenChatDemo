package com.github.pablo.warrenchatdemo.presenters

/**
 * A question to be shown with delayed texts using type writer animation. this class is just a POJO
 * but is in this package because is have to be only used in presenter->view layers
 */
class DelayedMessage(val parts: List<DelayedText>)

class DelayedText(
        val text: String,
        val delay: Long,
        val actionWrite: Boolean = true
)
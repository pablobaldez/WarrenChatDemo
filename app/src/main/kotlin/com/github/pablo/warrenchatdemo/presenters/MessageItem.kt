package com.github.pablo.warrenchatdemo.presenters

/**
 * A question to be shown with delayed texts using type writer animation. this class is just a POJO
 * but is in this package because is have to be only used in presenter->view layers
 */
class MessageItem(val parts: List<DelayedText>? = null,
                  val answer: String? = null,
                  var animationFinished: Boolean = false,
                  var finalMessage: String? = null
) {

    fun getFullText(): String {
        if(parts != null) {
            var text = ""
            parts.forEach {
                if(it.actionWrite) {
                    text += it.text
                }
            }
            return text
        }
        return ""
    }

}

class DelayedText(
        val text: String,
        val delay: Long,
        val actionWrite: Boolean = true
)
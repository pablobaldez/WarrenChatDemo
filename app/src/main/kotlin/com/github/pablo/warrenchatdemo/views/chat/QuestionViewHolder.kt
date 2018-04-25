package com.github.pablo.warrenchatdemo.views.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.pablo.warrenchatdemo.R
import com.github.pablo.warrenchatdemo.presenters.MessageItem
import com.github.pablo.warrenchatdemo.views.widgets.TypeWriterTextView

class QuestionViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val typeWriterTextView: TypeWriterTextView? by lazy { itemView.findViewById<TypeWriterTextView>(R.id.text) }

    fun bind(messageItem: MessageItem) {
        when {
            messageItem.animationFinished -> typeWriterTextView?.text = messageItem.getFullText()
            messageItem.finalMessage != null -> messageItem.animateFinalMessage()
            else -> messageItem.animate()
        }
    }

    private fun MessageItem.animateFinalMessage() {
        if(typeWriterTextView?.isRunning == false) {
            typeWriterTextView?.text = ""
        }
        finalMessage?.let {
            typeWriterTextView?.type(it)
        }
    }

    private fun MessageItem.animate() {
        if(typeWriterTextView?.isRunning == false) {
            typeWriterTextView?.text = ""
        }
        parts?.forEach {delayedText ->
            typeWriterTextView?.let { typeWriterTextView ->
                if(delayedText.actionWrite) {
                    typeWriterTextView.type(delayedText.text).pause(delayedText.delay)
                } else {
                    typeWriterTextView.type(delayedText.text).pause(delayedText.delay)
                            .erase(delayedText.text).pause(delayedText.delay)
                }
            }
        }
    }

    companion object {
        fun new(parent: ViewGroup): QuestionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = inflater.inflate(R.layout.vh_question, parent, false)
            return QuestionViewHolder(binding)
        }
    }

}
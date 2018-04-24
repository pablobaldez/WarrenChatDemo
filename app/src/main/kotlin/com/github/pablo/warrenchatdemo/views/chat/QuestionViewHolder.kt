package com.github.pablo.warrenchatdemo.views.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.pablo.warrenchatdemo.R
import com.github.pablo.warrenchatdemo.presenters.DelayedMessage
import com.github.pablo.warrenchatdemo.views.widgets.TypeWriterTextView

class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val typeWriterTextView by lazy { itemView.findViewById<TypeWriterTextView>(R.id.text) }

    fun bind(delayedMessage: DelayedMessage) {
        delayedMessage.parts.forEach {
            if(it.actionWrite) {
                typeWriterTextView.type(it.text).pause(it.delay)
            } else {
                typeWriterTextView.erase(it.text).pause(it.delay)
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
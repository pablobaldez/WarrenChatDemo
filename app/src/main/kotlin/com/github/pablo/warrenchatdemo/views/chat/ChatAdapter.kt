package com.github.pablo.warrenchatdemo.views.chat

import android.view.ViewGroup
import com.github.pablo.warrenchatdemo.presenters.DelayedMessage
import com.github.pablo.warrenchatdemo.views.base.BaseAdapter

class ChatAdapter : BaseAdapter<DelayedMessage, QuestionViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder.new(parent)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        list?.getOrNull(position)?.let {
            holder.bind(it)
        }
    }
}
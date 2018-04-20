package com.github.pablo.warrenchatdemo.views.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.pablo.warrenchatdemo.R
import com.github.pablo.warrenchatdemo.presenters.ChatMessage

class ChatViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(chatMessage: ChatMessage) {

    }

    companion object {
        fun new(inflater: LayoutInflater, parent: ViewGroup): ChatViewHolder{
            val view = inflater.inflate(R.layout.activity_main, parent, false)
            return ChatViewHolder(view)
        }
    }

}
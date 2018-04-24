package com.github.pablo.warrenchatdemo.views.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.pablo.warrenchatdemo.R

class UserAnswerViewHolder private constructor(itemView: View)
    : RecyclerView.ViewHolder(itemView){

    private val iconTextViewHolder by lazy { itemView.findViewById<TextView>(R.id.icon_text) }
    private val answerTextView by lazy { itemView.findViewById<TextView>(R.id.answer_text) }

    fun bind(message: String) {
        iconTextViewHolder.visibility = if(userInitial != null) {
            View.VISIBLE
        } else {
            View.GONE
        }
        iconTextViewHolder.text = userInitial
        answerTextView.text = message
    }

    companion object {

        var userInitial: String? = null

        fun new(parent: ViewGroup): UserAnswerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.vh_user_answer, parent, false)
            return UserAnswerViewHolder(view)
        }
    }
}
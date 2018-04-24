package com.github.pablo.warrenchatdemo.views.chat

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.github.pablo.warrenchatdemo.R
import com.github.pablo.warrenchatdemo.injection.ActivityComponent
import com.github.pablo.warrenchatdemo.model.InputMask
import com.github.pablo.warrenchatdemo.presenters.ChatPresenter
import com.github.pablo.warrenchatdemo.presenters.ChatView
import com.github.pablo.warrenchatdemo.presenters.DelayedMessage
import com.github.pablo.warrenchatdemo.views.base.setOnClickImeOptionsClickListener
import com.github.pablo.warrenchatdemo.views.base.setupNameMask
import com.github.pablo.warrenchatdemo.views.base.string
import java.util.*
import javax.inject.Inject

class ChatActivity : AppCompatActivity(), ChatView {

    var presenter: ChatPresenter? = null @Inject set
    private lateinit var adapter: ChatAdapter

    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recycler_view) }
    private val answerEditText by lazy { findViewById<EditText>(R.id.answer_input) }
    private val sendButton by lazy { findViewById<FloatingActionButton>(R.id.send_fab) }
    private val globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        // TODO show input with delayed animation
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        ActivityComponent.new(this).inject(this)
        setupRecyclerView()
        setupSendClickListener()
        presenter?.onAttachView(this)
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter {
            // todo show input area
        }
        adapter.list = ArrayList()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupSendClickListener() {
        sendButton.setOnClickListener {
            presenter?.onClickSend(answerEditText.string)
        }
        answerEditText.setOnClickImeOptionsClickListener(EditorInfo.IME_ACTION_SEND) {
            presenter?.onClickSend(answerEditText.string)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onDetachView()
    }

    override fun showMessages(messages: Queue<DelayedMessage>) {
        adapter.messageQueue = messages
    }

    override fun showInputArea(inputMask: InputMask) {
        when(inputMask) {
            InputMask.NAME -> answerEditText.setupNameMask()
            InputMask.INTEGER -> {}
            InputMask.CURRENCY -> {}
            InputMask.EMAIL -> {}
        }
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    override fun hideInputArea() {
        // TODO descer com animação
    }

    override fun showErrorMessage() {
        // toast de erro
    }

}
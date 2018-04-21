package com.github.pablo.warrenchatdemo.views.chat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver
import com.github.pablo.warrenchatdemo.R
import com.github.pablo.warrenchatdemo.injection.ActivityComponent
import com.github.pablo.warrenchatdemo.model.InputMask
import com.github.pablo.warrenchatdemo.presenters.ChatMessage
import com.github.pablo.warrenchatdemo.presenters.ChatPresenter
import com.github.pablo.warrenchatdemo.presenters.ChatView
import javax.inject.Inject

class ChatActivity : AppCompatActivity(), ChatView {

    var presenter: ChatPresenter? = null @Inject set
    private lateinit var adapter: ChatAdapter
    private val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.recycler_view)
    }
    private val globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        // TODO show input with delayed animation
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        ActivityComponent.new(this).inject(this)
        setupRecyclerView()
        presenter?.onAttachView(this)
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter()
        adapter.list = ArrayList()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onDetachView()
    }

    override fun addMessage(message: ChatMessage) {
        adapter.list?.add(message)
    }

    override fun showInputArea(inputMask: InputMask) {
        adapter.notifyDataSetChanged()
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    override fun hideInputArea() {
        // TODO descer com animação
    }

    override fun showErrorMessage() {
        // toast de erro
    }

}
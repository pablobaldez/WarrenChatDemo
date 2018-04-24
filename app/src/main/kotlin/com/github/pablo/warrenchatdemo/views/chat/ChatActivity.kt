package com.github.pablo.warrenchatdemo.views.chat

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.design.widget.FloatingActionButton
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.github.pablo.warrenchatdemo.R
import com.github.pablo.warrenchatdemo.injection.ActivityComponent
import com.github.pablo.warrenchatdemo.model.InputMask
import com.github.pablo.warrenchatdemo.presenters.ChatPresenter
import com.github.pablo.warrenchatdemo.presenters.ChatView
import com.github.pablo.warrenchatdemo.presenters.DelayedMessage
import com.github.pablo.warrenchatdemo.views.base.*

import java.util.*
import javax.inject.Inject

class ChatActivity : AppCompatActivity(), ChatView {

    var presenter: ChatPresenter? = null @Inject set
    private lateinit var adapter: ChatAdapter

    private val rootLayout by lazy { findViewById<ConstraintLayout>(R.id.root) }
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recycler_view) }
    private val answerEditText by lazy { findViewById<EditText>(R.id.answer_input) }
    private val sendButton by lazy { findViewById<FloatingActionButton>(R.id.send_fab) }
    private val opt1TextView by lazy { findViewById<TextView>(R.id.opt_1) }
    private val opt2TextView by lazy { findViewById<TextView>(R.id.opt_2) }
    private var currentInputMask: InputMask? = null
    private var currentFirstAnswer: String? = null
    private var currentSecondAnswer: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        ActivityComponent.new(this).inject(this)
        setupRecyclerView()
        setupSendClickListener()
        setupOptionsClickListeners()
        presenter?.onAttachView(this)
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter { showBottomLayout() }
        adapter.list = ArrayList()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupSendClickListener() {
        sendButton.setOnClickListener {
            hideKeyboard()
            presenter?.onClickSend(answerEditText.string)
        }
        answerEditText.setOnClickImeOptionsClickListener(EditorInfo.IME_ACTION_SEND) {
            hideKeyboard()
            presenter?.onClickSend(answerEditText.string)
        }
    }

    private fun setupOptionsClickListeners() {
        opt1TextView.setOnClickListener {
            presenter?.onClickFirstAnswer()
        }
        opt2TextView.setOnClickListener {
            presenter?.onClickSecondAnswer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onDetachView()
    }

    override fun showMessages(messages: Queue<DelayedMessage>, mask: InputMask) {
        currentInputMask = mask
        adapter.messageQueue = messages
        currentFirstAnswer = null
        currentSecondAnswer = null
    }

    override fun showMessages(messages: Queue<DelayedMessage>, firstAnswer: String, secondAnswer: String) {
        currentInputMask = null
        currentFirstAnswer = firstAnswer
        currentSecondAnswer = secondAnswer
        adapter.messageQueue = messages
    }

    private fun showBottomLayout() {
        if(currentInputMask != null) {
            showInputLayout()
        } else if(currentFirstAnswer != null && currentSecondAnswer != null) {
            showOptionsLayout()
        }
    }

    private fun showInputLayout() {
        currentInputMask?.let {
            answerEditText.setText("")
            when(it) {
                InputMask.NAME -> answerEditText.setupNameMask()
                InputMask.INTEGER -> answerEditText.setupIntegerMask()
                InputMask.CURRENCY -> answerEditText.setupCurrencyMask()
                InputMask.EMAIL -> answerEditText.setupEmailMask()
            }
            applyConstraintSet(R.layout.activity_chat_input_expanded)
        }
    }

    private fun showOptionsLayout() {
        opt1TextView.text = currentFirstAnswer
        opt2TextView.text = currentSecondAnswer
        applyConstraintSet(R.layout.activity_chat_options_expanded)
    }

    override fun hideAnswerArea() {
        applyConstraintSet(R.layout.activity_chat)
    }

    private fun applyConstraintSet(@LayoutRes layoutResId: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this, layoutResId)
        TransitionManager.beginDelayedTransition(rootLayout)
        constraintSet.applyTo(rootLayout)
    }

    override fun showErrorMessage() {
        Toast.makeText(this, R.string.message_error_generic, Toast.LENGTH_SHORT).show()
    }

}
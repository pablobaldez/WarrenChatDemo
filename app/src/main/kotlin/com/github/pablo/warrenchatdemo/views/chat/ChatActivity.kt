package com.github.pablo.warrenchatdemo.views.chat

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.design.widget.FloatingActionButton
import android.support.transition.AutoTransition
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.github.pablo.warrenchatdemo.R
import com.github.pablo.warrenchatdemo.injection.ActivityComponent
import com.github.pablo.warrenchatdemo.model.InputMask
import com.github.pablo.warrenchatdemo.presenters.ChatPresenter
import com.github.pablo.warrenchatdemo.presenters.ChatView
import com.github.pablo.warrenchatdemo.presenters.MessageItem
import com.github.pablo.warrenchatdemo.views.base.*
import com.github.pablo.warrenchatdemo.views.widgets.SmoothScrollLayoutManager
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
    private val seeProfileView by lazy { findViewById<View>(R.id.see_profile) }
    private var currentInputMask: InputMask? = null
    private var currentFirstAnswer: String? = null
    private var currentSecondAnswer: String? = null
    private var isInFinalMessage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setSupportActionBar(findViewById(R.id.toolbar))
        ActivityComponent.new(this).inject(this)
        setupRecyclerView()
        setupFinalViewClick()
        setupSendClickListener()
        setupOptionsClickListeners()
        presenter?.onAttachView(this)
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter()
        adapter.setListeners({
            showBottomLayout()
            recyclerView.scrollToBottom()
        }, {})
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                recyclerView.scrollToBottom()
            }
        })
        val layoutManager = SmoothScrollLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun setupSendClickListener() {
        sendButton.setOnClickListener {
            onClickSend()
        }
        answerEditText.setOnClickImeOptionsClickListener(EditorInfo.IME_ACTION_SEND) {
            onClickSend()
        }
    }

    private fun setupFinalViewClick() {
        seeProfileView.setOnClickListener {
            presenter?.onClickSeeProfile()
        }
    }

    private fun onClickSend() {
        hideKeyboard()
        presenter?.onClickSend(answerEditText.string)
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

    override fun showMessages(messages: Queue<MessageItem>, mask: InputMask) {
        currentInputMask = mask
        isInFinalMessage = false
        currentFirstAnswer = null
        currentSecondAnswer = null
        adapter.messageQueue = messages
    }

    override fun showMessages(messages: Queue<MessageItem>, firstAnswer: String, secondAnswer: String) {
        isInFinalMessage = false
        currentInputMask = null
        currentFirstAnswer = firstAnswer
        currentSecondAnswer = secondAnswer
        adapter.messageQueue = messages
    }

    override fun showFinalMessage(message: MessageItem) {
        isInFinalMessage = true
        currentFirstAnswer = null
        currentSecondAnswer = null
        currentInputMask = null
        adapter.add(message)
    }

    override fun showProfile() {
        startActivity<ProfileActivity>()
        finish()
    }

    private fun showBottomLayout() {
        when {
            isInFinalMessage -> showFinalOptionsLayout()
            currentInputMask != null -> showInputLayout()
            currentFirstAnswer != null && currentSecondAnswer != null -> showOptionsLayout()
        }
    }

    private fun showFinalOptionsLayout() {
        applyConstraintSet(R.layout.activity_chat_final_answer_expanded)
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

    override fun showUserInitial(userInitial: String) {
        UserAnswerViewHolder.userInitial = userInitial
    }

    override fun showUserAnswer(item: MessageItem) {
        applyConstraintSet(R.layout.activity_chat)
        adapter.add(item)
    }

    private fun applyConstraintSet(@LayoutRes layoutResId: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this, layoutResId)
        val transition = AutoTransition()
        transition.startDelay = 100
        TransitionManager.beginDelayedTransition(rootLayout)
        constraintSet.applyTo(rootLayout)
    }

    override fun showErrorMessage() {
        Toast.makeText(this, R.string.message_error_generic, Toast.LENGTH_SHORT).show()
    }

}
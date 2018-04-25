package com.github.pablo.warrenchatdemo.views.chat

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.design.widget.FloatingActionButton
import android.support.transition.AutoTransition
import android.support.transition.TransitionManager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.github.pablo.warrenchatdemo.R
import com.github.pablo.warrenchatdemo.injection.ActivityComponent
import com.github.pablo.warrenchatdemo.model.InputMask
import com.github.pablo.warrenchatdemo.presenters.ChatPresenter
import com.github.pablo.warrenchatdemo.presenters.ChatView
import com.github.pablo.warrenchatdemo.presenters.MessageItem
import com.github.pablo.warrenchatdemo.views.base.*
import com.github.pablo.warrenchatdemo.views.widgets.CurrencyTextWatcher
import com.github.pablo.warrenchatdemo.views.widgets.NonBlankTextWatcher
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
    private val swipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }
    private lateinit var currencyTextWatcher: CurrencyTextWatcher
    private lateinit var nonBlankTextWatcher: NonBlankTextWatcher

    private var currentInputMask: InputMask? = null
    private var currentFirstAnswer: String? = null
    private var currentSecondAnswer: String? = null
    private var isInFinalMessage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setSupportActionBar(findViewById(R.id.toolbar))
        setTitle(R.string.discovering_your_profile)
        ActivityComponent.new(this).inject(this)
        setupSwipe()
        setupRecyclerView()
        setupFinalViewClick()
        setupSendClickListener()
        setupOptionsClickListeners()
        presenter?.onAttachView(this)
    }

    private fun setupSwipe() {
        swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.orange)
        swipeRefreshLayout.setOnRefreshListener {
            presenter?.onRefresh()
        }
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter()
        adapter.allMessagesTypedListener = {
            showBottomLayout()
            recyclerView.scrollToBottom()
        }
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
        sendButton.isEnabled = false
        sendButton.setOnClickListener {
            onClickSend()
        }
        answerEditText.setOnClickImeOptionsClickListener(EditorInfo.IME_ACTION_SEND) {
            onClickSend()
        }
        nonBlankTextWatcher = NonBlankTextWatcher(sendButton)
        currencyTextWatcher = CurrencyTextWatcher(answerEditText)
        answerEditText.addTextChangedListener(nonBlankTextWatcher)
    }

    private fun setupFinalViewClick() {
        seeProfileView.setOnClickListener {
            presenter?.onClickSeeProfile()
        }
    }

    private fun onClickSend() {
        if(answerEditText.text.isNotBlank()) {
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

    override fun showMessages(messages: Queue<MessageItem>, mask: InputMask) {
        swipeRefreshLayout.isRefreshing = false
        currentInputMask = mask
        isInFinalMessage = false
        currentFirstAnswer = null
        currentSecondAnswer = null
        adapter.messageQueue = messages
    }

    override fun showMessages(messages: Queue<MessageItem>, firstAnswer: String, secondAnswer: String) {
        swipeRefreshLayout.isRefreshing = false
        isInFinalMessage = false
        currentInputMask = null
        currentFirstAnswer = firstAnswer
        currentSecondAnswer = secondAnswer
        adapter.messageQueue = messages
    }

    override fun showFinalMessage(message: MessageItem) {
        swipeRefreshLayout.isRefreshing = false
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

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        seeProfileView.isEnabled = false
        seeProfileView.isClickable = false
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
        seeProfileView.isEnabled = true
        seeProfileView.isClickable = true
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
            answerEditText.removeTextChangedListener(currencyTextWatcher)
            sendButton.isEnabled = false
            when(it) {
                InputMask.NAME -> answerEditText.setupNameMask()
                InputMask.INTEGER -> answerEditText.setupIntegerMask()
                InputMask.EMAIL -> answerEditText.setupEmailMask()
                InputMask.CURRENCY -> {
                    answerEditText.addTextChangedListener(currencyTextWatcher)
                    answerEditText.setupCurrencyMask()
                }
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
        swipeRefreshLayout.isRefreshing = false
        Toast.makeText(this, R.string.message_error_generic, Toast.LENGTH_SHORT).show()
    }

}
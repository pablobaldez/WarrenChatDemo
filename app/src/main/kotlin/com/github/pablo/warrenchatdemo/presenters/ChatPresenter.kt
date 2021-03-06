package com.github.pablo.warrenchatdemo.presenters

import com.github.pablo.warrenchatdemo.api.MessagesApi
import com.github.pablo.warrenchatdemo.model.SuitabilityQuestion
import com.github.pablo.warrenchatdemo.model.UserAnswer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

class ChatPresenter @Inject constructor(private val messagesApi: MessagesApi) {

    private var chatView: ChatView? = null
    private var disposable: Disposable? = null
    private var userAnswer = UserAnswer()
    private var currentQuestion: SuitabilityQuestion? = null

    fun onAttachView(chatView: ChatView) {
        this.chatView = chatView
        loadQuestion()
    }

    fun onRefresh() {
        loadQuestion()
    }

    fun onClickSend(answer: String) {
        if(currentQuestion?.id == QUESTION_NAME_ID && answer.isNotEmpty()) {
            chatView?.showUserInitial(Character.toString(answer[0]))
        }
        formatAnswer(answer)
        currentQuestion?.id?.let {
            userAnswer.put(it, answer)
            loadQuestion()
        }
    }

    fun onClickFirstAnswer() {
        onClickButtonAnswer(0)
    }

    fun onClickSecondAnswer() {
        onClickButtonAnswer(1)
    }

    private fun onClickButtonAnswer(index: Int) {
        currentQuestion?.id?.let {
            formatAnswer(currentQuestion?.getButtonTitle(index) ?: "")
            val answer = currentQuestion?.getButtonValue(index)
            if(answer != null) {
                userAnswer.put(it, answer)
                loadQuestion()
            }
        }
    }

    private fun formatAnswer(answer: String) {
        val response = currentQuestion?.getResponse()
        val finalAnswer = if(response != null) {
            val regex = Regex("\\{\\{.*?\\}\\}")
            response.replace(regex, answer)
        } else {
            answer
        }
        chatView?.showUserAnswer(MessageItem(answer = finalAnswer))
    }

    private fun loadQuestion() {
        val list = LinkedList<MessageItem>()
        disposable = messagesApi.answer(userAnswer)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                // for some reason i'm getting UnknownHostException sometimes
                .retry(2) { it is UnknownHostException }
                .flatMapObservable {
                    currentQuestion = it
                    Observable.fromIterable(it.messages)
                }
                .map { message -> message.value }
                .map {
                    if(currentQuestion?.id == FINAL_QUESTION_ID) {
                        MessageItem(finalMessage = currentQuestion?.getMessageValue(0))
                    } else {
                        MessageSplitter.split(it, DEFAULT_WRITER_ANIMATION_DELAY)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { list.add(it) },
                        { chatView?.showErrorMessage() },
                        { onCompleteQuestionLoad(list) }
                )
    }

    private fun onCompleteQuestionLoad(list: LinkedList<MessageItem>) {
        val inputMask = currentQuestion?.getMask()
        when {
            currentQuestion?.id == FINAL_QUESTION_ID -> chatView?.showFinalMessage(list[0])
            inputMask != null -> chatView?.showMessages(list, inputMask)
            else ->{
                val firstOption = currentQuestion?.getButtonTitle(0)
                val secondOption = currentQuestion?.getButtonTitle(1)
                if(firstOption != null && secondOption != null) {
                    chatView?.showMessages(list, firstOption, secondOption)
                }
            }
        }
    }

    fun onClickSeeProfile() {
        userAnswer.finish()
        disposable = messagesApi.finish(userAnswer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { chatView?.showLoading() }
                .subscribe({
                    chatView?.hideLoading()
                    chatView?.showProfile()
                }, {
                    chatView?.hideLoading()
                    chatView?.showErrorMessage()
                })
    }

    fun onDetachView() {
        if(disposable?.isDisposed == false) {
            disposable?.dispose()
        }
        chatView = null
    }

    companion object {
        const val DEFAULT_WRITER_ANIMATION_DELAY = 1000L
        const val QUESTION_NAME_ID = "question_name"
        const val FINAL_QUESTION_ID = "final"
    }

}
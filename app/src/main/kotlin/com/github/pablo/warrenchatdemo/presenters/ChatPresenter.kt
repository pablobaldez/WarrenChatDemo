package com.github.pablo.warrenchatdemo.presenters

import com.github.pablo.warrenchatdemo.api.MessagesApi
import com.github.pablo.warrenchatdemo.model.SuitabilityQuestion
import com.github.pablo.warrenchatdemo.model.UserAnswer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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

    fun onClickSend(answer: String) {
        currentQuestion?.id?.let {
            userAnswer.put(it, answer)
            loadQuestion()
        }
    }

    private fun loadQuestion() {
        val list = LinkedList<DelayedMessage>()
        disposable = messagesApi.answer(userAnswer)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMapObservable {
                    currentQuestion = it
                    Observable.fromIterable(it.messages)
                }
                .map { message -> message.value }
                .map { MessageSplitter.split(it, DEFAULT_WRITER_ANIMATION_DELAY) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    chatView?.hideInputArea()
                }
                .subscribe({ list.add(it) }, { onError() }, { onCompleteQuestionLoad(list) })
    }

    private fun onError() {
        chatView?.showErrorMessage()
    }

    private fun onCompleteQuestionLoad(list: LinkedList<DelayedMessage>) {
        chatView?.showMessages(list)
    }

    fun onDetachView() {
        if(disposable?.isDisposed == false) {
            disposable?.dispose()
        }
        chatView = null
    }

    companion object {
        const val DEFAULT_WRITER_ANIMATION_DELAY = 1000L
    }

}
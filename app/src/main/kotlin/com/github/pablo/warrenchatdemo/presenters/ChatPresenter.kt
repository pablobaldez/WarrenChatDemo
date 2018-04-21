package com.github.pablo.warrenchatdemo.presenters

import com.github.pablo.warrenchatdemo.api.MessagesApi
import com.github.pablo.warrenchatdemo.model.Message
import com.github.pablo.warrenchatdemo.model.SuitabilityQuestion
import com.github.pablo.warrenchatdemo.model.UserAnswer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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
        disposable = messagesApi.answer(userAnswer)
                .flatMapObservable {
                    currentQuestion = it
                    Observable.fromIterable(it.messages)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    chatView?.hideInputArea()
                }
                .subscribe({ onNext(it) }, { onError() }, { onCompleteQuestionLoad() })
    }

    private fun onNext(message: Message) {
        message.value?.let {
            chatView?.addMessage(ChatMessage(it, it.startsWith("A")))
        }
    }

    private fun onError() {
        chatView?.showErrorMessage()
    }

    private fun onCompleteQuestionLoad() {
        currentQuestion?.inputs?.let { inputs ->
            val mask = inputs.getOrNull(0)?.mask
            if(mask != null) {
                chatView?.showInputArea(mask)
            }
        }
    }

    fun onDetachView() {
        if(disposable?.isDisposed == false) {
            disposable?.dispose()
        }
        chatView = null
    }

}
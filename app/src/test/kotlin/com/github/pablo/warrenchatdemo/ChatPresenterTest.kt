package com.github.pablo.warrenchatdemo

import com.github.pablo.warrenchatdemo.api.MessagesApi
import com.github.pablo.warrenchatdemo.model.SuitabilityQuestion
import com.github.pablo.warrenchatdemo.model.UserAnswer
import com.github.pablo.warrenchatdemo.presenters.ChatPresenter
import com.github.pablo.warrenchatdemo.presenters.ChatView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.only
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.emptyOrNullString
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


@RunWith(MockitoJUnitRunner::class)
class ChatPresenterTest {

    @Mock
    private lateinit var chatView: ChatView
    @Mock
    private lateinit var messagesApi: MessagesApi
    @InjectMocks
    private lateinit var presenter: ChatPresenter

    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpRxSchedulers() {
            val immediate = object : Scheduler() {
                override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                    return super.scheduleDirect(run, 0L, unit)
                }

                override fun createWorker(): Worker {
                    return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
                }
            }

            RxJavaPlugins.setInitIoSchedulerHandler { _ -> immediate }
            RxJavaPlugins.setInitComputationSchedulerHandler { _ -> immediate }
            RxJavaPlugins.setInitNewThreadSchedulerHandler { _ -> immediate }
            RxJavaPlugins.setInitSingleSchedulerHandler { _ -> immediate }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> immediate }
        }
    }

    @Test
    fun `test empty answer when view is attached`() {
        val response = SuitabilityQuestion()
        `when`(messagesApi.answer(any())).thenReturn(Single.just(response))
        val captor = argumentCaptor<UserAnswer>()
        presenter.onAttachView(chatView)
        verify(messagesApi, only()).answer(captor.capture())
        val answer = captor.firstValue
        assertThat(answer.id, `is`(emptyOrNullString()))
    }

}
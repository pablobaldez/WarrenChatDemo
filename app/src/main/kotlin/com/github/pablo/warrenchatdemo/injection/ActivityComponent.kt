package com.github.pablo.warrenchatdemo.injection

import android.app.Activity
import com.github.pablo.warrenchatdemo.views.base.warrenDemoApp
import com.github.pablo.warrenchatdemo.views.chat.ChatActivity
import dagger.Subcomponent

@Subcomponent
interface ActivityComponent {

    fun inject(activity: ChatActivity)

    companion object {
        fun new(activity: Activity): ActivityComponent {
            return activity.warrenDemoApp.appComponent.plus()
        }
    }

}
package com.github.pablo.warrenchatdemo

import android.app.Application
import com.github.pablo.warrenchatdemo.injection.AppComponent
import com.github.pablo.warrenchatdemo.injection.DaggerAppComponent

class WarrenDemoApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }

}
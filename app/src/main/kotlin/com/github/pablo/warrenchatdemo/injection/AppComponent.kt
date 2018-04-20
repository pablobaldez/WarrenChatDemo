package com.github.pablo.warrenchatdemo.injection

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class])
interface AppComponent {

    fun plus(): ActivityComponent

}
package com.github.pablo.warrenchatdemo.views.initial

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.github.pablo.warrenchatdemo.R
import com.github.pablo.warrenchatdemo.views.base.startActivity
import com.github.pablo.warrenchatdemo.views.chat.ChatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.start_button).setOnClickListener {
            startActivity<ChatActivity>()
        }
    }
}

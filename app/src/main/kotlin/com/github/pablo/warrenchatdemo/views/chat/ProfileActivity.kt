package com.github.pablo.warrenchatdemo.views.chat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.pablo.warrenchatdemo.R


class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        findViewById<View>(R.id.button).setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.oiwarren.oiwarren"))
                startActivity(intent)
            } catch (e: android.content.ActivityNotFoundException) {
                val intent = Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.oiwarren.oiwarren"))
                startActivity(intent)
            }
        }
    }

}
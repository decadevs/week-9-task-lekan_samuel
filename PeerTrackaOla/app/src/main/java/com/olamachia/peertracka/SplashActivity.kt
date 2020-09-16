package com.olamachia.peertracka

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SplashActivity: AppCompatActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Delaying the splash screen by 1000 milliseconds  */

        val runnable = Runnable {
            Intent(this, MapsActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        Handler().postDelayed(runnable, 1000)
    }
}
package com.example.streamline

import android.app.Application

class StreamlineApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TargetRepository.initialize(this)
    }
}
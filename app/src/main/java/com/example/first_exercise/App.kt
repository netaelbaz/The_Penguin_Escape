package com.example.first_exercise
import android.app.Application
import com.example.first_exercise.SignalManager

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
    }
}
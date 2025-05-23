package com.example.first_exercise
import android.app.Application
import com.example.first_exercise.SignalManager
import com.example.first_exercise.utilities.SharedPreferencesManager

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
        SignalManager.init(this)
    }
}
package com.example.first_exercise.utilities

import android.content.Context

class SharedPreferencesManager private constructor(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        Constants.SharedPreferences.HIGH_SCORES_KEY,
        Context.MODE_PRIVATE
    )

    companion object{
        @Volatile
        private var instance: SharedPreferencesManager? = null

        fun init(context: Context): SharedPreferencesManager{
            return instance ?: synchronized(this){
                instance?: SharedPreferencesManager(context).also {  instance = it }
            }
        }

        fun getInstance(): SharedPreferencesManager{
            return instance ?: throw IllegalStateException(
                "SharedPreferencesManager must be initialized by calling init(context) before use."
            )
        }
    }


    fun putString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }

    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences
            .getString(
                key,
                defaultValue
            ) ?: defaultValue
    }

    fun putBoolean(key: String, value: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(
            key, defaultValue
        )
    }
}
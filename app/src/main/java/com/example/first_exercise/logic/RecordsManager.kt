package com.example.first_exercise.logic

import com.example.first_exercise.model.Score
import com.example.first_exercise.model.TopScores
import com.example.first_exercise.utilities.Constants
import com.example.first_exercise.utilities.SharedPreferencesManager
import com.google.gson.Gson
import android.util.Log


class RecordsManager(private val keepLast: Int = 10) {

    private val sharedPreferencesManager = SharedPreferencesManager.getInstance()

    fun loadScores(): TopScores {
        val scoresAsJson = sharedPreferencesManager.getString(Constants.SharedPreferences.HIGH_SCORES_KEY, "")
        if (scoresAsJson.isEmpty())
            return  TopScores.Builder().build() // Return empty if no data
        return try {
                val gson = Gson()
                val scoreList = gson.fromJson(scoresAsJson, Array<Score>::class.java)?.toList() ?: emptyList()
                TopScores.Builder(scoreList.toMutableList()).build()
            } catch (e: Exception) {
            Log.e("ScoreManager", "Failed to load scores: ${e.message}")
            TopScores.Builder().build() // Return empty if any error
        }
    }

    // Save a new score
    fun saveScore(score: Int, lat: Double, lon: Double) {
        val newScore = Score.Builder()
            .score(score)
            .lat(lat)
            .lon(lon)
            .build()
        val allScores = loadScores().topScores.toMutableList()
        val updatedScores = TopScores.Builder(allScores)
            .addScore(newScore)
            .build()
            .topScores
            .distinctBy { it.score }
            .sortedByDescending { it.score }
            .take(keepLast)

        val gson = Gson()
        val scoresAsJson = gson.toJson(updatedScores)
        SharedPreferencesManager.getInstance().putString(
            Constants.SharedPreferences.HIGH_SCORES_KEY,
            scoresAsJson
        )
        Log.d("scores as json", scoresAsJson)
    }

}
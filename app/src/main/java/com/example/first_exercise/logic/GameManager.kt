package com.example.first_exercise.logic

import com.example.first_exercise.utilities.Constants
import kotlin.collections.copyOf

class GameManager(private val lives: Int = 3) {
    var crashes: Int = 0
        private set

    var carLane = 1
        private set

    var objectsMatrix = Array(Constants.GameDetails.ROWS) { IntArray(Constants.GameDetails.COLS) { 0 } }

    val isGameOver: Boolean
        get() = crashes == lives

    fun updateMatrix() {
        for (i in Constants.GameDetails.ROWS - 1 downTo 1) {
            objectsMatrix[i] = objectsMatrix[i - 1].copyOf()
        }

        val generatePercent = (0 until 100).random()
        val isGenerateNew = if (generatePercent < 65) true else false
        if (isGenerateNew) {
            val obstacleLane = (0 until Constants.GameDetails.COLS).random()
            objectsMatrix[0] = IntArray(Constants.GameDetails.COLS) { if (it == obstacleLane) 1 else 0 }
        } else {
            objectsMatrix[0] = IntArray(Constants.GameDetails.COLS) { 0 }
        }

        if (objectsMatrix[Constants.GameDetails.ROWS - 1][carLane] == 1) {
            crashes++
        }
    }


    fun startNewGame() {
        crashes = 0
        objectsMatrix = Array(Constants.GameDetails.ROWS) { IntArray(Constants.GameDetails.COLS) { 0 } }
    }

    fun moveRight() {
        if (carLane < Constants.GameDetails.COLS - 1) carLane++
    }

    fun moveLeft() {
        if (carLane > 0) carLane--
    }
}
package com.example.first_exercise.logic

import com.example.first_exercise.SignalManager
import com.example.first_exercise.interfaces.GameEventListenerCallback
import com.example.first_exercise.utilities.Constants
import kotlin.collections.copyOf

class GameManager(private val lives: Int = 3) {

    var score: Int = 0
        private set

    var distance: Int = 0
        private set

    var crashes: Int = 0
        private set

    var carLane: Int = 2
        private set

    var objectsMatrix = Array(Constants.GameDetails.ROWS) { IntArray(Constants.GameDetails.COLS) { 0 } }

    val isGameOver: Boolean
        get() = crashes == lives

    val recordsManager: RecordsManager = RecordsManager(10)

    var gameEventListener: GameEventListenerCallback? = null

    fun updateMatrix() {
        for (i in Constants.GameDetails.ROWS - 1 downTo 1) {
            objectsMatrix[i] = objectsMatrix[i - 1].copyOf()
        }

        distance ++ // in each loop iteration i want to increase the distance
        score += Constants.Score.distanceWorth // for each distance passed increase score

        val generatePercent = (0 until 100).random()
        val isGenerateNew = (generatePercent < 85)
        if (isGenerateNew) {
            val targetLane = (0 until Constants.GameDetails.COLS).random()
            val generateCoin = (0 until 100).random() < 15
            val value = if (generateCoin) Constants.objectValues.coinValue else Constants.objectValues.obstacleValue
            objectsMatrix[0] = IntArray(Constants.GameDetails.COLS) { if (it == targetLane) value else 0 }
        } else {
            objectsMatrix[0] = IntArray(Constants.GameDetails.COLS) { 0 }
        }

        if (objectsMatrix[Constants.GameDetails.ROWS - 1][carLane] == Constants.objectValues.obstacleValue) {
            // crashed happened
            crashes++
            SignalManager
                .getInstance()
                .toast("Crashed")
            SignalManager
                .getInstance()
                .vibrate()
            gameEventListener?.onCrash()
        }
        else if (objectsMatrix[Constants.GameDetails.ROWS - 1][carLane] == Constants.objectValues.coinValue) {
            score += Constants.Score.coinsWorth
//            scoreManager.updateScore(Constants.Score.coinsWorth)
            gameEventListener?.onCoinCollecting()
        }
    }

    fun gameOver(lat: Double, lon: Double) {
        recordsManager.saveScore(score, lat, lon)
    }


//    fun startNewGame() {
//        crashes = 0
//        objectsMatrix = Array(Constants.GameDetails.ROWS) { IntArray(Constants.GameDetails.COLS) { 0 } }
//        scoreManager.resetScore()
//    }

    fun moveRight() {
        if (carLane < Constants.GameDetails.COLS - 1) carLane++
    }

    fun moveLeft() {
        if (carLane > 0) carLane--
    }

    fun getCurrentScore(): Int {
        return score
    }

//    fun getTopScore(): Int {
////        return scoreManager.getBestScore()
//    }
}
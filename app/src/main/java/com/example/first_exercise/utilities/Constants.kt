package com.example.first_exercise.utilities

class Constants {
    object Scheduler {
        const val SLOW_DELAY: Long = 1_200L
        const val FAST_DELAY: Long = 500L
    }
    object GameDetails {
        const val ROWS: Int = 6
        const val COLS: Int = 5
    }
    object ObjectValues {
        const val OBSTACLE_VALUE: Int = 1
        const val COIN_VALUE: Int = 2
    }
    object Score {
        const val DISTANCE_WORTH: Int = 5
        const val COINS_WORTH: Int = 15
    }
    object BundleKeys{
        const val BUTTONS_MODE: String = "BUTTONS_MODE"
        const val FAST_MODE: String = "FAST_MODE"
    }
    object SharedPreferences {
        const val HIGH_SCORES_KEY: String = "HighScoreKey"
    }
}
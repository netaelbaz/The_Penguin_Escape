package com.example.first_exercise.utilities

class Constants {
    object scheduler {
        const val SLOW_DELAY: Long = 1_500L
        const val FAST_DELAY: Long = 700L
    }
    object GameDetails {
        const val ROWS: Int = 6
        const val COLS: Int = 5
    }
    object objectValues {
        const val obstacleValue: Int = 1
        const val coinValue: Int = 2
    }
    object Score {
        const val distanceWorth: Int = 5
        const val coinsWorth: Int = 15
    }
    object BundleKeys{
        const val BUTTONS_MODE: String = "BUTTONS_MODE"
        const val FAST_MODE: String = "FAST_MODE"
        const val SCORE_KEY: String = "SCORE_KEY"
    }
    object SharedPreferences {
        const val HIGH_SCORES_KEY: String = "HighScoreKey"
        const val LOCATION_PERMISSION_KEY: String = "LocationPermissionKey"
    }
}
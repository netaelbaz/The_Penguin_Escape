package com.example.first_exercise.model

data class Score private constructor(
    val score: Int,
    val lat: Double,
    val lon: Double
) {
    class Builder(
        var score: Int = 0,
        var lat: Double = 0.0,
        var lon: Double = 0.0
    ) {
        fun score(score: Int) = apply { this.score = score }
        fun lon(lon: Double) = apply { this.lon = lon}
        fun lat(lat: Double) = apply { this.lat = lat}
        fun build() = Score(score,lat,lon)
    }
}

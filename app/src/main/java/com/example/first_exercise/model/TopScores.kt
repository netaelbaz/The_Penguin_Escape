package com.example.first_exercise.model

data class TopScores private constructor(
    val topScores: List<Score>
) {
    class Builder (
        var topScores: List<Score> = mutableListOf()
    ){
        fun addScore(score: Score) = apply { (this.topScores as MutableList).add(score) }
        fun build() = TopScores(topScores)
    }
}

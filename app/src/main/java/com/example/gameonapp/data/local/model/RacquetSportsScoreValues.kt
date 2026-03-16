package com.example.gameonapp.data.local.model

enum class RacquetSportsScoreValues(val display: String) {
    ZERO("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    ADVANTAGE("AD"),
    GAME("GAME");

    fun next(): RacquetSportsScoreValues = when (this) {
        ZERO -> FIFTEEN
        FIFTEEN -> THIRTY
        THIRTY -> FORTY
        FORTY -> GAME
        ADVANTAGE -> GAME
        GAME -> GAME
    }
}
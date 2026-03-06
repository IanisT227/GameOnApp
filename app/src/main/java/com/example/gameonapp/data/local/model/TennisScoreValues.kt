package com.example.gameonapp.data.local.model

enum class TennisScoreValues(val display: String) {
    ZERO("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    ADVANTAGE("AD"),
    GAME("GAME");

    fun next(): TennisScoreValues = when (this) {
        ZERO -> FIFTEEN
        FIFTEEN -> THIRTY
        THIRTY -> FORTY
        FORTY -> GAME
        ADVANTAGE -> GAME
        GAME -> GAME
    }
}
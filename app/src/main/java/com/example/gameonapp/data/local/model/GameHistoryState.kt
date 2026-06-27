package com.example.gameonapp.data.local.model

data class GameHistoryState(
    val gameList: List<GameEntity> = listOf(),
    val totalTime: Int = 0,
    val totalCalories: Int = 0
)
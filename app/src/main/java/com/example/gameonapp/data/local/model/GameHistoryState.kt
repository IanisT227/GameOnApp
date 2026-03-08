package com.example.gameonapp.data.local.model

data class GameHistoryState(
    val gameList: List<GameEntity>,
    val totalTime: Int
)
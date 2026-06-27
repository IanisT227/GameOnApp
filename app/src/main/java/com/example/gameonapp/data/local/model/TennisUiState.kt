package com.example.gameonapp.data.local.model

import com.example.gameonapp.utils.AWAY
import com.example.gameonapp.utils.HOME

data class TennisUiState(
    val matchState: TennisMatchState = TennisMatchState(
        homeScore = TennisScore(name = HOME),
        awayScore = TennisScore(name = AWAY),
        setsToWin = 2
    ),
    val history: List<TennisMatchState> = emptyList()
)
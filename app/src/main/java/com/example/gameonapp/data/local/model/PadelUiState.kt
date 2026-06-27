package com.example.gameonapp.data.local.model

import com.example.gameonapp.utils.AWAY
import com.example.gameonapp.utils.HOME

data class PadelUiState(
    val matchState: PadelMatchState = PadelMatchState(
        homeScore = PadelScore(name = HOME, display = "HOME"),
        awayScore = PadelScore(name = AWAY, display = "AWAY")
    ),
    val history: List<PadelMatchState> = emptyList()
)
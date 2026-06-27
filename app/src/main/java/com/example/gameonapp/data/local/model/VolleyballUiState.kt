package com.example.gameonapp.data.local.model

data class VolleyballUiState(
    val score: VolleyballScore = VolleyballScore(),
    val isFinished: Boolean = false
)
package com.example.gameonapp.data.local.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("padelMatch")
data class PadelMatchState(
    val homeScore: PadelScore,
    val awayScore: PadelScore,
    val completedSets: List<SetResult> = emptyList(),
    val servingIsHome: Boolean = true,
    val tiebreak: PadelTiebreakState? = null,   // non-null when tiebreak is active
    val advantageRounds: Int = 0,               // Tracks how many deuce cycles have occurred
    val isStarPoint: Boolean = false,
    val isFinished: Boolean = false
) : GameScore {
    val servePosition: PadelServePosition
        get() = if (tiebreak != null) {
            val total = tiebreak.homePoints + tiebreak.awayPoints
            if (total % 2 == 0) PadelServePosition.RIGHT else PadelServePosition.LEFT
        } else {
            val total = homeScore.points.ordinal + awayScore.points.ordinal
            if (total % 2 == 0) PadelServePosition.RIGHT else PadelServePosition.LEFT
        }

    fun getFinalScore(): String {
        val homeSetsWon = completedSets.count { it.homePlayerWon }
        val awaySetsWon = completedSets.count { !it.homePlayerWon }
        return "$homeSetsWon - $awaySetsWon"
    }

    fun getSetScores(): String {
        return completedSets.mapIndexed { _, set ->
            "$set"
        }.joinToString("\n")
    }
}

@Serializable
data class PadelTiebreakState(
    val homePoints: Int = 0,
    val awayPoints: Int = 0,
    val servePosition: PadelServePosition = PadelServePosition.RIGHT,
    val servingIsHome: Boolean = true
)
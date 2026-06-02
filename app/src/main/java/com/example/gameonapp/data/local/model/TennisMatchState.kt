package com.example.gameonapp.data.local.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("tennisMatch")
data class TennisMatchState(
    val homeScore: TennisScore,
    val awayScore: TennisScore,
    val completedSets: List<SetResult> = emptyList(),
    val currentSet: Int = 1,
    val matchWinner: Boolean? = null,       // HOME / AWAY / null while ongoing
    val setsToWin: Int = 2,
    val homeServesFirst: Boolean = true,
    /**
     * True once both sides reach 6 games in the current set.
     * While true, normal 15/30/40 scoring is replaced by raw tiebreak points.
     */
    val isTiebreak: Boolean = false,
    val homeTiebreakPoints: Int = 0,
    val awayTiebreakPoints: Int = 0
) : GameScore {
    fun side(isHome: Boolean): TennisScore =
        if (isHome == homeScore.name) homeScore else awayScore

    val servingIsHome: Boolean
        get() {
            val gamesInCompletedSets = completedSets.sumOf { it.homeGames + it.awayGames }
            val gamesInCurrentSet = homeScore.games + awayScore.games
            val totalGames = gamesInCompletedSets + gamesInCurrentSet
            return if (totalGames % 2 == 0) homeServesFirst else !homeServesFirst
        }

    fun isMatchFinished(): Boolean {
        val homeSetsWon = completedSets.count { it.homePlayerWon }
        val awaySetsWon = completedSets.count { !it.homePlayerWon }

        return homeSetsWon >= setsToWin || awaySetsWon >= setsToWin
    }

    fun getFinalScore(): String {
        val homeSetsWon = completedSets.count { it.homePlayerWon }
        val awaySetsWon = completedSets.count { !it.homePlayerWon }
        return "$homeSetsWon - $awaySetsWon"
    }

    fun getSetScores(): String {
        return completedSets.mapIndexed { index, set ->
            "$set"
        }.joinToString("\n")
    }
}
package com.example.gameonapp.data.local.model

data class TennisMatchState(
    val homeScore: TennisScore,
    val awayScore: TennisScore,
    val completedSets: List<SetResult> = emptyList(),
    val currentSet: Int = 1,
    val isDeuce: Boolean = false,
    val matchWinner: Boolean? = null,   // null while match is ongoing
    val setsToWin: Int = 2
) {
    val homeSetsWon: Int get() = completedSets.count { it.homePlayerWon }
    val awaySetsWon: Int get() = completedSets.count { !it.homePlayerWon }
    fun side(isHome: Boolean): TennisScore = if (isHome == homeScore.name) homeScore else awayScore

}
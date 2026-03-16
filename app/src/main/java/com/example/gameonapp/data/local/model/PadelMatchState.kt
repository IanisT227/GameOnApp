package com.example.gameonapp.data.local.model

data class PadelMatchState(
    val homeScore: PadelScore,
    val awayScore: PadelScore,
    val completedSets: List<SetResult> = emptyList(),
    val servingIsHome: Boolean = true,
    val tiebreak: PadelTiebreakState? = null,   // non-null when tiebreak is active
    val isGoldenPoint: Boolean = false,         // 40-40 reached, next point decides
    val isFinished: Boolean = false
)
{
    val servePosition: PadelServePosition
        get() = if (tiebreak != null) {
            val total = tiebreak.homePoints + tiebreak.awayPoints
            if (total % 2 == 0) PadelServePosition.RIGHT else PadelServePosition.LEFT
        } else {
            val total = homeScore.points.ordinal + awayScore.points.ordinal
            if (total % 2 == 0) PadelServePosition.RIGHT else PadelServePosition.LEFT
        }
}

data class PadelTiebreakState(
    val homePoints: Int = 0,
    val awayPoints: Int = 0,
    val servePosition: PadelServePosition = PadelServePosition.RIGHT,
    val servingIsHome: Boolean = true   // tracks who serves in tiebreak (changes every 2 points)
)
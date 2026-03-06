package com.example.gameonapp.data.local.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface GameScore

@Serializable
@SerialName("simple")
data class SimpleScore(
    val home: Int = 0,
    val away: Int = 0,
) : GameScore {
    override fun toString(): String {
        return "{$home} - {$away}"
    }
}

@Serializable
@SerialName("volleyball")
data class VolleyballScore(
    val scoresPerSet: List<VolleyballSet> = List(5) {
        VolleyballSet(pointsHome = 0, pointsAway = 0)
    },
    val currentSet: Int = 0,
) : GameScore {
    val targetPointsPerSet = listOf(25, 25, 25, 25, 15)
    val setsHome: Int
        get() = scoresPerSet.mapIndexed { index, set ->
            val target = targetPointsPerSet.getOrElse(index) { 25 }
            if (set.pointsHome > set.pointsAway && set.pointsHome >= target) 1 else 0
        }.sum()

    val setsAway: Int
        get() = scoresPerSet.mapIndexed { index, set ->
            val target = targetPointsPerSet.getOrElse(index) { 25 }
            if (set.pointsAway > set.pointsHome && set.pointsAway >= target) 1 else 0
        }.sum()

    fun getFinalScore(): String = "$setsHome - $setsAway"
    fun getSetScores(): String {
        val setScoresString =
            scoresPerSet.mapIndexed { index, set -> "${index + 1}. ${scoresPerSet[index].pointsHome} - ${scoresPerSet[index].pointsAway}" }
                .joinToString("\n")
        return setScoresString
    }
}

@Serializable
data class VolleyballSet(
    var pointsHome: Int = 0,
    var pointsAway: Int = 0,
)


@Serializable
@SerialName("tennis")
data class TennisScore(
    val name: Boolean,
    val points: TennisScoreValues = TennisScoreValues.ZERO,
    val games: Int = 0,
    val sets: List<Int> = emptyList()
) : GameScore

data class SetResult(
    val homeGames: Int,
    val awayGames: Int
) {
    val homePlayerWon: Boolean get() = homeGames > awayGames
    override fun toString(): String = "$homeGames – $awayGames"
}


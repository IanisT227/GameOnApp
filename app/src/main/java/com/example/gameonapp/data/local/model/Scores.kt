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
) : GameScore

@Serializable
@SerialName("volleyball")
data class VolleyballScore(
    val scoresPerSet: List<VolleyballSet> = List(5) {
        VolleyballSet(pointsHome = 0, pointsAway = 0)
    },
) : GameScore {
    val setsHome: Int get() = scoresPerSet.count { it.pointsHome > it.pointsAway }
    val setsAway: Int get() = scoresPerSet.count { it.pointsAway > it.pointsHome }
    val isMatchFinished: Boolean get() = setsHome == 3 || setsAway == 3
    var currentSet = 0
}

@Serializable
data class VolleyballSet(
    var pointsHome: Int = 0,
    var pointsAway: Int = 0,
)

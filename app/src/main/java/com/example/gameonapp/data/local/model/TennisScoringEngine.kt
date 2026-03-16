package com.example.gameonapp.data.local.model
import com.example.gameonapp.utils.AWAY
import com.example.gameonapp.utils.HOME
import kotlin.math.abs

object TennisScoringEngine {

    fun scorePoint(state: TennisMatchState, isHome: Boolean): TennisMatchState {
        val scorer = state.side(isHome)
        val other = state.side(!isHome)

        return when {
            state.isTiebreak -> scoreTiebreakPoint(state, isHome)

            scorer.points == RacquetSportsScoreValues.ADVANTAGE -> awardGame(state, isHome)

            other.points == RacquetSportsScoreValues.ADVANTAGE -> put(
                state, isHome,
                scorer.copy(points = RacquetSportsScoreValues.FORTY),
                other.copy(points = RacquetSportsScoreValues.FORTY)
            )

            scorer.points == RacquetSportsScoreValues.FORTY && other.points == RacquetSportsScoreValues.FORTY ->
                put(state, isHome, scorer.copy(points = RacquetSportsScoreValues.ADVANTAGE), other)

            scorer.points.next() == RacquetSportsScoreValues.GAME -> awardGame(state, isHome)

            else -> put(state, isHome, scorer.copy(points = scorer.points.next()), other)
        }
    }

    fun undoPoint(state: TennisMatchState, isHome: Boolean): TennisMatchState {
        if (state.isTiebreak) return undoTiebreakPoint(state, isHome)

        val scorer = state.side(isHome)
        val prev = when (scorer.points) {
            RacquetSportsScoreValues.ADVANTAGE -> RacquetSportsScoreValues.FORTY
            RacquetSportsScoreValues.FORTY -> RacquetSportsScoreValues.THIRTY
            RacquetSportsScoreValues.THIRTY -> RacquetSportsScoreValues.FIFTEEN
            else -> RacquetSportsScoreValues.ZERO
        }
        return put(state, isHome, scorer.copy(points = prev), state.side(!isHome))
    }

    fun isMatchFinished(state: TennisMatchState): Boolean = state.matchWinner != null

    private fun scoreTiebreakPoint(state: TennisMatchState, isHome: Boolean): TennisMatchState {
        val newHome = if (isHome) state.homeTiebreakPoints + 1 else state.homeTiebreakPoints
        val newAway = if (isHome) state.awayTiebreakPoints else state.awayTiebreakPoints + 1
        val tiebreakWon = (newHome >= 7 || newAway >= 7) && abs(newHome - newAway) >= 2

        return if (tiebreakWon) {
            val homeWonTiebreak = newHome > newAway
            closeSet(
                state.copy(homeTiebreakPoints = newHome, awayTiebreakPoints = newAway),
                homeGames = if (homeWonTiebreak) 7 else 6,
                awayGames = if (homeWonTiebreak) 6 else 7
            )
        } else {
            state.copy(homeTiebreakPoints = newHome, awayTiebreakPoints = newAway)
        }
    }

    private fun undoTiebreakPoint(state: TennisMatchState, isHome: Boolean): TennisMatchState {
        val newHome = if (isHome) maxOf(0, state.homeTiebreakPoints - 1) else state.homeTiebreakPoints
        val newAway = if (isHome) state.awayTiebreakPoints else maxOf(0, state.awayTiebreakPoints - 1)
        return state.copy(homeTiebreakPoints = newHome, awayTiebreakPoints = newAway)
    }

    private fun awardGame(state: TennisMatchState, isHome: Boolean): TennisMatchState {
        val scorer = state.side(isHome)
        val other = state.side(!isHome)
        val newHomeGames = if (isHome) scorer.games + 1 else other.games
        val newAwayGames = if (isHome) other.games else scorer.games + 1

        val afterGame = state.copy(
            homeScore = state.homeScore.copy(points = RacquetSportsScoreValues.ZERO, games = newHomeGames),
            awayScore = state.awayScore.copy(points = RacquetSportsScoreValues.ZERO, games = newAwayGames)
        )
        return checkSetWin(afterGame)
    }

    private fun checkSetWin(state: TennisMatchState): TennisMatchState {
        val h = state.homeScore.games
        val a = state.awayScore.games
        return when {
            h == 6 && a == 6 -> state.copy(isTiebreak = true)
            (h >= 6 && h - a >= 2) || (a >= 6 && a - h >= 2) -> closeSet(state, h, a)
            else -> state
        }
    }

    private fun closeSet(
        state: TennisMatchState,
        homeGames: Int,
        awayGames: Int
    ): TennisMatchState {
        val updatedSets = state.completedSets + SetResult(
            homeGames = homeGames,
            awayGames = awayGames
        )
        val afterSet = state.copy(
            homeScore = state.homeScore.copy(games = 0, points = RacquetSportsScoreValues.ZERO),
            awayScore = state.awayScore.copy(games = 0, points = RacquetSportsScoreValues.ZERO),
            completedSets = updatedSets,
            currentSet = state.currentSet + 1,
            isTiebreak = false,
            homeTiebreakPoints = 0,
            awayTiebreakPoints = 0
        )
        val homeSets = updatedSets.count { it.homePlayerWon }
        val awaySets = updatedSets.count { !it.homePlayerWon }
        return when {
            homeSets >= state.setsToWin -> afterSet.copy(matchWinner = HOME)
            awaySets >= state.setsToWin -> afterSet.copy(matchWinner = AWAY)
            else -> afterSet
        }
    }

    private fun put(
        state: TennisMatchState,
        isHome: Boolean,
        scorer: TennisScore,
        other: TennisScore
    ): TennisMatchState = if (isHome) {
        state.copy(homeScore = scorer, awayScore = other)
    } else {
        state.copy(homeScore = other, awayScore = scorer)
    }
}
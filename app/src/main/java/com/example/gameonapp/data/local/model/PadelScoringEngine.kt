package com.example.gameonapp.data.local.model

object PadelScoringEngine {
    private const val GAMES_TO_WIN_SET = 6
    private const val TIEBREAK_TARGET = 7
    private const val SETS_TO_WIN_MATCH = 2

    fun scorePoint(state: PadelMatchState, winner: PadelPointWinner): PadelMatchState {
        if (state.isFinished) return state
        if (state.tiebreak != null) return handleTiebreakPoint(state, winner)

        val homeWon = winner == PadelPointWinner.HOME
        val home = state.homeScore
        val away = state.awayScore

        // Golden point — next point wins the game outright
        if (state.isGoldenPoint) return handleGameWon(state, homeWon)

        val newHomePoints = if (homeWon) home.points.next() else home.points
        val newAwayPoints = if (!homeWon) away.points.next() else away.points

        // Both reach FORTY → golden point
        if (newHomePoints == RacquetSportsScoreValues.FORTY && newAwayPoints == RacquetSportsScoreValues.FORTY) {
            return state.copy(
                homeScore = home.copy(points = RacquetSportsScoreValues.FORTY),
                awayScore = away.copy(points = RacquetSportsScoreValues.FORTY),
                isGoldenPoint = true
            )
        }

        // Game won
        if (newHomePoints == RacquetSportsScoreValues.GAME) return handleGameWon(state, homeWon = true)
        if (newAwayPoints == RacquetSportsScoreValues.GAME) return handleGameWon(state, homeWon = false)

        return state.copy(
            homeScore = home.copy(points = newHomePoints),
            awayScore = away.copy(points = newAwayPoints)
        )
    }

    private fun handleGameWon(state: PadelMatchState, homeWon: Boolean): PadelMatchState {
        val home = state.homeScore
        val away = state.awayScore

        val newHomeGames = if (homeWon) home.games + 1 else home.games
        val newAwayGames = if (!homeWon) away.games + 1 else away.games

        // Check if tiebreak should start (6-6)
        if (newHomeGames == GAMES_TO_WIN_SET && newAwayGames == GAMES_TO_WIN_SET) {
            val (newServingIsHome, _) = rotateServe(
                state.servingIsHome, state.servePosition
            )
            return state.copy(
                homeScore = home.copy(points = RacquetSportsScoreValues.ZERO, games = newHomeGames),
                awayScore = away.copy(points = RacquetSportsScoreValues.ZERO, games = newAwayGames),
                isGoldenPoint = false,
                servingIsHome = newServingIsHome,
                tiebreak = PadelTiebreakState(
                    servingIsHome = newServingIsHome, servePosition = PadelServePosition.RIGHT
                )
            )
        }

        // Check if set is won
        val homeWinsSet = newHomeGames >= GAMES_TO_WIN_SET && newHomeGames - newAwayGames >= 2
        val awayWinsSet = newAwayGames >= GAMES_TO_WIN_SET && newAwayGames - newHomeGames >= 2

        if (homeWinsSet || awayWinsSet) {
            return handleSetWon(state, homeWon, newHomeGames, newAwayGames)
        }

        // Game over, rotate serve position
        val (newServingIsHome, _) = rotateServe(
            state.servingIsHome, state.servePosition
        )
        return state.copy(
            homeScore = home.copy(points = RacquetSportsScoreValues.ZERO, games = newHomeGames),
            awayScore = away.copy(points = RacquetSportsScoreValues.ZERO, games = newAwayGames),
            isGoldenPoint = false,
            servingIsHome = newServingIsHome,
        )
    }

    private fun handleSetWon(
        state: PadelMatchState, homeWon: Boolean, homeGames: Int, awayGames: Int
    ): PadelMatchState {
        val newSet = SetResult(
            homeGames = homeGames,
            awayGames = awayGames,
        )
        val updatedSets = state.completedSets + newSet

        val newHomeSets = if (homeWon) state.homeScore.setsWon + 1 else state.homeScore.setsWon
        val newAwaySets = if (!homeWon) state.awayScore.setsWon + 1 else state.awayScore.setsWon
        val matchOver = newHomeSets >= SETS_TO_WIN_MATCH || newAwaySets >= SETS_TO_WIN_MATCH

        val (newServingIsHome, _) = rotateServe(
            state.servingIsHome, state.servePosition
        )
        return state.copy(
            homeScore = state.homeScore.copy(
                points = RacquetSportsScoreValues.ZERO, games = 0, setsWon = newHomeSets
            ),
            awayScore = state.awayScore.copy(
                points = RacquetSportsScoreValues.ZERO, games = 0, setsWon = newAwaySets
            ),
            completedSets = updatedSets,
            isGoldenPoint = false,
            isFinished = matchOver,
            servingIsHome = newServingIsHome,
        )
    }

    private fun handleTiebreakPoint(
        state: PadelMatchState, winner: PadelPointWinner
    ): PadelMatchState {
        val tb = state.tiebreak!!
        val homeWon = winner == PadelPointWinner.HOME

        val newHomePoints = if (homeWon) tb.homePoints + 1 else tb.homePoints
        val newAwayPoints = if (!homeWon) tb.awayPoints + 1 else tb.awayPoints
        val totalPoints = newHomePoints + newAwayPoints

        // Tiebreak won: first to 7, win by 2
        val homeWinsTb = newHomePoints >= TIEBREAK_TARGET && newHomePoints - newAwayPoints >= 2
        val awayWinsTb = newAwayPoints >= TIEBREAK_TARGET && newAwayPoints - newHomePoints >= 2

        if (homeWinsTb || awayWinsTb) {
            return handleSetWon(
                state.copy(tiebreak = null),
                homeWon = homeWinsTb,
                homeGames = if (homeWinsTb) 7 else 6,
                awayGames = if (!homeWinsTb) 7 else 6
            )
        }

        // Rotate serve every 2 points in tiebreak (first point then every 2)
        val (newServingIsHome, newServePos) = if (totalPoints == 1 || totalPoints % 2 == 1) {
            rotateServe(tb.servingIsHome, tb.servePosition)
        } else {
            Pair(tb.servingIsHome, tb.servePosition)
        }

        return state.copy(
            tiebreak = tb.copy(
                homePoints = newHomePoints,
                awayPoints = newAwayPoints,
                servingIsHome = newServingIsHome,
                servePosition = newServePos
            )
        )
    }

    // Rotates serve: RIGHT → LEFT → next player RIGHT → ...
    private fun rotateServe(
        servingIsHome: Boolean, position: PadelServePosition
    ): Pair<Boolean, PadelServePosition> {
        return if (position == PadelServePosition.RIGHT) {
            Pair(servingIsHome, PadelServePosition.LEFT)
        } else {
            Pair(!servingIsHome, PadelServePosition.RIGHT)
        }
    }

    fun isMatchFinished(state: PadelMatchState): Boolean = state.isFinished
}
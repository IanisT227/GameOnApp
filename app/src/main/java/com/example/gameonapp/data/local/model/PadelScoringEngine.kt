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

        // Star Point logic: winner of this point takes the game immediately
        if (state.isStarPoint) return handleGameWon(state, homeWon)

        // Current points
        val currentHome = home.points
        val currentAway = away.points

        // Case: Someone currently has Advantage
        if (currentHome == RacquetSportsScoreValues.ADVANTAGE || currentAway == RacquetSportsScoreValues.ADVANTAGE) {
            val advantageForHome = currentHome == RacquetSportsScoreValues.ADVANTAGE

            return if ((advantageForHome && homeWon) || (!advantageForHome && !homeWon)) {
                // Team with advantage wins -> Win Game
                handleGameWon(state, homeWon)
            } else {
                // Team with advantage loses -> Return to 40-40
                val nextRounds = state.advantageRounds + 1
                state.copy(
                    homeScore = home.copy(points = RacquetSportsScoreValues.FORTY),
                    awayScore = away.copy(points = RacquetSportsScoreValues.FORTY),
                    advantageRounds = nextRounds,
                    isStarPoint = nextRounds >= 2 // After losing 2 advantages, next deuce is Star Point
                )
            }
        }

        // Standard scoring
        return when {
            // If they are at Deuce (40-40), winner gets Advantage
            currentHome == RacquetSportsScoreValues.FORTY && currentAway == RacquetSportsScoreValues.FORTY -> {
                state.copy(
                    homeScore = home.copy(points = if (homeWon) RacquetSportsScoreValues.ADVANTAGE else RacquetSportsScoreValues.FORTY),
                    awayScore = away.copy(points = if (!homeWon) RacquetSportsScoreValues.ADVANTAGE else RacquetSportsScoreValues.FORTY)
                )
            }

            // Winning from 40-0, 40-15, 40-30 (Next point is Game)
            homeWon && currentHome == RacquetSportsScoreValues.FORTY -> handleGameWon(state, true)
            !homeWon && currentAway == RacquetSportsScoreValues.FORTY -> handleGameWon(state, false)

            // Otherwise increment normally (e.g., 15 -> 30)
            else -> {
                state.copy(
                    homeScore = home.copy(points = if (homeWon) currentHome.next() else currentHome),
                    awayScore = away.copy(points = if (!homeWon) currentAway.next() else currentAway)
                )
            }
        }
    }

    private fun handleGameWon(state: PadelMatchState, homeWon: Boolean): PadelMatchState {
        val home = state.homeScore
        val away = state.awayScore

        val newHomeGames = if (homeWon) home.games + 1 else home.games
        val newAwayGames = if (!homeWon) away.games + 1 else away.games

        // 1. Check if tiebreak should start (6-6)
        if (newHomeGames == GAMES_TO_WIN_SET && newAwayGames == GAMES_TO_WIN_SET) {
            val (newServingIsHome, _) = rotateServe(
                state.servingIsHome, state.servePosition
            )
            return state.copy(
                homeScore = home.copy(points = RacquetSportsScoreValues.ZERO, games = newHomeGames),
                awayScore = away.copy(points = RacquetSportsScoreValues.ZERO, games = newAwayGames),
                advantageRounds = 0,  // Already correctly resetting here
                isStarPoint = false,
                servingIsHome = newServingIsHome,
                tiebreak = PadelTiebreakState(
                    servingIsHome = newServingIsHome, servePosition = PadelServePosition.RIGHT
                )
            )
        }

        // 2. Check if set is won
        val homeWinsSet = newHomeGames >= GAMES_TO_WIN_SET && newHomeGames - newAwayGames >= 2
        val awayWinsSet = newAwayGames >= GAMES_TO_WIN_SET && newAwayGames - newHomeGames >= 2

        if (homeWinsSet || awayWinsSet) {
            return handleSetWon(state, homeWon, newHomeGames, newAwayGames)
        }

        // 3. Normal Game over: MUST reset advantageRounds
        val (newServingIsHome, _) = rotateServe(
            state.servingIsHome, state.servePosition
        )
        return state.copy(
            homeScore = home.copy(points = RacquetSportsScoreValues.ZERO, games = newHomeGames),
            awayScore = away.copy(points = RacquetSportsScoreValues.ZERO, games = newAwayGames),
            advantageRounds = 0, // Fix: Reset for the next game
            isStarPoint = false,
            servingIsHome = newServingIsHome,
        )
    }

    private fun handleSetWon(
        state: PadelMatchState, homeWon: Boolean, homeGames: Int, awayGames: Int
    ): PadelMatchState {
        val newSet = SetResult(homeGames = homeGames, awayGames = awayGames)
        val updatedSets = state.completedSets + newSet

        val newHomeSets = if (homeWon) state.homeScore.setsWon + 1 else state.homeScore.setsWon
        val newAwaySets = if (!homeWon) state.awayScore.setsWon + 1 else state.awayScore.setsWon
        val matchOver = newHomeSets >= SETS_TO_WIN_MATCH || newAwaySets >= SETS_TO_WIN_MATCH

        val (newServingIsHome, _) = rotateServe(state.servingIsHome, state.servePosition)

        return state.copy(
            homeScore = state.homeScore.copy(
                points = RacquetSportsScoreValues.ZERO, games = 0, setsWon = newHomeSets
            ),
            awayScore = state.awayScore.copy(
                points = RacquetSportsScoreValues.ZERO, games = 0, setsWon = newAwaySets
            ),
            completedSets = updatedSets,
            advantageRounds = 0, // Fix: Reset for the next set
            isStarPoint = false,
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

}
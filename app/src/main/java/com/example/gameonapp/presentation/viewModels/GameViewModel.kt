package com.example.gameonapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameonapp.data.local.model.GameEntity
import com.example.gameonapp.data.local.model.GameHistoryState
import com.example.gameonapp.data.local.model.SetResult
import com.example.gameonapp.data.local.model.SimpleScore
import com.example.gameonapp.data.local.model.TennisMatchState
import com.example.gameonapp.data.local.model.TennisScore
import com.example.gameonapp.data.local.model.TennisScoreValues
import com.example.gameonapp.data.local.model.VolleyballScore
import com.example.gameonapp.data.local.model.VolleyballSet
import com.example.gameonapp.domain.repository.GameRepository
import com.example.gameonapp.utils.AWAY
import com.example.gameonapp.utils.DECREMENT
import com.example.gameonapp.utils.GameType
import com.example.gameonapp.utils.HOME
import com.example.gameonapp.utils.INCREMENT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import kotlin.math.abs

class GameViewModel(private val gameRepository: GameRepository) : ViewModel() {
    private val _simpleScores = MutableStateFlow(SimpleScore())
    val simpleScores: StateFlow<SimpleScore> = _simpleScores

    private val _volleyballScores = MutableStateFlow(VolleyballScore())
    val volleyballScores: StateFlow<VolleyballScore> = _volleyballScores

    private val _volleyballGameFinished = MutableStateFlow(false)
    val volleyballGameFinished: StateFlow<Boolean> = _volleyballGameFinished
    private val _gameData = MutableStateFlow(
        value = GameEntity()
    )
    val gameData: StateFlow<GameEntity> = _gameData
    //todo: REWORK GAME MODEL INTO STATES FOR EACH TYPE OF GAME

    private val _gameHistoryState = MutableStateFlow(
        value = GameHistoryState(
            gameList = emptyList(),
            totalTime = 0
        )
    )

    val gameHistoryState: StateFlow<GameHistoryState> = _gameHistoryState.asStateFlow()


    private val _tennisMatchState = MutableStateFlow(
        TennisMatchState(
            homeScore = TennisScore(name = HOME),
            awayScore = TennisScore(name = AWAY),
            setsToWin = 2
        )
    )
    val tennisMatchState: StateFlow<TennisMatchState> = _tennisMatchState.asStateFlow()

    fun insertGame(gameEntity: GameEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            gameRepository.insertGame(gameEntity)
        }
    }

    fun removeGame(gameEntity: GameEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            gameRepository.removeGame(gameEntity)
        }
    }

    fun removeGame(gameId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val gameEntity = gameRepository.getGameById(gameId) ?: GameEntity()
            gameRepository.removeGame(gameEntity)
        }
    }

    fun getGamesHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = gameRepository.getGamesHistory()
            val totalTime = gameRepository.getTotalTime().toInt()
            withContext(Dispatchers.Main) {
                _gameHistoryState.update {
                    GameHistoryState(
                        gameList = list,
                        totalTime = totalTime
                    )
                }
            }
        }
    }

    fun getGameById(gameId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val gameData = gameRepository.getGameById(gameId) ?: GameEntity()
            withContext(Dispatchers.Main) {
                _gameData.value = gameData
            }
        }
    }

    fun adjustFootballScore(team: Boolean, adjustType: Boolean) {
        val current = _simpleScores.value
        _simpleScores.value = when {
            team == HOME && adjustType == INCREMENT -> current.copy(home = current.home + 1)
            team == HOME && adjustType == DECREMENT -> current.copy(home = current.home - 1)
            team == AWAY && adjustType == INCREMENT -> current.copy(away = current.away + 1)
            team == AWAY && adjustType == DECREMENT -> current.copy(away = current.away - 1)
            else -> current
        }
    }

    fun adjustBasketballScore(team: Boolean, scoreAmount: Int) {
        val current = _simpleScores.value
        _simpleScores.value = when (team) {
            HOME -> current.copy(home = current.home + scoreAmount)
            AWAY -> current.copy(away = current.away + scoreAmount)
            else -> current
        }
    }

    fun adjustVolleyballScore(team: Boolean, scoreAmount: Int) {
        val current = _volleyballScores.value
        if (_volleyballGameFinished.value) return

        val updatedSets = current.scoresPerSet.mapIndexed { index, set ->
            if (index == current.currentSet) {
                if (team == HOME) {
                    set.copy(pointsHome = (set.pointsHome + scoreAmount).coerceAtLeast(0))
                } else {
                    set.copy(pointsAway = (set.pointsAway + scoreAmount).coerceAtLeast(0))
                }
            } else {
                set
            }
        }
        _volleyballScores.value = current.copy(scoresPerSet = updatedSets)
        checkVolleyballSetFinished()
    }

    private fun checkVolleyballSetFinished() {
        val current = _volleyballScores.value
        val currentSetIndex = current.currentSet
        val targetPointsPerSet = listOf(25, 25, 25, 25, 15)

        if (currentSetIndex >= current.scoresPerSet.size) return

        val currentSet = current.scoresPerSet[currentSetIndex]
        val diff = abs(currentSet.pointsHome - currentSet.pointsAway)
        val target = targetPointsPerSet.getOrElse(currentSetIndex) { 25 }

        // Check if the current set is finished
        if ((currentSet.pointsHome >= target || currentSet.pointsAway >= target) && diff >= 2) {

            // If either team has already won 3 sets, the match is finished
            if (current.setsHome >= 3 || current.setsAway >= 3) {
                _volleyballGameFinished.value = true
                return
            }

            val nextSetIndex = (currentSetIndex + 1).coerceAtMost(current.scoresPerSet.lastIndex)
            val updatedScoresPerSet = current.scoresPerSet.mapIndexed { index, set ->
                if (index == nextSetIndex) VolleyballSet(0, 0) else set
            }

            _volleyballScores.value = current.copy(
                currentSet = nextSetIndex,
                scoresPerSet = updatedScoresPerSet
            )
        }
    }

    fun resetScore() {
        _simpleScores.value = SimpleScore(0, 0)
    }

    fun buildEndGameEntity(
        durationSeconds: Int,
        averageBPM: Int,
        date: Date,
        gameType: GameType,
    ): GameEntity = GameEntity(
        gameType = gameType,
        score = SimpleScore(home = simpleScores.value.home, away = simpleScores.value.away),
        matchDate = date,
        durationSeconds = durationSeconds,
        averageBPM = averageBPM,
        gameId = 0L
    )

    fun buildVolleyballEndGameEntity(
        durationSeconds: Int,
        averageBPM: Int,
        date: Date,
        gameType: GameType,
    ): GameEntity = GameEntity(
        gameType = gameType,
        score = volleyballScores.value,
        matchDate = date,
        durationSeconds = durationSeconds,
        averageBPM = averageBPM,
        gameId = 0L
    )

    // ----- Tennis -----
    fun addPoint(isHome: Boolean) {
        if (_tennisMatchState.value.matchWinner != null) return
        _tennisMatchState.update { current ->
            if (current.isTiebreak) scoreTiebreakPoint(current, isHome)
            else scorePoint(current, isHome)
        }
    }

    fun removePoint(isHome: Boolean) {
        if (_tennisMatchState.value.matchWinner != null) return
        _tennisMatchState.update { current ->
            if (current.isTiebreak) undoTiebreakPoint(current, isHome)
            else undoPoint(current, isHome)
        }
    }

    private fun scorePoint(state: TennisMatchState, isHome: Boolean): TennisMatchState {
        val scorer = state.side(isHome)
        val other = state.side(!isHome)

        return when {
            scorer.points == TennisScoreValues.ADVANTAGE -> awardGame(state, isHome)

            other.points == TennisScoreValues.ADVANTAGE -> put(
                state, isHome,
                scorer.copy(points = TennisScoreValues.FORTY),
                other.copy(points = TennisScoreValues.FORTY)
            )

            // Deuce → scorer gets Advantage immediately (one tap)
            scorer.points == TennisScoreValues.FORTY && other.points == TennisScoreValues.FORTY ->
                put(state, isHome, scorer.copy(points = TennisScoreValues.ADVANTAGE), other)

            scorer.points.next() == TennisScoreValues.GAME -> awardGame(state, isHome)

            else -> put(state, isHome, scorer.copy(points = scorer.points.next()), other)
        }
    }

    private fun awardGame(state: TennisMatchState, isHome: Boolean): TennisMatchState {
        val scorer = state.side(isHome)
        val other = state.side(!isHome)

        val newHomeGames = if (isHome) scorer.games + 1 else other.games
        val newAwayGames = if (isHome) other.games else scorer.games + 1

        // Reset points for both sides
        val afterGame = state.copy(
            homeScore = state.homeScore.copy(
                points = TennisScoreValues.ZERO,
                games = newHomeGames
            ),
            awayScore = state.awayScore.copy(
                points = TennisScoreValues.ZERO,
                games = newAwayGames
            )
        )

        return checkSetWin(afterGame)
    }

    private fun checkSetWin(state: TennisMatchState): TennisMatchState {
        val h = state.homeScore.games
        val a = state.awayScore.games

        return when {
            // 6–6 → start tiebreak, don't close the set yet
            h == 6 && a == 6 -> state.copy(isTiebreak = true)

            // Normal set win: reach 6 with 2-game lead (covers 6–0 … 7–5)
            (h >= 6 && h - a >= 2) || (a >= 6 && a - h >= 2) -> closeSet(state, h, a)

            else -> state
        }
    }

    private fun scoreTiebreakPoint(state: TennisMatchState, isHome: Boolean): TennisMatchState {
        val newHome = if (isHome) state.homeTiebreakPoints + 1 else state.homeTiebreakPoints
        val newAway = if (isHome) state.awayTiebreakPoints else state.awayTiebreakPoints + 1

        val tiebreakWon = (newHome >= 7 || newAway >= 7) && abs(newHome - newAway) >= 2

        return if (tiebreakWon) {
            // Whoever reached the winning tiebreak point wins the set 7–6
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
        val newHome =
            if (isHome) maxOf(0, state.homeTiebreakPoints - 1) else state.homeTiebreakPoints
        val newAway =
            if (isHome) state.awayTiebreakPoints else maxOf(0, state.awayTiebreakPoints - 1)
        return state.copy(homeTiebreakPoints = newHome, awayTiebreakPoints = newAway)
    }

    // ── Close a completed set ─────────────────────────────────────────────────

    private fun closeSet(
        state: TennisMatchState,
        homeGames: Int,
        awayGames: Int
    ): TennisMatchState {
        val updatedSets =
            state.completedSets + SetResult(homeGames = homeGames, awayGames = awayGames)

        val afterSet = state.copy(
            homeScore = state.homeScore.copy(games = 0, points = TennisScoreValues.ZERO),
            awayScore = state.awayScore.copy(games = 0, points = TennisScoreValues.ZERO),
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

    // ── Undo normal point ─────────────────────────────────────────────────────

    private fun undoPoint(state: TennisMatchState, isHome: Boolean): TennisMatchState {
        val scorer = state.side(isHome)
        val prev = when (scorer.points) {
            TennisScoreValues.ADVANTAGE -> TennisScoreValues.FORTY
            TennisScoreValues.FORTY -> TennisScoreValues.THIRTY
            TennisScoreValues.THIRTY -> TennisScoreValues.FIFTEEN
            else -> TennisScoreValues.ZERO
        }
        return put(state, isHome, scorer.copy(points = prev), state.side(!isHome))
    }

    // ── Helper ────────────────────────────────────────────────────────────────

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

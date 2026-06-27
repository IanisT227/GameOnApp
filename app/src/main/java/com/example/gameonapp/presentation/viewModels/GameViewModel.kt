package com.example.gameonapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameonapp.data.local.model.GameEntity
import com.example.gameonapp.data.local.model.GameHistoryState
import com.example.gameonapp.data.local.model.GameScore
import com.example.gameonapp.data.local.model.PadelMatchState
import com.example.gameonapp.data.local.model.PadelPointWinner
import com.example.gameonapp.data.local.model.PadelScore
import com.example.gameonapp.data.local.model.PadelScoringEngine
import com.example.gameonapp.data.local.model.SimpleScore
import com.example.gameonapp.data.local.model.TennisMatchState
import com.example.gameonapp.data.local.model.TennisScore
import com.example.gameonapp.data.local.model.TennisScoringEngine
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

    // region States

    // --- General / History ---
    private val _gameHistoryState = MutableStateFlow(GameHistoryState())
    val gameHistoryState: StateFlow<GameHistoryState> = _gameHistoryState.asStateFlow()

    private val _gameData = MutableStateFlow(GameEntity())
    val gameData: StateFlow<GameEntity> = _gameData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // --- Football ---
    private val _footballScore = MutableStateFlow(SimpleScore())
    val footballScore: StateFlow<SimpleScore> = _footballScore.asStateFlow()

    // --- Basketball ---
    private val _basketballScore = MutableStateFlow(SimpleScore())
    val basketballScore: StateFlow<SimpleScore> = _basketballScore.asStateFlow()

    // --- Volleyball ---
    data class VolleyballUiState(
        val score: VolleyballScore = VolleyballScore(),
        val isFinished: Boolean = false
    )

    private val _volleyballState = MutableStateFlow(VolleyballUiState())
    val volleyballState: StateFlow<VolleyballUiState> = _volleyballState.asStateFlow()

    // --- Tennis ---
    data class TennisUiState(
        val matchState: TennisMatchState = TennisMatchState(
            homeScore = TennisScore(name = HOME),
            awayScore = TennisScore(name = AWAY),
            setsToWin = 2
        ),
        val history: List<TennisMatchState> = emptyList()
    )

    private val _tennisState = MutableStateFlow(TennisUiState())
    val tennisState: StateFlow<TennisUiState> = _tennisState.asStateFlow()

    // --- Padel ---
    data class PadelUiState(
        val matchState: PadelMatchState = PadelMatchState(
            homeScore = PadelScore(name = HOME, display = "HOME"),
            awayScore = PadelScore(name = AWAY, display = "AWAY")
        ),
        val history: List<PadelMatchState> = emptyList()
    )

    private val _padelState = MutableStateFlow(PadelUiState())
    val padelState: StateFlow<PadelUiState> = _padelState.asStateFlow()

    // endregion

    // region Repository Operations

    fun insertGame(gameEntity: GameEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            gameRepository.insertGame(gameEntity)
        }
    }

    fun removeGame(gameId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            gameRepository.getGameById(gameId)?.let {
                gameRepository.removeGame(it)
            }
        }
    }

    fun getGamesHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val list = gameRepository.getGamesHistory()
            val totalTime = gameRepository.getTotalTime().toInt()
            val totalCalories = gameRepository.getTotalCalories()
            withContext(Dispatchers.Main) {
                _gameHistoryState.update {
                    it.copy(gameList = list, totalTime = totalTime, totalCalories = totalCalories)
                }
            }
            _isLoading.value = false
        }
    }

    fun getGameById(gameId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = gameRepository.getGameById(gameId) ?: GameEntity()
            withContext(Dispatchers.Main) {
                _gameData.value = data
            }
        }
    }

    // endregion

    // region Football Actions

    fun adjustFootballScore(team: Boolean, adjustType: Boolean) {
        _footballScore.update { current ->
            when {
                team == HOME && adjustType == INCREMENT -> current.copy(home = current.home + 1)
                team == HOME && adjustType == DECREMENT -> current.copy(
                    home = (current.home - 1).coerceAtLeast(
                        0
                    )
                )

                team == AWAY && adjustType == INCREMENT -> current.copy(away = current.away + 1)
                team == AWAY && adjustType == DECREMENT -> current.copy(
                    away = (current.away - 1).coerceAtLeast(
                        0
                    )
                )

                else -> current
            }
        }
    }

    // endregion

    // region Basketball Actions

    fun adjustBasketballScore(team: Boolean, scoreAmount: Int) {
        _basketballScore.update { current ->
            if (team == HOME) {
                current.copy(home = (current.home + scoreAmount).coerceAtLeast(0))
            } else {
                current.copy(away = (current.away + scoreAmount).coerceAtLeast(0))
            }
        }
    }

    // endregion

    // region Volleyball Actions

    fun adjustVolleyballScore(team: Boolean, scoreAmount: Int) {
        if (_volleyballState.value.isFinished) return

        _volleyballState.update { currentState ->
            val score = currentState.score
            val updatedSets = score.scoresPerSet.mapIndexed { index, set ->
                if (index == score.currentSet) {
                    if (team == HOME) {
                        set.copy(pointsHome = (set.pointsHome + scoreAmount).coerceAtLeast(0))
                    } else {
                        set.copy(pointsAway = (set.pointsAway + scoreAmount).coerceAtLeast(0))
                    }
                } else {
                    set
                }
            }
            val updatedScore = score.copy(scoresPerSet = updatedSets)
            checkVolleyballSetFinished(updatedScore)
        }
    }

    private fun checkVolleyballSetFinished(current: VolleyballScore): VolleyballUiState {
        val currentSetIndex = current.currentSet
        val targetPointsPerSet = listOf(25, 25, 25, 25, 15)

        if (currentSetIndex >= current.scoresPerSet.size) {
            return VolleyballUiState(current, isFinished = true)
        }

        val currentSet = current.scoresPerSet[currentSetIndex]
        val diff = abs(currentSet.pointsHome - currentSet.pointsAway)
        val target = targetPointsPerSet.getOrElse(currentSetIndex) { 25 }

        if ((currentSet.pointsHome >= target || currentSet.pointsAway >= target) && diff >= 2) {
            // Check if match finished
            if (current.setsHome >= 3 || current.setsAway >= 3) {
                return VolleyballUiState(current, isFinished = true)
            }

            val nextSetIndex = (currentSetIndex + 1).coerceAtMost(current.scoresPerSet.lastIndex)
            val updatedScoresPerSet = current.scoresPerSet.mapIndexed { index, set ->
                if (index == nextSetIndex) VolleyballSet(0, 0) else set
            }

            val nextScore = current.copy(
                currentSet = nextSetIndex,
                scoresPerSet = updatedScoresPerSet
            )

            return VolleyballUiState(
                nextScore,
                isFinished = nextScore.setsHome >= 3 || nextScore.setsAway >= 3
            )
        }
        return VolleyballUiState(current, isFinished = false)
    }

    // endregion

    // region Tennis Actions

    fun addTennisPoint(isHome: Boolean) {
        if (_tennisState.value.matchState.matchWinner != null) return
        _tennisState.update { currentState ->
            val nextMatchState = TennisScoringEngine.scorePoint(currentState.matchState, isHome)
            currentState.copy(
                matchState = nextMatchState,
                history = currentState.history + currentState.matchState
            )
        }
    }

    fun undoTennisPoint() {
        _tennisState.update { currentState ->
            if (currentState.history.isEmpty()) currentState
            else {
                val previousState = currentState.history.last()
                currentState.copy(
                    matchState = previousState,
                    history = currentState.history.dropLast(1)
                )
            }
        }
    }

    // endregion

    // region Padel Actions

    fun onPadelPoint(winner: PadelPointWinner) {
        if (_padelState.value.matchState.isFinished) return
        _padelState.update { currentState ->
            val nextMatchState = PadelScoringEngine.scorePoint(currentState.matchState, winner)
            currentState.copy(
                matchState = nextMatchState,
                history = currentState.history + currentState.matchState
            )
        }
    }

    fun undoPadelPoint() {
        _padelState.update { currentState ->
            if (currentState.history.isEmpty()) currentState
            else {
                val previousState = currentState.history.last()
                currentState.copy(
                    matchState = previousState,
                    history = currentState.history.dropLast(1)
                )
            }
        }
    }

    // endregion

    // region End Game Entity Builders

    fun buildEndGameEntity(
        durationSeconds: Int,
        averageBPM: Int,
        maxBPM: Int,
        calories: Int,
        date: Date,
        gameType: GameType,
    ): GameEntity {
        val score: GameScore = when (gameType) {
            GameType.FOOTBALL -> _footballScore.value
            GameType.BASKETBALL -> _basketballScore.value
            GameType.VOLLEYBALL -> _volleyballState.value.score
            GameType.TENNIS -> _tennisState.value.matchState
            GameType.PADEL -> _padelState.value.matchState
            else -> SimpleScore()
        }

        return GameEntity(
            gameType = gameType,
            score = score,
            matchDate = date,
            durationSeconds = durationSeconds,
            averageBPM = averageBPM,
            maxBPM = maxBPM,
            calories = calories,
            gameId = 0L
        )
    }

    // endregion

    fun resetAllScores() {
        _footballScore.value = SimpleScore()
        _basketballScore.value = SimpleScore()
        _volleyballState.value = VolleyballUiState()
        _tennisState.value = TennisUiState()
        _padelState.value = PadelUiState()
    }
}
package com.example.gameonapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameonapp.data.local.model.GameEntity
import com.example.gameonapp.data.local.model.GameHistoryState
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

    private val _tennisHistory = ArrayDeque<TennisMatchState>()

    private val _padelState = MutableStateFlow(
        PadelMatchState(
            homeScore = PadelScore(name = HOME, display = "HOME"),
            awayScore = PadelScore(name = AWAY, display = "AWAY")
        )
    )
    val padelState: StateFlow<PadelMatchState> = _padelState.asStateFlow()

    private val _padelHistory = ArrayDeque<PadelMatchState>()



    fun onPadelPoint(winner: PadelPointWinner) {
        _padelHistory.addLast(_padelState.value)  // snapshot before every change
        _padelState.update { PadelScoringEngine.scorePoint(it, winner) }
    }

    fun undoPadelPoint() {
        if (_padelHistory.isEmpty()) return
        _padelState.value = _padelHistory.removeLast()
    }

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

    fun addPoint(isHome: Boolean) {
        if (_tennisMatchState.value.matchWinner != null) return
        _tennisHistory.addLast(_tennisMatchState.value)
        _tennisMatchState.update { TennisScoringEngine.scorePoint(it, isHome) }
    }

    fun removePoint(isHome: Boolean) {
        if (_tennisHistory.isEmpty()) return
        _tennisMatchState.value = _tennisHistory.removeLast()
    }

    // ── Helper ────────────────────────────────────────────────────────────────

}

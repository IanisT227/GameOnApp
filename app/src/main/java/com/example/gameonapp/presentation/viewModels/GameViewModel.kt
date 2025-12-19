package com.example.gameonapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameonapp.data.local.model.GameEntity
import com.example.gameonapp.data.local.model.SimpleScore
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import kotlin.math.abs

class GameViewModel(private val gameRepository: GameRepository) : ViewModel() {
    private val _gameList = MutableStateFlow<List<GameEntity>>(emptyList())
    val gameList: StateFlow<List<GameEntity>> = _gameList
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

    private val _totalTime = MutableStateFlow(0)
    val totalTime: StateFlow<Int> = _totalTime


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
            withContext(Dispatchers.Main) {
                _gameList.value = list
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

    fun getTotalTime() {
        viewModelScope.launch(Dispatchers.IO) {
            val totalTime = gameRepository.getTotalTime().toInt()
            withContext(Dispatchers.Main) {
                _totalTime.value = totalTime
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
        val current = _volleyballScores.value ?: return
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
}
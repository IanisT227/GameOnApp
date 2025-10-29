package com.example.gameonapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameonapp.data.local.model.GameEntity
import com.example.gameonapp.data.local.model.Score
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

class GameViewModel(private val gameRepository: GameRepository) : ViewModel() {
    private val _gameList = MutableStateFlow<List<GameEntity>>(emptyList())
    val gameList: StateFlow<List<GameEntity>> = _gameList
    private val _scores = MutableStateFlow(Score())
    val scores: StateFlow<Score> = _scores

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

    fun getGamesHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = gameRepository.getGamesHistory()
            withContext(Dispatchers.Main) {
                _gameList.value = list
            }
        }
    }

    fun adjustScore(team: Boolean, adjustType: Boolean) {
        val current = _scores.value

        _scores.value = when {
            team == HOME && adjustType == INCREMENT -> current.copy(home = current.home + 1)
            team == HOME && adjustType == DECREMENT -> current.copy(home = current.home - 1)
            team == AWAY && adjustType == INCREMENT -> current.copy(away = current.away + 1)
            team == AWAY && adjustType == DECREMENT -> current.copy(away = current.away - 1)
            else -> current
        }
    }

    fun resetScore() {
        _scores.value = Score(0, 0)
    }

    fun buildEndGameEntity(
        durationSeconds: Int,
        averageBPM: Int,
        date: Date,
        gameType: GameType,
    ): GameEntity = GameEntity(
        gameType = gameType,
        scoreA = scores.value.home,
        scoreB = scores.value.away,
        matchDate = date,
        durationSeconds = durationSeconds,
        averageBPM = averageBPM,
        gameId = 0L
    )
}
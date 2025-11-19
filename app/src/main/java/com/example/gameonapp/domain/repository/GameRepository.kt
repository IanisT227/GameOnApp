package com.example.gameonapp.domain.repository

import com.example.gameonapp.data.local.dao.GameDao
import com.example.gameonapp.data.local.model.GameEntity

class GameRepository(private val gameDao: GameDao) {
    suspend fun insertGame(game: GameEntity) {
        gameDao.insertGame(game)
    }

    suspend fun removeGame(game: GameEntity) {
        gameDao.removeFromGames(game)
    }

    suspend fun getGamesHistory(): List<GameEntity> = gameDao.getGames()

    suspend fun getGameById(gameId: Long) = gameDao.getGameById(gameId = gameId)
}
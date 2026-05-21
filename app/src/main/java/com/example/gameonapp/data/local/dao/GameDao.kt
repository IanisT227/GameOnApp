package com.example.gameonapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gameonapp.data.local.model.GameEntity

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameEntity)

    @Query("SELECT * FROM games ORDER BY matchDate DESC")
    suspend fun getGames(): List<GameEntity>

    @Delete
    suspend fun removeFromGames(game: GameEntity)

    @Query("SELECT * FROM games WHERE gameId = :gameId LIMIT 1")
    suspend fun getGameById(gameId: Long): GameEntity?

    @Query("SELECT COALESCE(SUM(durationSeconds), 0) FROM games")
    suspend fun getTotalTime(): Long
}
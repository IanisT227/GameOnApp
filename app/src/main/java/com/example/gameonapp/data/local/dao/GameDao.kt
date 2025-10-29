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

    @Query("SELECT * FROM games ")
    suspend fun getGames(): List<GameEntity>

    @Delete()
    suspend fun removeFromGames(game: GameEntity)
}
package com.example.gameonapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gameonapp.utils.GameType
import java.util.Date

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true) val gameId: Long,
    val gameType: GameType,
    val scoreA: Int,
    val scoreB: Int,
    val matchDate: Date,
    val durationSeconds: Int,
    val averageBPM: Int,
)

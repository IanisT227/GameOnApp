package com.example.gameonapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gameonapp.utils.GameType
import java.util.Date

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true) val gameId: Long = 0,
    val gameType: GameType = GameType.OTHER,
    val scoreA: Int = 0,
    val scoreB: Int = 0,
    val matchDate: Date = Date(),
    val durationSeconds: Int = 0,
    val averageBPM: Int = 0,
)

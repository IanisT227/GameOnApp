package com.example.gameonapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gameonapp.data.local.converter.DateConverter
import com.example.gameonapp.data.local.converter.GameScoreConverter
import com.example.gameonapp.data.local.converter.GameTypeConverter
import com.example.gameonapp.data.local.dao.GameDao
import com.example.gameonapp.data.local.model.GameEntity

@Database(
    entities = [GameEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(GameTypeConverter::class, DateConverter::class, GameScoreConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}
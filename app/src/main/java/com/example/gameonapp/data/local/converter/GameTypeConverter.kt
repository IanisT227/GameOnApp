package com.example.gameonapp.data.local.converter

import android.util.Log
import androidx.room.TypeConverter
import com.example.gameonapp.utils.GameType

class GameTypeConverter {
    @TypeConverter
    fun fromGameType(type: GameType): String = type.name

    @TypeConverter
    fun toGameType(value: String): GameType = try {
        GameType.valueOf(value)
    } catch (e: Exception) {
        Log.e(TAG, e.message.toString())
        GameType.OTHER
    }
}

const val TAG = "GameTypeConverter"
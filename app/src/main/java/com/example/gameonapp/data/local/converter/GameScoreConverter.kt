package com.example.gameonapp.data.local.converter

import androidx.room.TypeConverter
import com.example.gameonapp.data.local.model.GameScore
import com.example.gameonapp.data.local.model.SimpleScore
import com.example.gameonapp.data.local.model.TennisScore
import com.example.gameonapp.data.local.model.VolleyballScore
import com.google.gson.Gson
import com.google.gson.JsonParser


class GameScoreConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromGameScore(score: GameScore?): String {
        if (score == null) return ""
        val json = gson.toJsonTree(score).asJsonObject
        // Add a type field
        when (score) {
            is SimpleScore -> json.addProperty("type", "simple")
            is VolleyballScore -> json.addProperty("type", "volleyball")
            is TennisScore -> json.addProperty("type", "tennis")
        }
        return gson.toJson(json)
    }

    @TypeConverter
    fun toGameScore(jsonString: String?): GameScore? {
        if (jsonString.isNullOrBlank()) return null
        val jsonElement = JsonParser.parseString(jsonString).asJsonObject
        val type = jsonElement.get("type")?.asString ?: return null

        return when (type) {
            "simple" -> gson.fromJson(jsonString, SimpleScore::class.java)
            "volleyball" -> gson.fromJson(jsonString, VolleyballScore::class.java)
            else -> null
        }
    }
}
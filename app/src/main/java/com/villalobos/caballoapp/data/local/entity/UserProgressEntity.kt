package com.villalobos.caballoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

/**
 * Entidad Room para almacenar el progreso del usuario.
 * Incluye estadísticas de quiz, XP, nivel, racha, y logros.
 */
@Entity(tableName = "user_progress")
@TypeConverters(UserProgressConverters::class)
data class UserProgressEntity(
    @PrimaryKey
    val id: Int = 1, // Single user, always ID 1
    val totalQuizzes: Int = 0,
    val bestScore: Int = 0,
    val musclesStudied: Int = 0,
    val studyStreak: Int = 0,
    val perfectQuizzes: Int = 0,
    val fastestQuizTime: Long = Long.MAX_VALUE,
    val totalXp: Int = 0,
    val level: Int = 1,
    val lastStudyDate: Long = 0L,
    val regionScores: Map<Int, Int> = emptyMap(),
    val unlockedAchievements: Set<String> = emptySet(),
    val studiedMuscleIds: Set<Int> = emptySet()
)

/**
 * Type converters para UserProgressEntity.
 */
class UserProgressConverters {
    @TypeConverter
    fun fromIntMap(map: Map<Int, Int>): String {
        return map.entries.joinToString(",") { "${it.key}:${it.value}" }
    }

    @TypeConverter
    fun toIntMap(value: String): Map<Int, Int> {
        if (value.isEmpty()) return emptyMap()
        return value.split(",").mapNotNull { entry ->
            val parts = entry.split(":")
            if (parts.size == 2) {
                parts[0].toIntOrNull()?.let { key ->
                    parts[1].toIntOrNull()?.let { value ->
                        key to value
                    }
                }
            } else null
        }.toMap()
    }

    @TypeConverter
    fun fromStringSet(set: Set<String>): String {
        return set.joinToString(",")
    }

    @TypeConverter
    fun toStringSet(value: String): Set<String> {
        return if (value.isEmpty()) emptySet() else value.split(",").toSet()
    }

    @TypeConverter
    fun fromIntSet(set: Set<Int>): String {
        return set.joinToString(",")
    }

    @TypeConverter
    fun toIntSet(value: String): Set<Int> {
        if (value.isEmpty()) return emptySet()
        return value.split(",").mapNotNull { it.toIntOrNull() }.toSet()
    }
}

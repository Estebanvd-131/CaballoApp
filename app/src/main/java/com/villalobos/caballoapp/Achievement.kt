package com.villalobos.caballoapp

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconRes: Int,
    val requirement: AchievementRequirement,
    val points: Int = 10,
    val rarity: Rarity = Rarity.COMMON
)

enum class Rarity {
    COMMON, RARE, EPIC, LEGENDARY
}

sealed class AchievementRequirement {
    data class QuizScore(val minScore: Int, val regionId: Int? = null) : AchievementRequirement()
    data class QuizCount(val count: Int) : AchievementRequirement()
    data class MuscleStudied(val count: Int) : AchievementRequirement()
    data class Streak(val days: Int) : AchievementRequirement()
    data class PerfectQuiz(val regionId: Int) : AchievementRequirement()
    data class SpeedQuiz(val timeLimit: Long) : AchievementRequirement()
}
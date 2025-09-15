package com.villalobos.caballoapp

object AchievementData {

    val achievements = listOf(
        // Logros básicos de estudio
        Achievement(
            id = "first_quiz",
            title = "Primer Paso",
            description = "Completa tu primer quiz",
            iconRes = android.R.drawable.star_on,
            requirement = AchievementRequirement.QuizCount(1),
            points = 10,
            rarity = Rarity.COMMON
        ),

        Achievement(
            id = "quiz_master",
            title = "Maestro del Quiz",
            description = "Completa 10 quizzes",
            iconRes = android.R.drawable.ic_menu_sort_by_size,
            requirement = AchievementRequirement.QuizCount(10),
            points = 50,
            rarity = Rarity.RARE
        ),

        Achievement(
            id = "perfect_score",
            title = "Puntuación Perfecta",
            description = "Obtén 100% en cualquier quiz",
            iconRes = android.R.drawable.checkbox_on_background,
            requirement = AchievementRequirement.QuizScore(100),
            points = 25,
            rarity = Rarity.RARE
        ),

        // Logros por región
        Achievement(
            id = "head_expert",
            title = "Experto en Cabeza",
            description = "Completa todos los quizzes de cabeza con 80% o más",
            iconRes = android.R.drawable.ic_menu_camera,
            requirement = AchievementRequirement.QuizScore(80, 1),
            points = 30,
            rarity = Rarity.RARE
        ),

        Achievement(
            id = "neck_specialist",
            title = "Especialista en Cuello",
            description = "Completa todos los quizzes de cuello con 80% o más",
            iconRes = android.R.drawable.ic_menu_call,
            requirement = AchievementRequirement.QuizScore(80, 2),
            points = 30,
            rarity = Rarity.RARE
        ),

        Achievement(
            id = "trunk_master",
            title = "Maestro del Tronco",
            description = "Completa todos los quizzes de tronco con 80% o más",
            iconRes = android.R.drawable.ic_menu_compass,
            requirement = AchievementRequirement.QuizScore(80, 3),
            points = 30,
            rarity = Rarity.RARE
        ),

        Achievement(
            id = "thoracic_ace",
            title = "As en Miembros Torácicos",
            description = "Completa todos los quizzes de miembros torácicos con 80% o más",
            iconRes = android.R.drawable.ic_menu_directions,
            requirement = AchievementRequirement.QuizScore(80, 4),
            points = 30,
            rarity = Rarity.RARE
        ),

        Achievement(
            id = "pelvic_champion",
            title = "Campeón Pélvico",
            description = "Completa todos los quizzes de miembros pélvicos con 80% o más",
            iconRes = android.R.drawable.ic_menu_gallery,
            requirement = AchievementRequirement.QuizScore(80, 5),
            points = 30,
            rarity = Rarity.RARE
        ),

        // Logros avanzados
        Achievement(
            id = "speed_demon",
            title = "Demonio de la Velocidad",
            description = "Completa un quiz en menos de 2 minutos",
            iconRes = android.R.drawable.ic_menu_recent_history,
            requirement = AchievementRequirement.SpeedQuiz(120000), // 2 minutos en ms
            points = 40,
            rarity = Rarity.EPIC
        ),

        Achievement(
            id = "study_streak",
            title = "Racha de Estudio",
            description = "Estudia durante 7 días consecutivos",
            iconRes = android.R.drawable.ic_menu_today,
            requirement = AchievementRequirement.Streak(7),
            points = 60,
            rarity = Rarity.EPIC
        ),

        Achievement(
            id = "muscle_encyclopedia",
            title = "Enciclopedia Muscular",
            description = "Estudia 50 músculos diferentes",
            iconRes = android.R.drawable.ic_menu_search,
            requirement = AchievementRequirement.MuscleStudied(50),
            points = 75,
            rarity = Rarity.EPIC
        ),

        Achievement(
            id = "grand_master",
            title = "Gran Maestro",
            description = "Obtén puntuación perfecta en todas las regiones",
            iconRes = android.R.drawable.ic_menu_manage,
            requirement = AchievementRequirement.PerfectQuiz(0), // Todas las regiones
            points = 100,
            rarity = Rarity.LEGENDARY
        ),

        // Logros divertidos
        Achievement(
            id = "early_bird",
            title = "Madrugador",
            description = "Completa un quiz antes de las 6 AM",
            iconRes = android.R.drawable.ic_menu_rotate,
            requirement = AchievementRequirement.QuizCount(1), // Se verifica por hora
            points = 15,
            rarity = Rarity.COMMON
        ),

        Achievement(
            id = "night_owl",
            title = "Búho Nocturno",
            description = "Completa un quiz después de las 10 PM",
            iconRes = android.R.drawable.ic_menu_more,
            requirement = AchievementRequirement.QuizCount(1), // Se verifica por hora
            points = 15,
            rarity = Rarity.COMMON
        )
    )

    // Función para obtener logros por rareza
    fun getAchievementsByRarity(rarity: Rarity): List<Achievement> {
        return achievements.filter { it.rarity == rarity }
    }

    // Función para obtener logros desbloqueados (simulación)
    fun getUnlockedAchievements(userStats: UserStats): List<Achievement> {
        return achievements.filter { achievement ->
            when (achievement.requirement) {
                is AchievementRequirement.QuizCount -> {
                    userStats.totalQuizzes >= (achievement.requirement as AchievementRequirement.QuizCount).count
                }
                is AchievementRequirement.QuizScore -> {
                    val req = achievement.requirement as AchievementRequirement.QuizScore
                    val regionId = req.regionId
                    if (regionId != null) {
                        userStats.getRegionScore(regionId) >= req.minScore
                    } else {
                        userStats.bestScore >= req.minScore
                    }
                }
                is AchievementRequirement.MuscleStudied -> {
                    userStats.musclesStudied >= (achievement.requirement as AchievementRequirement.MuscleStudied).count
                }
                is AchievementRequirement.Streak -> {
                    userStats.studyStreak >= (achievement.requirement as AchievementRequirement.Streak).days
                }
                is AchievementRequirement.PerfectQuiz -> {
                    userStats.perfectQuizzes >= 1 // Simplificado
                }
                is AchievementRequirement.SpeedQuiz -> {
                    userStats.fastestQuizTime <= (achievement.requirement as AchievementRequirement.SpeedQuiz).timeLimit
                }
            }
        }
    }

    // Función para obtener logros bloqueados
    fun getLockedAchievements(userStats: UserStats): List<Achievement> {
        return achievements.filter { achievement ->
            !getUnlockedAchievements(userStats).contains(achievement)
        }
    }
}

// Clase auxiliar para estadísticas del usuario (simplificada)
data class UserStats(
    val totalQuizzes: Int = 0,
    val bestScore: Int = 0,
    val musclesStudied: Int = 0,
    val studyStreak: Int = 0,
    val perfectQuizzes: Int = 0,
    val fastestQuizTime: Long = Long.MAX_VALUE,
    val regionScores: Map<Int, Int> = emptyMap()
) {
    fun getRegionScore(regionId: Int): Int {
        return regionScores[regionId] ?: 0
    }
}
package com.villalobos.caballoapp.data.repository

import android.content.Context
import com.villalobos.caballoapp.data.local.dao.QuizQuestionDao
import com.villalobos.caballoapp.data.local.dao.UserProgressDao
import com.villalobos.caballoapp.data.local.entity.UserProgressEntity
import com.villalobos.caballoapp.data.model.QuizQuestion
import com.villalobos.caballoapp.data.model.RegionIds
import com.villalobos.caballoapp.data.source.QuizData
import com.villalobos.caballoapp.data.source.UserStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository para manejar datos del Quiz.
 * Usa Room Database con fallback a SharedPreferences para compatibilidad.
 */
@Singleton
class QuizRepository @Inject constructor(
    private val context: Context,
    private val quizQuestionDao: QuizQuestionDao,
    private val userProgressDao: UserProgressDao
) {

    companion object {
        private const val PREFS_NAME = "quiz_stats"
        private const val KEY_TOTAL_QUIZZES = "total_quizzes"
        private const val KEY_BEST_SCORE = "best_score"
        private const val KEY_PERFECT_QUIZZES = "perfect_quizzes"
        private const val KEY_FASTEST_TIME = "fastest_quiz_time"
        private const val KEY_REGION_SCORE_PREFIX = "region_score_"
        
        // XP Constants
        private const val XP_PER_CORRECT_ANSWER = 10
        private const val XP_BONUS_PERFECT_QUIZ = 50
        private const val XP_BONUS_FAST_QUIZ = 25
        private const val XP_PER_LEVEL = 100
    }

    private val prefs by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // ============ Preguntas (Room) ============

    /**
     * Obtiene preguntas aleatorias para un quiz usando Room.
     */
    suspend fun getQuizQuestionsAsync(regionId: Int? = null, count: Int = 5): List<QuizQuestion> {
        val normalizedRegionId = regionId?.let { RegionIds.normalize(it) }

        val entities = if (normalizedRegionId != null) {
            val primary = quizQuestionDao.getRandomQuestionsByRegion(normalizedRegionId, count)
            if (primary.isNotEmpty()) {
                primary
            } else if (normalizedRegionId == RegionIds.REGION_DISTAL) {
                quizQuestionDao.getRandomQuestionsByRegion(RegionIds.REGION_DISTAL_LEGACY, count)
            } else {
                emptyList()
            }
        } else {
            quizQuestionDao.getRandomQuestions(count)
        }
        
        // Fallback a hardcoded si Room está vacío
        return if (entities.isNotEmpty()) {
            entities.map { it.toModel() }
        } else {
            QuizData.getQuizQuestions(normalizedRegionId, count)
        }
    }

    /**
     * Obtiene todas las preguntas como Flow (reactivo).
     */
    fun getAllQuestionsFlow(): Flow<List<QuizQuestion>> {
        return quizQuestionDao.getAllQuestions().map { entities ->
            entities.map { it.toModel() }
        }
    }

    /**
     * Obtiene preguntas por región como Flow (reactivo).
     */
    fun getQuestionsByRegionFlow(regionId: Int): Flow<List<QuizQuestion>> {
        val normalizedRegionId = RegionIds.normalize(regionId)
        return quizQuestionDao.getQuestionsByRegion(normalizedRegionId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    // ============ Preguntas (Compatibilidad) ============

    /**
     * Obtiene preguntas aleatorias para un quiz (fallback a hardcoded).
     */
    fun getQuizQuestions(regionId: Int? = null, count: Int = 5): List<QuizQuestion> {
        return QuizData.getQuizQuestions(regionId?.let { RegionIds.normalize(it) }, count)
    }

    /**
     * Obtiene preguntas de una región específica.
     */
    fun getQuestionsByRegion(regionId: Int): List<QuizQuestion> {
        return QuizData.getQuestionsByRegion(RegionIds.normalize(regionId))
    }

    /**
     * Obtiene una pregunta aleatoria.
     */
    fun getRandomQuestion(regionId: Int? = null): QuizQuestion {
        return QuizData.getRandomQuestion(regionId?.let { RegionIds.normalize(it) })
    }

    // ============ Progreso del Usuario (Room) ============

    /**
     * Obtiene el progreso del usuario como Flow (reactivo).
     */
    fun getUserProgressFlow(): Flow<UserProgressEntity?> {
        return userProgressDao.getUserProgress()
    }

    /**
     * Guarda el resultado de un quiz completado usando Room.
     */
    suspend fun saveQuizResultAsync(
        score: Int,
        correctAnswers: Int,
        regionId: Int?,
        timeSpent: Long
    ) {
        val normalizedRegionId = regionId?.let { RegionIds.normalize(it) }

        // Obtener progreso actual o crear nuevo
        var progress = userProgressDao.getUserProgressSync() ?: UserProgressEntity()
        
        // Calcular XP ganado
        var xpGained = correctAnswers * XP_PER_CORRECT_ANSWER
        if (score == 100) xpGained += XP_BONUS_PERFECT_QUIZ
        if (timeSpent < 120000) xpGained += XP_BONUS_FAST_QUIZ // Bonus si < 2 min
        
        // Actualizar progreso
        val newTotalXp = progress.totalXp + xpGained
        val newLevel = (newTotalXp / XP_PER_LEVEL) + 1
        
        // Actualizar región scores
        val newRegionScores = progress.regionScores.toMutableMap()
        normalizedRegionId?.let { id ->
            val currentScore = newRegionScores[id] ?: 0
            if (score > currentScore) {
                newRegionScores[id] = score
            }
        }
        
        // Actualizar progreso completo
        progress = progress.copy(
            totalQuizzes = progress.totalQuizzes + 1,
            bestScore = maxOf(progress.bestScore, score),
            perfectQuizzes = if (score == 100) progress.perfectQuizzes + 1 else progress.perfectQuizzes,
            fastestQuizTime = minOf(progress.fastestQuizTime, timeSpent),
            totalXp = newTotalXp,
            level = newLevel,
            regionScores = newRegionScores
        )
        
        userProgressDao.insertOrUpdate(progress)
        
        // También guardar en SharedPreferences para compatibilidad
        saveQuizResult(score, normalizedRegionId, timeSpent)
    }

    /**
     * Obtiene estadísticas del usuario priorizando Room y usando SharedPreferences como fallback.
     */
    suspend fun getUserStatsAsync(): UserStats {
        val progress = userProgressDao.getUserProgressSync()
        return if (progress != null) {
            UserStats(
                totalQuizzes = progress.totalQuizzes,
                bestScore = progress.bestScore,
                musclesStudied = progress.musclesStudied,
                studyStreak = progress.studyStreak,
                perfectQuizzes = progress.perfectQuizzes,
                fastestQuizTime = progress.fastestQuizTime,
                regionScores = progress.regionScores
            )
        } else {
            getUserStats()
        }
    }

    /**
     * Obtiene XP total del usuario.
     */
    suspend fun getTotalXpAsync(): Int {
        return userProgressDao.getTotalXp() ?: 0
    }

    /**
     * Obtiene nivel actual del usuario.
     */
    suspend fun getLevelAsync(): Int {
        return userProgressDao.getLevel() ?: 1
    }

    /**
     * Obtiene racha de estudio.
     */
    suspend fun getStreakAsync(): Int {
        return userProgressDao.getStreak() ?: 0
    }

    /**
     * Actualiza la racha de estudio.
     */
    suspend fun updateStreakAsync(streak: Int, date: Long) {
        userProgressDao.updateStreak(streak, date)
    }

    /**
     * Registra un músculo como estudiado.
     */
    suspend fun markMuscleAsStudiedAsync(muscleId: Int) {
        val progress = userProgressDao.getUserProgressSync() ?: UserProgressEntity()
        val newStudiedMuscles = progress.studiedMuscleIds.toMutableSet().apply {
            add(muscleId)
        }
        userProgressDao.insertOrUpdate(
            progress.copy(
                studiedMuscleIds = newStudiedMuscles,
                musclesStudied = newStudiedMuscles.size
            )
        )
    }

    /**
     * Desbloquea un logro.
     */
    suspend fun unlockAchievementAsync(achievementId: String) {
        val progress = userProgressDao.getUserProgressSync() ?: UserProgressEntity()
        val newAchievements = progress.unlockedAchievements.toMutableSet().apply {
            add(achievementId)
        }
        userProgressDao.insertOrUpdate(
            progress.copy(unlockedAchievements = newAchievements)
        )
    }

    /**
     * Obtiene logros desbloqueados.
     */
    suspend fun getUnlockedAchievementsAsync(): Set<String> {
        return userProgressDao.getUserProgressSync()?.unlockedAchievements ?: emptySet()
    }

    // ============ Estadísticas (SharedPreferences - Compatibilidad) ============

    /**
     * Guarda el resultado de un quiz completado.
     */
    fun saveQuizResult(
        score: Int,
        regionId: Int?,
        timeSpent: Long
    ) {
        val normalizedRegionId = regionId?.let { RegionIds.normalize(it) }

        prefs.edit().apply {
            val totalQuizzes = prefs.getInt(KEY_TOTAL_QUIZZES, 0) + 1
            putInt(KEY_TOTAL_QUIZZES, totalQuizzes)

            val bestScore = maxOf(prefs.getInt(KEY_BEST_SCORE, 0), score)
            putInt(KEY_BEST_SCORE, bestScore)

            normalizedRegionId?.let { id ->
                val regionScore = prefs.getInt("$KEY_REGION_SCORE_PREFIX$id", 0)
                if (score > regionScore) {
                    putInt("$KEY_REGION_SCORE_PREFIX$id", score)
                }
            }

            if (score == 100) {
                val perfectCount = prefs.getInt(KEY_PERFECT_QUIZZES, 0) + 1
                putInt(KEY_PERFECT_QUIZZES, perfectCount)
            }

            val fastestTime = prefs.getLong(KEY_FASTEST_TIME, Long.MAX_VALUE)
            if (timeSpent < fastestTime) {
                putLong(KEY_FASTEST_TIME, timeSpent)
            }

            apply()
        }
    }

    /**
     * Obtiene las estadísticas del usuario.
     */
    fun getUserStats(): UserStats {
        val regionScores = mutableMapOf<Int, Int>()
        for (regionId in RegionIds.canonicalIds) {
            val score = prefs.getInt("$KEY_REGION_SCORE_PREFIX$regionId", 0)
            if (score > 0) {
                regionScores[regionId] = score
            }
        }

        val legacyDistalScore = prefs.getInt("$KEY_REGION_SCORE_PREFIX${RegionIds.REGION_DISTAL_LEGACY}", 0)
        if (legacyDistalScore > 0) {
            val currentDistalScore = regionScores[RegionIds.REGION_DISTAL] ?: 0
            regionScores[RegionIds.REGION_DISTAL] = maxOf(currentDistalScore, legacyDistalScore)
        }

        return UserStats(
            totalQuizzes = prefs.getInt(KEY_TOTAL_QUIZZES, 0),
            bestScore = prefs.getInt(KEY_BEST_SCORE, 0),
            musclesStudied = 0,
            studyStreak = 0,
            perfectQuizzes = prefs.getInt(KEY_PERFECT_QUIZZES, 0),
            fastestQuizTime = prefs.getLong(KEY_FASTEST_TIME, Long.MAX_VALUE),
            regionScores = regionScores
        )
    }

    /**
     * Obtiene el mejor puntaje de una región específica.
     */
    fun getRegionBestScore(regionId: Int): Int {
        return prefs.getInt("$KEY_REGION_SCORE_PREFIX$regionId", 0)
    }

    /**
     * Reinicia todas las estadísticas del usuario.
     */
    fun resetStats() {
        prefs.edit().clear().apply()
    }

    /**
     * Reinicia todo el progreso en Room.
     */
    suspend fun resetProgressAsync() {
        userProgressDao.deleteAll()
        userProgressDao.insertOrUpdate(UserProgressEntity())
        resetStats()
    }
}


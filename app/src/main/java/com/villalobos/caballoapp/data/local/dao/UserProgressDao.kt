package com.villalobos.caballoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.villalobos.caballoapp.data.local.entity.UserProgressEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de base de datos relacionadas con el progreso del usuario.
 */
@Dao
interface UserProgressDao {
    
    /**
     * Obtiene el progreso del usuario actual.
     */
    @Query("SELECT * FROM user_progress WHERE id = 1")
    fun getUserProgress(): Flow<UserProgressEntity?>
    
    /**
     * Obtiene el progreso del usuario de forma síncrona.
     */
    @Query("SELECT * FROM user_progress WHERE id = 1")
    suspend fun getUserProgressSync(): UserProgressEntity?
    
    /**
     * Inserta o reemplaza el progreso del usuario.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: UserProgressEntity)
    
    /**
     * Actualiza el progreso del usuario.
     */
    @Update
    suspend fun update(progress: UserProgressEntity)
    
    /**
     * Actualiza el total de quizzes completados.
     */
    @Query("UPDATE user_progress SET totalQuizzes = totalQuizzes + 1 WHERE id = 1")
    suspend fun incrementQuizCount()
    
    /**
     * Actualiza el mejor puntaje si es mayor.
     */
    @Query("UPDATE user_progress SET bestScore = :score WHERE id = 1 AND :score > bestScore")
    suspend fun updateBestScoreIfBetter(score: Int)
    
    /**
     * Actualiza el XP total.
     */
    @Query("UPDATE user_progress SET totalXp = totalXp + :xp WHERE id = 1")
    suspend fun addXp(xp: Int)
    
    /**
     * Actualiza el nivel.
     */
    @Query("UPDATE user_progress SET level = :level WHERE id = 1")
    suspend fun updateLevel(level: Int)
    
    /**
     * Actualiza la racha de estudio.
     */
    @Query("UPDATE user_progress SET studyStreak = :streak, lastStudyDate = :date WHERE id = 1")
    suspend fun updateStreak(streak: Int, date: Long)
    
    /**
     * Incrementa los quizzes perfectos.
     */
    @Query("UPDATE user_progress SET perfectQuizzes = perfectQuizzes + 1 WHERE id = 1")
    suspend fun incrementPerfectQuizzes()
    
    /**
     * Actualiza el tiempo más rápido si es menor.
     */
    @Query("UPDATE user_progress SET fastestQuizTime = :time WHERE id = 1 AND :time < fastestQuizTime")
    suspend fun updateFastestTimeIfBetter(time: Long)
    
    /**
     * Obtiene el XP total.
     */
    @Query("SELECT totalXp FROM user_progress WHERE id = 1")
    suspend fun getTotalXp(): Int?
    
    /**
     * Obtiene el nivel actual.
     */
    @Query("SELECT level FROM user_progress WHERE id = 1")
    suspend fun getLevel(): Int?
    
    /**
     * Obtiene la racha de estudio.
     */
    @Query("SELECT studyStreak FROM user_progress WHERE id = 1")
    suspend fun getStreak(): Int?
    
    /**
     * Elimina el progreso del usuario (reset).
     */
    @Query("DELETE FROM user_progress")
    suspend fun deleteAll()
}

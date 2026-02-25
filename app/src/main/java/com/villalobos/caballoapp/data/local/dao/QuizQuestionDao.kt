package com.villalobos.caballoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.villalobos.caballoapp.data.local.entity.QuizQuestionEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de base de datos relacionadas con preguntas de quiz.
 */
@Dao
interface QuizQuestionDao {
    
    /**
     * Obtiene todas las preguntas.
     */
    @Query("SELECT * FROM quiz_questions")
    fun getAllQuestions(): Flow<List<QuizQuestionEntity>>
    
    /**
     * Obtiene todas las preguntas de forma síncrona.
     */
    @Query("SELECT * FROM quiz_questions")
    suspend fun getAllQuestionsSync(): List<QuizQuestionEntity>
    
    /**
     * Obtiene preguntas por región.
     */
    @Query("SELECT * FROM quiz_questions WHERE regionId = :regionId")
    fun getQuestionsByRegion(regionId: Int): Flow<List<QuizQuestionEntity>>
    
    /**
     * Obtiene preguntas por región de forma síncrona.
     */
    @Query("SELECT * FROM quiz_questions WHERE regionId = :regionId")
    suspend fun getQuestionsByRegionSync(regionId: Int): List<QuizQuestionEntity>
    
    /**
     * Obtiene N preguntas aleatorias.
     */
    @Query("SELECT * FROM quiz_questions ORDER BY RANDOM() LIMIT :count")
    suspend fun getRandomQuestions(count: Int): List<QuizQuestionEntity>
    
    /**
     * Obtiene N preguntas aleatorias de una región.
     */
    @Query("SELECT * FROM quiz_questions WHERE regionId = :regionId ORDER BY RANDOM() LIMIT :count")
    suspend fun getRandomQuestionsByRegion(regionId: Int, count: Int): List<QuizQuestionEntity>
    
    /**
     * Obtiene una pregunta por ID.
     */
    @Query("SELECT * FROM quiz_questions WHERE id = :id")
    suspend fun getQuestionById(id: Int): QuizQuestionEntity?
    
    /**
     * Cuenta el total de preguntas.
     */
    @Query("SELECT COUNT(*) FROM quiz_questions")
    suspend fun getQuestionCount(): Int
    
    /**
     * Cuenta preguntas por región.
     */
    @Query("SELECT COUNT(*) FROM quiz_questions WHERE regionId = :regionId")
    suspend fun getQuestionCountByRegion(regionId: Int): Int
    
    /**
     * Inserta múltiples preguntas.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<QuizQuestionEntity>)
    
    /**
     * Inserta una pregunta.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: QuizQuestionEntity)
    
    /**
     * Elimina todas las preguntas.
     */
    @Query("DELETE FROM quiz_questions")
    suspend fun deleteAll()
}

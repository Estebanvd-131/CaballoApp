package com.villalobos.caballoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.villalobos.caballoapp.data.model.Difficulty
import com.villalobos.caballoapp.data.model.QuestionType
import com.villalobos.caballoapp.data.model.QuizQuestion

/**
 * Entidad Room para almacenar preguntas de quiz en la base de datos.
 */
@Entity(tableName = "quiz_questions")
@TypeConverters(QuizConverters::class)
data class QuizQuestionEntity(
    @PrimaryKey
    val id: Int,
    val regionId: Int,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int,
    val explanation: String,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val questionType: QuestionType = QuestionType.MULTIPLE_CHOICE
) {
    /**
     * Convierte la entidad a modelo de dominio.
     */
    fun toModel(): QuizQuestion = QuizQuestion(
        id = id,
        regionId = regionId,
        question = question,
        options = options,
        correctAnswer = correctAnswer,
        explanation = explanation,
        difficulty = difficulty,
        questionType = questionType
    )

    companion object {
        /**
         * Crea una entidad desde el modelo de dominio.
         */
        fun fromModel(question: QuizQuestion): QuizQuestionEntity = QuizQuestionEntity(
            id = question.id,
            regionId = question.regionId,
            question = question.question,
            options = question.options,
            correctAnswer = question.correctAnswer,
            explanation = question.explanation,
            difficulty = question.difficulty,
            questionType = question.questionType
        )
    }
}

/**
 * Type converters para Room.
 */
class QuizConverters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString("|||")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split("|||")
    }

    @TypeConverter
    fun fromDifficulty(difficulty: Difficulty): String {
        return difficulty.name
    }

    @TypeConverter
    fun toDifficulty(value: String): Difficulty {
        return try {
            Difficulty.valueOf(value)
        } catch (e: Exception) {
            Difficulty.MEDIUM
        }
    }

    @TypeConverter
    fun fromQuestionType(type: QuestionType): String {
        return type.name
    }

    @TypeConverter
    fun toQuestionType(value: String): QuestionType {
        return try {
            QuestionType.valueOf(value)
        } catch (e: Exception) {
            QuestionType.MULTIPLE_CHOICE
        }
    }
}

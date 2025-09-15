package com.villalobos.caballoapp

data class QuizQuestion(
    val id: Int,
    val regionId: Int,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int, // Index in options list (0-3)
    val explanation: String,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val questionType: QuestionType = QuestionType.MULTIPLE_CHOICE
)

enum class Difficulty {
    EASY, MEDIUM, HARD
}

enum class QuestionType {
    MULTIPLE_CHOICE, TRUE_FALSE, IDENTIFICATION
}
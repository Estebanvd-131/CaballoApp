package com.villalobos.caballoapp.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.villalobos.caballoapp.data.local.AppDatabase
import com.villalobos.caballoapp.data.local.entity.QuizQuestionEntity
import com.villalobos.caballoapp.data.model.Difficulty
import com.villalobos.caballoapp.data.model.QuestionType
import com.villalobos.caballoapp.data.model.RegionIds
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuizRepositoryIntegrationTest {

    private lateinit var context: Context
    private lateinit var database: AppDatabase
    private lateinit var repository: QuizRepository

    @Before
    fun setup() = runBlocking {
        context = ApplicationProvider.getApplicationContext()
        context.getSharedPreferences("quiz_stats", Context.MODE_PRIVATE).edit().clear().commit()

        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repository = QuizRepository(
            context = context,
            quizQuestionDao = database.quizQuestionDao(),
            userProgressDao = database.userProgressDao()
        )

        database.quizQuestionDao().insertAll(
            listOf(
                QuizQuestionEntity(
                    id = 7001,
                    regionId = RegionIds.REGION_DISTAL_LEGACY,
                    question = "Pregunta legacy distal",
                    options = listOf("A", "B", "C", "D"),
                    correctAnswer = 0,
                    explanation = "exp",
                    difficulty = Difficulty.EASY,
                    questionType = QuestionType.MULTIPLE_CHOICE
                )
            )
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getQuizQuestionsAsync_handlesLegacyDistalRegionId() = runBlocking {
        val questions = repository.getQuizQuestionsAsync(RegionIds.REGION_DISTAL, 1)

        assertTrue(questions.isNotEmpty())
        assertEquals(RegionIds.REGION_DISTAL_LEGACY, questions.first().regionId)
    }

    @Test
    fun saveQuizResultAsync_updatesRoomProgress() = runBlocking {
        repository.saveQuizResultAsync(
            score = 80,
            correctAnswers = 4,
            regionId = RegionIds.REGION_DISTAL,
            timeSpent = 90_000L
        )

        val progress = database.userProgressDao().getUserProgressSync()
        checkNotNull(progress)

        assertEquals(1, progress.totalQuizzes)
        assertEquals(80, progress.bestScore)
        assertEquals(1, progress.level)
        assertEquals(80, progress.regionScores[RegionIds.REGION_DISTAL])
    }
}

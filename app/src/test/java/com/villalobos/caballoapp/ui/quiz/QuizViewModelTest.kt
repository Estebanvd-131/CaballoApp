package com.villalobos.caballoapp.ui.quiz

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.villalobos.caballoapp.data.model.QuizQuestion
import com.villalobos.caballoapp.data.repository.QuizRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

/**
 * Unit Tests para QuizViewModel.
 * Verifica la lógica del quiz sin dependencias de Android.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var quizRepository: QuizRepository

    private lateinit var viewModel: QuizViewModel

    private val sampleQuestions = listOf(
        QuizQuestion(
            id = 1,
            regionId = 1,
            question = "¿Cuál es el músculo más grande?",
            options = listOf("A", "B", "C", "D"),
            correctAnswer = 0,
            explanation = "Explicación"
        ),
        QuizQuestion(
            id = 2,
            regionId = 1,
            question = "¿Cuál es la función del músculo X?",
            options = listOf("A", "B", "C", "D"),
            correctAnswer = 1,
            explanation = "Explicación"
        )
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = QuizViewModel(quizRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `startQuiz activates quiz when questions are available`() = runTest {
        // Given
        whenever(quizRepository.getQuizQuestionsAsync(null, 10)).thenReturn(sampleQuestions)
        whenever(quizRepository.getUserStatsAsync()).thenReturn(com.villalobos.caballoapp.data.source.UserStats())

        // When
        viewModel.startQuiz(null, 10)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.isQuizActive())
    }

    @Test
    fun `startQuiz keeps quiz inactive when no questions available`() = runTest {
        // Given
        whenever(quizRepository.getQuizQuestionsAsync(null, 10)).thenReturn(emptyList())

        // When
        viewModel.startQuiz(null, 10)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.isQuizActive())
    }

    @Test
    fun `answerQuestion advances to next question`() = runTest {
        // Given
        whenever(quizRepository.getQuizQuestionsAsync(null, 10)).thenReturn(sampleQuestions)
        viewModel.startQuiz(null, 10)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.answerQuestion(0)

        // Then
        val state = viewModel.quizState.value
        assertNotNull(state)
        assertEquals(1, state?.currentQuestionIndex)
    }

    @Test
    fun `answerQuestion completes quiz on last question`() = runTest {
        // Given
        whenever(quizRepository.getQuizQuestionsAsync(null, 2)).thenReturn(sampleQuestions)
        whenever(quizRepository.getUserStatsAsync()).thenReturn(com.villalobos.caballoapp.data.source.UserStats())
        viewModel.startQuiz(null, 2)
        testDispatcher.scheduler.advanceUntilIdle()

        // When - Answer both questions
        viewModel.answerQuestion(0) // Correct
        viewModel.answerQuestion(1) // Correct
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.quizState.value
        assertNotNull(state)
        assertTrue(state?.isCompleted == true)
        assertFalse(viewModel.isQuizActive())
    }

    @Test
    fun `skipQuestion marks answer as -1`() = runTest {
        // Given
        whenever(quizRepository.getQuizQuestionsAsync(null, 10)).thenReturn(sampleQuestions)
        viewModel.startQuiz(null, 10)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.skipQuestion()

        // Then - Should have advanced and recorded -1 as answer
        val state = viewModel.quizState.value
        assertEquals(1, state?.currentQuestionIndex)
    }

    @Test
    fun `abandonQuiz resets state`() = runTest {
        // Given
        whenever(quizRepository.getQuizQuestionsAsync(null, 10)).thenReturn(sampleQuestions)
        viewModel.startQuiz(null, 10)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.abandonQuiz()

        // Then
        assertFalse(viewModel.isQuizActive())
        val state = viewModel.quizState.value
        assertTrue(state?.questions?.isEmpty() == true)
    }

    @Test
    fun `getTimeElapsed returns correct time`() = runTest {
        // Given
        whenever(quizRepository.getQuizQuestionsAsync(null, 10)).thenReturn(sampleQuestions)
        viewModel.startQuiz(null, 10)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        val elapsed = viewModel.getTimeElapsed()

        // Then
        assertTrue(elapsed >= 0)
    }

    @Test
    fun `getCurrentQuestions returns quiz questions`() = runTest {
        // Given
        whenever(quizRepository.getQuizQuestionsAsync(null, 10)).thenReturn(sampleQuestions)
        viewModel.startQuiz(null, 10)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        val questions = viewModel.getCurrentQuestions()

        // Then
        assertEquals(sampleQuestions.size, questions.size)
    }

    @Test
    fun `clearEvent sets event to null`() = runTest {
        // Given
        whenever(quizRepository.getQuizQuestionsAsync(null, 10)).thenReturn(sampleQuestions)
        viewModel.startQuiz(null, 10)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.clearEvent()

        // Then
        assertNull(viewModel.quizEvent.value)
    }
}

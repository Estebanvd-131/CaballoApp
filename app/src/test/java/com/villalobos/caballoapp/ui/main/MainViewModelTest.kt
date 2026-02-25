package com.villalobos.caballoapp.ui.main

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.villalobos.caballoapp.data.local.entity.UserProgressEntity
import com.villalobos.caballoapp.data.repository.QuizRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    @Mock
    private lateinit var quizRepository: QuizRepository

    private lateinit var viewModel: MainViewModel

    // Mock Flow for user progress
    private val userProgressFlow = MutableStateFlow<UserProgressEntity?>(null)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        // Mock SharedPreferences behavior
        whenever(sharedPreferences.edit()).thenReturn(sharedPreferencesEditor)
        whenever(sharedPreferencesEditor.putBoolean(org.mockito.kotlin.any(), org.mockito.kotlin.any())).thenReturn(sharedPreferencesEditor)

        // Mock Repository Flow
        whenever(quizRepository.getUserProgressFlow()).thenReturn(userProgressFlow)

        viewModel = MainViewModel(sharedPreferences, quizRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state uses default values when no progress exists`() = runTest {
        // Given flow emits null (initially)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then defaults
        assertEquals(1, viewModel.userLevel.value)
        assertEquals(0, viewModel.userXp.value)
        assertEquals(0, viewModel.userStreak.value)
    }

    @Test
    fun `updates state when user progress changes`() = runTest {
        // Given
        val progress = UserProgressEntity(
            level = 5,
            totalXp = 500,
            studyStreak = 10
        )

        // When
        userProgressFlow.emit(progress)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(5, viewModel.userLevel.value)
        assertEquals(500, viewModel.userXp.value)
        assertEquals(10, viewModel.userStreak.value)
    }

    @Test
    fun `navigation events trigger correctly`() {
        viewModel.navigateToRegionMenu()
        assertEquals(MainViewModel.MainEvent.NavigateToRegionMenu, viewModel.event.value)

        viewModel.navigateToAccessibility()
        assertEquals(MainViewModel.MainEvent.NavigateToAccessibility, viewModel.event.value)
        
        viewModel.clearEvent()
        assertNull(viewModel.event.value)
    }
}

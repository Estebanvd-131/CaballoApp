package com.villalobos.caballoapp.ui.accessibility

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.villalobos.caballoapp.data.model.AccessibilityConfig
import com.villalobos.caballoapp.data.model.ColorblindType
import com.villalobos.caballoapp.data.model.TextScale
import com.villalobos.caballoapp.data.repository.AccessibilityRepository
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AccessibilityViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: AccessibilityRepository

    private lateinit var viewModel: AccessibilityViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        whenever(repository.getAccessibilityConfig()).thenReturn(
            AccessibilityConfig(
                colorblindType = ColorblindType.NORMAL,
                highContrast = false,
                textScale = TextScale.NORMAL
            )
        )
        whenever(repository.getColorblindTypeName(any())).thenReturn("Modo")
        whenever(repository.getColorblindTypeDescription(any())).thenReturn("Descripción")

        viewModel = AccessibilityViewModel(repository)
    }

    @Test
    fun `shouldShowDisableButton is false for NORMAL`() {
        viewModel.setColorblindType(ColorblindType.NORMAL)

        assertFalse(viewModel.shouldShowDisableButton())
    }

    @Test
    fun `shouldShowDisableButton is true for PROTANOPIA`() {
        viewModel.setColorblindType(ColorblindType.PROTANOPIA)

        assertTrue(viewModel.shouldShowDisableButton())
    }

    @Test
    fun `saveConfig persists current accessibility state`() {
        viewModel.setColorblindType(ColorblindType.TRITANOPIA)
        viewModel.setHighContrast(true)
        viewModel.setLargeText(true)

        viewModel.saveConfig()

        verify(repository).saveAccessibilityConfig(
            AccessibilityConfig(
                colorblindType = ColorblindType.TRITANOPIA,
                highContrast = true,
                textScale = TextScale.LARGE
            )
        )
    }
}

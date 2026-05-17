package com.villalobos.caballoapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.villalobos.caballoapp.data.model.AccessibilityConfig
import com.villalobos.caballoapp.data.model.ColorblindType
import com.villalobos.caballoapp.data.model.TextScale
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

/**
 * Unit tests para AccessibilityRepository.
 */
class AccessibilityRepositoryTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    private lateinit var repository: AccessibilityRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        
        whenever(mockContext.getSharedPreferences(anyString(), anyInt()))
            .thenReturn(mockSharedPreferences)
        whenever(mockSharedPreferences.edit()).thenReturn(mockEditor)
        whenever(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor)
        whenever(mockEditor.putBoolean(anyString(), anyBoolean())).thenReturn(mockEditor)
        whenever(mockEditor.putFloat(anyString(), anyFloat())).thenReturn(mockEditor)
        
        // Default values for getAccessibilityConfig
        whenever(mockSharedPreferences.getString("colorblind_type", "NORMAL"))
            .thenReturn("NORMAL")
        whenever(mockSharedPreferences.getString("text_scale", "NORMAL"))
            .thenReturn("NORMAL")
        whenever(mockSharedPreferences.getBoolean("high_contrast", false))
            .thenReturn(false)
        
        repository = AccessibilityRepository(mockContext)
    }

    // ============ Tests de tipo de daltonismo ============

    @Test
    fun `getColorblindType returns type from config`() {
        whenever(mockSharedPreferences.getString("colorblind_type", "NORMAL"))
            .thenReturn("NORMAL")
        
        val result = repository.getColorblindType()
        
        assertEquals(ColorblindType.NORMAL, result)
    }

    @Test
    fun `isHighContrastEnabled returns false by default`() {
        whenever(mockSharedPreferences.getBoolean("high_contrast", false))
            .thenReturn(false)
        
        val result = repository.isHighContrastEnabled()
        
        assertFalse(result)
    }

    @Test
    fun `isHighContrastEnabled returns true when enabled`() {
        whenever(mockSharedPreferences.getBoolean("high_contrast", false))
            .thenReturn(true)
        
        val result = repository.isHighContrastEnabled()
        
        assertTrue(result)
    }

    @Test
    fun `isLargeTextEnabled returns false when text scale is NORMAL`() {
        whenever(mockSharedPreferences.getString("text_scale", "NORMAL"))
            .thenReturn("NORMAL")
        
        val result = repository.isLargeTextEnabled()
        
        assertFalse(result)
    }

    @Test
    fun `isLargeTextEnabled returns true when text scale is LARGE`() {
        whenever(mockSharedPreferences.getString("text_scale", "NORMAL"))
            .thenReturn("LARGE")
        
        val result = repository.isLargeTextEnabled()
        
        assertTrue(result)
    }

    @Test
    fun `isLargeTextEnabled returns true when text scale is EXTRA_LARGE`() {
        whenever(mockSharedPreferences.getString("text_scale", "NORMAL"))
            .thenReturn("EXTRA_LARGE")
        
        val result = repository.isLargeTextEnabled()
        
        assertTrue(result)
    }

    // ============ Tests de nombres legibles ============

    @Test
    fun `getColorblindTypeName returns correct name for NORMAL`() {
        val name = repository.getColorblindTypeName(ColorblindType.NORMAL)
        assertEquals("Colores estándar", name)
    }

    @Test
    fun `getColorblindTypeName returns correct name for PROTANOPIA`() {
        val name = repository.getColorblindTypeName(ColorblindType.PROTANOPIA)
        assertEquals("Protanopia", name)
    }

    @Test
    fun `getColorblindTypeName returns correct name for DEUTERANOPIA`() {
        val name = repository.getColorblindTypeName(ColorblindType.DEUTERANOPIA)
        assertEquals("Deuteranopia", name)
    }

    @Test
    fun `getColorblindTypeName returns correct name for TRITANOPIA`() {
        val name = repository.getColorblindTypeName(ColorblindType.TRITANOPIA)
        assertEquals("Tritanopia", name)
    }

    @Test
    fun `getColorblindTypeName returns correct name for ACHROMATOPSIA`() {
        val name = repository.getColorblindTypeName(ColorblindType.ACHROMATOPSIA)
        assertEquals("Acromatopsia", name)
    }

    // ============ Tests de descripciones ============

    @Test
    fun `getColorblindTypeDescription returns correct description for NORMAL`() {
        val description = repository.getColorblindTypeDescription(ColorblindType.NORMAL)
        assertEquals("Visión normal de colores", description)
    }

    @Test
    fun `getColorblindTypeDescription returns correct description for PROTANOPIA`() {
        val description = repository.getColorblindTypeDescription(ColorblindType.PROTANOPIA)
        assertEquals("Dificultad para distinguir rojos", description)
    }

    @Test
    fun `getColorblindTypeDescription returns correct description for DEUTERANOPIA`() {
        val description = repository.getColorblindTypeDescription(ColorblindType.DEUTERANOPIA)
        assertEquals("Dificultad para distinguir verdes", description)
    }

    @Test
    fun `getColorblindTypeDescription returns correct description for TRITANOPIA`() {
        val description = repository.getColorblindTypeDescription(ColorblindType.TRITANOPIA)
        assertEquals("Dificultad para distinguir azules y amarillos", description)
    }

    @Test
    fun `getColorblindTypeDescription returns correct description for ACHROMATOPSIA`() {
        val description = repository.getColorblindTypeDescription(ColorblindType.ACHROMATOPSIA)
        assertEquals("Visión en escala de grises", description)
    }

    // ============ Tests de NONE == NORMAL ============

    @Test
    fun `getColorblindTypeName returns correct name for NONE`() {
        val name = repository.getColorblindTypeName(ColorblindType.NONE)
        assertEquals("Colores estándar", name)
    }

    @Test
    fun `getColorblindTypeDescription returns correct description for NONE`() {
        val description = repository.getColorblindTypeDescription(ColorblindType.NONE)
        assertEquals("Visión normal de colores", description)
    }
}

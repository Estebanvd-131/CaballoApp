package com.villalobos.caballoapp.util

import android.content.Context

object ProgressionManager {
    private const val PREF_NAME = "AppProgression"
    private const val ZONE_PREFIX = "completed_zone_"
    private const val MUSCLE_PREFIX = "completed_muscle_"

    /**
     * Checks if a specific item index in a region is unlocked.
     * The first item (index 0) is always unlocked.
     * Subsequent items are unlocked only if the previous item is completed.
     */
    fun isUnlocked(context: Context, regionId: Int, index: Int): Boolean {
        if (index <= 0) return true
        return isCompleted(context, regionId, index - 1)
    }

    /**
     * Checks if a specific item index in a region has been completed.
     */
    fun isCompleted(context: Context, regionId: Int, index: Int): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("completed_${regionId}_${index}", false)
    }

    /**
     * Marks a specific item index in a region as completed.
     */
    fun markAsCompleted(context: Context, regionId: Int, index: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("completed_${regionId}_${index}", true).apply()
    }

    /**
     * Checks if a specific muscle (by ID) in a region has been completed.
     */
    fun isMuscleCompleted(context: Context, regionId: Int, muscleId: Int): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("${MUSCLE_PREFIX}${regionId}_${muscleId}", false)
    }

    /**
     * Marks a specific muscle (by ID) in a region as completed.
     */
    fun markMuscleAsCompleted(context: Context, regionId: Int, muscleId: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("${MUSCLE_PREFIX}${regionId}_${muscleId}", true).apply()
    }

    /**
     * Checks if a specific zone index in a region is unlocked.
     * The first zone (index 0) is always unlocked.
     */
    fun isZoneUnlocked(context: Context, regionId: Int, zoneIndex: Int): Boolean {
        if (zoneIndex <= 0) return true
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("${ZONE_PREFIX}${regionId}_${zoneIndex - 1}", false)
    }

    /**
     * Marks a specific zone index in a region as completed.
     */
    fun markZoneAsCompleted(context: Context, regionId: Int, zoneIndex: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("${ZONE_PREFIX}${regionId}_${zoneIndex}", true).apply()
    }
    
    /**
     * Resets progress for a region (useful for testing or resetting).
     */
    fun resetProgress(context: Context, regionId: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        // This is a simple implementation, might need to be more robust if we have many items
        // For now, we just clear everything for the region if we knew the count, but since we don't,
        // we might need to clear all or iterate.
        // For safety, let's just clear all keys starting with "completed_${regionId}_"
        prefs.all.keys.filter { it.startsWith("completed_${regionId}_") }.forEach { key ->
            editor.remove(key)
        }
        prefs.all.keys.filter { it.startsWith("${ZONE_PREFIX}${regionId}_") }.forEach { key ->
            editor.remove(key)
        }
        prefs.all.keys.filter { it.startsWith("${MUSCLE_PREFIX}${regionId}_") }.forEach { key ->
            editor.remove(key)
        }
        editor.apply()
    }
}

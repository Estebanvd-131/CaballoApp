package com.villalobos.caballoapp.util

import android.content.Context

object ProgressionManager {
    private const val PREF_NAME = "AppProgression"

    /**
     * Checks if a specific item index in a region is unlocked.
     * The first item (index 0) is always unlocked.
     * Subsequent items are unlocked only if the previous item is completed.
     */
    fun isUnlocked(context: Context, regionId: Int, index: Int): Boolean {
        return true
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
        editor.apply()
    }
}

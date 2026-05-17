package com.villalobos.caballoapp.di

import android.content.Context
import android.content.SharedPreferences
import com.villalobos.caballoapp.data.local.AppDatabase
import com.villalobos.caballoapp.data.local.dao.MusculoDao
import com.villalobos.caballoapp.data.local.dao.QuizQuestionDao
import com.villalobos.caballoapp.data.local.dao.UserProgressDao
import com.villalobos.caballoapp.data.repository.AccessibilityRepository
import com.villalobos.caballoapp.data.repository.MusculoRepository
import com.villalobos.caballoapp.data.repository.QuizRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 * Módulo de Hilt para proveer Repositories y base de datos.
 * Centraliza la creación de dependencias de datos.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // ============ Room Database ============

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideMusculoDao(database: AppDatabase): MusculoDao {
        return database.musculoDao()
    }

    @Provides
    @Singleton
    fun provideQuizQuestionDao(database: AppDatabase): QuizQuestionDao {
        return database.quizQuestionDao()
    }

    @Provides
    @Singleton
    fun provideUserProgressDao(database: AppDatabase): UserProgressDao {
        return database.userProgressDao()
    }

    // ============ SharedPreferences ============

    @Provides
    @Singleton
    @Named("quiz_prefs")
    fun provideQuizPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("quiz_stats", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    @Named("accessibility_prefs")
    fun provideAccessibilityPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("accessibility_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    @Named("tutorial_prefs")
    fun provideTutorialPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("tutorial_prefs", Context.MODE_PRIVATE)
    }

    // ============ Repositories ============

    @Provides
    @Singleton
    fun provideQuizRepository(
        @ApplicationContext context: Context,
        quizQuestionDao: QuizQuestionDao,
        userProgressDao: UserProgressDao
    ): QuizRepository {
        return QuizRepository(context, quizQuestionDao, userProgressDao)
    }

    @Provides
    @Singleton
    fun provideAccessibilityRepository(@ApplicationContext context: Context): AccessibilityRepository {
        return AccessibilityRepository(context)
    }

    @Provides
    @Singleton
    fun provideMusculoRepository(musculoDao: MusculoDao): MusculoRepository {
        return MusculoRepository(musculoDao)
    }
}


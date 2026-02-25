package com.villalobos.caballoapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.villalobos.caballoapp.data.local.dao.MusculoDao
import com.villalobos.caballoapp.data.local.dao.QuizQuestionDao
import com.villalobos.caballoapp.data.local.dao.UserProgressDao
import com.villalobos.caballoapp.data.local.entity.MusculoEntity
import com.villalobos.caballoapp.data.local.entity.QuizConverters
import com.villalobos.caballoapp.data.local.entity.QuizQuestionEntity
import com.villalobos.caballoapp.data.local.entity.UserProgressConverters
import com.villalobos.caballoapp.data.local.entity.UserProgressEntity
import com.villalobos.caballoapp.data.model.RegionIds
import com.villalobos.caballoapp.data.source.DatosMusculares
import com.villalobos.caballoapp.data.source.QuizData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Base de datos Room principal de la aplicación.
 * Contiene tablas para músculos, preguntas de quiz, y progreso del usuario.
 */
@Database(
    entities = [
        MusculoEntity::class,
        QuizQuestionEntity::class,
        UserProgressEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(QuizConverters::class, UserProgressConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun musculoDao(): MusculoDao
    abstract fun quizQuestionDao(): QuizQuestionDao
    abstract fun userProgressDao(): UserProgressDao

    companion object {
        private const val DATABASE_NAME = "equusapp_database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(PrepopulateCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Callback que prepopula la base de datos con datos iniciales
     * al momento de creación.
     */
    private class PrepopulateCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    prepopulateDatabase(database)
                }
            }
        }

        private suspend fun prepopulateDatabase(database: AppDatabase) {
            // Insertar músculos desde DatosMusculares
            val musculoEntities = DatosMusculares.obtenerTodosLosMusculos().map { musculo ->
                MusculoEntity.fromModel(musculo)
            }
            database.musculoDao().insertAll(musculoEntities)

            // Insertar preguntas de quiz desde QuizData
            val quizEntities = QuizData.quizQuestions.map { question ->
                QuizQuestionEntity.fromModel(
                    question.copy(regionId = RegionIds.normalize(question.regionId))
                )
            }
            database.quizQuestionDao().insertAll(quizEntities)

            // Crear progreso inicial del usuario
            database.userProgressDao().insertOrUpdate(UserProgressEntity())
        }
    }
}

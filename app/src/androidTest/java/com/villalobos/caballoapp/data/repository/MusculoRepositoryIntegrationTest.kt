package com.villalobos.caballoapp.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.villalobos.caballoapp.data.local.AppDatabase
import com.villalobos.caballoapp.data.local.entity.MusculoEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MusculoRepositoryIntegrationTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: MusculoRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repository = MusculoRepository(database.musculoDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getMusclesByRegionUnified_usesRoomAsPrimarySource() = runBlocking {
        database.musculoDao().insert(
            MusculoEntity(
                id = 9001,
                nombre = "Músculo de prueba",
                origen = "Origen",
                insercion = "Inserción",
                funcion = "Función",
                regionId = 1,
                hotspotX = 0.2f,
                hotspotY = 0.3f,
                descripcion = "Desc",
                imagen = "img_test"
            )
        )

        val muscles = repository.getMusclesByRegionUnified(1)
        assertTrue(muscles.isNotEmpty())
        assertEquals(9001, muscles.first().id)
    }

    @Test
    fun getMusclesByRegionUnified_fallsBackWhenRoomIsEmpty() = runBlocking {
        database.musculoDao().deleteAll()

        val muscles = repository.getMusclesByRegionUnified(1)
        assertTrue(muscles.isNotEmpty())
    }
}

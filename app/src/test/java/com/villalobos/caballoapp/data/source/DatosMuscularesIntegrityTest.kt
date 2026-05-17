package com.villalobos.caballoapp.data.source

import com.villalobos.caballoapp.util.DrawableNameResolver
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DatosMuscularesIntegrityTest {

    @Test
    fun allRegionsExposeAValidDrawableAndZoneSet() {
        DatosMusculares.regiones.forEach { region ->
            assertNotEquals(
                "La región ${region.id} no resuelve drawable principal",
                0,
                DrawableNameResolver.resolve(region.nombreImagen)
            )

            val zonas = DatosMusculares.obtenerSubZonasPorRegion(region.id)
            assertFalse("La región ${region.id} no tiene zonas configuradas", zonas.isEmpty())
        }
    }

    @Test
    fun everyZoneHasDrawableMusclesAndNormalizedCoordinates() {
        DatosMusculares.regiones.forEach { region ->
            DatosMusculares.obtenerSubZonasPorRegion(region.id).forEach { zona ->
                assertNotNull("La zona ${zona.id} no tiene imagenMapa", zona.imagenMapa)
                assertNotEquals(
                    "La zona ${zona.id} usa un drawable inexistente: ${zona.imagenMapa}",
                    0,
                    DrawableNameResolver.resolve(zona.imagenMapa)
                )
                assertFalse("La zona ${zona.id} no tiene músculos asociados", zona.musculos.isEmpty())
                assertTrue("La zona ${zona.id} tiene hotspotX fuera de rango", zona.hotspotX in 0f..1f)
                assertTrue("La zona ${zona.id} tiene hotspotY fuera de rango", zona.hotspotY in 0f..1f)
            }
        }
    }

    @Test
    fun everyRegionProvidesAResolvableFallbackImage() {
        DatosMusculares.regiones.forEach { region ->
            val fallback = DatosMusculares.obtenerImagenFallbackPorRegion(region.id)
            assertNotNull("La región ${region.id} no tiene imagen fallback", fallback)
            assertNotEquals(
                "La región ${region.id} tiene fallback inexistente: $fallback",
                0,
                DrawableNameResolver.resolve(fallback)
            )
        }
    }
}
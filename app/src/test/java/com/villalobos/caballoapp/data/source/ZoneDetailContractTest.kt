package com.villalobos.caballoapp.data.source

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ZoneDetailContractTest {

    private val expectedRepresentativeByZoneId = mapOf(
        1001 to "Músculo Temporal",
        1002 to "Músculo Auricular Superior",
        1003 to "Músculo Orbicular del Ojo",
        1004 to "Músculo Mentalis",
        1005 to "Músculo Elevador Nasolabial",
        1006 to "Músculo Cutáneo (Parte Mandibular)",
        1007 to "Músculo Masetero",
        1008 to "Músculo Buccinador",
        2001 to "Músculo Braquiocefálico",
        2002 to "Músculo Romboides (Cervical)",
        2003 to "Músculo Trapecio (Porción Cervical)",
        2004 to "Músculo Cutáneo del Cuello",
        2005 to "Músculo Esternotiroideo",
        2006 to "Estilofaríngeo Caudal",
        3001 to "Músculo Trapecio (Torácico)",
        3002 to "Músculo Longísimo del Dorso",
        3003 to "Músculo Longísimo del Dorso",
        3004 to "Músculos Intercostales Externos",
        3005 to "Músculo Oblicuo Externo",
        3006 to "Músculo Pectoral Superficial",
        3007 to "Músculo Recto del Abdomen",
        3008 to "Músculo Oblicuo Interno",
        3009 to "Músculo Oblicuo Externo",
        4001 to "Músculo Supraespinoso",
        4002 to "Músculo Infraespinoso",
        4003 to "Músculo Bíceps Braquial",
        4004 to "Tríceps Braquial (Cabeza Lateral)",
        4005 to "Músculo Extensor Radial del Carpo",
        4006 to "Músculo Suspensor del Nudillo",
        5001 to "Músculo Sacrocaudal Dorsal",
        5002 to "Músculo Glúteo Medio",
        5003 to "Músculo Bíceps Femoral",
        5004 to "Músculo Gastrocnemio",
        5005 to "Músculo Sacrocaudal Dorsal",
        6001 to "Corion de la Ranilla",
        6002 to "Pared del Casco"
    )

    @Test
    fun everyZoneResolvesTheExpectedRepresentativeMuscle() {
        val allZones = DatosMusculares.regiones.flatMap { region ->
            DatosMusculares.obtenerSubZonasPorRegion(region.id)
        }

        assertEquals(
            "El contrato esperado debe cubrir todas las zonas configuradas",
            allZones.map { it.id }.toSet(),
            expectedRepresentativeByZoneId.keys
        )

        allZones.forEach { zona ->
            val representative = DatosMusculares.obtenerMusculoRepresentativo(zona)

            assertNotNull("La zona ${zona.id} debe resolver un músculo representativo", representative)
            assertEquals(
                "La zona ${zona.id} debe abrir el detalle esperado",
                expectedRepresentativeByZoneId[zona.id],
                representative?.nombre
            )
            assertEquals(
                "La zona ${zona.id} debe abrir un músculo de su misma región",
                zona.regionId,
                representative?.regionId
            )
            assertTrue(
                "La zona ${zona.id} debe tener al menos un músculo asociado",
                zona.musculos.isNotEmpty()
            )
        }
    }

    @Test
    fun onlyKnownGroupedZonesFallBackToTheFirstMuscle() {
        val fallbackZoneIds = DatosMusculares.regiones.flatMap { region ->
            DatosMusculares.obtenerSubZonasPorRegion(region.id)
        }.filter { zona ->
            zona.musculos.none { musculo -> musculo.imagen == zona.imagenMapa }
        }.map { it.id }.toSet()

        assertEquals(
            "Solo las zonas agrupadas sin ilustración muscular individual deberían usar fallback",
            setOf(2003, 5005),
            fallbackZoneIds
        )
    }
}
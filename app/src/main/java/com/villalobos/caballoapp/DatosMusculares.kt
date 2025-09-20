package com.villalobos.caballoapp

object DatosMusculares {
    
    // Regiones disponibles
    val regiones = listOf(
        Region(1, "CABEZA", "Región de la Cabeza", "Músculos de la región cefálica del caballo", "cabeza_lateral", "#D4A574", 1),
        Region(2, "CUELLO", "Región del Cuello", "Músculos de la región cervical del caballo", "cuello_y_torax", "#B8956A", 2),
        Region(3, "TRONCO", "Región del Tronco", "Músculos del tronco del caballo", "torsoequino", "#FFA500", 3),
        Region(4, "MIEMBROS_TORACICOS", "Miembros Torácicos", "Músculos de los miembros torácicos del caballo", "hombro_miembro_anterior", "#C8A882", 4),
        Region(5, "MIEMBROS_PELVICOS", "Región Pélvica", "Músculos de los miembros pélvicos del caballo", "2- MusculoGluteoMedio.png", "#A0825C", 5)
    )
    
    // Músculos por región - Basados en los mockups
    val musculosCabeza = listOf(
        Musculo(
            id = 1,
            nombre = "Músculo Orbicular de los Párpados",
            origen = "Porción orbital: borde orbitario. Porción palpebral: ligamento palpebral",
            insercion = "Forma un anillo alrededor del ojo, las fibras musculares se insertan en la piel",
            funcion = "Cierre del ojo, protección, parpadeo",
            regionId = 1,
            hotspotX = 0.45f,
            hotspotY = 0.35f,
            hotspotNumero = 1,
            descripcion = "Músculo responsable del cierre de los párpados",
            imagen = "orbicularcaballo"

        ),
        Musculo(
            id = 2,
            nombre = "Músculo Masetero",
            origen = "Arco cigomático y hueso maxilar",
            insercion = "Rama y ángulo de la mandíbula",
            funcion = "Elevación de la mandíbula, masticación",
            regionId = 1,
            hotspotX = 0.35f,
            hotspotY = 0.55f,
            hotspotNumero = 2,
            descripcion = "Músculo masticador principal",
            imagen= "zona_meseterica"

        ),
        Musculo(
            id = 3,
            nombre = "Músculo Temporal",
            origen = "Fosa temporal del hueso frontal y parietal",
            insercion = "Proceso coronoides de la mandíbula",
            funcion = "Elevación y retracción de la mandíbula",
            regionId = 1,
            hotspotX = 0.25f,
            hotspotY = 0.25f,
            hotspotNumero = 3,
            descripcion = "Músculo masticador profundo",
            imagen = "zona_parieto_temporal"
        ),
        Musculo(
            id = 4,
            nombre = "Músculo Buccinador",
            origen = "Borde alveolar de maxilar y mandíbula",
            insercion = "Comisura labial y piel de la mejilla",
            funcion = "Compresión de la mejilla, ayuda en la masticación",
            regionId = 1,
            hotspotX = 0.40f,
            hotspotY = 0.65f,
            hotspotNumero = 4,
            descripcion = "Músculo de la mejilla",
            imagen = "musculo_businador"
        ),
        Musculo(
            id = 5,
            nombre = "Músculo Digástrico",
            origen = "Proceso mastoideo del temporal",
            insercion = "Borde ventral de la mandíbula",
            funcion = "Depresión de la mandíbula, apertura bucal",
            regionId = 1,
            hotspotX = 0.30f,
            hotspotY = 0.75f,
            hotspotNumero = 5,
            descripcion = "Músculo que ayuda a abrir la boca",
            imagen = "zona_mentoniana"
        ),
        Musculo(
            id = 6,
            nombre = "Músculo Auricular Posterior",
            origen = "Cresta nucal",
            insercion = "Base de la oreja, en la cara medial",
            funcion = "Retracción de la oreja",
            regionId = 1,
            hotspotX = 0.20f,
            hotspotY = 0.60f,
            hotspotNumero = 6,
            descripcion = "Músculo auricular que retrae el pabellón auricular hacia atrás",
            imagen = "oreja"
        ),

                Musculo(
            id = 7,
            nombre = "Músculo Occipitofrontal",
            origen = "Cresta occipital externa",
            insercion = "Fascia frontal y superciliar",
            funcion = "Movimiento de la piel de la frente",
            regionId = 1,
            hotspotX = 0.15f,
            hotspotY = 0.15f,
            hotspotNumero = 7,
            descripcion = "Músculo de la expresión facial",
                    imagen = "expresion"
        )
    )
    
    val musculosCuello = listOf(
        Musculo(
            id = 8,
            nombre = "Músculo Esternocleidomastoideo",
            origen = "Manubrio del esternón y clavícula",
            insercion = "Proceso mastoideo del temporal",
            funcion = "Flexión lateral y rotación del cuello",
            regionId = 2,
            hotspotX = 0.35f,
            hotspotY = 0.45f,
            hotspotNumero = 1,
            descripcion = "Músculo principal del cuello"
        ),
        Musculo(
            id = 9,
            nombre = "Músculo Trapecio Cervical",
            origen = "Ligamento nucal y apófisis espinosas cervicales",
            insercion = "Espina de la escápula",
            funcion = "Elevación y rotación de la escápula",
            regionId = 2,
            hotspotX = 0.25f,
            hotspotY = 0.35f,
            hotspotNumero = 2,
            descripcion = "Porción cervical del trapecio"
        ),
        Musculo(
            id = 10,
            nombre = "Músculo Esplenio",
            origen = "Ligamento nucal y apófisis espinosas",
            insercion = "Cresta occipital y proceso mastoideo",
            funcion = "Extensión y rotación de la cabeza",
            regionId = 2,
            hotspotX = 0.20f,
            hotspotY = 0.25f,
            hotspotNumero = 3,
            descripcion = "Músculo extensor del cuello"
        )
    )
    
    val musculosTronco = listOf(
        Musculo(
            id = 11,
            nombre = "Músculo Dorsal Ancho",
            origen = "Apófisis espinosas de vértebras torácicas y lumbares",
            insercion = "Tubérculo menor del húmero",
            funcion = "Aducción y extensión del brazo",
            regionId = 3,
            hotspotX = 0.40f,
            hotspotY = 0.45f,
            hotspotNumero = 1,
            descripcion = "Músculo amplio del dorso"
        ),
        Musculo(
            id = 12,
            nombre = "Músculo Trapecio Torácico",
            origen = "Apófisis espinosas torácicas",
            insercion = "Espina de la escápula",
            funcion = "Estabilización y movimiento de la escápula",
            regionId = 3,
            hotspotX = 0.30f,
            hotspotY = 0.35f,
            hotspotNumero = 2,
            descripcion = "Porción torácica del trapecio"
        ),
        Musculo(
            id = 13,
            nombre = "Músculo Serrato Ventral",
            origen = "Costillas y apófisis transversas cervicales",
            insercion = "Cara medial de la escápula",
            funcion = "Soporte y movimiento de la escápula",
            regionId = 3,
            hotspotX = 0.35f,
            hotspotY = 0.55f,
            hotspotNumero = 3,
            descripcion = "Músculo que sostiene la escápula"
        )
    )
    
    val musculosToracicos = listOf(
        Musculo(
            id = 14,
            nombre = "Músculo Deltoides",
            origen = "Espina de la escápula",
            insercion = "Tuberosidad deltoidea del húmero",
            funcion = "Abducción y flexión del brazo",
            regionId = 4,
            hotspotX = 0.40f,
            hotspotY = 0.40f,
            hotspotNumero = 1,
            descripcion = "Músculo del hombro",
            imagen = "musculo_gluteo_medio.png"
        ),
        Musculo(
            id = 15,
            nombre = "Músculo Bíceps Braquial",
            origen = "Tuberosidad supraglenoidea de la escápula",
            insercion = "Tuberosidad radial",
            funcion = "Flexión del antebrazo",
            regionId = 4,
            hotspotX = 0.35f,
            hotspotY = 0.55f,
            hotspotNumero = 2,
            descripcion = "Músculo flexor del antebrazo"
        ),
        Musculo(
            id = 16,
            nombre = "Músculo Tríceps Braquial",
            origen = "Tuberosidad infraglenoidea y diáfisis del húmero",
            insercion = "Olécranon del cúbito",
            funcion = "Extensión del antebrazo",
            regionId = 4,
            hotspotX = 0.30f,
            hotspotY = 0.50f,
            hotspotNumero = 3,
            descripcion = "Músculo extensor del antebrazo"
        )
    )

    val musculosPelvicos = listOf(
        Musculo(
            id = 17,
            nombre = "Músculo Glúteo Accesorio",
            origen = "Tuberosidad coxal del ilion",
            insercion = "Cresta ventral del trocánter mayor del fémur",
            funcion = "Extiende la articulación de la cadera y abduce el miembro",
            regionId = 5,
            hotspotX = 0.38f,
            hotspotY = 0.32f,
            hotspotNumero = 1,
            descripcion = "Músculo accesorio de la región glútea",
            imagen = "musculo_1"

        ),
        Musculo(
            id = 18,
            nombre = "Músculo Glúteo Medio",
            origen = "Cara lateral del ilion, sacro y primeras vértebras caudales",
            insercion = "Trocánter mayor del fémur",
            funcion = "Extiende la cadera y participa en la propulsión",
            regionId = 5,
            hotspotX = 0.42f,
            hotspotY = 0.30f,
            hotspotNumero = 2,
            descripcion = "Músculo potente extensor de la cadera",
            imagen = "musculo_gluteo_medio"
        ),
        Musculo(
            id = 19,
            nombre = "Músculo Tensor de la Fascia Lata",
            origen = "Tuberosidad coxal",
            insercion = "Fascia lata y tibia",
            funcion = "Tensa la fascia lata, flexiona la cadera y extiende la rodilla",
            regionId = 5,
            hotspotX = 0.48f,
            hotspotY = 0.40f,
            hotspotNumero = 3,
            descripcion = "Músculo tensor y flexor de la cadera",
            imagen = "musculo_tensor_fascia"
        ),
        Musculo(
            id = 20,
            nombre = "Músculo Glúteo Superficial",
            origen = "Tuberosidad coxal, borde lateral del ilion y fascia glútea",
            insercion = "Tercer trocánter del fémur",
            funcion = "Abduce el miembro, flexiona la cadera y tensa la fascia glútea",
            regionId = 5,
            hotspotX = 0.46f,
            hotspotY = 0.34f,
            hotspotNumero = 4,
            descripcion = "Músculo superficial de la región glútea",
            imagen = "musculo_superficial"
        ),
        Musculo(
            id = 21,
            nombre = "Músculo Bíceps Femoral",
            origen = "Ligamento sacroilíaco dorsal y lateral, fascia glútea y tuberosidad isquiática",
            insercion = "Superficie caudal del fémur, tibia, tuberosidad calcánea y ligamento rotuliano lateral",
            funcion = "Extiende miembro, corvejón y rodilla",
            regionId = 5,
            hotspotX = 0.39f,
            hotspotY = 0.52f,
            hotspotNumero = 5,
            descripcion = "Músculo largo y fuerte de la región caudal",
            imagen = "musculo_femoral"

        ),
        Musculo(
            id = 22,
            nombre = "Músculo Semitendinoso",
            origen = "Apófisis transversas de la I y II vértebras caudales y tuberosidad isquiática",
            insercion = "Tibia y tuberosidad calcánea",
            funcion = "Extiende cadera y corvejón, rota medialmente el miembro",
            regionId = 5,
            hotspotX = 0.34f,
            hotspotY = 0.57f,
            hotspotNumero = 6,
            descripcion = "Músculo caudal con inserción en el corvejón",
            imagen = "musculo_semitendinoso"
        ),
        Musculo(
            id = 23,
            nombre = "Músculo Semimembranoso",
            origen = "Superficie ventral de la tuberosidad isquiática y ligamento sacrotuberal ancho",
            insercion = "Epicóndilo medial del fémur",
            funcion = "Extiende cadera y aduce el miembro",
            regionId = 5,
            hotspotX = 0.36f,
            hotspotY = 0.60f,
            hotspotNumero = 7,
            descripcion = "Músculo interno de la región caudal",
            imagen = "musculo_semimembranoso"
        ),
        Musculo(
            id = 24,
            nombre = "Músculo Gastrocnemio",
            origen = "Cabeza lateral y medial de la tuberosidad supracondiloidea",
            insercion = "Tuberosidad calcánea",
            funcion = "Extiende el corvejón y flexiona la rodilla",
            regionId = 5,
            hotspotX = 0.37f,
            hotspotY = 0.68f,
            hotspotNumero = 8,
            descripcion = "Músculo principal del tendón de Aquiles",
            imagen = "musculo_gastrocnemio"
        ),
        Musculo(
            id = 25,
            nombre = "Músculo Extensor Digital Largo",
            origen = "Cóndilo lateral del fémur",
            insercion = "Tercer falange distal",
            funcion = "Flexiona corvejón y extiende el miembro",
            regionId = 5,
            hotspotX = 0.39f,
            hotspotY = 0.75f,
            hotspotNumero = 9,
            descripcion = "Músculo extensor del dedo y miembro posterior",
            imagen = "musculo_digital_largo"
        )
    )


    // Función para obtener músculos por región
    fun obtenerMusculosPorRegion(regionId: Int): List<Musculo> {
        return when (regionId) {
            1 -> musculosCabeza
            2 -> musculosCuello
            3 -> musculosTronco
            4 -> musculosToracicos
            5 -> musculosPelvicos
            else -> emptyList()
        }
    }
    
    // Función para obtener todos los músculos
    fun obtenerTodosLosMusulos(): List<Musculo> {
        return musculosCabeza + musculosCuello + musculosTronco + musculosToracicos + musculosPelvicos
    }
    
    // Función para obtener músculo por ID
    fun obtenerMusculoPorId(id: Int): Musculo? {
        return obtenerTodosLosMusulos().find { it.id == id }
    }
    
    // Función para obtener región por ID
    fun obtenerRegionPorId(id: Int): Region? {
        return regiones.find { it.id == id }
    }
} 
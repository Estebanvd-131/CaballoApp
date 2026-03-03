package com.villalobos.caballoapp.data.source

import com.villalobos.caballoapp.data.model.Musculo
import com.villalobos.caballoapp.data.model.Region
import com.villalobos.caballoapp.data.model.Zona

object DatosMusculares {

    // =========================================================
    // REGIONES DISPONIBLES
    // =========================================================
    val regiones = listOf(
        Region(1, "CABEZA", "Región de la Cabeza", "Músculos de la región cefálica del caballo", "cabeza_lateral", "#D4A574", 1),
        Region(2, "CUELLO", "Región del Cuello", "Músculos de la región cervical del caballo", "zona_cervico_lateral", "#B8956A", 2),
        Region(3, "TRONCO", "Región del Tronco", "Músculos del tronco y abdomen", "torsoequino", "#FFA500", 3),
        Region(4, "MIEMBROS_TORACICOS", "Miembros Torácicos", "Músculos y tendones de los miembros anteriores", "hombro_miembro_anterior", "#C8A882", 4),
        Region(5, "MIEMBROS_PELVICOS", "Miembros Pélvicos", "Músculos de la región pélvica, sacra y miembros posteriores", "grupa_miembro_posterior", "#A0825C", 5),
        Region(6, "REGION_DISTAL", "Región Distal (Casco)", "Estructuras y biomecánica del casco", "miembro_distal", "#8D6E63", 6)
    )

    // =========================================================
    // 1. MÚSCULOS DE LA CABEZA (IDs 1 - 99)
    // =========================================================

    val mTemporal = Musculo(
        id = 1, nombre = "Músculo Temporal",
        origen = "Hueso parietal, hueso temporal, hueso frontal y cresta sagital externa.",
        insercion = "Apófisis coronoides de la mandíbula.",
        funcion = "Eleva la mandíbula y la presiona contra los dientes superiores.",
        regionId = 1, descripcion = "Músculo masticador profundo.", imagen = "regioncabezavistalateral_1_zonaparieto_temporal"
    )

    val mMasetero = Musculo(
        id = 2, nombre = "Músculo Masetero",
        origen = "Arco cigomático y cresta facial.",
        insercion = "Cara lateral de la mandíbula (rama mandibular).",
        funcion = "Eleva la mandíbula para cerrar la boca y permitir la masticación.",
        regionId = 1, descripcion = "Músculo masticador principal.", imagen = "regioncabezavistalateral_7_zonameseterica"
    )

    val mEsplenioCabeza = Musculo(
        id = 3, nombre = "Músculo Esplenio (Cabeza)",
        origen = "Apófisis espinosas T3-T5 y Ligamento nucal.",
        insercion = "Hueso occipital y proceso mastoideo del temporal.",
        funcion = "Extiende y eleva la cabeza.",
        regionId = 1, descripcion = "Extensor de la cabeza.", imagen = "regioncabezavistalateral_1_zonaparieto_temporal"
    )

    val mAuricularSuperior = Musculo(
        id = 4, nombre = "Músculo Auricular Superior",
        origen = "Fascia de la región parietal.",
        insercion = "Base del pabellón auricular.",
        funcion = "Eleva la oreja y la dirige hacia adelante.",
        regionId = 1, descripcion = "Posiciona la oreja.", imagen = "regioncabezavistalateral_2_zonaauricular"
    )

    val mAuricularPosterior = Musculo(
        id = 5, nombre = "Músculo Auricular Posterior",
        origen = "Cresta nucal.",
        insercion = "Base de la oreja, cara medial.",
        funcion = "Retracción de la oreja.",
        regionId = 1, descripcion = "Retrae la oreja.", imagen = "regioncabezavistalateral_2_zonaauricular"
    )

    val mOrbicularOjo = Musculo(
        id = 6, nombre = "Músculo Orbicular del Ojo",
        origen = "Ligamento palpebral medial.",
        insercion = "Piel de los párpados.",
        funcion = "Cierra los párpados.",
        regionId = 1, descripcion = "Cierra el ojo.", imagen = "orbicularcaballo"
    )

    val mCigomatico = Musculo(
        id = 7, nombre = "Músculo Cigomático",
        origen = "Arco cigomático.",
        insercion = "Piel del ángulo de la boca.",
        funcion = "Retrae y eleva la comisura labial.",
        regionId = 1, descripcion = "Expresión facial.", imagen = "orbicularcaballo"
    )

    val mMentalis = Musculo(
        id = 8, nombre = "Músculo Mentalis",
        origen = "Superficie rostral del cuerpo de la mandíbula.",
        insercion = "Piel y mucosa del labio inferior.",
        funcion = "Eleva y protruye el labio inferior.",
        regionId = 1, descripcion = "Músculo del mentón.", imagen = "regioncabezavistalateral_4_zonamentoniana"
    )

    val mDepresorLabioInferior = Musculo(
        id = 9, nombre = "Músculo Depresor Labio Inferior",
        origen = "Superficie lateral del cuerpo de la mandíbula.",
        insercion = "Piel del labio inferior.",
        funcion = "Deprime el labio inferior.",
        regionId = 1, descripcion = "Baja el labio.", imagen = "regioncabezavistalateral_4_zonamentoniana"
    )

    val mMilohioideo = Musculo(
        id = 10, nombre = "Músculo Milohioideo",
        origen = "Superficie medial del cuerpo de la mandíbula.",
        insercion = "Rafe medio y hueso hioides.",
        funcion = "Eleva el suelo de la boca y ayuda en la deglución.",
        regionId = 1, descripcion = "Suelo de la boca.", imagen = "regioncabezavistalateral_4_zonamentoniana"
    )

    val mGenihioideo = Musculo(
        id = 11, nombre = "Músculo Genihioideo",
        origen = "Superficie medial de la mandíbula (sínfisis).",
        insercion = "Cuerpo del hueso hioides.",
        funcion = "Desplaza el hioides y la lengua rostralmente.",
        regionId = 1, descripcion = "Movilidad lingual.", imagen = "regioncabezavistalateral_4_zonamentoniana"
    )

    val mElevadorNasolabial = Musculo(
        id = 12, nombre = "Músculo Elevador Nasolabial",
        origen = "Región frontal y nasal.",
        insercion = "Labio superior y ala de la nariz.",
        funcion = "Eleva el labio superior y dilata la nariz.",
        regionId = 1, descripcion = "Dilatador nasal.", imagen = "regioncabezavistalateral_5_zonamaxilar"
    )

    val mCanino = Musculo(
        id = 13, nombre = "Músculo Canino",
        origen = "Maxilar, cerca del foramen infraorbitario.",
        insercion = "Labio superior y cartílago nasal lateral.",
        funcion = "Retrae el labio superior y ensancha la fosa nasal.",
        regionId = 1, descripcion = "Retractor labial.", imagen = "regioncabezavistalateral_5_zonamaxilar"
    )

    val mCutaneoCuelloMandibular = Musculo(
        id = 14, nombre = "Músculo Cutáneo (Parte Mandibular)",
        origen = "Fascia cervical.",
        insercion = "Comisura labial y piel de la mandíbula.",
        funcion = "Mueve la comisura labial.",
        regionId = 1, descripcion = "Superficial mandibular.", imagen = "regioncabezavistalateral_6_zonamandibular"
    )

    val mPterigoideoMedial = Musculo(
        id = 15, nombre = "Músculo Pterigoideo Medial",
        origen = "Proceso pterigoides del esfenoides y palatino.",
        insercion = "Superficie medial de la mandíbula.",
        funcion = "Movimientos laterales de masticación.",
        regionId = 1, descripcion = "Masticación lateral.", imagen = "regioncabezavistalateral_7_zonameseterica"
    )

    val mPterigoideoLateral = Musculo(
        id = 16, nombre = "Músculo Pterigoideo Lateral",
        origen = "Cresta esfenopalatina.",
        insercion = "Fóvea pterigoidea (cuello mandíbula).",
        funcion = "Protruye la mandíbula y lateralización.",
        regionId = 1, descripcion = "Protrusión mandibular.", imagen = "regioncabezavistalateral_7_zonameseterica"
    )

    val mDigastrico = Musculo(
        id = 17, nombre = "Músculo Digástrico",
        origen = "Proceso paracondilar del occipital.",
        insercion = "Superficie ventral de la mandíbula.",
        funcion = "Abre la mandíbula al deprimirse.",
        regionId = 1, descripcion = "Apertura bucal.", imagen = "regioncabezavistalateral_7_zonameseterica"
    )

    val mBuccinador = Musculo(
        id = 18, nombre = "Músculo Buccinador",
        origen = "Maxilar y mandíbula (alvéolos).",
        insercion = "Comisura labial y mucosa de la mejilla.",
        funcion = "Comprime las mejillas contra los dientes.",
        regionId = 1, descripcion = "Pared de la mejilla.", imagen = "regioncabezavistalateral_8_zonabucal"
    )

    val mOrbicularLabios = Musculo(
        id = 19, nombre = "Músculo Orbicular de los Labios",
        origen = "Rodea la apertura bucal.",
        insercion = "Fibras entrelazadas en comisura.",
        funcion = "Cierra y ajusta la tensión de los labios.",
        regionId = 1, descripcion = "Esfínter bucal.", imagen = "regioncabezavistalateral_8_zonabucal"
    )

    // Músculos adicionales cabeza (según documento zona mandibular ventral)
    val mEstilohioideo = Musculo(
        id = 20, nombre = "Músculo Estilohioideo",
        origen = "Parte distal del hueso estiloideo.",
        insercion = "Parte basihioides del hueso hioides.",
        funcion = "Eleva y estabiliza el aparato hioideo.",
        regionId = 1, descripcion = "Estabilizador hioides.", imagen = "regioncabezavistalateral_6_zonamandibular"
    )

    val mOccipitomandibular = Musculo(
        id = 21, nombre = "Músculo Occipitomandibular",
        origen = "Hueso occipital.",
        insercion = "Rama de la mandíbula.",
        funcion = "Ayuda a abrir la mandíbula.",
        regionId = 1, descripcion = "Apertura mandibular.", imagen = "regioncabezavistalateral_6_zonamandibular"
    )

    // =========================================================
    // 2. MÚSCULOS DEL CUELLO (IDs 100 - 199)
    // =========================================================

    val mBraquiocefalico = Musculo(
        id = 100, nombre = "Músculo Braquiocefálico",
        origen = "Ligamento nucal y apófisis mastoidea.",
        insercion = "Cresta del húmero.",
        funcion = "Extiende cabeza/cuello, flexiona lateralmente y extiende miembro torácico.",
        regionId = 2, descripcion = "Conexión cabeza-brazo.", imagen = "regioncuello_1_zonacervicolateral"
    )

    val mEsternocefalico = Musculo(
        id = 101, nombre = "Músculo Esternocefálico",
        origen = "Manubrio o cartílago caniforme del esternón.",
        insercion = "Apófisis mastoidea y rama de la mandíbula.",
        funcion = "Flexiona y lateraliza la cabeza y cuello; baja la cabeza.",
        regionId = 2, descripcion = "Flexor ventral del cuello.", imagen = "regioncuello_1_zonacervicolateral"
    )

    val mEsplenioCuello = Musculo(
        id = 102, nombre = "Músculo Esplenio (Cuello)",
        origen = "Ligamento supraespinoso y fascia toracolumbar.",
        insercion = "Apófisis mastoidea y transversas cervicales.",
        funcion = "Extensión y lateralización del cuello.",
        regionId = 2, descripcion = "Extensor dorsal.", imagen = "regioncuello_1_zonacervicolateral"
    )

    val mSerratoVentralCuello = Musculo(
        id = 103, nombre = "Músculo Serrato Ventral (Cervical)",
        origen = "Apófisis transversas C3-C7.",
        insercion = "Cara serrata de la escápula.",
        funcion = "Sostiene la escápula y ayuda en movilidad del cuello.",
        regionId = 2, descripcion = "Soporte escapular.", imagen = "regioncuello_1_zonacervicolateral"
    )

    val mTrapecioCervical = Musculo(
        id = 104, nombre = "Músculo Trapecio (Porción Cervical)",
        origen = "Ligamento nucal.",
        insercion = "Espina de la escápula.",
        funcion = "Eleva y retrae la escápula.",
        regionId = 2, descripcion = "Elevador superficial.", imagen = "regioncuello_1_zonacervicolateral"
    )

    val mRomboidesCervical = Musculo(
        id = 105, nombre = "Músculo Romboides (Cervical)",
        origen = "Ligamento nucal.",
        insercion = "Borde medial del cartílago de la escápula.",
        funcion = "Eleva la escápula y la aduce.",
        regionId = 2, descripcion = "Profundo al trapecio.", imagen = "regioncuello_2_zonacervicodorsal"
    )

    val mCutaneoCuello = Musculo(
        id = 106, nombre = "Músculo Cutáneo del Cuello",
        origen = "Cartílago caniforme del esternón y fascia.",
        insercion = "Dermis y fascia superficial.",
        funcion = "Mueve la piel (reflejo panniculus).",
        regionId = 2, descripcion = "Músculo dérmico.", imagen = "regioncuello_group_2"
    )

    val mEsternotiroideo = Musculo(
        id = 107, nombre = "Músculo Esternotiroideo",
        origen = "Manubrio esternal.",
        insercion = "Cartílago tiroides de la laringe.",
        funcion = "Retrae la laringe hacia caudal.",
        regionId = 2, descripcion = "Zona laríngea.", imagen = "regioncuello_group_1_1"
    )

    val mEsternohioideo = Musculo(
        id = 108, nombre = "Músculo Esternohioideo",
        origen = "Manubrio esternal.",
        insercion = "Cuerpo del hueso hioides.",
        funcion = "Retrae y baja el hioides y la laringe.",
        regionId = 2, descripcion = "Zona laríngea ventral.", imagen = "regioncuello_group_1_1"
    )

    val mOmohioideo = Musculo(
        id = 109, nombre = "Músculo Omohioideo",
        origen = "Fascia subescapular y cuello (C3-C4).",
        insercion = "Cuerpo del hueso hioides.",
        funcion = "Retrae y baja el hioides y la laringe.",
        regionId = 2, descripcion = "Separador cuello-hombro.", imagen = "regioncuello_group_1_1"
    )

    val mEstilofaringeo = Musculo(
        id = 110, nombre = "Estilofaríngeo Caudal",
        origen = "Hueso estilohioideo.",
        insercion = "Pared lateral de la faringe.",
        funcion = "Eleva y dilata la faringe.",
        regionId = 2, descripcion = "Deglución.", imagen = "regioncuello_group_1"
    )

    val mConstrictoresFaringe = Musculo(
        id = 111, nombre = "Constrictores de la Faringe",
        origen = "Hioides, tiroides y cricoides.",
        insercion = "Rafe faríngeo.",
        funcion = "Comprimen la faringe (bolo alimenticio).",
        regionId = 2, descripcion = "Deglución activa.", imagen = "regioncuello_group_1"
    )

    val mPalatofaringeo = Musculo(
        id = 112, nombre = "Músculo Palatofaríngeo",
        origen = "Borde caudal del paladar blando.",
        insercion = "Pared lateral de la faringe.",
        funcion = "Reduce el diámetro faríngeo.",
        regionId = 2, descripcion = "Dirección alimento.", imagen = "regioncuello_group_1"
    )

    // =========================================================
    // 3. MÚSCULOS DEL TRONCO (IDs 200 - 299)
    // =========================================================

    val mTrapecioToracico = Musculo(
        id = 200, nombre = "Músculo Trapecio (Torácico)",
        origen = "Ligamento supraespinoso.",
        insercion = "Espina de la escápula.",
        funcion = "Retrae la escápula.",
        regionId = 3, descripcion = "Superficial dorso.", imagen = "cuello_y_torax"
    )

    val mRomboidesToracico = Musculo(
        id = 201, nombre = "Músculo Romboides (Torácico)",
        origen = "Ligamento supraespinoso torácico.",
        insercion = "Cartílago escapular.",
        funcion = "Mantiene la escápula contra el tronco.",
        regionId = 3, descripcion = "Fijador escapular.", imagen = "cuello_y_torax"
    )

    val mLongisimoDorso = Musculo(
        id = 202, nombre = "Músculo Longísimo del Dorso",
        origen = "Sacro, ilion y procesos vertebrales lumbares/torácicos.",
        insercion = "Apófisis transversas y costillas.",
        funcion = "Extiende y estabiliza la columna vertebral.",
        regionId = 3, descripcion = "Principal extensor columna.", imagen = "cuello_y_torax"
    )

    val mEspinalToracico = Musculo(
        id = 203, nombre = "Músculo Espinal Torácico",
        origen = "Vértebras torácicas y lumbares.",
        insercion = "Vértebras torácicas y cervicales.",
        funcion = "Extiende el cuello y el dorso.",
        regionId = 3, descripcion = "Extensor espinal.", imagen = "cuello_y_torax"
    )

    val mIntercostalesExternos = Musculo(
        id = 204, nombre = "Músculos Intercostales Externos",
        origen = "Borde caudal de la costilla.",
        insercion = "Borde craneal de la costilla posterior.",
        funcion = "Expansión torácica (Inspiración).",
        regionId = 3, descripcion = "Respiratorio.", imagen = "flanco_torax"
    )

    val mSerratoVentralToracico = Musculo(
        id = 205, nombre = "Músculo Serrato Ventral (Torácico)",
        origen = "Caras externas de las costillas.",
        insercion = "Superficie medial de la escápula.",
        funcion = "Soporte del tronco y movimiento del miembro.",
        regionId = 3, descripcion = "Suspensión del tronco.", imagen = "flanco_torax"
    )

    val mOblicuoExterno = Musculo(
        id = 206, nombre = "Músculo Oblicuo Externo",
        origen = "Costillas y fascia toracolumbar.",
        insercion = "Línea alba y tuberosidad coxal.",
        funcion = "Comprime el abdomen y flexiona lateralmente.",
        regionId = 3, descripcion = "Pared abdominal lateral.", imagen = "flanco_torax"
    )

    val mTransversoAbdomen = Musculo(
        id = 207, nombre = "Músculo Transverso del Abdomen",
        origen = "Vértebras lumbares y costillas caudales.",
        insercion = "Línea alba.",
        funcion = "Compresión visceral profunda.",
        regionId = 3, descripcion = "Faja abdominal natural.", imagen = "flanco_torax"
    )

    val mPectoralSuperficial = Musculo(
        id = 208, nombre = "Músculo Pectoral Superficial",
        origen = "Esternón y cartílagos costales.",
        insercion = "Húmero (cresta tubérculo mayor).",
        funcion = "Flexiona el hombro y aduce.",
        regionId = 3, descripcion = "Pecho superficial.", imagen = "pecho_ventral"
    )

    val mPectoralProfundo = Musculo(
        id = 209, nombre = "Músculo Pectoral Profundo",
        origen = "Esternón y cartílagos costales.",
        insercion = "Tubérculo menor del húmero.",
        funcion = "Retracción de la extremidad.",
        regionId = 3, descripcion = "Pecho profundo.", imagen = "pecho_ventral"
    )

    val mRectoToracico = Musculo(
        id = 210, nombre = "Músculo Recto Torácico",
        origen = "Primeras costillas y esternón.",
        insercion = "Cartílagos costales y fascia.",
        funcion = "Movilidad del tórax.",
        regionId = 3, descripcion = "Continuación del recto abdominal.", imagen = "pecho_ventral"
    )

    val mRectoAbdomen = Musculo(
        id = 211, nombre = "Músculo Recto del Abdomen",
        origen = "Cartílagos costales y esternón.",
        insercion = "Pubis y tendón prepubiano.",
        funcion = "Flexión del tronco y presión abdominal.",
        regionId = 3, descripcion = "Suelo abdominal.", imagen = "pecho_ventral"
    )

    val mDiafragma = Musculo(
        id = 212, nombre = "Diafragma",
        origen = "Apófisis xifoides, costillas, vértebras.",
        insercion = "Centro frénico.",
        funcion = "Principal músculo de la respiración.",
        regionId = 3, descripcion = "Respiración.", imagen = "pecho_ventral"
    )

    val mOblicuoInterno = Musculo(
        id = 213, nombre = "Músculo Oblicuo Interno",
        origen = "Fascia toracolumbar y tuberosidad coxal.",
        insercion = "Línea alba.",
        funcion = "Comprime abdomen, micción/defecación.",
        regionId = 3, descripcion = "Capa media abdominal.", imagen = "flanco_torax"
    )

    val mPectineo = Musculo(
        id = 214, nombre = "Músculo Pectíneo",
        origen = "Cresta pectínea del pubis.",
        insercion = "Línea áspera del fémur.",
        funcion = "Aduce el miembro pélvico.",
        regionId = 3, descripcion = "Aductor zona púbica.", imagen = "flanco_torax"
    )

    // =========================================================
    // 4. MÚSCULOS MIEMBROS TORÁCICOS (IDs 300 - 399)
    // =========================================================

    val mSupraespinoso = Musculo(
        id = 300, nombre = "Músculo Supraespinoso",
        origen = "Fosa supraespinosa de la escápula.",
        insercion = "Tubérculos mayor y menor del húmero.",
        funcion = "Extiende la articulación del hombro.",
        regionId = 4, descripcion = "Extensor hombro.", imagen = "regiondemiembrostoracicos_2_musculosupraespinoso"
    )

    val mInfraespinoso = Musculo(
        id = 301, nombre = "Músculo Infraespinoso",
        origen = "Fosa infraespinosa de la escápula.",
        insercion = "Tubérculo mayor del húmero.",
        funcion = "Abduce y estabiliza el hombro.",
        regionId = 4, descripcion = "Estabilizador lateral.", imagen = "regiondemiembrostoracicos_1_cabezalargadeltricepsbraquial"
    )

    val mBicepsBraquial = Musculo(
        id = 302, nombre = "Músculo Bíceps Braquial",
        origen = "Tubérculo supraglenoideo.",
        insercion = "Tuberosidad radial y tendón extensor carpo.",
        funcion = "Flexiona el codo y extiende hombro.",
        regionId = 4, descripcion = "Flexor codo.", imagen = "regiondemiembrostoracicos_3_bicepsbraquial"
    )

    val mTricepsCabezaLarga = Musculo(
        id = 303, nombre = "Tríceps Braquial (Cabeza Larga)",
        origen = "Borde caudal de la escápula.",
        insercion = "Olécranon de la ulna.",
        funcion = "Extiende el codo y flexiona el hombro.",
        regionId = 4, descripcion = "Extensor principal codo.", imagen = "regiondemiembrostoracicos_1_cabezalargadeltricepsbraquial"
    )

    val mTricepsCabezaLateral = Musculo(
        id = 304, nombre = "Tríceps Braquial (Cabeza Lateral)",
        origen = "Cara lateral del húmero (línea tricipital).",
        insercion = "Olécranon de la ulna.",
        funcion = "Extiende el codo.",
        regionId = 4, descripcion = "Extensor codo.", imagen = "regiondemiembrostoracicos_4_cabezalateraldeltricepsbraquial"
    )

    val mAnconeo = Musculo(
        id = 305, nombre = "Músculo Ancóneo",
        origen = "Fosa olecraneana del húmero.",
        insercion = "Olécranon proximal.",
        funcion = "Extiende y estabiliza el codo.",
        regionId = 4, descripcion = "Estabilizador codo.", imagen = "regiondemiembrostoracicos_4_cabezalateraldeltricepsbraquial"
    )

    val mExtensorRadialCarpo = Musculo(
        id = 306, nombre = "Músculo Extensor Radial del Carpo",
        origen = "Cresta supracondílea lateral del húmero.",
        insercion = "Tuberosidad del tercer metacarpiano.",
        funcion = "Extiende el carpo.",
        regionId = 4, descripcion = "Extensor anterior.", imagen = "regiondemiembrostoracicos_6_musculoextensorradialdelcarpo"
    )

    val mExtensorDigitalComun = Musculo(
        id = 307, nombre = "Músculo Extensor Digital Común",
        origen = "Epicóndilo lateral del húmero.",
        insercion = "Proceso extensor de la tercera falange.",
        funcion = "Extiende carpo y falanges.",
        regionId = 4, descripcion = "Extensor dedo.", imagen = "regiondemiembrostoracicos_6_musculoextensorradialdelcarpo"
    )

    val mExtensorDigitalLateral = Musculo(
        id = 308, nombre = "Músculo Extensor Digital Lateral",
        origen = "Radio y cúbito (lateral).",
        insercion = "Falange proximal.",
        funcion = "Extiende menudillo y carpo.",
        regionId = 4, descripcion = "Extensor lateral.", imagen = "regiondemiembrostoracicos_7_musculoextensordigitallateral"
    )

    val mExtensorCubitalCarpo = Musculo(
        id = 309, nombre = "Músculo Extensor Cubital del Carpo",
        origen = "Epicóndilo lateral del húmero.",
        insercion = "Hueso accesorio y 4to metacarpiano.",
        funcion = "Actúa como flexor del carpo (paradójico).",
        regionId = 4, descripcion = "Flexor lateral.", imagen = "regiondemiembrostoracicos_6_musculoextensorradialdelcarpo"
    )

    val mSuspensorNudillo = Musculo(
        id = 310, nombre = "Músculo Suspensor del Nudillo",
        origen = "Cara palmar del 3er metacarpiano.",
        insercion = "Huesos sesamoideos proximales.",
        funcion = "Estabilidad y soporte (Aparato de estancia).",
        regionId = 4, descripcion = "Ligamento suspensor.", imagen = "regiondemiembrostoracicos_9_musculointeroseo"
    )

    // =========================================================
    // 5. MÚSCULOS MIEMBROS PÉLVICOS (IDs 400 - 499)
    // Incluye Región Sacro-Caudal y Pélvica
    // =========================================================

    val mGluteoAccesorio = Musculo(
        id = 400, nombre = "Músculo Glúteo Accesorio",
        origen = "Tuberosidad coxal del ilion.",
        insercion = "Trocánter mayor del fémur.",
        funcion = "Extiende cadera y abduce.",
        regionId = 5, descripcion = "Profundo glúteo.", imagen = "regionpelvica_1_musculogluteoaccesorio"
    )

    val mGluteoMedio = Musculo(
        id = 401, nombre = "Músculo Glúteo Medio",
        origen = "Ilion, sacro y primeras vértebras caudales.",
        insercion = "Trocánter mayor del fémur.",
        funcion = "Extensor potente de la cadera, propulsión.",
        regionId = 5, descripcion = "Principal propulsor.", imagen = "regionpelvica_2_musculogluteomedio"
    )

    val mGluteoSuperficial = Musculo(
        id = 402, nombre = "Músculo Glúteo Superficial",
        origen = "Tuberosidad coxal y fascia glútea.",
        insercion = "Tercer trocánter del fémur.",
        funcion = "Abducción y flexión de cadera.",
        regionId = 5, descripcion = "Superficial cadera.", imagen = "regionpelvica_4_musculogluteosuperficial"
    )

    val mTensorFasciaLata = Musculo(
        id = 403, nombre = "Músculo Tensor de la Fascia Lata",
        origen = "Tuberosidad coxal.",
        insercion = "Fascia lata, rótula y tibia.",
        funcion = "Flexiona cadera y extiende rodilla.",
        regionId = 5, descripcion = "Tensor lateral.", imagen = "regionpelvica_3_musculotensordelafascialata"
    )

    val mBicepsFemoral = Musculo(
        id = 404, nombre = "Músculo Bíceps Femoral",
        origen = "Sacro, fascia glútea y tuberosidad isquiática.",
        insercion = "Fémur, tibia, calcáneo y rótula.",
        funcion = "Extensión de miembro, cadera y corvejón.",
        regionId = 5, descripcion = "Músculo isquiotibial lateral.", imagen = "regionpelvica_6_musculosemitendinoso"
    )

    val mSemitendinoso = Musculo(
        id = 405, nombre = "Músculo Semitendinoso",
        origen = "Vertebras caudales y tuberosidad isquiática.",
        insercion = "Tibia craneal y tuberosidad calcánea.",
        funcion = "Extiende cadera y corvejón.",
        regionId = 5, descripcion = "Isquiotibial posterior.", imagen = "regionpelvica_6_musculosemitendinoso"
    )

    val mSemimembranoso = Musculo(
        id = 406, nombre = "Músculo Semimembranoso",
        origen = "Tuberosidad isquiática.",
        insercion = "Epicóndilo medial del fémur.",
        funcion = "Extiende cadera y aduce.",
        regionId = 5, descripcion = "Isquiotibial medial.", imagen = "regionpelvica_6_musculosemitendinoso"
    )

    val mGastrocnemio = Musculo(
        id = 407, nombre = "Músculo Gastrocnemio",
        origen = "Tuberosidad supracondiloidea del fémur.",
        insercion = "Tuberosidad calcánea (Tendón de Aquiles).",
        funcion = "Extiende corvejón y flexiona rodilla.",
        regionId = 5, descripcion = "Músculo de la pantorrilla.", imagen = "regionpelvica_8_musculogastrocnemio"
    )

    val mExtensorDigitalLargo = Musculo(
        id = 408, nombre = "Músculo Extensor Digital Largo",
        origen = "Fosa extensora del fémur.",
        insercion = "Tercera falange (casco).",
        funcion = "Flexión del corvejón y extensión del dedo.",
        regionId = 5, descripcion = "Extensor posterior.", imagen = "regionpelvica_9_musculoextensordigitallargo"
    )

    val mSacrocaudalDorsal = Musculo(
        id = 409, nombre = "Músculo Sacrocaudal Dorsal",
        origen = "Vértebras lumbares/sacras.",
        insercion = "Vértebras caudales.",
        funcion = "Eleva la cola.",
        regionId = 5, descripcion = "Elevador cola.", imagen = "regionsacrocaudalyglutea_group_1"
    )

    val mSacrocaudalLateral = Musculo(
        id = 410, nombre = "Músculo Sacrocaudal Lateral",
        origen = "Sacro y cresta ilíaca.",
        insercion = "Costados vértebras caudales.",
        funcion = "Movimiento lateral de la cola.",
        regionId = 5, descripcion = "Lateralizador cola.", imagen = "regionsacrocaudalyglutea_group_1"
    )

    // =========================================================
    // 6. ESTRUCTURAS DISTALES / CASCO (IDs 500 - 599)
    // =========================================================

    val eCorionRanilla = Musculo(
        id = 500, nombre = "Corion de la Ranilla",
        origen = "Interior del casco.", insercion = "Ranilla.",
        funcion = "Amortigua y absorbe impactos.",
        regionId = 6, descripcion = "Tejido sensitivo plantar.", imagen = "regiondistaldelosmiembrospalmar_2_coriondelapalma"
    )

    val eParedCasco = Musculo(
        id = 501, nombre = "Pared del Casco",
        origen = "Corona.", insercion = "Suelo.",
        funcion = "Soporta peso y protege estructuras internas.",
        regionId = 6, descripcion = "Capa dura protectora.", imagen = "regiondistaldelosmiembroslateral_4_corona"
    )

    val eLineaAlba = Musculo(
        id = 502, nombre = "Línea Alba (Casco)",
        origen = "Unión pared y suela.", insercion = "N/A",
        funcion = "Unión sellada entre pared y planta.",
        regionId = 6, descripcion = "Línea blanca.", imagen = "regiondistaldelosmiembrospalmar_2_coriondelapalma"
    )


    // =========================================================
    // CREACIÓN DE ZONAS (Agrupación de Músculos)
    // =========================================================

    // --- CABEZA ---
    // --- CABEZA: Coordenadas calibradas por análisis visual de zonas pintadas ---
    val zonaParietoTemporal = Zona(1001, "Zona Parieto-Temporal", 1, 0.60f, 0.25f, 1, "Músculos superiores masticadores.", "regioncabezavistalateral_1_zonaparieto_temporal", listOf(mTemporal, mMasetero, mEsplenioCabeza))
    val zonaAuricular = Zona(1002, "Zona Auricular", 1, 0.69f, 0.07f, 2, "Control orejas.", "regioncabezavistalateral_2_zonaauricular", listOf(mAuricularSuperior, mAuricularPosterior))
    val zonaOrbital = Zona(1003, "Zona Orbital", 1, 0.75f, 0.38f, 3, "Ojos y párpados.", "orbicularcaballo", listOf(mOrbicularOjo, mCigomatico))
    val zonaMentoniana = Zona(1004, "Zona Mentoniana", 1, 0.71f, 0.88f, 4, "Barbilla y labio inferior.", "regioncabezavistalateral_4_zonamentoniana", listOf(mMentalis, mDepresorLabioInferior, mMilohioideo, mGenihioideo))
    val zonaMaxilar = Zona(1005, "Zona Maxilar", 1, 0.74f, 0.58f, 5, "Mandíbula superior y nariz.", "regioncabezavistalateral_5_zonamaxilar", listOf(mElevadorNasolabial, mCanino))
    val zonaMandibular = Zona(1006, "Zona Mandibular", 1, 0.62f, 0.71f, 6, "Mandíbula inferior.", "regioncabezavistalateral_6_zonamandibular", listOf(mMasetero, mCutaneoCuelloMandibular, mEstilohioideo, mOccipitomandibular))
    val zonaMeseterica = Zona(1007, "Zona Mesetérica", 1, 0.40f, 0.45f, 7, "Potencia masticatoria.", "regioncabezavistalateral_7_zonameseterica", listOf(mMasetero, mPterigoideoMedial, mPterigoideoLateral, mDigastrico))
    val zonaBucal = Zona(1008, "Zona Bucal", 1, 0.82f, 0.88f, 8, "Mejillas y labios.", "regioncabezavistalateral_8_zonabucal", listOf(mBuccinador, mOrbicularLabios, mCanino))

    val subZonasCabeza = listOf(zonaOrbital, zonaMeseterica, zonaParietoTemporal, zonaAuricular, zonaMentoniana, zonaMaxilar, zonaMandibular, zonaBucal)

    // --- CUELLO ---
    // --- CUELLO: Coordenadas calibradas por análisis visual ---
    val zonaCervicoLateral = Zona(2001, "Zona Cérvico Lateral", 2, 0.45f, 0.45f, 1, "Lateral del cuello.", "regioncuello_1_zonacervicolateral", listOf(mBraquiocefalico, mEsternocefalico, mEsplenioCuello, mSerratoVentralCuello, mTrapecioCervical))
    val zonaCervicoDorsal = Zona(2002, "Zona Cérvico Dorsal", 2, 0.38f, 0.18f, 2, "Parte superior cuello.", "regioncuello_2_zonacervicodorsal", listOf(mTrapecioCervical, mRomboidesCervical, mEsplenioCuello))
    val zonaPreescapular = Zona(2003, "Zona Preescapular", 2, 0.75f, 0.35f, 3, "Delante de la escápula.", "regioncuello_group_1", listOf(mTrapecioCervical))
    val zonaCervicoVentral = Zona(2004, "Zona Cérvico Ventral", 2, 0.55f, 0.72f, 4, "Parte inferior cuello.", "regioncuello_group_2", listOf(mCutaneoCuello, mEsternocefalico))
    val zonaLaringea = Zona(2005, "Zona Laríngea", 2, 0.15f, 0.65f, 5, "Músculos de la laringe.", "regioncuello_group_1_1", listOf(mEsternotiroideo, mEsternohioideo, mOmohioideo))
    val zonaFaringea = Zona(2006, "Zona Faríngea", 2, 0.12f, 0.42f, 6, "Músculos de la faringe.", "regioncuello_group_1", listOf(mEstilofaringeo, mConstrictoresFaringe, mPalatofaringeo))

    val subZonasCuello = listOf(zonaCervicoLateral, zonaCervicoDorsal, zonaPreescapular, zonaCervicoVentral, zonaLaringea, zonaFaringea)

    // --- TRONCO ---
    // --- TRONCO: Coordenadas calibradas por análisis visual ---
    val zonaInterescapular = Zona(3001, "Zona Interescapular Alta", 3, 0.15f, 0.12f, 1, "Cruz del caballo.", "cuello_y_torax", listOf(mTrapecioToracico, mRomboidesToracico))
    val zonaVertebralToracica = Zona(3002, "Zona Vertebral Torácica", 3, 0.42f, 0.12f, 2, "Columna torácica.", "cuello_y_torax", listOf(mLongisimoDorso, mEspinalToracico))
    val zonaLumbar = Zona(3003, "Zona Lumbar", 3, 0.72f, 0.12f, 3, "Región de los lomos.", "cuello_y_torax", listOf(mLongisimoDorso))
    val zonaCostal = Zona(3004, "Zona Costal", 3, 0.25f, 0.42f, 4, "Costillas.", "flanco_torax", listOf(mIntercostalesExternos, mSerratoVentralToracico))
    val zonaAbdominalLateral = Zona(3005, "Zona Abdominal Lateral", 3, 0.68f, 0.48f, 5, "Flanco.", "flanco_torax", listOf(mOblicuoExterno, mTransversoAbdomen))
    val zonaEsternal = Zona(3006, "Zona Esternal", 3, 0.15f, 0.78f, 6, "Pecho/Esternón.", "pecho_ventral", listOf(mPectoralSuperficial, mPectoralProfundo, mRectoToracico))
    val zonaXifoidea = Zona(3007, "Zona Xifoidea", 3, 0.38f, 0.78f, 7, "Ventral anterior.", "pecho_ventral", listOf(mRectoAbdomen, mDiafragma))
    val zonaUmbilical = Zona(3008, "Zona Umbilical", 3, 0.58f, 0.78f, 8, "Ventral media.", "flanco_torax", listOf(mOblicuoInterno))
    val zonaPubica = Zona(3009, "Zona Púbica", 3, 0.82f, 0.78f, 9, "Ventral posterior.", "flanco_torax", listOf(mRectoAbdomen, mOblicuoExterno, mOblicuoInterno, mTransversoAbdomen, mPectineo))

    val subZonasTronco = listOf(zonaInterescapular, zonaVertebralToracica, zonaLumbar, zonaCostal, zonaAbdominalLateral, zonaEsternal, zonaXifoidea, zonaUmbilical, zonaPubica)

    // --- MIEMBROS TORÁCICOS ---
    // --- MIEMBROS TORÁCICOS: Coordenadas calibradas por análisis visual ---
    val zonaEscapular = Zona(4001, "Zona Escapular", 4, 0.18f, 0.15f, 1, "Paleta.", "regiondemiembrostoracicos_2_musculosupraespinoso", listOf(mSupraespinoso, mInfraespinoso))
    val zonaDorsalEscapular = Zona(4002, "Zona Dorsal Escapular", 4, 0.35f, 0.08f, 2, "Hombro externo.", "regiondemiembrostoracicos_1_cabezalargadeltricepsbraquial", listOf(mSupraespinoso, mInfraespinoso))
    val zonaBraquial = Zona(4003, "Zona Braquial", 4, 0.25f, 0.35f, 3, "Brazo.", "regiondemiembrostoracicos_3_bicepsbraquial", listOf(mBicepsBraquial, mTricepsCabezaLarga))
    val zonaOlecraneana = Zona(4004, "Zona Olecraneana", 4, 0.12f, 0.45f, 4, "Codo.", "regiondemiembrostoracicos_4_cabezalateraldeltricepsbraquial", listOf(mTricepsCabezaLarga, mTricepsCabezaLateral, mAnconeo))
    val zonaAntebraquial = Zona(4005, "Zona Antebraquial", 4, 0.22f, 0.62f, 5, "Antebrazo.", "regiondemiembrostoracicos_6_musculoextensorradialdelcarpo", listOf(mExtensorRadialCarpo, mExtensorDigitalComun, mExtensorDigitalLateral, mExtensorCubitalCarpo))
    val zonaMetacarpiana = Zona(4006, "Zona Metacarpiana", 4, 0.28f, 0.85f, 6, "Caña.", "regiondemiembrostoracicos_9_musculointeroseo", listOf(mSuspensorNudillo))

    val subZonasToracicas = listOf(zonaEscapular, zonaDorsalEscapular, zonaBraquial, zonaOlecraneana, zonaAntebraquial, zonaMetacarpiana)

    // --- MIEMBROS PÉLVICOS (y Sacro) ---
    // --- MIEMBROS PÉLVICOS: Coordenadas calibradas por análisis visual ---
    val zonaSacra = Zona(5001, "Zona Sacra", 5, 0.85f, 0.08f, 1, "Grupa dorsal.", "regionsacrocaudalyglutea_group_1", listOf(mSacrocaudalDorsal, mSacrocaudalLateral, mGluteoMedio))
    val zonaPelvicaGlutea = Zona(5002, "Zona Glútea", 5, 0.55f, 0.18f, 2, "Grupa lateral.", "regionpelvica_2_musculogluteomedio", listOf(mGluteoAccesorio, mGluteoMedio, mGluteoSuperficial, mTensorFasciaLata))
    val zonaIsquiatica = Zona(5003, "Zona Isquiática", 5, 0.72f, 0.38f, 3, "Nalga posterior.", "regionpelvica_6_musculosemitendinoso", listOf(mBicepsFemoral, mSemitendinoso, mSemimembranoso))
    val zonaPierna = Zona(5004, "Zona Pierna/Gastrocnemio", 5, 0.55f, 0.62f, 4, "Pierna.", "regionpelvica_8_musculogastrocnemio", listOf(mGastrocnemio, mExtensorDigitalLargo))
    val zonaCoccigea = Zona(5005, "Zona Coccígea", 5, 0.92f, 0.25f, 5, "Cola.", "regionsacrocaudalyglutea_group_1_1", listOf(mSacrocaudalDorsal, mSacrocaudalLateral))

    val subZonasPelvicas = listOf(zonaSacra, zonaPelvicaGlutea, zonaIsquiatica, zonaPierna, zonaCoccigea)

    // --- DISTAL / CASCO ---
    // --- DISTAL/CASCO: Coordenadas calibradas por análisis visual ---
    val zonaCascoPalmar = Zona(6001, "Vista Palmar", 6, 0.25f, 0.45f, 1, "Suela del casco.", "regiondistaldelosmiembrospalmar_2_coriondelapalma", listOf(eCorionRanilla, eLineaAlba))
    val zonaCascoLateral = Zona(6002, "Vista Lateral", 6, 0.75f, 0.45f, 2, "Pared del casco.", "regiondistaldelosmiembroslateral_4_corona", listOf(eParedCasco))

    val subZonasDistal = listOf(zonaCascoPalmar, zonaCascoLateral)

    // =========================================================
    // FUNCIONES DE ACCESO
    // =========================================================

    fun obtenerSubZonasPorRegion(regionId: Int): List<Zona> {

        val zonas = when (regionId) {
            1 -> subZonasCabeza
            2 -> subZonasCuello
            3 -> subZonasTronco
            4 -> subZonasToracicas
            5 -> subZonasPelvicas
            6 -> subZonasDistal
            else -> emptyList()
        }

        return zonas.sortedBy { it.hotspotNumero }
    }

    fun obtenerMusculosPorRegion(regionId: Int): List<Musculo> {
        return obtenerSubZonasPorRegion(regionId)
            .flatMap { it.musculos }
            .distinctBy { it.id } // Evita duplicados si un músculo está en varias zonas.
    }

    fun obtenerTodosLosMusculos(): List<Musculo> {
        val todos = (subZonasCabeza + subZonasCuello + subZonasTronco +
                subZonasToracicas + subZonasPelvicas + subZonasDistal)
            .flatMap { it.musculos }
            .distinctBy { it.id }
        return todos
    }

    fun obtenerMusculoPorId(id: Int): Musculo? {
        return obtenerTodosLosMusculos().find { it.id == id }
    }

    fun obtenerRegionPorId(id: Int): Region? {
        return regiones.find { it.id == id }
    }
}
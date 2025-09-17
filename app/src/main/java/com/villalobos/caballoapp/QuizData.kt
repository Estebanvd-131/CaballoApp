package com.villalobos.caballoapp

object QuizData {

    val quizQuestions = listOf(
        // Región de la Cabeza (ID: 1)
        QuizQuestion(
            id = 1,
            regionId = 1,
            question = "¿Qué músculo eleva la mandíbula y la presiona contra los dientes superiores?",
            options = listOf(
                "Masetero",
                "Temporal",
                "Orbicular del ojo",
                "Buccinador"
            ),
            correctAnswer = 1,
            explanation = "El músculo temporal eleva la mandíbula y la presiona contra los dientes superiores."
        ),

        QuizQuestion(
            id = 2,
            regionId = 1,
            question = "El músculo auricular posterior tiene como función:",
            options = listOf(
                "Elevar la oreja",
                "Rotar la mandíbula",
                "Retracción de la oreja",
                "Dilatar la nariz"
            ),
            correctAnswer = 2,
            explanation = "El músculo auricular posterior tiene como función la retracción de la oreja."
        ),

        QuizQuestion(
            id = 3,
            regionId = 1,
            question = "¿Dónde se inserta el músculo masetero?",
            options = listOf(
                "Pabellón auricular",
                "Arco cigomático",
                "Cara lateral de la mandíbula",
                "Hueso hioides"
            ),
            correctAnswer = 2,
            explanation = "El músculo masetero se inserta en la cara lateral de la mandíbula."
        ),

        QuizQuestion(
            id = 4,
            regionId = 1,
            question = "¿Cuál es la función del músculo orbicular del ojo?",
            options = listOf(
                "Elevar el párpado",
                "Cerrar los párpados",
                "Dilatar la pupila",
                "Mover la oreja"
            ),
            correctAnswer = 1,
            explanation = "El músculo orbicular del ojo tiene como función cerrar los párpados."
        ),

        QuizQuestion(
            id = 5,
            regionId = 1,
            question = "El músculo mentalis se caracteriza por:",
            options = listOf(
                "Deprimir el labio inferior",
                "Cerrar la mandíbula",
                "Elevar y protruir el labio inferior",
                "Comprimir las mejillas"
            ),
            correctAnswer = 2,
            explanation = "El músculo mentalis se caracteriza por elevar y protruir el labio inferior."
        ),

        QuizQuestion(
            id = 6,
            regionId = 1,
            question = "¿Qué músculo se origina en el arco cigomático y permite la masticación?",
            options = listOf(
                "Temporal",
                "Esplenio",
                "Masetero",
                "Milohioideo"
            ),
            correctAnswer = 2,
            explanation = "El músculo masetero se origina en el arco cigomático y permite la masticación."
        ),

        QuizQuestion(
            id = 7,
            regionId = 1,
            question = "¿Cuál de los siguientes músculos participa en la deglución?",
            options = listOf(
                "Genihioideo",
                "Orbicular del ojo",
                "Canino",
                "Cigomático"
            ),
            correctAnswer = 0,
            explanation = "El músculo genihioideo participa en la deglución."
        ),

        QuizQuestion(
            id = 8,
            regionId = 1,
            question = "¿Qué músculo retrae y eleva la comisura labial?",
            options = listOf(
                "Cigomático",
                "Orbicular del ojo",
                "Mentalis",
                "Depresor del labio inferior"
            ),
            correctAnswer = 0,
            explanation = "El músculo cigomático retrae y eleva la comisura labial."
        ),

        QuizQuestion(
            id = 9,
            regionId = 1,
            question = "El músculo elevador naso labial tiene como función:",
            options = listOf(
                "Elevar el labio superior y dilatar la nariz",
                "Cerrar los párpados",
                "Retracción de la mandíbula",
                "Flexión de la lengua"
            ),
            correctAnswer = 0,
            explanation = "El músculo elevador naso labial tiene como función elevar el labio superior y dilatar la nariz."
        ),

        QuizQuestion(
            id = 10,
            regionId = 1,
            question = "El músculo pterigoideo medial se caracteriza por:",
            options = listOf(
                "Cerrar los ojos",
                "Movimientos laterales de masticación",
                "Extensión de la lengua",
                "Elevar las orejas"
            ),
            correctAnswer = 1,
            explanation = "El músculo pterigoideo medial se caracteriza por realizar movimientos laterales de masticación."
        ),

        // Región del Cuello (ID: 2)
        QuizQuestion(
            id = 11,
            regionId = 2,
            question = "¿Cuál es la función del músculo braquiocefálico en el caballo?",
            options = listOf(
                "Elevar la escápula",
                "Extender cabeza y cuello, y flexionar lateralmente el cuello",
                "Comprimir la tráquea",
                "Flexionar la mandíbula"
            ),
            correctAnswer = 1,
            explanation = "El músculo braquiocefálico extiende cabeza y cuello, y flexiona lateralmente el cuello."
        ),

        QuizQuestion(
            id = 12,
            regionId = 2,
            question = "¿Dónde se origina el músculo esternocefálico?",
            options = listOf(
                "Apófisis mastoidea",
                "Ligamento nucal",
                "Manubrio del esternón",
                "Fascia toracolumbar"
            ),
            correctAnswer = 2,
            explanation = "El músculo esternocefálico se origina en el manubrio del esternón."
        ),

        QuizQuestion(
            id = 13,
            regionId = 2,
            question = "¿Cuál es la función del músculo esplenio del cuello?",
            options = listOf(
                "Flexiona la cabeza",
                "Extiende y lateraliza el cuello",
                "Estabiliza el húmero",
                "Dilata la tráquea"
            ),
            correctAnswer = 1,
            explanation = "El músculo esplenio del cuello extiende y lateraliza el cuello."
        ),

        QuizQuestion(
            id = 14,
            regionId = 2,
            question = "El músculo serrato ventral del cuello se inserta en:",
            options = listOf(
                "Escápula",
                "Vértebras cervicales",
                "Costillas",
                "Esternón"
            ),
            correctAnswer = 0,
            explanation = "El músculo serrato ventral del cuello se inserta en la escápula."
        ),

        QuizQuestion(
            id = 15,
            regionId = 2,
            question = "¿Cuál de los siguientes músculos sostiene la escápula y contribuye al movimiento del cuello?",
            options = listOf(
                "Trapecio",
                "Esternocefálico",
                "Serrato ventral del cuello",
                "Braquiocefálico"
            ),
            correctAnswer = 2,
            explanation = "El músculo serrato ventral del cuello sostiene la escápula y contribuye al movimiento del cuello."
        ),

        QuizQuestion(
            id = 16,
            regionId = 2,
            question = "¿Dónde se origina el músculo trapecio (porción cervical)?",
            options = listOf(
                "Esternón",
                "Ligamento nucal",
                "Vértebras torácicas",
                "Fascia glútea"
            ),
            correctAnswer = 1,
            explanation = "El músculo trapecio (porción cervical) se origina en el ligamento nucal."
        ),

        QuizQuestion(
            id = 17,
            regionId = 2,
            question = "¿Qué función cumple el músculo romboides?",
            options = listOf(
                "Comprime la tráquea",
                "Aduce y eleva la escápula",
                "Flexiona el miembro anterior",
                "Abduce la mandíbula"
            ),
            correctAnswer = 1,
            explanation = "El músculo romboides aduce y eleva la escápula."
        ),

        QuizQuestion(
            id = 18,
            regionId = 2,
            question = "¿En qué región se encuentra el músculo esplenio con funciones de extensión de cuello y cabeza?",
            options = listOf(
                "Zona abdominal",
                "Zona costal",
                "Zona cérvico dorsal",
                "Zona mandibular"
            ),
            correctAnswer = 2,
            explanation = "El músculo esplenio se encuentra en la zona cérvico dorsal con funciones de extensión de cuello y cabeza."
        ),

        QuizQuestion(
            id = 19,
            regionId = 2,
            question = "El músculo trapecio (porción torácica) ayuda principalmente a:",
            options = listOf(
                "Contraer la lengua",
                "Elevar y retraer la escápula",
                "Flexionar la mandíbula",
                "Comprimir las costillas"
            ),
            correctAnswer = 1,
            explanation = "El músculo trapecio (porción torácica) ayuda principalmente a elevar y retraer la escápula."
        ),

        QuizQuestion(
            id = 20,
            regionId = 2,
            question = "¿Qué músculo se inserta en el borde medial del cartílago de la escápula y se origina en el ligamento nucal?",
            options = listOf(
                "Trapecio",
                "Romboides",
                "Esternocefálico",
                "Milohioideo"
            ),
            correctAnswer = 1,
            explanation = "El músculo romboides se inserta en el borde medial del cartílago de la escápula y se origina en el ligamento nucal."
        ),

        // Región del Tronco (ID: 3)
        QuizQuestion(
            id = 21,
            regionId = 3,
            question = "¿Cuál es la función principal del músculo trapecio (porción torácica y cervical)?",
            options = listOf(
                "Flexionar el cuello",
                "Elevar y retraer la escápula",
                "Comprimir el tórax",
                "Rotar la columna lumbar"
            ),
            correctAnswer = 1,
            explanation = "La función principal del músculo trapecio (porción torácica y cervical) es elevar y retraer la escápula."
        ),

        QuizQuestion(
            id = 22,
            regionId = 3,
            question = "¿Dónde se inserta el músculo romboides del tronco?",
            options = listOf(
                "Espina de la escápula",
                "Costillas",
                "Cartílago escapular",
                "Vértebras torácicas"
            ),
            correctAnswer = 2,
            explanation = "El músculo romboides del tronco se inserta en el cartílago escapular."
        ),

        QuizQuestion(
            id = 23,
            regionId = 3,
            question = "El músculo longísimo del dorso se origina en:",
            options = listOf(
                "Costillas craneales",
                "Esternón y cartílagos costales",
                "Sacro, ilion y vértebras lumbares",
                "Vértebras cervicales y escápula"
            ),
            correctAnswer = 2,
            explanation = "El músculo longísimo del dorso se origina en el sacro, ilion y vértebras lumbares."
        ),

        QuizQuestion(
            id = 24,
            regionId = 3,
            question = "¿Cuál es la biomecánica del músculo espinal torácico?",
            options = listOf(
                "Flexión lateral del cuello",
                "Estabilización de la escápula",
                "Extensión del cuello y dorso",
                "Abducción de la escápula"
            ),
            correctAnswer = 2,
            explanation = "La biomecánica del músculo espinal torácico es la extensión del cuello y dorso."
        ),

        QuizQuestion(
            id = 25,
            regionId = 3,
            question = "¿Qué músculo actúa como propulsor durante el movimiento y se localiza en la zona lumbar?",
            options = listOf(
                "Longísimo del dorso",
                "Romboides",
                "Músculo de la cresta ilíaca",
                "Esternocefálico"
            ),
            correctAnswer = 2,
            explanation = "El músculo de la cresta ilíaca actúa como propulsor durante el movimiento y se localiza en la zona lumbar."
        ),

        QuizQuestion(
            id = 26,
            regionId = 3,
            question = "El músculo intercostal externo tiene la función de:",
            options = listOf(
                "Flexionar la columna",
                "Expandir la caja torácica durante la inspiración",
                "Cerrar los espacios intercostales",
                "Elevar la escápula"
            ),
            correctAnswer = 1,
            explanation = "El músculo intercostal externo tiene la función de expandir la caja torácica durante la inspiración."
        ),

        QuizQuestion(
            id = 27,
            regionId = 3,
            question = "El músculo serrato ventral torácico se inserta en:",
            options = listOf(
                "Costillas dorsales",
                "Superficie medial de la escápula",
                "Esternón",
                "Apófisis espinosas"
            ),
            correctAnswer = 1,
            explanation = "El músculo serrato ventral torácico se inserta en la superficie medial de la escápula."
        ),

        QuizQuestion(
            id = 28,
            regionId = 3,
            question = "¿Cuál es la función del músculo oblicuo externo del abdomen?",
            options = listOf(
                "Comprime el abdomen",
                "Extiende el dorso",
                "Flexiona el cuello",
                "Expande los pulmones"
            ),
            correctAnswer = 0,
            explanation = "La función del músculo oblicuo externo del abdomen es comprimir el abdomen."
        ),

        QuizQuestion(
            id = 29,
            regionId = 3,
            question = "¿Dónde se inserta el músculo pectoral superficial?",
            options = listOf(
                "Vértebras torácicas",
                "Trocánter del fémur",
                "Cresta del tubérculo mayor del húmero y fascia del antebrazo",
                "Pubis"
            ),
            correctAnswer = 2,
            explanation = "El músculo pectoral superficial se inserta en la cresta del tubérculo mayor del húmero y fascia del antebrazo."
        ),

        QuizQuestion(
            id = 30,
            regionId = 3,
            question = "¿Qué músculo tiene como función principal la respiración y se origina en la apófisis xifoides?",
            options = listOf(
                "Oblicuo interno",
                "Recto del abdomen",
                "Diafragma",
                "Intercostal interno"
            ),
            correctAnswer = 2,
            explanation = "El diafragma tiene como función principal la respiración y se origina en la apófisis xifoides."
        ),

        // Región de los Miembros Torácicos (ID: 4)
        QuizQuestion(
            id = 31,
            regionId = 4,
            question = "¿Cuál es la función del músculo cabeza larga del tríceps braquial?",
            options = listOf(
                "Flexionar el codo",
                "Extender la articulación del codo",
                "Extender el hombro",
                "Estabilizar la escápula"
            ),
            correctAnswer = 1,
            explanation = "La función del músculo cabeza larga del tríceps braquial es extender la articulación del codo."
        ),

        QuizQuestion(
            id = 32,
            regionId = 4,
            question = "El músculo supraespinoso se origina en:",
            options = listOf(
                "Cresta del húmero",
                "Tubérculo supraglenoideo",
                "Fosa supraespinosa de la escápula",
                "Olécranon de la ulna"
            ),
            correctAnswer = 2,
            explanation = "El músculo supraespinoso se origina en la fosa supraespinosa de la escápula."
        ),

        QuizQuestion(
            id = 33,
            regionId = 4,
            question = "¿Qué músculo flexiona la articulación del codo?",
            options = listOf(
                "Supraespinoso",
                "Bíceps braquial",
                "Tríceps braquial",
                "Interóseo"
            ),
            correctAnswer = 1,
            explanation = "El músculo bíceps braquial flexiona la articulación del codo."
        ),

        QuizQuestion(
            id = 34,
            regionId = 4,
            question = "¿Dónde se inserta la cabeza lateral del tríceps braquial?",
            options = listOf(
                "Trocánter del húmero",
                "Tuberosidad radial",
                "Olécranon de la ulna",
                "Escápula"
            ),
            correctAnswer = 2,
            explanation = "La cabeza lateral del tríceps braquial se inserta en el olécranon de la ulna."
        ),

        QuizQuestion(
            id = 35,
            regionId = 4,
            question = "¿Cuál es la función del músculo extensor radial del carpo?",
            options = listOf(
                "Flexionar el carpo",
                "Extender la articulación del carpo",
                "Flexionar las falanges",
                "Retraer el miembro"
            ),
            correctAnswer = 1,
            explanation = "La función del músculo extensor radial del carpo es extender la articulación del carpo."
        ),

        QuizQuestion(
            id = 36,
            regionId = 4,
            question = "El músculo extensor digital común se caracteriza por:",
            options = listOf(
                "Flexionar la articulación del menudillo",
                "Extender el hombro",
                "Extender carpo y falanges",
                "Flexionar el carpo"
            ),
            correctAnswer = 2,
            explanation = "El músculo extensor digital común se caracteriza por extender carpo y falanges."
        ),

        QuizQuestion(
            id = 37,
            regionId = 4,
            question = "¿Qué función tiene el músculo extensor cubital del carpo?",
            options = listOf(
                "Extender el codo",
                "Flexionar el carpo",
                "Extender el hombro",
                "Elevar la escápula"
            ),
            correctAnswer = 1,
            explanation = "La función del músculo extensor cubital del carpo es flexionar el carpo."
        ),

        QuizQuestion(
            id = 38,
            regionId = 4,
            question = "El músculo flexor digital superficial se inserta en:",
            options = listOf(
                "Tercer metacarpiano",
                "Huesos sesamoideos",
                "Falanges distales",
                "Escápula"
            ),
            correctAnswer = 2,
            explanation = "El músculo flexor digital superficial se inserta en las falanges distales."
        ),

        QuizQuestion(
            id = 39,
            regionId = 4,
            question = "¿Cuál es la función principal del músculo interóseo?",
            options = listOf(
                "Flexionar el carpo",
                "Extender el codo",
                "Estabilizar el miembro durante la locomoción",
                "Elevar la escápula"
            ),
            correctAnswer = 2,
            explanation = "La función principal del músculo interóseo es estabilizar el miembro durante la locomoción."
        ),

        QuizQuestion(
            id = 40,
            regionId = 4,
            question = "El músculo flexor digital profundo se inserta en:",
            options = listOf(
                "Tercera falange (superficie flexora)",
                "Segunda falange",
                "Tuberosidad radial",
                "Tercer metacarpiano"
            ),
            correctAnswer = 0,
            explanation = "El músculo flexor digital profundo se inserta en la tercera falange (superficie flexora)."
        ),

        // Región Pélvica (ID: 5)
        QuizQuestion(
            id = 41,
            regionId = 5,
            question = "¿Cuál es la función principal del músculo glúteo medio?",
            options = listOf(
                "Flexiona la articulación del corvejón",
                "Extiende la articulación de la cadera y participa en la propulsión del miembro posterior",
                "Aduce el miembro",
                "Comprime el abdomen"
            ),
            correctAnswer = 1,
            explanation = "La función principal del músculo glúteo medio es extender la articulación de la cadera y participar en la propulsión del miembro posterior."
        ),

        QuizQuestion(
            id = 42,
            regionId = 5,
            question = "¿Dónde se origina el músculo glúteo accesorio?",
            options = listOf(
                "Tuberosidad isquiática",
                "Trocánter mayor del fémur",
                "Tuberosidad coxal del ilion",
                "Apófisis transversas de vértebras caudales"
            ),
            correctAnswer = 2,
            explanation = "El músculo glúteo accesorio se origina en la tuberosidad coxal del ilion."
        ),

        QuizQuestion(
            id = 43,
            regionId = 5,
            question = "¿Cuál es la inserción del músculo glúteo superficial?",
            options = listOf(
                "Tuberosidad calcánea",
                "Tercer trocánter del fémur",
                "Fascia lata",
                "Trocánter mayor del fémur"
            ),
            correctAnswer = 1,
            explanation = "La inserción del músculo glúteo superficial es el tercer trocánter del fémur."
        ),

        QuizQuestion(
            id = 44,
            regionId = 5,
            question = "¿Cuál es la función del músculo tensor de la fascia lata?",
            options = listOf(
                "Abduce el miembro",
                "Tensa la fascia lata y flexiona la cadera",
                "Extiende el corvejón",
                "Comprime la cavidad abdominal"
            ),
            correctAnswer = 1,
            explanation = "La función del músculo tensor de la fascia lata es tensar la fascia lata y flexionar la cadera."
        ),

        QuizQuestion(
            id = 45,
            regionId = 5,
            question = "¿Dónde se inserta el músculo bíceps femoral?",
            options = listOf(
                "Tuberosidad isquiática",
                "Trocánter mayor",
                "Borde craneal de la tibia, fémur y tuberosidad calcánea",
                "Línea alba"
            ),
            correctAnswer = 2,
            explanation = "El músculo bíceps femoral se inserta en el borde craneal de la tibia, fémur y tuberosidad calcánea."
        ),

        QuizQuestion(
            id = 46,
            regionId = 5,
            question = "¿Qué función tiene el músculo semitendinoso?",
            options = listOf(
                "Extiende cadera y corvejón; rota el miembro medialmente",
                "Flexiona el miembro posterior",
                "Aduce la extremidad",
                "Extiende las falanges"
            ),
            correctAnswer = 0,
            explanation = "La función del músculo semitendinoso es extender cadera y corvejón; rota el miembro medialmente."
        ),

        QuizQuestion(
            id = 47,
            regionId = 5,
            question = "¿Dónde se origina el músculo semimembranoso?",
            options = listOf(
                "Superficie lateral del ilion",
                "Apófisis transversas caudales y tuberosidad isquiática",
                "Tuberosidad coxal",
                "Ligamento rotuliano"
            ),
            correctAnswer = 1,
            explanation = "El músculo semimembranoso se origina en las apófisis transversas caudales y tuberosidad isquiática."
        ),

        QuizQuestion(
            id = 48,
            regionId = 5,
            question = "El músculo gastrocnemio se caracteriza por:",
            options = listOf(
                "Flexionar la cadera",
                "Extender el corvejón y flexionar la rodilla",
                "Rotar la pelvis",
                "Extender las falanges"
            ),
            correctAnswer = 1,
            explanation = "El músculo gastrocnemio se caracteriza por extender el corvejón y flexionar la rodilla."
        ),

        QuizQuestion(
            id = 49,
            regionId = 5,
            question = "¿Cuál es la función principal del músculo extensor digital largo?",
            options = listOf(
                "Extiende el corvejón y la cadera",
                "Abduce el miembro posterior",
                "Flexiona el tarso y extiende el miembro",
                "Comprime la región glútea"
            ),
            correctAnswer = 2,
            explanation = "La función principal del músculo extensor digital largo es flexionar el tarso y extender el miembro."
        ),

        QuizQuestion(
            id = 50,
            regionId = 5,
            question = "¿Cuál de los siguientes músculos se origina en la fascia glútea y se inserta en el tercer trocánter?",
            options = listOf(
                "Glúteo medio",
                "Glúteo accesorio",
                "Glúteo superficial",
                "Bíceps femoral"
            ),
            correctAnswer = 2,
            explanation = "El músculo glúteo superficial se origina en la fascia glútea y se inserta en el tercer trocánter."
        ),

        // Región Sacro Caudal y Glútea (ID: 6)
        QuizQuestion(
            id = 51,
            regionId = 6,
            question = "¿Cuál es la función del músculo sacrocaudal medio cuando actúan ambos lados simultáneamente?",
            options = listOf(
                "Abducen la cola",
                "Flexionan la cola",
                "Elevan (extienden) la cola",
                "Comprimen la base de la cola"
            ),
            correctAnswer = 2,
            explanation = "Cuando actúan ambos lados simultáneamente, el músculo sacrocaudal medio eleva (extiende) la cola."
        ),

        QuizQuestion(
            id = 52,
            regionId = 6,
            question = "¿Qué ocurre cuando el músculo sacrocaudal dorsal actúa de forma unilateral?",
            options = listOf(
                "La cola se eleva hacia ambos lados",
                "La cola se retrae",
                "La cola se desvía lateralmente",
                "La cola se flexiona hacia abajo"
            ),
            correctAnswer = 2,
            explanation = "Cuando el músculo sacrocaudal dorsal actúa de forma unilateral, la cola se desvía lateralmente."
        ),

        QuizQuestion(
            id = 53,
            regionId = 6,
            question = "¿Dónde se inserta el músculo sacrocaudal ventral?",
            options = listOf(
                "Apófisis espinosas caudales",
                "Trocánter mayor",
                "Superficie ventral de las vértebras caudales",
                "Ligamento sacrotuberal ancho"
            ),
            correctAnswer = 2,
            explanation = "El músculo sacrocaudal ventral se inserta en la superficie ventral de las vértebras caudales."
        ),

        QuizQuestion(
            id = 54,
            regionId = 6,
            question = "¿Cuál es la función del músculo sacrocaudal ventral al actuar bilateralmente?",
            options = listOf(
                "Elevar la cola",
                "Extender la cola",
                "Flexionar la cola hacia abajo",
                "Estabilizar la escápula"
            ),
            correctAnswer = 2,
            explanation = "Al actuar bilateralmente, la función del músculo sacrocaudal ventral es flexionar la cola hacia abajo."
        ),

        QuizQuestion(
            id = 55,
            regionId = 6,
            question = "¿Qué estructura sirve como origen del músculo sacrocaudal dorsal?",
            options = listOf(
                "Tercera vértebra lumbar",
                "Últimas vértebras sacras y primeras vértebras caudales",
                "Fascia glútea",
                "Trocánter menor del fémur"
            ),
            correctAnswer = 1,
            explanation = "El origen del músculo sacrocaudal dorsal son las últimas vértebras sacras y primeras vértebras caudales."
        ),

        QuizQuestion(
            id = 56,
            regionId = 6,
            question = "¿Cuál de los siguientes músculos también participa en la propulsión del miembro posterior?",
            options = listOf(
                "Sacrocaudal medio",
                "Glúteo medio",
                "Sacrocaudal dorsal",
                "Tensor de la fascia lata"
            ),
            correctAnswer = 1,
            explanation = "El músculo glúteo medio también participa en la propulsión del miembro posterior."
        ),

        QuizQuestion(
            id = 57,
            regionId = 6,
            question = "¿Dónde se origina el músculo glúteo medio según esta región?",
            options = listOf(
                "Vértebras torácicas",
                "Tuberosidad isquiática",
                "Cara lateral del ilion, sacro y primeras vértebras caudales",
                "Ligamento supraespinoso"
            ),
            correctAnswer = 2,
            explanation = "El músculo glúteo medio se origina en la cara lateral del ilion, sacro y primeras vértebras caudales."
        ),

        QuizQuestion(
            id = 58,
            regionId = 6,
            question = "¿Qué acción realiza el músculo sacrocaudal medio cuando actúa de forma unilateral?",
            options = listOf(
                "Flexiona la cola hacia abajo",
                "Eleva e inclina lateralmente la cola",
                "Abduce el miembro",
                "Extiende el miembro"
            ),
            correctAnswer = 1,
            explanation = "Cuando actúa de forma unilateral, el músculo sacrocaudal medio eleva e inclina lateralmente la cola."
        ),

        QuizQuestion(
            id = 59,
            regionId = 6,
            question = "¿Qué músculos están implicados en el control motor de la cola?",
            options = listOf(
                "Glúteo superficial y medio",
                "Sacrocaudal medio, dorsal y ventral",
                "Tensor de la fascia lata y bíceps femoral",
                "Gastrocnemio y semimembranoso"
            ),
            correctAnswer = 1,
            explanation = "Los músculos implicados en el control motor de la cola son el sacrocaudal medio, dorsal y ventral."
        ),

        QuizQuestion(
            id = 60,
            regionId = 6,
            question = "¿Cuál es la inserción del músculo glúteo medio?",
            options = listOf(
                "Tercer trocánter",
                "Cresta ventral del ilion",
                "Tuberosidad isquiática",
                "Trocánter mayor del fémur"
            ),
            correctAnswer = 3,
            explanation = "La inserción del músculo glúteo medio es el trocánter mayor del fémur."
        ),

        // Región Distal de los Miembros (ID: 7)
        QuizQuestion(
            id = 61,
            regionId = 7,
            question = "¿Cuál es la principal función del corion de la ranilla?",
            options = listOf(
                "Elevar el menudillo",
                "Proteger las falanges",
                "Amortiguar impactos y distribuir la presión",
                "Facilitar la flexión del carpo"
            ),
            correctAnswer = 2,
            explanation = "La principal función del corion de la ranilla es amortiguar impactos y distribuir la presión."
        ),

        QuizQuestion(
            id = 62,
            regionId = 7,
            question = "El corion de la palma del casco del caballo tiene como función:",
            options = listOf(
                "Retractar el miembro",
                "Nutrir y sostener el casco",
                "Actuar como músculo flexor",
                "Expandir la ranilla"
            ),
            correctAnswer = 1,
            explanation = "El corion de la palma del casco del caballo tiene como función nutrir y sostener el casco."
        ),

        QuizQuestion(
            id = 63,
            regionId = 7,
            question = "¿Qué estructura representa la unión entre la pared y la planta del casco?",
            options = listOf(
                "Línea alba",
                "Talón",
                "Ranilla",
                "Palma"
            ),
            correctAnswer = 0,
            explanation = "La línea alba representa la unión entre la pared y la planta del casco."
        ),

        QuizQuestion(
            id = 64,
            regionId = 7,
            question = "¿Cuál es la función principal de la pared del casco?",
            options = listOf(
                "Lubricar el casco",
                "Nutrir las estructuras internas",
                "Cubrir y proteger las estructuras internas del casco",
                "Favorecer la expansión de la corona"
            ),
            correctAnswer = 2,
            explanation = "La función principal de la pared del casco es cubrir y proteger las estructuras internas del casco."
        ),

        QuizQuestion(
            id = 65,
            regionId = 7,
            question = "¿Qué parte del casco representa la zona superior desde donde crece la pezuña?",
            options = listOf(
                "Línea blanca",
                "Talón",
                "Corona",
                "Palma"
            ),
            correctAnswer = 2,
            explanation = "La corona es la parte del casco que representa la zona superior desde donde crece la pezuña."
        ),

        QuizQuestion(
            id = 66,
            regionId = 7,
            question = "¿Qué función cumplen las ramas extensoras del músculo interóseo medio?",
            options = listOf(
                "Flexionar el carpo",
                "Insertarse en el tendón del músculo extensor digital común",
                "Elevar el casco",
                "Comprimir el menudillo"
            ),
            correctAnswer = 1,
            explanation = "Las ramas extensoras del músculo interóseo medio se insertan en el tendón del músculo extensor digital común."
        ),

        QuizQuestion(
            id = 67,
            regionId = 7,
            question = "El cartílago lateral de la falange distal ayuda a:",
            options = listOf(
                "Reforzar el carpo",
                "Distribuir el peso del caballo",
                "Dar flexibilidad al casco",
                "Cerrar la abertura bucal"
            ),
            correctAnswer = 2,
            explanation = "El cartílago lateral de la falange distal ayuda a dar flexibilidad al casco."
        ),

        QuizQuestion(
            id = 68,
            regionId = 7,
            question = "¿Dónde se encuentran los talones del casco del caballo?",
            options = listOf(
                "En la parte rostral",
                "En la parte caudal o trasera del casco",
                "En la corona",
                "En la superficie medial del metacarpo"
            ),
            correctAnswer = 1,
            explanation = "Los talones del casco del caballo se encuentran en la parte caudal o trasera del casco."
        ),

        QuizQuestion(
            id = 69,
            regionId = 7,
            question = "¿Cuál es la estructura que absorbe impacto y evita el deslizamiento del casco?",
            options = listOf(
                "Corona",
                "Corion de la ranilla",
                "Línea alba",
                "Cartílago lateral"
            ),
            correctAnswer = 1,
            explanation = "El corion de la ranilla es la estructura que absorbe impacto y evita el deslizamiento del casco."
        ),

        QuizQuestion(
            id = 70,
            regionId = 7,
            question = "¿Qué estructura participa directamente en el crecimiento continuo del casco?",
            options = listOf(
                "Falange distal",
                "Talón",
                "Corona",
                "Palma"
            ),
            correctAnswer = 2,
            explanation = "La corona es la estructura que participa directamente en el crecimiento continuo del casco."
        ),

        // Repaso General (ID: 0)
        QuizQuestion(
            id = 71,
            regionId = 0,
            question = "¿Cuál de los siguientes músculos está involucrado en los movimientos de lateralización de la mandíbula?",
            options = listOf(
                "Masetero",
                "Temporal",
                "Pterigoideo medial",
                "Canino"
            ),
            correctAnswer = 2,
            explanation = "El músculo pterigoideo medial está involucrado en los movimientos de lateralización de la mandíbula."
        ),

        QuizQuestion(
            id = 72,
            regionId = 0,
            question = "El músculo que sostiene la escápula y permite movilidad del cuello es:",
            options = listOf(
                "Trapecio",
                "Romboides",
                "Serrato ventral del cuello",
                "Esternocefálico"
            ),
            correctAnswer = 2,
            explanation = "El músculo serrato ventral del cuello sostiene la escápula y permite movilidad del cuello."
        ),

        QuizQuestion(
            id = 73,
            regionId = 0,
            question = "¿Qué músculo del tronco se encarga principalmente de expandir la caja torácica durante la inspiración?",
            options = listOf(
                "Intercostal externo",
                "Recto abdominal",
                "Oblicuo interno",
                "Serrato ventral torácico"
            ),
            correctAnswer = 0,
            explanation = "El músculo intercostal externo se encarga principalmente de expandir la caja torácica durante la inspiración."
        ),

        QuizQuestion(
            id = 74,
            regionId = 0,
            question = "El músculo que extiende la articulación del codo y se origina en la escápula es:",
            options = listOf(
                "Bíceps braquial",
                "Cabeza larga del tríceps braquial",
                "Extensor cubital del carpo",
                "Supraespinoso"
            ),
            correctAnswer = 1,
            explanation = "La cabeza larga del tríceps braquial extiende la articulación del codo y se origina en la escápula."
        ),

        QuizQuestion(
            id = 75,
            regionId = 0,
            question = "¿Cuál de los siguientes músculos se inserta en el tercer trocánter del fémur y permite abducción del miembro posterior?",
            options = listOf(
                "Glúteo medio",
                "Semimembranoso",
                "Glúteo superficial",
                "Tensor de la fascia lata"
            ),
            correctAnswer = 2,
            explanation = "El músculo glúteo superficial se inserta en el tercer trocánter del fémur y permite abducción del miembro posterior."
        ),

        QuizQuestion(
            id = 76,
            regionId = 0,
            question = "El músculo sacrocaudal medio actúa bilateralmente para:",
            options = listOf(
                "Flexionar la cola",
                "Abducir el miembro",
                "Extender (elevar) la cola",
                "Rotar la pelvis"
            ),
            correctAnswer = 2,
            explanation = "El músculo sacrocaudal medio actúa bilateralmente para extender (elevar) la cola."
        ),

        QuizQuestion(
            id = 77,
            regionId = 0,
            question = "¿Qué músculo del miembro torácico actúa como estabilizador durante la locomoción y se inserta en los huesos sesamoideos proximales?",
            options = listOf(
                "Flexor digital profundo",
                "Extensor radial del carpo",
                "Interóseo",
                "Supraespinoso"
            ),
            correctAnswer = 2,
            explanation = "El músculo interóseo actúa como estabilizador durante la locomoción y se inserta en los huesos sesamoideos proximales."
        ),

        QuizQuestion(
            id = 78,
            regionId = 0,
            question = "¿Qué estructura del casco del caballo amortigua el impacto y evita el deslizamiento?",
            options = listOf(
                "Cartílago lateral",
                "Corion de la ranilla",
                "Talón",
                "Corona"
            ),
            correctAnswer = 1,
            explanation = "El corion de la ranilla amortigua el impacto y evita el deslizamiento del casco del caballo."
        ),

        QuizQuestion(
            id = 79,
            regionId = 0,
            question = "El músculo diafragma tiene como función principal:",
            options = listOf(
                "Comprimir las vísceras",
                "Flexionar el tronco",
                "Respiración",
                "Elevar el esternón"
            ),
            correctAnswer = 2,
            explanation = "El músculo diafragma tiene como función principal la respiración."
        ),

        QuizQuestion(
            id = 80,
            regionId = 0,
            question = "¿Cuál de los siguientes músculos se origina en el sacro y se inserta en el trocánter mayor del fémur, participando en la propulsión?",
            options = listOf(
                "Glúteo medio",
                "Glúteo superficial",
                "Bíceps femoral",
                "Tensor de la fascia lata"
            ),
            correctAnswer = 0,
            explanation = "El músculo glúteo medio se origina en el sacro y se inserta en el trocánter mayor del fémur, participando en la propulsión."
        )
    )

    // Función para obtener preguntas por región
    fun getQuestionsByRegion(regionId: Int): List<QuizQuestion> {
        return if (regionId == 0) {
            quizQuestions.filter { it.regionId == 0 }
        } else {
            quizQuestions.filter { it.regionId == regionId }
        }
    }

    // Función para obtener preguntas por dificultad
    fun getQuestionsByDifficulty(difficulty: Difficulty): List<QuizQuestion> {
        return quizQuestions.filter { it.difficulty == difficulty }
    }

    // Función para obtener una pregunta aleatoria
    fun getRandomQuestion(regionId: Int? = null): QuizQuestion {
        val questions = if (regionId != null) {
            getQuestionsByRegion(regionId)
        } else {
            quizQuestions
        }
        return questions.random()
    }

    // Función para obtener un quiz completo (mezcla de preguntas)
    fun getQuizQuestions(regionId: Int? = null, count: Int = 5): List<QuizQuestion> {
        val availableQuestions = if (regionId != null) {
            getQuestionsByRegion(regionId)
        } else {
            quizQuestions
        }

        return availableQuestions.shuffled().take(minOf(count, availableQuestions.size))
    }
}
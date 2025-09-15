package com.villalobos.caballoapp

object QuizData {

    val quizQuestions = listOf(
        // Región Cabeza (ID: 1)
        QuizQuestion(
            id = 1,
            regionId = 1,
            question = "¿Cuál es la función principal del músculo Masetero?",
            options = listOf(
                "Cerrar el ojo",
                "Elevar la mandíbula para masticar",
                "Movimientos laterales de la mandíbula",
                "Depresión de la mandíbula"
            ),
            correctAnswer = 1,
            explanation = "El músculo Masetero es el principal músculo masticador responsable de elevar la mandíbula.",
            difficulty = Difficulty.EASY
        ),

        QuizQuestion(
            id = 2,
            regionId = 1,
            question = "¿Dónde se inserta el músculo Temporal?",
            options = listOf(
                "Rama y ángulo de la mandíbula",
                "Proceso coronoides de la mandíbula",
                "Borde alveolar de maxilar",
                "Comisura labial"
            ),
            correctAnswer = 1,
            explanation = "El músculo Temporal se inserta en el proceso coronoides de la mandíbula.",
            difficulty = Difficulty.MEDIUM
        ),

        QuizQuestion(
            id = 3,
            regionId = 1,
            question = "¿Cuál de estos músculos NO pertenece a la región de la cabeza?",
            options = listOf(
                "Músculo Orbicular de los Párpados",
                "Músculo Masetero",
                "Músculo Esternocleidomastoideo",
                "Músculo Temporal"
            ),
            correctAnswer = 2,
            explanation = "El músculo Esternocleidomastoideo pertenece al cuello, no a la cabeza.",
            difficulty = Difficulty.MEDIUM
        ),

        // Región Cuello (ID: 2)
        QuizQuestion(
            id = 4,
            regionId = 2,
            question = "¿Cuál es el origen del músculo Esternocleidomastoideo?",
            options = listOf(
                "Proceso mastoideo del temporal",
                "Manubrio del esternón y clavícula",
                "Ligamento nucal",
                "Cresta occipital externa"
            ),
            correctAnswer = 1,
            explanation = "El músculo Esternocleidomastoideo se origina en el manubrio del esternón y la clavícula.",
            difficulty = Difficulty.MEDIUM
        ),

        QuizQuestion(
            id = 5,
            regionId = 2,
            question = "¿Qué función realiza el músculo Esplenio?",
            options = listOf(
                "Flexión lateral del cuello",
                "Extensión y rotación de la cabeza",
                "Elevación de la escápula",
                "Soporte de la escápula"
            ),
            correctAnswer = 1,
            explanation = "El músculo Esplenio es responsable de la extensión y rotación de la cabeza.",
            difficulty = Difficulty.HARD
        ),

        // Región Tronco (ID: 3)
        QuizQuestion(
            id = 6,
            regionId = 3,
            question = "¿Cuál es la función principal del músculo Dorsal Ancho?",
            options = listOf(
                "Elevación de la escápula",
                "Aducción y extensión del brazo",
                "Extensión y rotación de la cabeza",
                "Flexión del antebrazo"
            ),
            correctAnswer = 1,
            explanation = "El músculo Dorsal Ancho realiza aducción y extensión del brazo (miembro torácico).",
            difficulty = Difficulty.MEDIUM
        ),

        QuizQuestion(
            id = 7,
            regionId = 3,
            question = "¿Dónde se origina el músculo Serrato Ventral?",
            options = listOf(
                "Apófisis espinosas torácicas",
                "Costillas y apófisis transversas cervicales",
                "Tuberosidad isquiática",
                "Espina de la escápula"
            ),
            correctAnswer = 1,
            explanation = "El músculo Serrato Ventral se origina en las costillas y apófisis transversas cervicales.",
            difficulty = Difficulty.HARD
        ),

        // Región Miembros Torácicos (ID: 4)
        QuizQuestion(
            id = 8,
            regionId = 4,
            question = "¿Qué tipo de movimiento realiza el músculo Deltoides?",
            options = listOf(
                "Flexión del antebrazo",
                "Abducción y flexión del brazo",
                "Extensión del antebrazo",
                "Rotación del brazo"
            ),
            correctAnswer = 1,
            explanation = "El músculo Deltoides realiza abducción y flexión del brazo.",
            difficulty = Difficulty.MEDIUM
        ),

        QuizQuestion(
            id = 9,
            regionId = 4,
            question = "¿Cuál es la inserción del músculo Bíceps Braquial?",
            options = listOf(
                "Olécranon del cúbito",
                "Tuberosidad radial",
                "Trocánter mayor del fémur",
                "Patela y tibia"
            ),
            correctAnswer = 1,
            explanation = "El músculo Bíceps Braquial se inserta en la tuberosidad radial.",
            difficulty = Difficulty.HARD
        ),

        // Región Miembros Pélvicos (ID: 5)
        QuizQuestion(
            id = 10,
            regionId = 5,
            question = "¿Cuál es el músculo principal de la grupa?",
            options = listOf(
                "Músculo Bíceps Femoral",
                "Músculo Semitendinoso",
                "Músculo Glúteo Superficial",
                "Músculo Tríceps Braquial"
            ),
            correctAnswer = 2,
            explanation = "El músculo Glúteo Superficial es el principal músculo de la grupa.",
            difficulty = Difficulty.EASY
        ),

        QuizQuestion(
            id = 11,
            regionId = 5,
            question = "¿Qué función NO realiza el músculo Bíceps Femoral?",
            options = listOf(
                "Extensión del muslo",
                "Flexión de la pierna",
                "Aducción del muslo",
                "Rotación del muslo"
            ),
            correctAnswer = 2,
            explanation = "El músculo Bíceps Femoral no realiza aducción del muslo; es principalmente extensor y flexor.",
            difficulty = Difficulty.HARD
        ),

        // Preguntas generales de anatomía
        QuizQuestion(
            id = 12,
            regionId = 0, // General
            question = "¿Qué significa 'origen' en anatomía muscular?",
            options = listOf(
                "Punto de inserción móvil del músculo",
                "Punto de inserción fijo del músculo",
                "Tipo de contracción muscular",
                "Grupo muscular al que pertenece"
            ),
            correctAnswer = 1,
            explanation = "El origen es el punto de inserción fijo del músculo, generalmente proximal.",
            difficulty = Difficulty.EASY
        ),

        QuizQuestion(
            id = 13,
            regionId = 0, // General
            question = "¿Cuál de estos NO es un tipo de músculo en equinos?",
            options = listOf(
                "Esquelético",
                "Cardíaco",
                "Liso",
                "Voluntario"
            ),
            correctAnswer = 3,
            explanation = "'Voluntario' no es un tipo de músculo; los tipos son esquelético, cardíaco y liso.",
            difficulty = Difficulty.MEDIUM
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
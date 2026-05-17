# Contexto de Dominio — EquusApp

## Glosario

### Entidades del Dominio

- **Musculo**: Estructura anatómica individual del caballo. Tiene nombre, origen, inserción, función biomecánica, y una posición (`hotspot`) en una imagen anatómica. Pertenece a una y solo una **Region**.
- **Region**: Zona corporal del caballo (Cabeza, Cuello, Tronco, Torácica, Pélvica, Distal). Agrupa múltiples **Musculos** y se muestra como una pantalla independiente con su propia ilustración.
- **Zona**: Subdivisión dentro de una **Region**. Un músculo puede pertenecer a una zona específica dentro de su región.
- **Hotspot**: Punto táctil en coordenadas normalizadas (0.0–1.0) que representa la posición de un **Musculo** en la ilustración de su **Region**. Detectado por `InteractiveAnatomyView`.
- **QuizQuestion**: Pregunta de evaluación sobre un **Musculo**. Pertenece a una **Region** o es global. Tiene 4 opciones, una correcta, y una explicación.
- **UserProgress**: Estado de progresión del usuario: XP acumulado, nivel actual, racha de estudio, quizzes completados. Fuente de verdad: Room (`UserProgressEntity`).

### Conceptos de Accesibilidad

- **ColorblindType**: Tipo de daltonismo soportado (Ninguno, Deuteranomalía, Protanomalía, Tritanomalía, Acromatopsia). Afecta la paleta de colores de toda la UI.
- **TextScale**: Factor de escalado de texto (Normal, Grande, Extra Grande). Afecta todos los TextViews de la app.
- **Modo Contraste Alto**: Alternativa visual que maximiza el contraste para usuarios con baja visión.

### Conceptos de Progresión

- **XP (Puntos de Experiencia)**: Puntos ganados al responder correctamente en el quiz. Se acumulan para subir de nivel.
- **Nivel**: Escalón de progresión basado en XP acumulado.
- **Racha**: Días consecutivos de uso de la app.

### Capas Técnicas (no de dominio, pero necesarias)

- **Repository**: Puente entre la fuente de datos (Room o hardcoded) y los ViewModels. Decide de dónde salen los datos.
- **DAO**: Interfaz de acceso a la base de datos Room por tabla.

## Fuentes de Verdad

| Dato | Fuente primaria | Fuente legacy/backup |
|---|---|---|
| Músculos | Room (`musculos`) pre-poblado desde `DatosMusculares.kt` | `DatosMusculares.kt` (fallback directo) |
| Preguntas de quiz | Room (`quiz_questions`) pre-poblado desde `QuizData.kt` | `QuizData.kt` (fallback directo) |
| Progreso del usuario | Room (`user_progress`) | `SharedPreferences` (`quiz_stats`) — legacy, aún activo |
| Config de accesibilidad | `SharedPreferences` (`accessibility_prefs`) | — |
| Tutorial visto | `SharedPreferences` (`tutorial_prefs`) | — |

## Flujo de Pantallas

Splash → Main → RegionMenu → [RegionCabeza|Cuello|Tronco|Toracica|Pelvica|Distal] → DetalleMusculo
Main → QuizActivity → CorrectAnswersActivity
Main → Accesibilidad
Main → Creditos
Primera vez: Main → TutorialActivity

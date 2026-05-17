# Diagnóstico Quirúrgico — EquusApp
> Fecha: 2026-05-17
> Evaluación completa pre-entrega final (APK + código académico)

---

## Resumen Ejecutivo

**Proyecto**: App educativa offline de miología del Caballo Criollo Colombiano
**Stack**: Kotlin, MVVM, Hilt, Room, Coroutines/Flow, ViewBinding, XML
**Tamaño**: ~97 archivos Kotlin, ~17K líneas, 20 layouts, 15 Activities
**Tests**: 11 unitarios, 5 instrumentados

**Veredicto general**: Arquitectura bien planteada, pero con bugs críticos que causan crashes y pérdida de datos en producción. Hay **32+ archivos duplicados** del old package structure que pueden causar conflictos de compilación. No hay configuración de firma para release APK. El componente estrella (`InteractiveAnatomyView`) es de calidad profesional. Los tests existen pero tienen cobertura insuficiente. La mayoría de los músculos no tienen hotspots calibrados.

---

## CRÍTICOS — Bloquean la evaluación SUS (causarían crash o pantalla vacía)

### C0. 32+ archivos Kotlin duplicados (paquete raíz vs subpaquetes)
- **Archivos**: Todos los archivos en `com/villalobos/caballoapp/*.kt` (raíz) que también existen en `ui/*`, `data/*`, `util/*`, etc.
- **Problema**: Refactoring a clean architecture quedó a medias. Hay duplicados de: MainActivity, Accesibilidad, Creditos, CorrectAnswersActivity, todas las regiones (6), DatosMusculares, QuizData, AchievementData, ErrorHandler, HotspotHelper, ImageAnimationHelper, AccesibilityHelper, AdaptadorMusculos, TutorialPaso, TutorialAdapter.
- **Impacto**: Conflictos de clase en compilación. El Manifest referencia las versiones nuevas, pero las viejas compilan y pueden causar comportamiento impredecible.
- **Fix**: Eliminar todos los archivos del paquete raíz que ya existen en subpaquetes.

### C0b. No hay configuración de firma para release APK
- **Archivo**: `app/build.gradle.kts`
- **Problema**: No existe `signingConfigs`. No se puede generar un APK firmado para distribuir a estudiantes.
- **Impacto**: No se puede entregar la app. Solo se puede generar debug APK (que ya existe en raíz, 192MB).
- **Fix**: Crear keystore con `keytool` y agregar `signingConfigs` al build.gradle.kts.

### C1. Race condition en pre-población de Room
- **Archivo**: `AppDatabase.kt:70-78`
- **Problema**: `PrepopulateCallback.onCreate()` usa `INSTANCE?.let` para obtener la DB. Pero `INSTANCE` se asigna DESPUÉS de `build()`. En instalación limpia, `INSTANCE` es `null` → datos anatómicos nunca se insertan.
- **Impacto**: Estudiante nuevo ve pantallas vacías sin músculos ni preguntas.
- **Fix**: Eliminar callback, hacer pre-población directa en `getDatabase()` después de asignar `INSTANCE`, con guard de `getMusculoCount() == 0`.

### C2. Handler memory leak en QuizActivity
- **Archivo**: `QuizActivity.kt:284-307`
- **Problema**: `Handler.postDelayed()` con referencia implícita a Activity. Si usuario rota pantalla o sale, handler sigue corriendo sobre Activity destruida.
- **Impacto**: Crash durante el quiz (funcionalidad central a evaluar).
- **Fix**: Usar `lifecycleScope` + `delay()` en vez de Handler, o limpiar en `onDestroy()`.

### C3. Race condition en estado del quiz (double-click)
- **Archivo**: `QuizViewModel.kt:128-157`
- **Problema**: `answerQuestion()` no es atómico. Clicks rápidos causan double submission, index OOB, respuestas perdidas.
- **Impacto**: Crash o comportamiento errático al responder rápido.
- **Fix**: Deshabilitar botón inmediatamente tras click, o usar Mutex.

### C4. Estado del quiz se pierde al rotar pantalla
- **Archivo**: `QuizViewModel.kt:81`
- **Problema**: `regionId` almacenado como propiedad normal, no en `SavedStateHandle`. Al rotar, se pierde contexto del quiz.
- **Impacto**: Quiz se resetea al rotar. Estudiante pierde progreso.
- **Fix**: Guardar `regionId` en `SavedStateHandle`.

### C5. Handler memory leak en Accesibilidad
- **Archivo**: `Accesibilidad.kt:168`
- **Problema**: `Handler(Looper.getMainLooper()).postDelayed({ viewModel.restartApp(this) }, 1000)` captura `this` (Activity). Si se destruye antes del delay, leak.
- **Impacto**: Crash al cambiar accesibilidad y rotar/salir rápido.
- **Fix**: Usar `lifecycleScope` + `delay()`.

### C6. OnPageChangeCallback nunca desregistrado en Tutorial
- **Archivo**: `TutorialActivity.kt:150-154`
- **Problema**: `registerOnPageChangeCallback()` sin correspondiente `unregisterOnPageChangeCallback()`.
- **Impacto**: Memory leak garantizado al salir del tutorial.
- **Fix**: Guardar referencia al callback y desregistrar en `onDestroy()`.

---

## ALTOS — Corrupción de datos o funcionalidad rota

### A1. Doble ruta de guardado para resultados del quiz
- **Archivo**: `QuizRepository.kt:129-173`
- **Problema**: `completeQuiz()` guarda en SharedPreferences Y Room por separado, sin sincronizar. El ViewModel usa la ruta legacy (SharedPreferences).
- **Impacto**: Progreso inconsistente. Estadísticas pueden mostrar datos diferentes según qué fuente se lea.

### A2. Activity reference leak en AccessibilityViewModel
- **Archivo**: `AccessibilityViewModel.kt:148-165`
- **Problema**: Métodos como `applyColorsPreview(activity: Activity)` y `restartApp(activity: Activity)` reciben Activity directamente.
- **Impacto**: Memory leak silencioso que degrada rendimiento con uso prolongado.

### A3. Region IDs inconsistentes entre fuentes de datos
- **Archivos**: `DatosMusculares.kt` vs `QuizData.kt`
- **Problema**:
  - `DatosMusculares` define 6 regiones (IDs 1-6). Region 6 = Distal.
  - `QuizData` usa regionId 6 = "Sacro Caudal y Glútea" y regionId 7 = "Distal de los Miembros".
  - `RegionIds.normalize()` mapea 7→6, pero el CONTENIDO de las preguntas no coincide con los músculos de la región.
  - `AchievementData` solo usa regiones 1-5.
- **Impacto**: Quiz de "Distal" podría mostrar preguntas de "Sacro Caudal". Contenido anatómicamente incorrecto.

### A4. TODOs en producción — achievements imposibles
- **Archivo**: Código del quiz engine
- **Problema**: `musclesStudied` y `studyStreak` hardcodeados en 0 con `// TODO: Implement`. Los logros "Enciclopedia" y "Racha de estudio" son imposibles de desbloquear.
- **Impacto**: Funcionalidad de gamificación rota. No bloquea evaluación pero decepciona al usuario.

### A5. IndexOutOfBounds en opciones del quiz
- **Archivo**: `QuizActivity.kt:164-167`
- **Problema**: Se asume que cada pregunta tiene exactamente 4 opciones sin validación. Si alguna pregunta tiene menos, crash.
- **Impacto**: Crash si hay pregunta con <4 opciones en QuizData.

### A6. Navigation back stack mal gestionado
- **Archivos**: `DetalleMusculo.kt:72-76`, `BaseRegionActivity.kt:286-292`
- **Problema**: Back navigation desde DetalleMusculo crea nuevo Intent en vez de usar `finish()`, creando Activities duplicadas en el stack. `navigateBackToRegionMenu()` usa `FLAG_ACTIVITY_CLEAR_TOP` que limpia todo el stack.
- **Impacto**: Comportamiento confuso del botón back. Memory pressure por Activities duplicadas.

### A7. DrawableNameResolver retorna 0 para recursos no encontrados
- **Archivo**: `DrawableNameResolver.kt:17-20`
- **Problema**: Retorna 0 cuando drawable no existe. Pero 0 es resource ID válido en Android. El fallback a `cabeza_lateral` es hardcoded para todas las regiones.
- **Impacto**: Imagen incorrecta o ausente para músculos de ciertas regiones.

### A8. Fallback de imagen inadecuado en DetalleMusculo
- **Archivo**: `DetalleMusculo.kt:175`
- **Problema**: Si no encuentra la imagen del músculo, carga `cabeza_lateral` sin importar la región. Un músculo pélvico mostraría imagen de la cabeza.
- **Impacto**: Contenido visual incorrecto.

### A9. ProgressionManager no conectado al sistema de quiz
- **Archivo**: `ProgressionManager.kt`
- **Problema**: El sistema de progresión está medio implementado. Quiz completion no llama a ProgressionManager. `resetProgress()` itera sobre todas las prefs.
- **Impacto**: Funcionalidad de desbloqueo secuencial no funciona. Todos los quizzes accesibles siempre.

### A10. SharedPreferences apply() race condition en accesibilidad
- **Archivo**: `AccesibilityHelper.kt:305`
- **Problema**: Usa `apply()` (async) pero el código lee inmediatamente después. Si la app reinicia rápido, se pierde la config.
- **Impacto**: Configuración de accesibilidad puede no persistir.

### A11. Room Type Converters con delimitadores colisionables
- **Archivos**: `UserProgressConverters.kt:36-74`, `QuizConverters.kt:63-70`
- **Problema**: Usan `:`, `,`, y `|||` como delimitadores para serializar Maps, Sets, y Lists. Si los datos contienen estos caracteres, se corrompe la deserialización. Ejemplo: `fromIntMap("1:2:3")` parsea incorrectamente.
- **Impacto**: Corrupción silenciosa de datos de progreso del usuario en Room.

### A12. Operaciones no atómicas en UserProgressDao
- **Archivo**: `UserProgressDao.kt:44-45`
- **Problema**: `totalQuizzes = totalQuizzes + 1` no es atómico en SQLite. Con actualizaciones concurrentes se pierden conteos.
- **Impacto**: Conteo de quizzes incorrecto tras uso prolongado.

### A13. Músculos con hotspots sin calibrar (hotspotX=0, hotspotY=0)
- **Archivo**: `DatosMusculares.kt`
- **Problema**: La mayoría de los músculos tienen valores default `hotspotX = 0f, hotspotY = 0f`. Solo algunos están calibrados con coordenadas reales.
- **Impacto**: La interacción táctil no funciona para esos músculos. El usuario toca la imagen y no pasa nada. Funcionalidad central de la app rota parcialmente.

### A14. Pregunta de quiz #25 referencia músculo inexistente
- **Archivo**: `QuizData.kt`
- **Problema**: La pregunta pregunta por "Músculo de la cresta ilíaca" pero no existe un músculo con ese nombre exacto en `DatosMusculares`.
- **Impacto**: Pregunta con posible respuesta incorrecta o confusa.

### A15. Índices faltantes en Room (regionId)
- **Archivo**: `QuizQuestionEntity`, entidades Room
- **Problema**: `regionId` se usa en WHERE clauses frecuentes pero no tiene `@Index`. La consulta `ORDER BY RANDOM()` hace full table scan.
- **Impacto**: Lentitud en la carga del quiz conforme crece la base de datos.

### A16. ColorblindType.NONE vs NORMAL — enum con valores duplicados
- **Archivo**: `ColorblindType.kt`
- **Problema**: El enum tiene AMBOS valores `NORMAL` y `NONE` como entradas distintas. Código usa indistintamente ambos. `AccessibilityConfig` defaulta a `NORMAL` pero partes comparan con `NONE`. Son valores diferentes en el enum → comparaciones fallan silenciosamente.
- **Impacto**: Configuración de daltonismo puede no aplicarse correctamente según qué valor se use.

---

## MEDIOS — Calidad de código y académica

### M1. fallbackToDestructiveMigration() en producción
- **Archivo**: `AppDatabase.kt:58`
- **Problema**: Cambio de esquema borra todos los datos del usuario.
- **Impacto**: Hoy no afecta (versión 1), pero si se cambia esquema antes de entregar, evaluadores pierden datos.

### M2. SharedPreferences duplicadas (DI vs internas)
- **Archivos**: `AccessibilityRepository.kt:25-27` + `AccesibilityHelper.kt:277,300`
- **Problema**: Repository crea sus propias SharedPreferences a pesar de que Hilt las provee. Helper también crea instancias separadas.
- **Impacto**: Viola principios de DI. Potencial inconsistencia de datos.

### M3. Strings hardcoded en layouts XML (~50+ instancias)
- **Archivos**: Todos los layouts bajo `res/layout/`
- **Problema**: Títulos de regiones, labels, descripciones de accesibilidad escritos directamente en XML en vez de usar `@string/`.
- **Impacto**: No se pueden traducir. Débil para evaluación académica de buenas prácticas.

### M4. Tests boilerplate vacíos
- **Archivos**: `ExampleUnitTest.kt`, `ExampleInstrumentedTest.kt`
- **Problema**: Solo contienen tests de placeholder sin valor real.
- **Impacto**: Ruido en el reporte de tests. Mal impresión académica.

### M5. Cobertura de tests insuficiente
- **Problema**: 0% cobertura de: achievements, ProgressionManager, integridad de datos entre fuentes, flujo completo de quiz, accesibilidad visual.
- **Impacto**: No hay seguridad de que el contenido anatómico sea correcto.

### M6. Recursive view traversal en accesibilidad
- **Archivo**: `AccesibilityHelper.kt:106-217`
- **Problema**: `applyColorsToButtonsRecursively()` y `applyTextScaleRecursively()` recorren toda la jerarquía de views en cada `onResume`.
- **Impacto**: Performance degradation en Activities complejas.

### M7. Animaciones sin cancelar en SplashActivity
- **Archivo**: `SplashActivity.kt:76-114`
- **Problema**: Múltiples animaciones iniciadas pero nunca canceladas en `onDestroy()`.
- **Impacto**: Memory leak si Activity se destruye durante splash.

### M8. Deprecated API usage en transiciones
- **Archivo**: `SplashActivity.kt:151-155`
- **Problema**: Usa `overrideActivityTransition()` deprecado en API 34+.
- **Impacto**: Puede fallar en versiones futuras de Android.

### M9. No hay layouts landscape ni tablet
- **Problema**: Solo existe `res/layout/`. No hay `res/layout-land/` ni `res/layout-sw600dp/`.
- **Impacto**: UX degradada en tablets y landscape.

### M10. Adaptador usa notifyDataSetChanged()
- **Archivo**: `AdaptadorMusculos.kt:58-61`
- **Problema**: Refresca toda la lista en vez de usar `DiffUtil`.
- **Impacto**: Performance en listas grandes. Parpadeo visual.

### M11. Dead code — legacy XML onClick functions
- **Archivo**: `RegionMenu.kt:117-142`
- **Problema**: Funciones `btnRegionCabeza(view)`, etc. marcadas como "Legacy functions for XML onClick compatibility".
- **Impacto**: Código muerto que confunde.

### M12. No hay ADRs ni documentación de decisiones
- **Problema**: No existe `docs/adr/`. Para una tesis de ingeniería, no hay justificación documentada de por qué se eligió MVVM, Room, Hilt, etc.
- **Impacto**: Débil para evaluación académica. Un jurado puede cuestionar decisiones sin evidencia.

### M13. Unsafe nullable handling en varios puntos
- **Archivos**: `DetalleMusculoViewModel.kt`, `RegionMenuViewModel.kt`, `BaseRegionActivity.kt`
- **Problema**: Fallbacks silenciosos (regionId default a 1, TipoRegion default a CABEZA, etc.) enmascaran errores.
- **Impacto**: Comportamiento silenciosamente incorrecto en vez de error claro.

### M14. 86 instancias de catch-all exception handling
- **Archivos**: Distribuidos en toda la app (AccesibilityHelper, ErrorHandler, Accesibilidad, etc.)
- **Problema**: `catch (e: Exception)` esconde bugs específicos y hace imposible diagnosticar problemas durante la evaluación SUS.
- **Impacto**: Errores silenciosos que se manifiestan como comportamiento incorrecto sin explicación.

### M15. Falta @Transaction en QuizRepository.saveQuizResultAsync
- **Archivo**: `QuizRepository.kt:129-173`
- **Problema**: Múltiples operaciones DB (leer-modificar-escribir) sin anotación `@Transaction`. No es atómico.
- **Impacto**: Datos inconsistentes si una operación falla a mitad de camino.

### M16. Código debug en producción
- **Archivo**: `InteractiveAnatomyView.kt:298`
- **Problema**: Comentario `// DEBUG:` con lógica que calcula distancias a todos los items. No debería estar en producción.
- **Impacto**: Información de debug visible, ligero overhead de performance.

### M17. QuizEngine.kt unsafe `!!` operator
- **Archivo**: `QuizEngine.kt:242`
- **Problema**: `currentSession!!.isCompleted` — uso innecesario de `!!` tras null check. Puede causar NPE.
- **Impacto**: Crash potencial si se llama en momento incorrecto.

### M18. Handler/Looper en QuizEngine (paquete raíz)
- **Archivo**: `QuizEngine.kt:36-37`
- **Problema**: Usa `Handler` y `Looper` para timer, mismo leak que QuizActivity. Versión del paquete raíz.
- **Impacto**: Memory leak en versión legacy del código.

---

## BAJOS — Code smells y mejoras menores

### B1. Generación del APK debug en raíz del proyecto
- **Archivo**: `CaballoApp-debug.apk` (192MB)
- **Problema**: APK de debug en el directorio raíz. No debería estar en el código fuente.

### B2. emulator.png en raíz del proyecto
- **Archivo**: `emulator.png` (1.3MB)
- **Problema**: Screenshot del emulador en el directorio raíz.

### B3. Magic numbers en XP/level calculations
- **Archivo**: Código de progresión
- **Problema**: Constantes como `XP_PER_CORRECT_ANSWER = 10` bien definidas, pero mezcladas con lógica de UI.

### B4. UI strings en ViewModel
- **Archivo**: `AccessibilityViewModel.kt`
- **Problema**: Strings como `"Configuración guardada. Modo: $typeName"` en ViewModel en vez de resources.

### B5. Ejemplo de uso de `entries` en vez de `values()`
- **Archivo**: `RegionMenuViewModel.kt:69`
- **Problema**: Usa `TipoRegion.entries` (Kotlin 1.9+). Compatible pero podría confundir.

### B6. Sin landscape ni tablet layouts
- **Problema**: App solo funciona bien en portrait en teléfono.

---

## Resumen Estadístico

| Severidad | Cantidad | Bloquea evaluación SUS |
|---|---|---|
| CRÍTICO | 8 | Sí |
| ALTO | 16 | Potencialmente |
| MEDIO | 18 | No directamente |
| BAJO | 6 | No |
| **TOTAL** | **48** | |

## Priorización sugerida para entrega

### Fase 0 — Pre-requisitos (sin esto no hay APK)
0. C0 — Eliminar 32+ archivos duplicados del paquete raíz
1. C0b — Crear keystore y configurar firma para release APK

### Fase 1 — Estabilidad (bloqueante para evaluación SUS)
2. C1 — Race condition pre-población Room
3. C2 — Handler leak QuizActivity
4. C3 — Race condition quiz double-click
5. C4 — Estado quiz perdido en rotación
6. C5 — Handler leak Accesibilidad
7. C6 — Callback leak Tutorial
8. A5 — IndexOutOfBounds opciones quiz
9. A3 — Region IDs inconsistentes
10. A13 — Hotspots sin calibrar (músculos no interactivos)

### Fase 2 — Integridad de datos
11. A1 — Doble ruta de guardado quiz
12. A10 — SharedPreferences apply() race
13. A11 — Type converters con delimitadores colisionables
14. A12 — Operaciones no atómicas UserProgressDao
15. A6 — Navigation back stack
16. A7+A8 — Image loading fallbacks
17. A14 — Pregunta quiz músculo inexistente

### Fase 3 — Calidad académica del código
13. M3 — Strings hardcoded
14. M4+M5 — Tests vacíos + cobertura
15. M12 — ADRs documentación
16. M2 — SharedPreferences duplicadas
17. Resto de MEDIUMs y LOWs

### Fase 4 — Limpieza pre-entrega
18. B1+B2 — Archivos basura en raíz
19. B4 — UI strings en ViewModel
20. B5+B6 — Minor code smells

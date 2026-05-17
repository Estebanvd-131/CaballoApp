# PRD: Estabilización y Corrección de EquusApp Pre-Entrega Final

## Problem Statement

EquusApp, la aplicación educativa de miología del Caballo Criollo Colombiano, fue desarrollada por desarrolladores junior y necesita ser entregada como producto final para dos propósitos: (1) evaluación de usabilidad SUS con 30 estudiantes de Medicina Veterinaria de la Universidad Santiago de Cali, y (2) entrega académica como parte de una tesis interdisciplinaria.

Una auditoría quirúrgica del código reveló **48 problemas** (8 críticos, 16 altos, 18 medios, 6 bajos) que incluyen: crashes por memory leaks, race conditions que dejan pantallas vacías en instalación limpia, 32+ archivos Kotlin duplicados de un refactoring incompleto, contenido anatómico incorrecto por inconsistencia de regiones, y la imposibilidad de generar un APK firmado para distribución. En su estado actual, la app **no puede ser entregada** a usuarios reales.

## Solution

Implementar un plan de estabilización en 4 fases secuenciales que resuelva primero los problemas que bloquean la evaluación SUS (crashes, datos vacíos, APK no distribuible), luego los que comprometen la integridad del contenido anatómico, y finalmente los que afectan la calidad académica del código entregado. Cada fase es un gate: no se avanza hasta que la anterior está completa y verificada.

## User Stories

### Fase 0 — Prerrequisitos de compilación y distribución

1. Como desarrollador, quiero eliminar los 32+ archivos Kotlin duplicados del paquete raíz, para que la app compile sin conflictos de clases duplicadas.
2. Como desarrollador, quiero crear un keystore y configurar la firma de release en el build, para poder generar un APK firmado distribuible a los estudiantes.
3. Como desarrollador, quiero limpiar los archivos basura del directorio raíz (APK debug de 192MB, screenshot de emulador), para mantener un repositorio limpio.

### Fase 1 — Estabilidad para evaluación SUS

4. Como estudiante evaluador, quiero que la app muestre músculos y preguntas al instalarla por primera vez, para poder usarla inmediatamente sin pantallas vacías.
5. Como estudiante evaluador, quiero poder completar un quiz sin que la app se cierre inesperadamente, para que mi progreso no se pierda.
6. Como estudiante evaluador, quiero poder rotar mi teléfono durante un quiz sin que se reinicie, para poder usar la app cómodamente.
7. Como estudiante evaluador, quiero que los hotspots táctiles de los músculos funcionen correctamente, para poder interactuar con la anatomía como está diseñado.
8. Como estudiante evaluador, quiero que el quiz de cada región contenga preguntas correctas para esa región, para que el contenido anatómico sea preciso.
9. Como estudiante evaluador, quiero poder responder rápidamente sin que clicks dobles causen comportamiento errático, para una experiencia fluida.
10. Como estudiante evaluador, quiero poder cambiar opciones de accesibilidad sin que la app se cierre, para usar la app con mis necesidades visuales.
11. Como estudiante evaluador, quiero que el tutorial no cause problemas de memoria, para que la primera experiencia sea fluida.

### Fase 2 — Integridad de datos y contenido

12. Como estudiante, quiero que mi progreso de quiz se guarde de forma consistente en una sola fuente de verdad, para ver estadísticas correctas.
13. Como estudiante, quiero que mi configuración de accesibilidad persista correctamente, para no tener que reconfigurar cada vez.
14. Como estudiante, quiero que el botón back me lleve a la pantalla correcta, para navegar intuitivamente sin Activities duplicadas.
15. Como estudiante, quiero ver la imagen correcta de cada músculo según su región, para que el contenido visual sea anatómicamente preciso.
16. Como estudiante, quiero que las preguntas del quiz referencien músculos que existen, para que las respuestas sean correctas.
17. Como estudiante, quiero que mi progreso (XP, nivel, racha) se calcule de forma atómica, para que no se pierdan datos por operaciones concurrentes.
18. Como estudiante, quiero que los datos serializados en Room no se corrompan, para que mi progreso sea confiable.

### Fase 3 — Calidad académica del código

19. Como evaluador académico, quiero ver strings externalizados en resources, para evidenciar buenas prácticas Android.
20. Como evaluador académico, quiero ver tests con cobertura significativa de los módulos críticos, para validar la calidad del software.
21. Como evaluador académico, quiero ver ADRs que documenten las decisiones arquitectónicas (MVVM, Room, Hilt), para evaluar el rigor ingenieril.
22. Como evaluador académico, quiero que no haya código debug en producción, para ver un producto profesional.
23. Como evaluador académico, quiero que el enum ColorblindType tenga un solo valor para "visión normal", para ver consistencia en el modelo de dominio.
24. Como evaluador académico, quiero ver exception handling específico en vez de catch-all genéricos, para evaluar robustez.

### Fase 4 — Pulido final

25. Como desarrollador, quiero eliminar dead code (legacy onClick handlers, deprecated HotspotHelper), para entregar código limpio.
26. Como desarrollador, quiero que UI strings no estén en ViewModels, para seguir separación de responsabilidades.
27. Como evaluador académico, quiero que el ProgresoManager esté conectado al sistema de quiz o eliminado, para no entregar funcionalidad a medias.

## Implementation Decisions

### Módulos a construir/modificar

**Módulo 1: Limpieza de paquetes**
- Eliminar los 32+ archivos Kotlin del paquete raíz `com.villalobos.caballoapp.*` que ya existen en los subpaquetes correctos (`ui.*`, `data.*`, `di.*`, `core.*`, `util.*`).
- El Manifest ya referencia las versiones correctas en subpaquetes. Los archivos raíz son vestigios de un refactoring incompleto.
- Se mantiene `RegionDistal.kt` si no existe duplicado, y `AdaptadorMusculos.kt` se reubica a `ui/region/` si ya no está ahí.

**Módulo 2: Configuración de release**
- Crear keystore de firma con `keytool`.
- Agregar `signingConfigs` y `buildTypes.release` con firma en `app/build.gradle.kts`.
- Las credenciales del keystore se almacenarán en `keystore.properties` (excluido de control de versiones).

**Módulo 3: Pre-población de Room**
- Eliminar `PrepopulateCallback` que usa `INSTANCE?.let` (race condition: INSTANCE es null cuando el callback se ejecuta).
- Mover la lógica de pre-población a una función `prepopulateIfNeeded()` que se ejecuta en `getDatabase()` después de asignar `INSTANCE`.
- Usar `MusculoDao.getMusculoCount() == 0` como guard para evitar duplicados.
- Ejecutar en `CoroutineScope(Dispatchers.IO)` ya existente.

**Módulo 4: Timer del quiz sin Handler**
- Reemplazar `Handler.postDelayed()` en `QuizActivity` por `lifecycleScope.launch { delay(1000) }` dentro de un loop.
- El ViewModel expone el tiempo transcurrido como LiveData. La Activity solo observa.
- Al destruir la Activity, el coroutine se cancela automáticamente con `lifecycleScope`.

**Módulo 5: Estado del quiz resilient a rotación**
- Mover `regionId` a `SavedStateHandle` en `QuizViewModel`, siguiendo el patrón que ya usa `CorrectAnswersViewModel`.
- Agregar guard en `answerQuestion()` para prevenir double-click (disable button en UI + check de estado en ViewModel).
- Validar que `options.size >= 4` antes de acceder por índice.

**Módulo 6: Consistencia de regiones**
- Unificar el contrato de regiones: `DatosMusculares` define 6 regiones (1-6), `QuizData` debe usar los mismos IDs.
- Las preguntas de "Sacro Caudal y Glútea" (regionId 6 en QuizData) deben alinearse con los músculos pélvicos (regionId 5 en DatosMusculares), o bien crear una séptima región con datos propios.
- `RegionIds.normalize()` debe reflejar la decisión final.
- `AchievementData` debe cubrir todas las regiones activas.

**Módulo 7: Hotspots calibrados**
- Verificar qué músculos tienen `hotspotX = 0f, hotspotY = 0f` (no calibrados).
- Calibrar usando el flujo de debug de `COORDINATES_GUIDE.md` (activar debugMode, tocar posición, leer coordenadas, actualizar DatosMusculares).
- Los músculos sin hotspot calibrado no responden al toque, rompiendo la funcionalidad central.

**Módulo 8: Fuente de verdad única para resultados**
- Eliminar la ruta legacy de SharedPreferences para resultados del quiz.
- Unificar `QuizRepository.completeQuiz()` para guardar exclusivamente via Room (`UserProgressDao`).
- Eliminar `saveQuizResult()` en SharedPreferences y los métodos legacy que leen de `quiz_stats`.

**Módulo 9: Accesibilidad robusta**
- Reemplazar `Handler.postDelayed()` en `Accesibilidad` por `lifecycleScope.launch { delay() }`.
- Eliminar parámetros `Activity` de `AccessibilityViewModel`. La configuración se aplica via Application context o un observer pattern.
- Unificar `ColorblindType`: eliminar `NONE`, usar solo `NORMAL` en todo el código.
- Cambiar `apply()` por `commit()` en guardado crítico de accesibilidad, o asegurar que la lectura no dependa de escritura inmediata.
- Unificar SharedPreferences: inyectar via Hilt en vez de crear instancias internas.

**Módulo 10: Image loading con fallback correcto**
- `DrawableNameResolver` debe retornar un ID de recurso válido específico por región, no 0.
- `DetalleMusculo` debe usar fallback por región (no siempre `cabeza_lateral`).
- Agregar logging cuando un drawable no se encuentra.

**Módulo 11: Room converters seguros**
- Reemplazar delimitadores custom (`:`, `,`, `|||`) por serialización JSON (usando Gson o kotlinx.serialization) en `QuizConverters` y `UserProgressConverters`.
- Agregar `@Index` en `regionId` de `QuizQuestionEntity`.
- Agregar `@Transaction` en operaciones multi-paso de `QuizRepository`.
- Hacer operaciones de conteo atómicas con SQL directo (`SET totalQuizzes = totalQuizzes + 1` ya es atómico en SQLite si se usa en un `@Query` con `@Transaction`).

**Módulo 12: Tutorial sin leaks**
- Guardar referencia al `OnPageChangeCallback` en `TutorialActivity` y desregistrar en `onDestroy()`.
- Guardar referencia al `OnBackPressedCallback` y removerlo en `onDestroy()`.

### Decisiones de dominio

- **Región 6 (Distal)**: Los músculos en `DatosMusculares` con regionId 6 son "Región Distal (Casco)". Las preguntas en `QuizData` con regionId 6 son "Sacro Caudal y Glútea". Se debe alinear: si Sacro Caudal es subregión de Pélvica (regionId 5), las preguntas migran a regionId 5. Si es región propia, se agrega al modelo y se crean sus músculos.
- **ColorblindType.NORMAL**: Se elimina `NONE` del enum. `NORMAL` es el valor canónico. Se actualizan todas las comparaciones.
- **Fuente de verdad de progreso**: Room es la única fuente. SharedPreferences legacy se elimina completamente.

## Testing Decisions

### Qué hace un buen test
- Tests de comportamiento externo, no detalles de implementación.
- Tests que fallen si el bug se reintroduce (regression tests).
- Tests que validen invariantes del dominio (ej: "todo quiz question referencia músculos existentes").

### Módulos a testear

**Prioridad alta (regression tests para bugs críticos):**
1. `AppDatabase` — Verificar que pre-población inserta datos correctamente (test de integración con in-memory DB).
2. `QuizViewModel` — Verificar que `answerQuestion()` es idempotente ante double-click, que `regionId` sobrevive rotación via SavedStateHandle.
3. `QuizRepository` — Verificar que resultados se guardan solo en Room, no SharedPreferences.
4. `DatosMusculares` + `QuizData` — Cross-validation: todos los regionId coinciden, todos los correctAnswer son índices válidos, los músculos referenciados existen.
5. `ColorblindType` — Verificar que solo existe un valor "normal" (NORMAL), que NONE no existe.

**Prioridad media (coverage de funcionalidad):**
6. `TypeConverters` — Verificar serialización/deserialización de Maps, Sets, Lists con datos que contengan caracteres especiales.
7. `DrawableNameResolver` — Verificar que retorna IDs válidos para nombres conocidos y fallback correcto para desconocidos.
8. `AchievementData` — Verificar que condiciones de logros son alcanzables con el código actual.

**Prioridad baja (existen ya):**
9. ViewModels existentes ya tienen tests. Ampliar con edge cases documentados.

### Prior art
- `DatosMuscularesIntegrityTest` valida resolución de drawables — usar como patrón para tests de integridad de datos.
- `ZoneDetailContractTest` valida contratos zona-músculo — extender para validar regionId consistency.
- `MusculoRepositoryIntegrationTest` usa in-memory Room — usar mismo patrón para test de pre-población.

## Out of Scope

- **Migración a Jetpack Compose**: La UI es XML+ViewBinding. No se reescribe.
- **Navigation Component**: Se mantiene navegación basada en Intents. No se introduce Navigation Graph.
- **Landscape/tablet layouts**: La app funciona en portrait en teléfonos. No se agregan layouts alternativos.
- **Internacionalización (i18n)**: Los strings hardcoded se externalizan a `strings.xml` pero no se traducen a otros idiomas.
- **DiffUtil en AdaptadorMusculos**: Se mantiene `notifyDataSetChanged()`. La lista es pequeña (<50 items).
- **ProgressionManager completo**: Se elimina o se conecta mínimamente. No se implementa el sistema completo de desbloqueo.
- **Sistema de achievements funcional**: Se documentan como incompletos. No se implementan los TODOs de `musclesStudied` y `studyStreak`.
- **ProGuard optimization**: Las reglas actuales son suficientes. No se agregan reglas adicionales.

## Further Notes

### Contexto académico
Esta app es parte de una tesis interdisciplinaria entre Medicina Veterinaria e Ingeniería de Sistemas de la Universidad Santiago de Cali. La evaluación SUS con N=30 estudiantes es un requisito metodológico de la tesis. Un crash o pantalla vacía durante la evaluación invalidaría los resultados y retrasaría la graduación.

### Fichas de referencia
- `docs/DIAGNOSTICO_QUIRURGICO.md` — Detalle completo de los 48 problemas con archivos y líneas.
- `CONTEXT.md` — Glosario del dominio, fuentes de verdad y flujo de pantallas.
- `COORDINATES_GUIDE.md` — Guía de calibración de hotspots.
- `GUIA_PROYECTO_EQUUSAPP.md` — Documentación general del proyecto.

### Fases son gates
Cada fase es un gate de calidad. No se avanza a la siguiente hasta que:
- **Fase 0**: La app compila sin errores y se genera un release APK firmado.
- **Fase 1**: Se puede completar un flujo completo (Splash → Región → Tocar músculo → Quiz → Resultados) sin crashes en instalación limpia.
- **Fase 2**: Los datos persisten correctamente y el contenido anatómico es consistente.
- **Fase 3**: Los tests pasan y la documentación está completa.

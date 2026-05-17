# Guía completa del proyecto EquusApp (para cualquier persona)

> Última actualización: 2026-02-24  
> Proyecto: **EquusApp** (Android nativo, Kotlin)

## Estado de mejoras (implementado)

- Se unificó el contrato de IDs de región con compatibilidad legacy (`6` y `7` para distal).
- El flujo de regiones ahora usa carga **Room-first** con fallback controlado.
- El flujo de quiz ahora inicia y guarda resultados por ruta asíncrona de `Room`.
- Se agregaron pruebas de integración (`androidTest`) para repositorios y una prueba UI smoke de `MainActivity`.

## 1) ¿Qué es EquusApp y para qué sirve?

**EquusApp** es una app educativa offline para estudiar la miología del Caballo Criollo Colombiano.

Permite:
- explorar regiones anatómicas,
- tocar zonas/músculos en imágenes interactivas,
- ver detalle anatómico de cada músculo,
- practicar con quiz,
- usar modos de accesibilidad (daltonismo, contraste, texto grande).

Está alineada con la visión pedagógica en:
- `PRD_CaballoApp.md`
- `contexto.txt`

---

## 2) Arranque rápido (primer día de un dev nuevo)

### Requisitos
- Android Studio (recomendado versión reciente)
- JDK 11
- SDK Android con `compileSdk 35`
- Gradle Wrapper (ya viene en el repo)

### Ejecutar
1. Abrir la carpeta raíz del repo.
2. Sincronizar Gradle.
3. Ejecutar `app` en emulador/dispositivo Android (API 24+).

### Build/test por terminal (Windows)
```bash
.\gradlew.bat assembleDebug
.\gradlew.bat test
```

---

## 3) Stack tecnológico (estado real del código)

- **Lenguaje**: Kotlin
- **Arquitectura**: MVVM
- **DI**: Hilt
- **UI**: XML + ViewBinding + Activities (+ 1 Fragment en tutorial)
- **Persistencia**:
  - Room (músculos, preguntas, progreso)
  - SharedPreferences (accesibilidad, tutorial, estadísticas legacy)
- **Concurrencia**: Coroutines + Flow

Archivos clave:
- `app/build.gradle.kts`
- `gradle/libs.versions.toml`
- `app/src/main/AndroidManifest.xml`

---

## 4) Mapa de arquitectura por capas

## Capa Core
- `core/EquusApplication.kt`: punto de entrada de aplicación (Hilt).

## Capa DI
- `di/RepositoryModule.kt`: provee Room, DAOs, SharedPreferences y Repositories.

## Capa Data
- `data/model`: modelos (`Musculo`, `Region`, `Zona`, `QuizQuestion`, etc.).
- `data/source`: datos hardcoded (`DatosMusculares`, `QuizData`, `AchievementData`).
- `data/local`: Room (`AppDatabase`, entities, converters, DAOs).
- `data/repository`: puente entre UI y datos (`MusculoRepository`, `QuizRepository`, `AccessibilityRepository`).

## Capa UI
- `ui/splash`: inicio visual de app.
- `ui/main`: menú principal + stats.
- `ui/region`: menú de regiones y pantallas regionales.
- `ui/detail`: detalle de músculo.
- `ui/quiz`: quiz y pantalla de respuestas.
- `ui/accessibility`: configuración de accesibilidad.
- `ui/tutorial`: onboarding con ViewPager2 + Fragment.
- `ui/base`: clases base compartidas de navegación/accesibilidad.
- `ui/components`: componentes personalizados (`InteractiveAnatomyView`).

## Capa Utilidades
- `util`: helpers transversales (accesibilidad, errores, anti-doble-click, animaciones, progresión).

---

## 5) Flujo funcional de la app (de principio a fin)

1. **Launch**
   - `SplashActivity` (launcher en Manifest)
   - espera breve + animaciones + pasa a `MainActivity`.

2. **Pantalla principal** (`MainActivity`)
   - botones: iniciar, accesibilidad, créditos, salir.
   - observa stats de usuario desde `MainViewModel`.
   - si es primera vez, lanza tutorial.

3. **Menú de regiones** (`RegionMenu`)
   - el usuario elige una región anatómica.

4. **Pantalla regional** (`RegionCabeza/Cuello/Tronco/Toracica/Pelvica/Distal`)
   - heredan de `BaseRegionActivity`.
   - cargan músculos + zonas por `RegionViewModel`.
   - detectan toques en imagen con `InteractiveAnatomyView`.
   - muestran lista de músculos y navegan al detalle.
   - botón para quiz de la región.

5. **Detalle del músculo** (`DetalleMusculo`)
   - muestra información anatómica detallada.

6. **Quiz** (`QuizActivity`)
   - sesión de preguntas por región o global.
   - score + tiempo + métricas.
   - muestra respuestas correctas en `CorrectAnswersActivity`.

7. **Accesibilidad** (`Accesibilidad`)
   - configura modo de color, contraste, tamaño de texto.

8. **Tutorial** (`TutorialActivity` + `TutorialPasoFragment`)
   - onboarding en pasos, controlado por preferencia de primera ejecución.

---

## 6) Flujo de datos explicado simple

## A) Contenido anatómico (regiones y músculos)
`DatosMusculares (hardcoded)` → `MusculoRepository` → `RegionViewModel` → `BaseRegionActivity` / adaptador / vista interactiva.

## B) Quiz
`QuizData (hardcoded)` o Room → `QuizRepository` → `QuizViewModel` → `QuizActivity`.

## C) Progreso del usuario
- Ruta moderna: Room (`UserProgressEntity`)
- Ruta legacy: SharedPreferences (`quiz_stats`)

`MainViewModel` observa `QuizRepository.getUserProgressFlow()` y actualiza nivel/xp/racha.

## D) Accesibilidad
`Accesibilidad` UI → `AccessibilityViewModel` → `AccessibilityRepository` / `AccesibilityHelper` → SharedPreferences + aplicación visual.

---

## 7) Persistencia: qué se guarda y dónde

## Room (`equusapp_database`)
- `musculos`
- `quiz_questions`
- `user_progress`

Se inicializa en `AppDatabase` y pre-carga datos desde `DatosMusculares` y `QuizData` al crearse DB.

## SharedPreferences
- `accessibility_prefs`: configuración visual/accesibilidad.
- `tutorial_prefs`: bandera para mostrar/no mostrar tutorial.
- `quiz_stats`: estadísticas legacy del quiz.

---

## 8) Sistema de hotspots (toques en la imagen)

El componente clave es:
- `ui/components/InteractiveAnatomyView.kt`

Usa coordenadas normalizadas (`0.0` a `1.0`) para que funcione en cualquier resolución.

Guía detallada de calibración:
- `COORDINATES_GUIDE.md`

---

## 9) Cómo agregar contenido nuevo (paso a paso)

## Caso 1: agregar un nuevo músculo
1. Añadir músculo en `DatosMusculares.kt` con:
   - datos anatómicos,
   - `regionId`,
   - `hotspotX`/`hotspotY`.
2. Verificar que aparece en la lista de su región.
3. Verificar que se puede tocar en la imagen.
4. Ajustar coordenadas con la guía de calibración si hace falta.

## Caso 2: agregar preguntas de quiz
1. Añadir preguntas en `QuizData.kt`.
2. Revisar `regionId` de la pregunta.
3. Ejecutar quiz por región y validar score/resultados.

## Caso 3: agregar una nueva región (si aplica)
1. Definir región en modelos/datos (`Region`, fuentes de datos).
2. Agregar botón de región en UI menú (`RegionMenu`).
3. Crear activity regional heredando `BaseRegionActivity`.
4. Registrar en `AndroidManifest.xml`.
5. Cargar imagen y hotspots de la región.

---

## 10) Testing: qué existe hoy

Pruebas unitarias en `app/src/test`:
- ViewModels: `Main`, `Region`, `DetalleMusculo`, `Quiz`
- Repositories: `MusculoRepository`, `AccessibilityRepository`
- Validación de modo daltonismo: `ColorblindModeTest`

Comando:
```bash
.\gradlew.bat test
```

No hay cobertura fuerte de UI instrumentada end-to-end en flujo de navegación.

---

## 11) Riesgos/deudas técnicas actuales (importante para continuar)

1. **Fuentes de verdad duplicadas**
   - Conviven Room + hardcoded + SharedPreferences para datos parecidos.

2. **Inconsistencia potencial de IDs por región**
   - Hay que cuidar alineación entre `DatosMusculares` y `QuizData`.

3. **QuizViewModel guarda por ruta legacy**
   - En `completeQuiz()` usa `saveQuizResult(...)` (SharedPreferences), no la ruta async completa de Room.

4. **Pre-población de Room sensible a timing**
   - `AppDatabase.PrepopulateCallback` usa `INSTANCE` en `onCreate`.

5. **ProgressionManager parcial**
   - Hay lógica de progresión aún simplificada.

---

## 12) Recomendaciones prácticas para próximos sprints

1. Definir **fuente de verdad única** por feature (preferir Room para progreso/quiz).
2. Centralizar IDs de regiones en un contrato único (enum/objeto común).
3. Unificar guardado de resultados del quiz en la vía async persistente.
4. Añadir pruebas instrumentadas mínimas de navegación crítica.
5. Mantener esta guía viva en cada PR estructural.

---

## 13) Glosario rápido (para mortales)

- **MVVM**: forma de separar UI, lógica y datos.
- **Repository**: capa que decide de dónde salen/guardan datos.
- **Room**: base de datos local SQLite con APIs Kotlin.
- **Flow**: flujo reactivo de datos (se actualiza solo).
- **Hilt**: inyección de dependencias automática.
- **Hotspot**: punto táctil en la imagen para seleccionar zona/músculo.

---

## 14) Checklist de onboarding (nuevo integrante)

- [ ] Ejecuté la app en emulador/dispositivo.
- [ ] Entendí flujo: Splash → Main → Región → Detalle → Quiz.
- [ ] Revisé `DatosMusculares.kt` y `QuizData.kt`.
- [ ] Leí `COORDINATES_GUIDE.md`.
- [ ] Corrí pruebas unitarias (`.\gradlew.bat test`).
- [ ] Identifiqué una mejora técnica para primer PR.

---

## 15) PDF: cómo generarlo desde este Markdown

No se detectó `pandoc` en el entorno actual. Opciones rápidas:

1. **VS Code**
   - Abrir este archivo Markdown.
   - `Ctrl+Shift+V` (preview).
   - Imprimir como PDF desde el preview (o extensión de export Markdown a PDF).

2. **Con Pandoc (si lo instalas)**
```bash
pandoc GUIA_PROYECTO_EQUUSAPP.md -o GUIA_PROYECTO_EQUUSAPP.pdf
```

---

## 16) Archivos más importantes para entender el proyecto rápido

- `app/src/main/AndroidManifest.xml`
- `app/src/main/java/com/villalobos/caballoapp/ui/main/MainActivity.kt`
- `app/src/main/java/com/villalobos/caballoapp/ui/base/BaseRegionActivity.kt`
- `app/src/main/java/com/villalobos/caballoapp/ui/components/InteractiveAnatomyView.kt`
- `app/src/main/java/com/villalobos/caballoapp/data/repository/MusculoRepository.kt`
- `app/src/main/java/com/villalobos/caballoapp/data/repository/QuizRepository.kt`
- `app/src/main/java/com/villalobos/caballoapp/data/local/AppDatabase.kt`
- `app/src/main/java/com/villalobos/caballoapp/di/RepositoryModule.kt`

---


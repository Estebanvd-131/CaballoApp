# Guía del Sistema de Coordenadas y Calibración - EquusApp

Este documento explica cómo funciona el sistema de detección táctil en la aplicación, dónde se almacenan los datos de los músculos y cómo calibrar nuevas regiones o corregir las existentes.

## 1. Concepto General: Coordenadas Normalizadas

La aplicación utiliza un sistema de **coordenadas normalizadas** para detectar qué músculo está tocando el usuario.

*   **Independencia de Resolución**: En lugar de usar píxeles (ej. `x=500, y=300`), usamos valores entre `0.0` y `1.0`.
*   **Eje X (Horizontal)**: `0.0` es el borde izquierdo, `1.0` es el borde derecho de la imagen.
*   **Eje Y (Vertical)**: `0.0` es el borde superior, `1.0` es el borde inferior de la imagen.

Esto garantiza que la detección funcione correctamente en **cualquier dispositivo**, sin importar el tamaño de la pantalla o la densidad de píxeles.

## 2. Ubicación del Código

### A. Datos (Coordenadas)
El archivo que contiene la definición de todos los músculos y sus posiciones es:
`app/src/main/java/com/villalobos/caballoapp/DatosMusculares.kt`

Cada objeto `Musculo` tiene dos propiedades clave:
```kotlin
Musculo(
    // ... otros datos ...
    hotspotX = 0.241f, // Posición horizontal (24.1% de la imagen)
    hotspotY = 0.120f, // Posición vertical (12.0% de la imagen)
    // ...
)
```

### B. Lógica (Vista Interactiva)
El componente que procesa los toques y dibuja la imagen es:
`app/src/main/java/com/villalobos/caballoapp/ui/components/InteractiveAnatomyView.kt`

Este componente se encarga de:
1.  Detectar el toque en la pantalla.
2.  Convertir los píxeles de pantalla a coordenadas de la imagen (considerando el escalado `fitXY` o `centerCrop`).
3.  Normalizar esas coordenadas a valores `0.0 - 1.0`.
4.  Calcular la distancia entre el toque y los `hotspotX/Y` definidos.
5.  Determinar si el toque está dentro de la `touchTolerance` (tolerancia).

## 3. Cómo Calibrar o Modificar Coordenadas

Si necesitas ajustar la posición de un músculo o añadir uno nuevo, sigue estos pasos:

### Paso 1: Activar el Modo Debug
Para ver dónde estás tocando exactamente, debes activar el modo de depuración en `InteractiveAnatomyView.kt`.

1.  Abre `app/src/main/java/com/villalobos/caballoapp/ui/components/InteractiveAnatomyView.kt`.
2.  Busca la propiedad `debugMode` y cámbiala a `true`:
    ```kotlin
    var debugMode: Boolean = true
    ```
3.  (Opcional) Aumenta la tolerancia para facilitar las pruebas:
    ```kotlin
    const val DEFAULT_TOUCH_TOLERANCE = 0.15f // 15% para pruebas
    ```

### Paso 2: Obtener las Coordenadas
1.  Ejecuta la aplicación en el emulador o dispositivo.
2.  Ve a la región que quieres calibrar.
3.  Verás círculos rojos (donde la app *cree* que están los músculos actualmente).
4.  **Toca en la pantalla** justo donde debería estar el músculo.
5.  Aparecerá un círculo azul y un texto en pantalla (o en los logs de Logcat) con las coordenadas exactas del toque:
    *   Ejemplo visual: `Touch: (0.241, 0.120)`
    *   Logcat: Busca el tag `InteractiveAnatomyView` -> `Coordenadas normalizadas: (0.2412, 0.1205)`

### Paso 3: Actualizar los Datos
1.  Abre `DatosMusculares.kt`.
2.  Busca el músculo correspondiente (por su `id` o `nombre`).
3.  Actualiza `hotspotX` y `hotspotY` con los valores que obtuviste en el paso anterior.

```kotlin
// Antes
hotspotX = 0.45f,
hotspotY = 0.35f,

// Después (con tus valores nuevos)
hotspotX = 0.241f,
hotspotY = 0.120f,
```

### Paso 4: Desactivar Debug
Una vez corregidos todos los músculos:
1.  Vuelve a `InteractiveAnatomyView.kt`.
2.  Pon `debugMode = false`.
3.  Restaura la tolerancia a un valor preciso (recomendado `0.06f`):
    ```kotlin
    const val DEFAULT_TOUCH_TOLERANCE = 0.06f
    ```

## 4. Parámetros de Configuración

En `InteractiveAnatomyView.kt` puedes ajustar:

*   **`touchTolerance`**: Define qué tan cerca debe tocar el usuario para activar el músculo.
    *   `0.05f` (5%): Muy preciso, requiere tocar casi exacto.
    *   `0.15f` (15%): Muy permisivo, útil si los músculos son grandes o están separados.
*   **`showTouchFeedback`**: `true`/`false`. Muestra un círculo momentáneo al tocar un músculo válido.

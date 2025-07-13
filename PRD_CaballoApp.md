# PRD - Aplicación de Miología del Caballo Criollo Colombiano

## 1. Resumen Ejecutivo

### 1.1 Visión del Producto
Desarrollar una aplicación móvil interactiva, de acceso libre y científicamente fundamentada, como herramienta pedagógica para el estudio de la miología y biomecánica funcional del Caballo Criollo Colombiano, dirigida a estudiantes y profesionales de medicina veterinaria.

### 1.2 Problema a Resolver
Las aplicaciones interactivas existentes para el estudio de la miología equina son limitadas y poco accesibles, ya que la mayoría requieren pago por suscripción o presentan deficiencias técnicas significativas. Existe una brecha en el mercado para una herramienta gratuita, especializada y de alta calidad.

### 1.3 Propuesta de Valor
- **Gratuita**: Acceso libre sin costo para estudiantes y profesionales
- **Especializada**: Enfoque específico en el Caballo Criollo Colombiano
- **Científicamente fundamentada**: Basada en investigación anatómica rigurosa
- **Interactiva**: Interfaz táctil intuitiva con exploración por regiones
- **Accesible**: Diseño inclusivo para personas con deficiencias visuales
- **Offline**: Funcionalidad sin conexión a internet

## 2. Objetivos del Producto

### 2.1 Objetivos Primarios
- Facilitar el aprendizaje de la miología equina mediante una interfaz interactiva
- Proporcionar información anatómica completa (origen, inserción, biomecánica) de cada músculo
- Mejorar la eficiencia en las metodologías de estudio de los estudiantes
- Democratizar el acceso a herramientas educativas de alta calidad

### 2.2 Objetivos Secundarios
- Evidenciar la eficacia del trabajo interdisciplinario entre ingeniería y medicina veterinaria
- Establecer un precedente para herramientas educativas adaptadas a contextos locales
- Contribuir a la digitalización de la educación veterinaria

## 3. Alcance del Producto

### 3.1 Incluido en el Alcance
- **Contenido**: Miología superficial y profunda del Caballo Criollo Colombiano
- **Plataforma**: Aplicación nativa para Android
- **Idioma**: Español completamente
- **Población objetivo**: Estudiantes de medicina veterinaria
- **Funcionalidad**: Exploración interactiva por regiones corporales
- **Accesibilidad**: Características inclusivas para deficiencias visuales

### 3.2 Excluido del Alcance
- Otros sistemas anatómicos (osteológico, nervioso, circulatorio) como contenido principal
- Versiones para iOS o aplicaciones web
- Idiomas adicionales al español
- Otras razas de caballos
- Funcionalidades de red social o colaborativas

## 4. Especificaciones Técnicas

### 4.1 Arquitectura
- **Plataforma**: Android (API nivel mínimo por definir)
- **Lenguaje**: Kotlin
- **Patrón arquitectónico**: MVVM (Model-View-ViewModel)
- **Base de datos**: Room (SQLite local)
- **Funcionalidad**: Offline-first

### 4.2 Componentes Principales
1. **Visor Anatómico Interactivo**
   - Zoom y paneo
   - Hotspots clicables
   - Navegación por regiones

2. **Base de Datos Local**
   - Información muscular estructurada
   - Imágenes optimizadas
   - Búsqueda eficiente

3. **Sistema de Navegación**
   - Menú por regiones corporales
   - Filtros de contenido
   - Búsqueda de músculos

## 5. Requerimientos Funcionales

### 5.1 Funcionalidades Core
#### RF-001: Visualización Interactiva
- El usuario debe poder ver ilustraciones del caballo organizadas por regiones
- Debe permitir zoom y paneo para explorar detalles
- Las áreas musculares deben ser clicables (hotspots)

#### RF-002: Información Muscular Detallada
- Al tocar un músculo, mostrar: nombre, origen, inserción, función biomecánica
- Incluir vista aislada del músculo cuando aplique
- Información presentada en ventanas emergentes o pantallas dedicadas

#### RF-003: Navegación por Regiones
- Menú de navegación para filtrar por regiones corporales:
  - Cabeza
  - Cuello
  - Tronco
  - Miembros torácicos
  - Miembros pélvicos

#### RF-004: Búsqueda y Filtros
- Búsqueda por nombre de músculo
- Filtros por región anatómica
- Listado alfabético de músculos

#### RF-005: Funcionalidad Offline
- La aplicación debe funcionar completamente sin conexión a internet
- Todos los recursos (imágenes, datos) almacenados localmente

### 5.2 Funcionalidades de Accesibilidad
#### RF-006: Soporte para Daltonismo
- Paleta de colores accesible
- Alternativas textuales para información codificada por color
- Contraste adecuado para legibilidad

#### RF-007: Soporte para Deficiencias Visuales
- Texto escalable
- Compatibilidad con lectores de pantalla
- Descripciones alternativas para imágenes

## 6. Requerimientos No Funcionales

### 6.1 Rendimiento
- **RNF-001**: Tiempo de carga inicial < 3 segundos
- **RNF-002**: Respuesta a interacciones < 300ms
- **RNF-003**: Navegación fluida entre pantallas
- **RNF-004**: Optimización de memoria para dispositivos de gama media

### 6.2 Usabilidad
- **RNF-005**: Interfaz intuitiva sin necesidad de tutorial
- **RNF-006**: Consistencia en patrones de navegación
- **RNF-007**: Adaptabilidad a diferentes tamaños de pantalla
- **RNF-008**: Puntuación SUS objetivo > 70 (buena usabilidad)

### 6.3 Confiabilidad
- **RNF-009**: Estabilidad sin crashes frecuentes
- **RNF-010**: Manejo correcto de errores
- **RNF-011**: Recuperación automática ante fallos menores

### 6.4 Compatibilidad
- **RNF-012**: Compatibilidad con Android 7.0+ (API 24+)
- **RNF-013**: Soporte para resoluciones desde 720p
- **RNF-014**: Funcionamiento en dispositivos con RAM mínima de 2GB

## 7. Contenido y Recursos

### 7.1 Contenido Base
- Información extraída de "MIOLOGÍA EN CABALLO CRIOLLO COLOMBIANO"
- Validación cruzada con textos de referencia en anatomía veterinaria
- Estructura de datos por músculo: nombre, origen, inserción, biomecánica

### 7.2 Recursos Visuales
- Ilustraciones digitales basadas en body painting sobre ejemplares vivos
- Fotografías de alta resolución desde múltiples ángulos
- Ilustraciones vectoriales organizadas por regiones anatómicas
- Vistas aisladas de músculos individuales

### 7.3 Organización del Contenido
```
Caballo Criollo Colombiano/
├── Cabeza/
│   ├── Músculos faciales
│   ├── Músculos masticatorios
│   └── Músculos cervicales superiores
├── Cuello/
│   ├── Músculos cervicales
│   └── Músculos hioideos
├── Tronco/
│   ├── Músculos dorsales
│   ├── Músculos ventrales
│   └── Músculos laterales
├── Miembros Torácicos/
│   ├── Músculos del hombro
│   ├── Músculos del brazo
│   └── Músculos del antebrazo
└── Miembros Pélvicos/
    ├── Músculos de la grupa
    ├── Músculos del muslo
    └── Músculos de la pierna
```

## 8. Interfaz de Usuario

### 8.1 Pantallas Principales
1. **Pantalla Principal**: Vista general del caballo con navegación por regiones
2. **Vista Regional**: Músculos específicos de una región seleccionada
3. **Detalle Muscular**: Información completa de un músculo específico
4. **Menú de Navegación**: Acceso rápido a todas las regiones
5. **Búsqueda**: Búsqueda y filtrado de músculos
6. **Configuración**: Ajustes de accesibilidad y preferencias

### 8.2 Principios de Diseño
- **Simplicidad**: Interfaz limpia y sin distracciones
- **Consistencia**: Patrones de navegación uniformes
- **Accesibilidad**: Cumplimiento de estándares de accesibilidad
- **Responsividad**: Adaptación a diferentes tamaños de pantalla

## 9. Criterios de Éxito

### 9.1 Métricas de Usabilidad
- Puntuación SUS > 70 (objetivo: > 80)
- Tiempo promedio para encontrar información específica < 30 segundos
- Tasa de finalización de tareas > 90%
- Satisfacción del usuario > 4/5 en escala Likert

### 9.2 Métricas de Adopción
- Descargas objetivo: 500+ en primeros 6 meses
- Retención de usuarios: 60% después de 1 semana
- Calificación en Google Play Store: > 4.0/5.0

### 9.3 Métricas de Impacto Educativo
- Percepción de utilidad como herramienta de estudio > 4/5
- Mejora reportada en motivación para estudiar anatomía > 4/5
- Recomendación a otros estudiantes > 80%

## 10. Cronograma de Desarrollo

### Fase I: Curación y Creación de Contenido (Meses 1-5)
- Recopilación y validación de datos textuales
- Sesiones de body painting y fotografía
- Creación de ilustraciones digitales vectoriales
- Estructuración de base de datos de contenido

### Fase II: Desarrollo Técnico (Meses 4-10)
- Configuración de arquitectura MVVM
- Implementación de base de datos local con Room
- Desarrollo del visor anatómico interactivo
- Creación de sistema de navegación
- Implementación de características de accesibilidad
- Testing y optimización

### Fase III: Evaluación y Validación (Meses 10-12)
- Pruebas con usuarios (N=30 estudiantes)
- Aplicación del cuestionario SUS
- Evaluación cualitativa del impacto pedagógico
- Análisis de resultados y refinamiento

## 11. Riesgos y Mitigaciones

### 11.1 Riesgos Técnicos
- **Riesgo**: Rendimiento pobre en dispositivos de gama baja
- **Mitigación**: Optimización temprana, testing en dispositivos variados

- **Riesgo**: Problemas de almacenamiento local
- **Mitigación**: Compresión de imágenes, arquitectura de datos eficiente

### 11.2 Riesgos de Contenido
- **Riesgo**: Imprecisiones en información anatómica
- **Mitigación**: Validación rigurosa con expertos veterinarios

- **Riesgo**: Calidad insuficiente de ilustraciones
- **Mitigación**: Proceso iterativo de refinamiento visual

### 11.3 Riesgos de Usabilidad
- **Riesgo**: Interfaz poco intuitiva
- **Mitigación**: Pruebas de usabilidad tempranas, diseño iterativo

## 12. Criterios de Calidad

### 12.1 Definición de "Terminado"
- Funcionalidad implementada y probada
- Pruebas de usabilidad completadas
- Documentación técnica actualizada
- Cumplimiento de estándares de accesibilidad
- Validación por expertos en anatomía veterinaria

### 12.2 Estándares de Calidad
- Cobertura de tests > 80%
- Cumplimiento de Android Development Guidelines
- Accesibilidad según WCAG 2.1 AA
- Validación científica del contenido anatómico

## 13. Stakeholders y Roles

### 13.1 Stakeholders Primarios
- **Estudiantes de Medicina Veterinaria**: Usuarios finales principales
- **Docentes de Anatomía**: Validadores de contenido y potenciales prescriptores
- **Equipo de Desarrollo**: Responsables de la implementación técnica

### 13.2 Stakeholders Secundarios
- **Universidad Santiago de Cali**: Institución patrocinadora
- **Profesionales Veterinarios**: Usuarios potenciales para educación continua
- **Comunidad Académica**: Beneficiarios de la investigación resultante

## 14. Consideraciones Futuras

### 14.1 Funcionalidades Futuras (Fase 2)
- Versión para iOS
- Contenido adicional (sistema nervioso, circulatorio)
- Realidad Aumentada para visualización 3D
- Integración con sistemas de gestión de aprendizaje (LMS)

### 14.2 Escalabilidad
- Arquitectura modular para fácil extensión
- API potencial para integración con otras aplicaciones
- Posibilidad de incluir otras razas de caballos

---

**Versión**: 1.0  
**Fecha**: Diciembre 2024  
**Autor**: Equipo de Desarrollo CaballoApp  
**Estado**: Activo 
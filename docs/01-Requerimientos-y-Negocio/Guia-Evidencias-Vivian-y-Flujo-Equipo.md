---
title: Guía de Evidencias de Vivian y Flujo del Equipo Humano
aliases:
  - Guía Vivian y Flujo Equipo
  - Evidencias Comerciales y Flujo QA
  - Flujo Colaborativo 3 Etapas
tags:
  - flujo-equipo
  - raci
  - evidencias-comerciales
  - defensa-catedra
  - globalfrio
owner: Vivian (Product Owner & Dueña del Frontend)
status: Aprobado
version: 1.0.0
updated: 2026-10-17
---

# Guía de Evidencias de Vivian y Flujo del Equipo Humano

> [!IMPORTANT]
> **Marco de Colaboración Humana y Evidencias de Negocio:**  
> Este documento establece la dinámica operativa, responsabilidades intransferibles y sinergia técnica del equipo humano que desarrolla el Sistema Web de Cotización de **Global Frío S.A.C.** (Ventanilla `07061`, Contacto: **Edgar Sabalu**, Tel: `904 209 489`).
> 
> Asimismo, fundamenta cómo los **5 documentos comerciales reales** provistos por **Vivian** sustentan cada entidad relacional, regla de negocio y patrón de diseño ante el docente **Ing. Mg. Carlos Alberto Ponte Ramírez** en la asignatura **Soluciones Web y Aplicaciones Distribuidas (SIST1402A - UPN)**, consolidando la preparación del equipo con un banco exhaustivo de 10 preguntas y respuestas de defensa.

---

## 1. Estructura del Equipo Humano y Matriz RACI

El equipo se compone de tres integrantes con especialidades técnicas complementarias y bien delimitadas:

| Integrante | Rol en el Proyecto | Especialidad Primaria | Compromiso de Calidad |
| :--- | :--- | :--- | :--- |
| **1. Orlando** | Arquitecto de Sistemas & Líder Backend | Java 17, Spring Boot 3.3.4, JPA, MySQL 8.x, OWASP | Entrega código limpio, compilable y seguro |
| **2. Compañero de QA** | QA Engineer & Bug-Fixer Activo | Pruebas continuas, diagnóstico y fix en código | Repara directamente cualquier defecto antes de Vivian |
| **3. Vivian** | Product Owner & Dueña del Frontend | Prototipo Vanilla, UI/UX Angular, 5 Docs Reales | Garantiza valor comercial y fidelidad visual |

### 1.1. Perfiles y Especialización
1. **Orlando (Arquitecto de Sistemas, Líder Backend & BD):**
   - Responsable de la arquitectura general N-capas en Java 17 y Spring Boot 3.3.4.
   - Migró la estructura original del prototipo en HTML/CSS/JS Vanilla (`frontend/prototipo-vanilla/`) hacia la arquitectura Single Page Application (SPA) en Angular (`frontend/src/app/`).
   - Modela la base de datos relacional (MySQL 8.x / H2), migraciones DDL, consultas JPQL parametrizadas, control transaccional `@Transactional`, seguridad Spring Security 6 con JWT y directivas OWASP Web + OWASP AI.
2. **Compañero de QA (QA Engineer & Bug-Fixer Activo):**
   - Diseña y ejecuta la batería integral de pruebas (Unitarias con JUnit 5/MockMvc, integración, regresión, pruebas de rollback transaccional forzado, seguridad y compatibilidad responsiva RNF03).
   - **Regla Mandataria de Ingeniería:** Cuando detecta un fallo, defecto o excepción en el backend o en la integración, **no se limita a reportar un ticket de incidencia: él mismo inspecciona el código fuente, diagnostica la causa-raíz y aplica la corrección directamente en el código Java o TypeScript**. Luego re-ejecuta las pruebas hasta certificar el 100% de éxito antes de entregar el incremento a Vivian.
3. **Vivian (Product Owner, Representante de Global Frío & Dueña del Frontend Angular):**
   - Autora original del prototipo en Vanilla (`frontend/prototipo-vanilla/`), garante de la identidad visual de la empresa familiar, paleta de colores y experiencia de usuario.
   - Propietaria de los requerimientos comerciales y aportante de los **5 documentos comerciales reales** de Global Frío.
   - **Regla Mandataria de Ingeniería:** Cualquier observación visual, refinamiento de estilos SCSS (`styles.scss`), maquetación de pantallas pendientes o ajustes en componentes Angular le compete directamente a Vivian; ella misma lo programa y ajusta en el código para asegurar la fidelidad con su diseño original.

### 1.2. Matriz RACI del Proyecto

*Nomenclatura: **R** = Responsible (Ejecuta la tarea), **A** = Accountable (Aprobador final), **C** = Consulted (Consultado), **I** = Informed (Informado).*

| Módulo / Actividad de Ingeniería | Orlando (Backend) | Compañero de QA (Bug-Fixer) | Vivian (PO / Frontend) | Criterio de Finalización |
| :--- | :---: | :---: | :---: | :--- |
| **Modelado de Requerimientos y PRD** | C | I | **A / R** | PRD aprobado con criterios Gherkin |
| **Arquitectura N-Capas y Monorepo** | **A / R** | C | I | `./mvnw clean compile` con salida 0 |
| **Prototipo Visual Original en Vanilla** | I | I | **A / R** | Interfaz en `frontend/prototipo-vanilla/` |
| **Migración Inicial a Módulos Angular** | **A / R** | C | C | Estructura SPA en `frontend/src/app/` |
| **Modelado Entidad-Relación y DDL (BD)** | **A / R** | C | C | 11 tablas relacionales y snapshots |
| **Persistencia EntityManager (Semana 03)** | **A / R** | C | I | Consultas con `EntityManager` sin JpaRepo |
| **CRUD JpaRepository y JPQL (Semana 04)** | **A / R** | C | I | Métodos derivados e inyección de queries |
| **Bean Validation y Seguridad JWT (S05)** | **A / R** | C | I | Tokens Bearer y validación de DTOs |
| **Diseño y Ejecución de Pruebas Unitarias** | C | **A / R** | I | Pass rate 100% en `./mvnw test` |
| **Detección y Reparación Directa de Bugs** | C | **A / R** | I | Commits `fix(modulo): [RF-SEMXX-YY]` |
| **Desarrollo de Componentes UI en Angular** | I | C | **A / R** | Vistas operativas en puerto 4200 |
| **Ajustes Estéticos CSS y Fidelidad Tokens** | I | I | **A / R** | Cumplimiento estricto `#5980a6`, Barlow |
| **Prueba de Rollback Transaccional (S13)** | C | **A / R** | I | Test forzado JUnit 5 verificado en BD |
| **Aporte y Defensa de los 5 Docs Reales** | C | I | **A / R** | Justificación comercial ante el jurado |
| **Empaquetado y Configuración HTTPS SSL** | **A / R** | C | I | Binario `.jar` y canal seguro 8443 |

---

## 2. Flujo de Trabajo Colaborativo en 3 Etapas

El ciclo de vida de cada incremento técnico sigue un circuito cerrado de tres etapas sucesivas, asegurando calidad desde el origen (*Shift-Left Testing*):

| Etapa del Flujo | Integrante Responsable | Acciones Clave de Ingeniería | Estándar de Commit y Entregable |
| :--- | :--- | :--- | :--- |
| **Etapa 1: Construcción** | **Orlando**<br>(Backend & BD) | - Diseña entidades relacionales, DTOs y persistencia JPA<br>- Implementa servicios transaccionales y seguridad JWT/OWASP<br>- Valida compilación base local (`./mvnw clean compile`) | `feat(backend): [RF-SEMXX-YY] descripción del avance`<br>Código base funcional entregado a QA |
| **Etapa 2: Pruebas y Bug-Fixing** | **Compañero de QA**<br>(Bug-Fixer Activo) | - Ejecuta batería de pruebas automatizadas, cURL, Postman y Swagger<br>- **Diagnostica causa raíz** ante fallos (400, 401, 403, 500)<br>- **Repara directamente el código fuente** en Java/TypeScript<br>- Re-ejecuta pruebas hasta certificar 100% de éxito | `fix(modulo): [RF-SEMXX-YY] arreglo de bug validado`<br>Build verificado y estabilizado entregado a Vivian |
| **Etapa 3: Validación y UX** | **Vivian**<br>(PO & Frontend) | - Recibe el build estabilizado técnica y funcionalmente<br>- Contrasta cálculos y montos con los 5 documentos comerciales reales<br>- Desarrolla maquetación pendiente y aplica ajustes estéticos en SCSS | `feat(ui): [RF-SEMXX-YY] ajuste visual Vivian`<br>Aprobación final para hito de evaluación UPN |

> [!TIP]
> Para visualizar el diagrama interactivo de este flujo con sus cuatro carriles (*swimlanes*) y compuertas de calidad, consulte:  
> [[diagramas/07-flujo-trabajo-equipo-qa-vivian.drawio|Diagrama 07: Flujo de Trabajo Equipo, QA y Vivian]].

---

## 3. Análisis Detallado de los 5 Documentos Comerciales Reales

**Vivian** aporta cinco artefactos comerciales físicos y digitales de las operaciones reales de **Global Frío S.A.C.** en Lima y Callao. Estos documentos no son meras referencias ilustrativas: constituyen el sustento directo e ineludible que justifica las entidades de base de datos, los patrones de diseño y los módulos arquitectónicos ante el profesor **Ing. Carlos Alberto Ponte Ramírez**.

### Documento 1: Proforma Comercial Membretada (PDF Oficial)
- **Datos de Origen y Operación:**
  - Emisor: **Global Frío S.A.C.** | RUC: `20601234567` | Dirección: Ventanilla, Callao (Código Postal `07061`), Lima.
  - Contacto Operativo: **Edgar Sabalu** (Teléfono Celular: `904 209 489`).
  - Identificador correlativo: `COT-2026-0042` | Fecha de emisión: 15 de Octubre de 2026 | Validez comercial: 7 días calendario.
- **Contenido Específico:**
  - Datos del cliente: Razón Social, RUC/DNI, teléfono, distrito y dirección física de instalación.
  - Tabla de desglose de suministros: Equipo Split Muro Inverter 18,000 BTU/h marca Gree, kit de instalación estándar de 5 metros de tubería de cobre de refrigeración (1/4" y 1/2") con aislamiento Armaflex, cable vulcanizado, ménsulas de soporte galvanizadas para condensador, carga inicial de gas refrigerante R-410A y mano de obra calificada.
  - Desglose numérico: Subtotal en Soles (`S/ 2,450.00`), Impuesto General a las Ventas (IGV 18% = `S/ 441.00`) y Total General (`S/ 2,891.00`).
- **Justificación Técnica ante el Profesor:**
  1. *Justificación de Columnas Snapshot:* Los precios de equipos y cobre fluctúan constantemente por tipo de cambio del dólar. Para evitar que una actualización en el catálogo altere proformas ya emitidas al cliente, la base de datos incorpora columnas inmutables: `snapshot_precio_unitario`, `snapshot_costo_instalacion`, `snapshot_flete` y `snapshot_total`.
  2. *Justificación de Renderizado Server-Side (Thymeleaf):* Sustenta el controlador `@Controller` en `/reportes/cotizacion/{id}` exigido en la Semana 01 y 14, permitiendo emitir una vista HTML/PDF con diseño membretado idéntico al documento físico oficial.

### Documento 2: Tarifario Interno de Precios por BTU, Equipos y Zonas de Lima
- **Datos de Origen y Operación:**
  - Matriz interna confidencial de precios de suministro clasificada por capacidad térmica (9000, 12000, 18000, 24000, 36000 y 60000 BTU/h) y tipología (Split Muro, Piso Techo, Cassette y Cámaras Frigoríficas).
  - Tabla de fletes de transporte y viáticos categorizada por zonas logísticas de Lima Metropolitana:
    - *Zona Norte (Base operativa):* Ventanilla, Puente Piedra, Comas, San Martín de Porres (Recargo: S/ 0.00 a S/ 50.00).
    - *Zona Centro / Moderna:* San Isidro, Miraflores, San Borja, Cercado de Lima (Recargo estándar: S/ 70.00).
    - *Zona Este / Sur / Balnearios:* Lurín, Ate, Chorrillos, Pachacámac, Asia (Recargo por distancia: S/ 120.00 a S/ 200.00).
- **Justificación Técnica ante el Profesor:**
  1. *Justificación del Patrón GoF Decorator:* Sustenta la implementación del servicio `PresupuestoCalculator`. Sobre el cálculo del equipo base se "decoran" dinámicamente los accesorios adicionales (metros extra de tubería de cobre) y el flete geográfico según el distrito seleccionado, manteniendo abierto el sistema para nuevos cargos sin modificar la clase base (Principio Open/Closed de SOLID).
  2. *Justificación del Modelo Relacional:* Sustenta las tablas `tarifario_zonas`, `tipo_equipo` y `precios_base` en la base de datos MySQL (Semana 03/04).

### Documento 3: Orden de Servicio (OS) Firmada en Terreno
- **Datos de Origen y Operación:**
  - Correlativo formal: `OS-2026-0019` | Referencia vinculada: Cotización `COT-2026-0042`.
  - Cuadrilla técnica asignada: Técnico Principal y Asistente electromecánico.
  - Protocolo de pruebas de campo: Presurización del sistema con gas nitrógeno seco a 350 PSI para prueba de hermeticidad (detección de fugas), vacío de líneas con bomba de doble etapa hasta alcanzar 500 micrones en vacuómetro digital, medición de amperaje de consumo del compresor y verificación de salto térmico en el evaporador ($\Delta T \ge 12^\circ\text{C}$).
  - Firmas de conformidad del cliente y del responsable técnico de Global Frío.
- **Justificación Técnica ante el Profesor:**
  1. *Justificación de la Máquina de Estados Finita (FSM):* La cotización no es un registro estático; atraviesa un flujo de estados riguroso (`PENDIENTE -> EN_REVISION -> ENVIADO -> ACEPTADO -> ORDEN_SERVICIO`). La aceptación formal es el disparador obligatorio para generar la orden.
  2. *Justificación del Control Transaccional Atómico (`@Transactional`):* La conversión de una cotización aceptada a una Orden de Servicio debe ocurrir en una única transacción ACID. Si falla la inserción de la orden o la reserva de cuadrilla, Hibernate debe realizar un `ROLLBACK` atómico del cambio de estado, impidiendo datos huérfanos o inconsistentes (Semanas 12 y 13).

### Documento 4: Catálogo Técnico Oficial Gree / Samsung / York
- **Datos de Origen y Operación:**
  - Hojas técnicas de fabricante con especificaciones termodinámicas y eléctricas:
    - Capacidad de refrigeración en BTU/h y Watts.
    - Ratio de Eficiencia Energética Estacional (SEER $\ge 16$) y Coeficiente de Rendimiento (COP).
    - Gas refrigerante ecológico: R-410A y R-32 con potencial de calentamiento global reducido.
    - Calibres de tubería de cobre: Línea de líquido (1/4") y línea de succión (3/8", 1/2", 5/8").
    - Alimentación eléctrica: 220V monofásico para potencias hasta 24,000 BTU/h; 220V/380V trifásico para potencias de 36,000 a 60,000 BTU/h.
- **Justificación Técnica ante el Profesor:**
  1. *Justificación del Algoritmo Recomendador de Carga Térmica (Paso 1 del Cotizador):* Sustenta la fórmula matemática que calcula el volumen ($m^3$) y factor de uso para sugerir el equipo óptimo.
  2. *Justificación de la Alineación con ODS 09:* Demuestra el impacto en eficiencia energética (Meta 9.4), ya que recomendar la potencia correcta de catálogo previene el sobredimensionamiento de compresores, reduciendo el consumo eléctrico de los comercios en un 15% a 25%.

### Documento 5: Ficha de Visita Técnica en Terreno con Fotos y Planos
- **Datos de Origen y Operación:**
  - Levantamiento de campo con croquis acotado y registro fotográfico:
    - Distancia métrica real entre el evaporador (unidad interior) y el condensador (unidad exterior).
    - Existencia de pases de viga o muros de concreto armado que requieren perforación diamantada.
    - Disponibilidad de punto de desagüe por gravedad o necesidad de bomba de condensados.
    - Fotos de la acometida del tablero eléctrico general y llave termomagnética asignada.
- **Justificación Técnica ante el Profesor:**
  1. *Justificación del Módulo de Carga de Archivos Adjuntos (Paso 2 del Cotizador Web):* El cliente o asesor puede adjuntar planos y fotos para evaluar la complejidad de instalación antes de despachar a la cuadrilla.
  2. *Justificación del Blindaje de Seguridad OWASP:* Sustenta la verificación estricta de Magic Bytes en el backend, la restricción de extensiones permitidas (JPG, PNG, PDF), el aislamiento del almacenamiento fuera del directorio público web y la asignación de nombres basados en `UUID.randomUUID()` para mitigar vulnerabilidades de subida de archivos maliciosos.

---

## 4. Banco de 10 Preguntas y Respuestas para la Defensa ante Cátedra

Este banco de defensa ha sido preparado con precisión técnica para responder a los cuestionamientos del **Ing. Mg. Carlos Alberto Ponte Ramírez** y el jurado calificador de la UPN:

---

### Pregunta 01: ¿Por qué en la Semana 03 utilizan `EntityManager` de forma manual y recién en la Semana 04 introducen `JpaRepository`?
- **Responde: Orlando (Arquitecto de Sistemas & Líder Backend)**
- **Argumentación Técnica:**
  > *"Ingeniero Ponte, respetando rigurosamente la progresión didáctica de la asignatura, en la Semana 03 nuestro objetivo pedagógico fue comprender el ciclo de vida del contexto de persistencia JPA y el patrón Unit of Work que gestiona Hibernate internamente. Mediante la inyección de `@PersistenceContext EntityManager entityManager` en una clase `@Repository`, manipulamos explícitamente los métodos fundamentales `persist()`, `merge()`, `find()` y `createQuery()`. Esto nos permitió entender cómo se administran los estados de las entidades (New, Managed, Detached, Removed) y cómo se sincronizan las transacciones con MySQL.*
  > 
  > *Recién en la Semana 04 dimos el salto hacia `JpaRepository` en Spring Data JPA para industrializar la capa de acceso a datos, aprovechando la generación automática de proxies y métodos derivados (`findByNombreContainingIgnoreCase`), pero con la base conceptual sólida de cómo opera la persistencia a bajo nivel sin ver a Spring Data como una 'caja negra'."*

---

### Pregunta 02: ¿Por qué está terminantemente prohibida la concatenación de cadenas en consultas JPQL y cómo garantizan la protección de los datos?
- **Responde: Orlando (Arquitecto de Sistemas & Líder Backend)**
- **Argumentación Técnica:**
  > *"La concatenación directa de variables en cadenas JPQL (por ejemplo: `\"WHERE c.cliente = '\" + input + \"'\"`) introduce la vulnerabilidad más crítica del OWASP Top 10: Inyección de Código / SQL Injection (A03: Injection). Un atacante podría enviar cargas como `' OR '1'='1` para alterar la lógica de la cláusula WHERE y extraer información confidencial de cotizaciones o modificar precios.*
  > 
  > *En nuestro sistema, la totalidad de consultas JPQL dinámicas se implementan con la anotación `@Query` y parámetros enlazados nombrados `:parametro` acompañados de `@Param(\"parametro\")`. De este modo, el analizador léxico de Hibernate precompila el árbol sintáctico de la consulta y trata cualquier entrada del usuario estrictamente como un dato literal fuertemente tipado, neutralizando cualquier intento de inyección sin importar los caracteres especiales ingresados."*

---

### Pregunta 03: ¿Por qué la anotación `@Transactional(rollbackFor = Exception.class)` se coloca exclusivamente en la capa de servicio y qué ocurre ante un fallo?
- **Responde: Orlando (Arquitecto de Sistemas & Líder Backend)**
- **Argumentación Técnica:**
  > *"La anotación `@Transactional` delimita la frontera de la transacción atómica de negocio (propiedades ACID) y debe ubicarse exclusivamente en la capa `@Service`, jamás en los controladores REST ni en los repositorios. La capa de servicio es la única que coordina múltiples operaciones sobre diferentes entidades.*
  > 
  > *Por defecto, Spring solo ejecuta rollback ante excepciones no verificadas (`RuntimeException`). Al declarar explícitamente `rollbackFor = Exception.class`, instruimos al `PlatformTransactionManager` para que ante cualquier falla —tanto verificadas como no verificadas, tales como un error de I/O al congelar los snapshots o una excepción de negocio al generar la Orden de Servicio— intercepte la señal y ordene al SGBD un `ROLLBACK` atómico total. Esto garantiza que si falla la creación de la orden, se revierta también el cambio de estado de la cotización, evitando que la base de datos quede en un estado inconsistente o con registros huérfanos."*

---

### Pregunta 04: ¿Cómo opera la autenticación JWT y por qué es indispensable el manejo de peticiones pre-flight CORS?
- **Responde: Orlando (Arquitecto de Sistemas & Líder Backend)**
- **Argumentación Técnica:**
  > *"Nuestra arquitectura es desacoplada: el cliente Angular SPA se ejecuta en `http://localhost:4200` y el backend Spring Boot en `http://localhost:8080`. Debido a la política del mismo origen (Same-Origin Policy), el navegador intercepta cualquier petición HTTP que contenga encabezados personalizados como `Authorization: Bearer <token>` o verbos no simples (PUT, DELETE).*
  > 
  > *Antes de enviar la petición real, el navegador despacha automáticamente una solicitud pre-flight con el método HTTP `OPTIONS`. Nuestra clase `CorsConfig` y la configuración de `SecurityFilterChain` en Spring Security interceptan este `OPTIONS` y responden con los encabezados `Access-Control-Allow-Origin: http://localhost:4200`, `Access-Control-Allow-Methods` y `Access-Control-Allow-Headers`. Una vez superado el pre-flight, la petición real ingresa a nuestro filtro `JwtAuthenticationFilter`, el cual extrae el token Bearer, valida su firma criptográfica HMAC-SHA256 y carga las credenciales y roles (`ROLE_ASESOR`, `ROLE_ADMIN`) en el `SecurityContextHolder`."*

---

### Pregunta 05: ¿Por qué decidieron migrar el prototipo Vanilla original a Angular en lugar de mantenerlo en JavaScript plano?
- **Responde: Vivian (Product Owner & Dueña del Frontend)**
- **Argumentación Técnica:**
  > *"Profesor Ponte, mi prototipo original en Vanilla (`frontend/prototipo-vanilla/`) fue vital para plasmar la identidad de marca de Global Frío, los tokens de color acero corporativo (`#5980a6`, `#1f5fa9`) y el flujo comercial exacto que mi familia requería para digitalizar la empresa. Sin embargo, para cumplir con el estándar de una aplicación empresarial distribuida (a partir de la Semana 07), JavaScript plano presentaba graves limitaciones de escalabilidad y mantenimiento.*
  > 
  > *La migración hacia Angular 18 SPA nos aportó tipado estricto con TypeScript, componentes desacoplados y reutilizables, formularios reactivos con validaciones visuales antes de la transmisión HTTP, manejo reactivo de flujos asíncronos con RxJS y, fundamentalmente, la capacidad de proteger rutas y paneles de administración mediante `Route Guards` (`AuthGuard`) e inyectar credenciales con `HttpInterceptor`, garantizando una solución modular y robusta."*

---

### Pregunta 06: ¿Cómo evitan que un usuario malicioso altere el precio final de una cotización desde las herramientas de desarrollo del navegador?
- **Responde: Orlando (Arquitecto de Sistemas & Líder Backend)**
- **Argumentación Técnica:**
  > *"Aplicamos el principio fundamental de seguridad de 'Confianza Cero en el Cliente' (Zero-Trust Client). Aunque Angular realiza un cálculo preliminar en la interfaz para brindar retroalimentación visual inmediata al usuario, el backend ignora completamente cualquier importe monetario, subtotal o total que viaje en el cuerpo JSON de la petición HTTP.*
  > 
  > *Cuando el endpoint `POST /api/cotizaciones` recibe la solicitud, toma únicamente los parámetros técnicos neutros (BTU del equipo, metraje adicional de tubería y distrito de Lima). En el servidor, el servicio `PresupuestoCalculator` recalcula desde cero cada ítem utilizando `BigDecimal` y consultando los tarifarios oficiales de la base de datos. El monto persistido en la base de datos es exclusivamente el procesado por el servidor, haciendo imposible cualquier fraude por manipulación en el cliente."*

---

### Pregunta 07: ¿Qué patrón GoF se utilizó para la lógica de cálculo de presupuestos y por qué es obligatorio usar `BigDecimal`?
- **Responde: Orlando (Arquitecto de Sistemas & Líder Backend)**
- **Argumentación Técnica:**
  > *"Para el cálculo de presupuestos implementamos el patrón estructural de diseño GoF **Decorator** (`PresupuestoCalculator`). Definimos una interfaz base `PresupuestoComponent` y una implementación concreta para el equipo de aire acondicionado base (`EquipoBasePresupuesto`). A este objeto se le envuelven dinámicamente decoradores como `InstalacionDecorator`, `TuberiaAdicionalDecorator` y `FleteZonalDecorator`. Esto respeta el principio Open/Closed de SOLID: si mañana Global Frío agrega un servicio de 'Desinstalación de equipo antiguo' o 'Bomba de condensados', se añade un nuevo decorador sin modificar la lógica existente.*
  > 
  > *Es obligatorio usar `BigDecimal` con escala a dos decimales y `RoundingMode.HALF_UP` porque los tipos primitivos de punto flotante de Java (`float` y `double`) utilizan representación binaria IEEE 754, lo cual introduce imprecisiones de redondeo inaceptables en transacciones contables y cálculo del IGV (18%)."*

---

### Pregunta 08: ¿De qué manera los 5 documentos comerciales reales justifican el diseño de la base de datos y la arquitectura del sistema?
- **Responde: Vivian (Product Owner & Dueña del Frontend)**
- **Argumentación Técnica:**
  > *"Ingeniero, los 5 documentos comerciales que extrajimos de la operación diaria de Global Frío en Ventanilla son la piedra angular que justifica cada decisión técnica de nuestro sistema:*
  > 1. *La **Proforma Membretada** justifica las columnas `snapshot_*` inmutables en la base de datos para que los presupuestos históricos no cambien si suben los precios futuros, y sustenta la vista Thymeleaf para exportación PDF.*
  > 2. *El **Tarifario Interno de BTU y Zonas** justifica el patrón Decorator y las tablas relacionales de fletes de Lima Metropolitana.*
  > 3. *La **Orden de Servicio Firmada** justifica la Máquina de Estados Finita (`PENDIENTE -> ACEPTADO -> ORDEN_SERVICIO`) y el control `@Transactional` atómico.*
  > 4. *El **Catálogo Técnico Gree/Samsung** sustenta el algoritmo recomendador de BTU para cumplir el ODS 09 evitando el sobredimensionamiento eléctrico.*
  > 5. *La **Ficha de Visita Técnica con Planos** justifica el módulo de adjuntos en el Paso 2 del cotizador y las reglas de validación Magic Bytes contra archivos maliciosos."*

---

### Pregunta 09: Si durante una prueba de integración se detecta un error HTTP 500 en un endpoint, ¿cuál es el procedimiento exacto que sigue el equipo?
- **Responde: Compañero de QA (QA Engineer & Bug-Fixer Activo)**
- **Argumentación Técnica:**
  > *"Profesor, en nuestro equipo el rol de QA no es pasivo ni se limita a redactar un informe de error para esperar a que otro integrante lo resuelva. Cuando una prueba unitaria de JUnit, una ejecución de Postman o una prueba en el navegador arroja un error HTTP 500, yo accedo de inmediato a los logs de consola de Spring Boot para analizar la traza de la excepción (`stack trace`).*
  > 
  > *Si identifico que el error se debe a una violación de clave foránea en la base de datos, un `NullPointerException` en el servicio o una omisión en el mapeo de un DTO, **yo mismo abro el archivo de código fuente en el entorno de desarrollo, corrijo la falla directamente en Java o TypeScript, y vuelvo a compilar**. Posteriormente re-ejecuto la suite completa con `./mvnw test` hasta lograr el 100% de pruebas aprobadas, realizo el commit de arreglo bajo el estándar `fix(modulo): [RF-SEMXX-YY] descripción del arreglo`, y recién en ese momento entrego la versión estabilizada a Vivian para su validación comercial."*

---

### Pregunta 10: ¿Cómo contribuye exactamente esta solución web al cumplimiento del ODS 09 de las Naciones Unidas?
- **Responde: Vivian (Product Owner & Dueña del Frontend)**
- **Argumentación Técnica:**
  > *"Nuestra solución web impacta directamente en tres metas estratégicas del ODS 09 (Industria, Innovación e Infraestructura):*
  > - *En la **Meta 9.1 (Infraestructura Resiliente y Salud):** Garantiza el cálculo termodinámico exacto para cámaras frigoríficas y salas limpias en postas médicas y centros de salud, evitando la pérdida de vacunas biológicas e insulina por ruptura de la cadena de frío.*
  > - *En la **Meta 9.4 (Eficiencia Energética y Tecnologías Limpias):** Nuestro recomendador de BTU evita el sobredimensionamiento de compresores, lo que reduce el consumo eléctrico entre 15% y 25% en comercios e industrias, fomentando además el uso de refrigerantes ecológicos R-410A y R-32 y eliminando el uso de papel físico.*
  > - *En la **Meta 9.b (Innovación para PYMEs Nacionales):** Empodera a una empresa técnica peruana de Ventanilla como Global Frío con una plataforma de cotización automatizada de nivel industrial, democratizando la tecnología frente a grandes consorcios extranjeros."*

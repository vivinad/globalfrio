---
title: Plan Maestro de Pruebas QA Continuo y Matriz de Aceptación
aliases:
  - QA Test Plan
  - Matriz de Aceptación Global Frío
  - Plan de Pruebas Shift-Left UPN
tags:
  - qa
  - testing
  - shift-left
  - quality-gates
  - globalfrio
  - upn-sist1402a
owner: Compañero de QA (QA Engineer & Bug-Fixer Activo)
status: Aprobado
version: 1.0.0
updated: 2026-10-17
---

# Plan Maestro de Pruebas QA Continuo y Matriz de Aceptación

> [!NOTE]
> **Contexto Académico e Institucional:**  
> Documento técnico canónico de Aseguramiento de Calidad (QA) para el **Sistema Web de Cotización Automatizada** de la empresa **Global Frío SAC** (Ventanilla, Callao / Lima 07061, RUC 20601234567, Teléfono: `904 209 489`, Representante: Edgar Sabalu). Desarrollado para la asignatura **Soluciones Web y Aplicaciones Distribuidas (SIST1402A)**, Ciclo 8°/9°, Facultad de Ingeniería de la **Universidad Privada del Norte (UPN)**, cátedra del **Ing. Mg. Carlos Alberto Ponte Ramírez**.

---

## 1. Introducción, Filosofía de Equipo y Diagrama Operativo

El presente plan maestro establece la estrategia de pruebas, verificación continua y criterios de aceptación deterministas para garantizar que la solución web de Global Frío cumpla con los más altos estándares de robustez, seguridad OWASP, consistencia transaccional ACID y usabilidad responsiva, respetando estrictamente la progresión didáctica de 16 semanas y los hitos evaluativos del periodo 2026-2.

> [!IMPORTANT]
> **División Humana del Equipo y Responsabilidad de QA:**
> 1. **Orlando (Arquitecto de Sistemas, Líder Backend & BD):** Diseña y programa la arquitectura N-capas en Java 17 + Spring Boot 3.3.4, la persistencia relacional MySQL/H2, la seguridad stateless JWT y la mitigación de riesgos OWASP.
> 2. **Compañero de QA (QA Engineer & Bug-Fixer Activo - Autor de este documento):** Ejecuta la batería de pruebas continuas en cada sprint. **Regla Mandataria:** Cuando detecta fallas o desviaciones en las pruebas, **no se limita a reportar un ticket:** ingresa directamente al código fuente, diagnostica la causa-raíz, aplica la corrección en caliente mediante commits convencionales (`fix(modulo): [RF-SEMXX-YY] ...`), re-ejecuta la suite hasta lograr el 100% de pass rate y entrega la versión estabilizada a Vivian.
> 3. **Vivian (Product Owner, Representante Comercial & Dueña de Frontend Angular):** Autora del prototipo original Vanilla (`frontend/prototipo-vanilla/`), aporta los 5 documentos comerciales reales y valida la exactitud contable de las cotizaciones. Cualquier ajuste visual o componente pendiente en Angular (`frontend/src/app/`) le compete exclusivamente a ella.

### 1.1. Diagrama de Flujo de Trabajo del Equipo y Hitos Académicos

A continuación se presenta el flujo sincronizado de trabajo entre los tres roles del equipo humano y su convergencia con los Quality Gates y los hitos evaluativos de la UPN:

![[diagramas/07-flujo-trabajo-equipo-qa-vivian.drawio]]
*Figura 1: Flujo de Trabajo Colaborativo en 4 Carriles: Orlando -> Compañero QA Bug-Fixer -> Vivian -> Hitos UPN.*  
[[diagramas/07-flujo-trabajo-equipo-qa-vivian.drawio|Abrir en Draw.io]]

---

## 2. Estrategia de Calidad Shift-Left a lo largo de los Hitos Académicos UPN

La estrategia de aseguramiento de calidad del proyecto no pospone la verificación para las semanas finales. Se rige por el principio **Shift-Left Testing**, introduciendo controles automatizados desde la primera línea de código en cada uno de los cuatro hitos oficiales del curso:

```
[Semana 01 - 05] Shift-Left Temprano: Unitarias + MockMvc + JPQL + JWT
       ↓
[Semana 06] HITO T1 (10%): Consolidación Backend, Persistencia Real y Swagger
       ↓
[Semana 07 - 09] Pruebas de Integración Cliente-Servidor + Angular Jasmine/Karma
       ↓
[Semana 10] HITO T2 (20%): Defensa de Solución Integrada Frontend + Backend + CORS
       ↓
[Semana 11 - 12] Pruebas Transaccionales ACID + Paginación Server-Side
       ↓
[Semana 13] HITO T3 (30%): Demostración de Atomicidad y Rollback Transaccional
       ↓
[Semana 14 - 15] Pruebas de Rendimiento, Seguridad HTTPS y Empaquetado
       ↓
[Semana 16] EVALUACIÓN FINAL (40%): Sustentación Integral (NO APLICA SUSTITUTORIO)
```

### 2.1. Desglose de Objetivos de Prueba por Hito Evaluativo

#### A. Hito T1 (Semana 06 — Ponderación: 10%)
- **Foco Técnico:** Backend N-capas Spring Boot 3.3.4, Java 17, persistencia relacional con `EntityManager` (Semana 03) y `JpaRepository` (Semana 04), seguridad stateless con Spring Security y JWT (Semana 05).
- **Tipos de Pruebas:**
  - Pruebas unitarias de controladores con `@WebMvcTest` y `@AutoConfigureMockMvc`.
  - Pruebas de repositorios JPA con `@DataJpaTest` en base de datos H2 en memoria y verificación en MySQL Server 8.x.
  - Verificación de la no existencia de `JpaRepository` en el código de la Semana 03 (`SPEC-SEM03-B`).
  - Pruebas de inyección SQL en consultas JPQL con parámetros nombrados `@Param`.
  - Pruebas de validación declarativa Bean Validation (`MethodArgumentNotValidException` -> HTTP 400 con mapa canónico de errores).
  - Pruebas de emisión de tokens JWT HMAC-SHA256 en `/api/auth/login`.

#### B. Hito T2 (Semana 10 — Ponderación: 20%)
- **Foco Técnico:** Integración de la Single Page Application (SPA) en Angular 18 con la API REST, autenticación reactiva, navegación protegida con Route Guards y manejo de errores HTTP.
- **Tipos de Pruebas:**
  - Pruebas unitarias de componentes Angular (`ng test --watch=false`) verificando binding bidireccional, interpolación y directivas estructurales `*ngIf`, `*ngFor`.
  - Pruebas de inyección de cabecera `Authorization: Bearer <token>` mediante `JwtInterceptor`.
  - Pruebas de denegación y redirección a `/login` con `AuthGuard` ante accesos directos por URL sin sesión activa.
  - Pruebas de políticas de intercambio de recursos cruzados (CORS) con solicitudes pre-flight `OPTIONS` desde `http://localhost:4200`.
  - Verificación de alertas amigables al usuario (SnackBar / Alertas Bootstrap) ante respuestas de error HTTP 400, 401, 403, 404 y 500 sin pantallas en blanco.

#### C. Hito T3 (Semana 13 — Ponderación: 30%)
- **Foco Técnico:** Atomicidad y consistencia transaccional ACID en la capa de servicios de negocio, paginación en servidor y documentación OpenAPI.
- **Tipos de Pruebas:**
  - Prueba automatizada de Rollback Transaccional: verificación obligatoria de `@Transactional(rollbackFor = Exception.class)` en `@Service`, induciendo fallos en caliente y comprobando que la base de datos queda intacta.
  - Pruebas de paginación eficiente en el servidor mediante Spring Data `Pageable` (`page`, `size`, `sort`) consumidas con `MatPaginator` en Angular.
  - Pruebas interactivas de contratos REST en Swagger UI (`/swagger-ui/index.html`) con cabecera Bearer Token.
  - Cuadro comparativo formal de interoperabilidad: servicios RESTful JSON vs servicios SOAP XML con contratos WSDL.

#### D. Sustentación Integral del Proyecto Final (Semana 16 — Ponderación: 40%)
- **Foco Técnico:** Solución distribuida integral en producción, empaquetado seguro, despliegue en servidor de aplicaciones y sustentación de sostenibilidad ODS 09.
- **Tipos de Pruebas:**
  - Pruebas E2E completas: flujo integral desde la recomendación térmica de BTU en la web pública hasta la generación de la Orden de Servicio en el panel del asesor.
  - Pruebas de empaquetado de artefactos de producción (`./mvnw clean package` -> `.jar` ejecutable y `ng build --configuration production`).
  - Verificación del canal de comunicación cifrado SSL/TLS (HTTPS en puerto 8443).
  - Auditoría de los 8 Quality Gates (`QG-01` a `QG-08`) superados al 100%.
  - **Aviso Mandatario de Cátedra:** Conforme a la reglamentación académica UPN, **NO APLICA EXAMEN SUSTITUTORIO**. La aprobación depende exclusivamente del cumplimiento de los hitos continuos.

---

## 3. Matriz de Compatibilidad Responsiva Multidispositivo (RNF03)

Para certificar que el sistema web opere de manera impecable tanto para clientes residenciales y comerciales desde sus teléfonos como para los técnicos y asesores en obra o en oficina, se define la siguiente matriz de verificación sobre 6 perfiles de pantalla:

| Perfil / Dispositivo | Viewport (Ancho x Alto) | Densidad | Componentes Críticos Inspeccionados | Criterio de Aceptación Determinista |
|---|---|---|---|---|
| **Móvil Angosto (Compacto)** | **360 x 640 px** | 2.0x (320-360 dpi) | Modal de cotización en 3 pasos, menú hamburguesa colapsable, botones touch primarios. | `overflow-x: hidden` absoluto (cero scroll horizontal); botones con altura mínima de `44px`; textos legibles sin zoom manual; inputs con font-size >= 16px para evitar auto-zoom en iOS Safari. |
| **Móvil Estándar Moderno** | **412 x 915 px** | 2.6x - 3.5x | Grilla de servicios HVAC (`/01` a `/04`), visualizador de proforma estimada, formulario de contacto. | Disposición en una columna (`grid-template-columns: 1fr`); espaciado vertical `--gf-section-y-sm: 64px`; padding lateral `--gf-gutter-sm: 20px`; logos e imágenes vectoriales nítidas. |
| **Tablet Vertical (Retrato)** | **768 x 1024 px** | 2.0x | Modal de cotización con formulario en 2 columnas, galería de trabajos realizados, tarjetas de mantenimiento. | Disposición en 2 columnas fluidas con Flexbox/CSS Grid; navbar con toggle accesible; tablas de cotización con scroll horizontal contenido o vista responsive en tarjetas. |
| **Tablet Horizontal / Laptop Pequeña** | **1024 x 768 px** | 1.0x - 1.5x | Panel administrativo del asesor (`/panel/solicitudes`), visualizador de fotos de visita técnica. | Menú lateral colapsable o barra de navegación superior completa; visor de planos y fotos con zoom modal; tablas `MatTable` con paginador visible sin cortes. |
| **Laptop / Monitor Estándar** | **1440 x 900 px** | 1.0x | Landing page corporativa completa, panel de administración con métricas y exportación a PDF. | Contenedor centrado con ancho máximo `--gf-max: 1240px`; márgenes automáticos; navbar expandido con botones de acción rápida; banner con acento acero `--gf-accent: #5980a6`. |
| **Monitor Full HD / Wide** | **1920 x 1080 px** | 1.0x (96 dpi) | Vista general de supervisión, módulo Thymeleaf SSR `/reportes/cotizacion/{id}`. | Centrado armónico del layout; contraste óptimo de texto claro `--gf-text: #e8eaeb` sobre fondo oscuro `--gf-bg: #15181a`; cero pixelado de fuentes tipográficas Barlow y Barlow Condensed. |

### 3.1. Pruebas de Accesibilidad y Ergonomía Visual
- **Ratio de Contraste WCAG 2.1 AA:** El color de acento acero `--gf-accent: #5980a6` sobre fondo `--gf-bg: #15181a` y el botón primario `--gf-btn: #1f5fa9` con texto blanco `--gf-btn-text: #ffffff` superan el ratio mínimo de contraste de 4.5:1.
- **Touch Target Size:** Todos los elementos interactivos en viewports móviles (botones "Siguiente paso", "Solicitar Asesoría", botones de modal y toggles) cuentan con un área mínima táctil de 48x48 píxeles.
- **Prevención de CLS (Cumulative Layout Shift):** Las imágenes de equipos Gree y Samsung cuentan con atributos `width` y `height` o ratios fijos en CSS (`aspect-ratio: 16/9`), evitando saltos visuales durante la carga asíncrona.

---

## 4. Catálogo de los 15 Casos de Prueba Críticos (`TC-CRIT-001` a `TC-CRIT-015`)

A continuación se detalla el catálogo exhaustivo de los 15 casos de prueba críticos del sistema, cubriendo seguridad, persistencia, validaciones, transacciones ACID, frontend reactivo y UX:

---

### TC-CRIT-001: Autenticación — Login Exitoso con Generación de JWT Bearer Token
- **Módulo:** Seguridad y Autenticación Backend (`pe.upn.sist1402a.controller.AuthController`).
- **Prioridad:** Crítica (P1).
- **Tipo de Prueba:** Funcional / Integración con MockMvc.
- **Trazabilidad:** `[RF-SEM05-02]`, `SPEC-SEM05-B`.
- **Precondiciones:**
  - Base de datos relacional inicializada con script `data-seed-globalfrio.sql`.
  - Usuario `asesor` con rol `ROLE_ASESOR` y contraseña cifrada con BCrypt (`Asesor123$`).
- **Datos de Entrada:**
  - Endpoint: `POST /api/auth/login`
  - Payload JSON:
    ```json
    {
      "username": "asesor",
      "password": "Asesor123$"
    }
    ```
- **Procedimiento Paso a Paso:**
  1. Despachar petición HTTP POST con el payload de credenciales hacia `/api/auth/login`.
  2. Verificar que Spring Security intercepte la solicitud en `DaoAuthenticationProvider`.
  3. Validar que se emita un token JWT con cabecera `Bearer` y claims de rol `ROLE_ASESOR`.
- **Resultado Esperado:**
  - Código HTTP: `200 OK`.
  - Cuerpo de respuesta JSON con estructura:
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "tipo": "Bearer",
      "username": "asesor",
      "rol": "ROLE_ASESOR",
      "expiracionMs": 86400000
    }
    ```
- **Criterio de Éxito:** Token JWT firmado válidamente con clave secreta HMAC-SHA256 y decodificable; expiración configurada en 24 horas.
- **Comando de Verificación:**
  ```bash
  curl -i -X POST http://localhost:8080/api/auth/login \
    -H "Content-Type: application/json" \
    -d "{\"username\":\"asesor\",\"password\":\"Asesor123$\"}"
  ```

---

### TC-CRIT-002: Autenticación — Rechazo de Login con Credenciales Inválidas
- **Módulo:** Seguridad y Autenticación Backend (`pe.upn.sist1402a.controller.AuthController`).
- **Prioridad:** Crítica (P1).
- **Tipo de Prueba:** Negativa / Seguridad OWASP A07.
- **Trazabilidad:** `[RF-SEM05-02]`, `SPEC-SEM05-B`.
- **Precondiciones:** Servidor Spring Boot activo.
- **Datos de Entrada:**
  - Payload JSON: `{"username": "asesor", "password": "PasswordErroneo2026!"}`
- **Procedimiento Paso a Paso:**
  1. Enviar petición POST a `/api/auth/login` con contraseña incorrecta.
  2. El proveedor de autenticación de Spring Security detecta discordancia de hash BCrypt.
  3. El manejador global `GlobalExceptionHandler` captura `BadCredentialsException`.
- **Resultado Esperado:**
  - Código HTTP: `401 Unauthorized`.
  - JSON de error canónico:
    ```json
    {
      "timestamp": "2026-10-17T22:30:00Z",
      "status": 401,
      "error": "Unauthorized",
      "message": "Credenciales inválidas de autenticación",
      "path": "/api/auth/login"
    }
    ```
- **Criterio de Éxito:** Ningún token emitido; mensaje genérico que no expone si el usuario existe o no en la base de datos (mitigación de User Enumeration).

---

### TC-CRIT-003: Seguridad — Denegación de Acceso a Ruta Protegida sin Token JWT
- **Módulo:** Filtros de Seguridad Web (`pe.upn.sist1402a.config.SecurityConfig`).
- **Prioridad:** Crítica (P1).
- **Tipo de Prueba:** Seguridad / Control de Acceso OWASP A01.
- **Trazabilidad:** `[RF-SEM05-02]`, `SPEC-SEM05-B`.
- **Precondiciones:** Backend en ejecución.
- **Datos de Entrada:** Petición anónima GET hacia endpoint privado `/api/panel/cotizaciones` sin cabecera `Authorization`.
- **Procedimiento Paso a Paso:**
  1. Despachar petición HTTP GET a `/api/panel/cotizaciones`.
  2. `JwtAuthenticationFilter` detecta la ausencia de la cabecera `Authorization: Bearer <token>`.
  3. `AuthenticationEntryPoint` bloquea la propagación de la solicitud hacia la capa de controladores.
- **Resultado Esperado:**
  - Código HTTP: `401 Unauthorized` o `403 Forbidden`.
  - Acceso denegado de forma inmediata.
- **Criterio de Éxito:** Cero datos de cotizaciones expuestos en el cuerpo de la respuesta.
- **Comando de Verificación:**
  ```bash
  curl -i -X GET http://localhost:8080/api/panel/cotizaciones
  ```

---

### TC-CRIT-004: Negocio — Anti-Tampering de Precios y Recálculo con Patrón Decorator
- **Módulo:** Lógica de Cotización y Dominio (`pe.upn.sist1402a.pattern.decorator.PresupuestoCalculator`).
- **Prioridad:** Crítica (P1).
- **Tipo de Prueba:** Integración / Integridad Financiera y Seguridad OWASP A04.
- **Trazabilidad:** `[RF-SEM02-02]`, `[RF-SEM04-02]`, `SPEC-SEM02-B`.
- **Precondiciones:** Equipo `SAM-DVM-18K` con precio de catálogo oficial S/ 2,980.00 en BD; distrito `San Miguel` con flete zonal S/ 40.00.
- **Datos de Entrada:**
  - Payload malicioso manipulado desde herramientas de desarrollador del cliente:
    ```json
    {
      "equipoSku": "SAM-DVM-18K",
      "distritoId": 3,
      "metrosTuberia": 8.0,
      "precioUnitario": 1.00,
      "subtotal": 5.00,
      "total": 10.00
    }
    ```
- **Procedimiento Paso a Paso:**
  1. Enviar payload adulterado hacia `POST /api/cotizaciones/publica`.
  2. `CotizacionController` extrae únicamente los identificadores (`equipoSku`, `distritoId`, `metrosTuberia`).
  3. El servicio invoca la cadena de decoradores `PresupuestoCalculator` utilizando exclusivamente aritmética `BigDecimal`:
     - Base equipo: S/ 2,980.00.
     - Instalación básica: S/ 350.00.
     - Tubería extra (8m - 3m = 5m * S/ 65.00): S/ 325.00.
     - Flete San Miguel: S/ 40.00.
     - Subtotal calculado en servidor: S/ 3,695.00.
     - IGV 18%: S/ 665.10.
     - Total calculado en servidor: **S/ 4,360.10**.
  4. La cotización se persiste en la BD congelando los valores calculados en las columnas `snapshot_*`.
- **Resultado Esperado:**
  - Código HTTP: `201 Created`.
  - El monto registrado en la BD es exactamente `S/ 4,360.10`, ignorando el total enviado de `S/ 10.00`.
- **Criterio de Éxito:** Tolerancia cero al precio provisto por el cliente; cálculo determinista 100% en backend con `BigDecimal`.

---

### TC-CRIT-005: Validación — Bean Validation API en Solicitud de Cotización
- **Módulo:** Capa de Validación de Entrada (`pe.upn.sist1402a.dto.CotizacionRequestDto`).
- **Prioridad:** Alta (P2).
- **Tipo de Prueba:** Validación de Modelos / Manejo de Excepciones.
- **Trazabilidad:** `[RF-SEM05-01]`, `SPEC-SEM05-A`.
- **Precondiciones:** Servidor activo.
- **Datos de Entrada:**
  - Payload con área térmica negativa, teléfono vacío y email con formato inválido:
    ```json
    {
      "areaM2": -15.5,
      "clienteTelefono": "",
      "clienteEmail": "correo-no-valido-sin-arroba",
      "tipoEspacio": "COMERCIAL"
    }
    ```
- **Procedimiento Paso a Paso:**
  1. Despachar POST a `/api/cotizaciones/publica` con el payload defectuoso.
  2. Spring valida el DTO mediante las anotaciones `@Valid`, `@Min(1)`, `@NotBlank`, `@Email`.
  3. Al fallar la validación, Spring arroja `MethodArgumentNotValidException`.
  4. `GlobalExceptionHandler` captura la excepción y arma un mapa con el detalle campo por campo.
- **Resultado Esperado:**
  - Código HTTP: `400 Bad Request`.
  - Cuerpo de respuesta estructurado:
    ```json
    {
      "timestamp": "2026-10-17T22:30:15Z",
      "status": 400,
      "error": "Bad Request",
      "message": "Error de validación en campos del formulario",
      "errores": {
        "areaM2": "El área en metros cuadrados debe ser mayor a 0",
        "clienteTelefono": "El teléfono de contacto es obligatorio",
        "clienteEmail": "El formato de correo electrónico es inválido"
      },
      "path": "/api/cotizaciones/publica"
    }
    ```
- **Criterio de Éxito:** Se detiene el procesamiento antes de tocar la base de datos; respuesta descriptiva y canónica.

---

### TC-CRIT-006: Persistencia — Inserción y Consulta Manual con `EntityManager` (Semana 03)
- **Módulo:** Persistencia Manual (`pe.upn.sist1402a.repository.manual.CotizacionManualDao`).
- **Prioridad:** Alta (P2).
- **Tipo de Prueba:** Persistencia Relacional / Cumplimiento de Restricción Didáctica.
- **Trazabilidad:** `[RF-SEM03-02]`, `SPEC-SEM03-B`.
- **Precondiciones:** Restricción absoluta de Semana 03: Prohibido el uso de `JpaRepository`.
- **Procedimiento Paso a Paso:**
  1. Instanciar clase DAO anotada con `@Repository` que inyecta `@PersistenceContext private EntityManager entityManager`.
  2. Invocar método para persistir una entidad `Producto` o `Cotizacion` utilizando `entityManager.persist(entidad)`.
  3. Ejecutar consulta JPQL manual mediante `entityManager.createQuery("SELECT c FROM Cotizacion c WHERE c.estado = :est", Cotizacion.class).setParameter("est", "BORRADOR").getResultList()`.
- **Resultado Esperado:**
  - La entidad recibe su ID generado (`GenerationType.IDENTITY`).
  - La consulta manual retorna la lista de entidades gestionadas en la sesión de Hibernate.
- **Criterio de Éxito:** Cero interfaces que extiendan de `JpaRepository` en el paquete de la Semana 03; datos persistidos sobreviven al reinicio de la aplicación.

---

### TC-CRIT-007: Persistencia — Operaciones CRUD y Consultas Derivadas con `JpaRepository` (Semana 04)
- **Módulo:** Persistencia Automatizada Spring Data (`pe.upn.sist1402a.repository.ProductoRepository`).
- **Prioridad:** Alta (P2).
- **Tipo de Prueba:** Persistencia / Repositorios Spring Data JPA.
- **Trazabilidad:** `[RF-SEM04-02]`, `SPEC-SEM04-B`.
- **Precondiciones:** Interfaz `ProductoRepository extends JpaRepository<Producto, Long>`.
- **Procedimiento Paso a Paso:**
  1. Ejecutar `save()` para registrar nuevo equipo `GREE-INV-12K`.
  2. Ejecutar `findById()` y verificar existencia.
  3. Ejecutar método derivado `findByNombreContainingIgnoreCase("Inverter")`.
  4. Ejecutar `deleteById()` y comprobar con `existsById()` que retorna `false`.
- **Resultado Esperado:** Todas las operaciones CRUD se resuelven deterministamente sin escribir sentencias SQL manuales.
- **Criterio de Éxito:** Cumplimiento del estándar Spring Data JPA; entidades mapeadas con `@Entity` y `@Table`.

---

### TC-CRIT-008: Seguridad — Blindaje contra Inyección SQL en Consultas JPQL Parametrizadas
- **Módulo:** Repositorios JPA y Mitigación de Vulnerabilidades (`pe.upn.sist1402a.repository.ProductoRepository`).
- **Prioridad:** Crítica (P1).
- **Tipo de Prueba:** Seguridad / OWASP A03 (Injection).
- **Trazabilidad:** `[RF-SEM04-03]`, `SPEC-SEM04-C`.
- **Precondiciones:** Repositorio con consulta anotada con `@Query("SELECT p FROM Producto p WHERE p.nombre = :nombre")`.
- **Datos de Entrada:**
  - Cadena maliciosa de inyección SQL como parámetro de búsqueda: `' OR '1'='1' --`
- **Procedimiento Paso a Paso:**
  1. Invocar búsqueda enviando el parámetro malicioso: `GET /api/productos/buscar?nombre=%27%20OR%20%271%27=%271%27%20--`.
  2. Hibernate procesa la consulta vinculando el parámetro como texto plano escapado mediante `@Param("nombre") String nombre`.
- **Resultado Esperado:**
  - Código HTTP: `200 OK`.
  - Lista vacía (`[]`) o únicamente productos cuyo nombre coincida literalmente con la cadena ingresada.
- **Criterio de Éxito:** La consulta no altera su árbol sintáctico ni expone registros no solicitados; cero concatenación de cadenas en código fuente.

---

### TC-CRIT-009: Transacciones — Verificación de Rollback Atómico con `@Transactional(rollbackFor = Exception.class)`
- **Módulo:** Capa de Servicios Transaccionales (`pe.upn.sist1402a.service.impl.CotizacionServiceImpl`).
- **Prioridad:** Crítica (P1).
- **Tipo de Prueba:** Atomicidad y Consistencia Transaccional (ACID).
- **Trazabilidad:** `[RF-SEM12-02]`, `[RF-SEM13-01]`, `SPEC-SEM12-B`.
- **Precondiciones:** Cotización `COT-2026-0042` en estado `ACEPTADA` registrada en base de datos.
- **Procedimiento Paso a Paso:**
  1. Invocar método de servicio `generarOrdenServicio(cotizacionId)` anotado con `@Transactional(rollbackFor = Exception.class)`.
  2. La rutina actualiza el estado de la cotización a `ORDEN_SERVICIO` e inserta la cabecera en `ordenes_servicio`.
  3. Se fuerza deliberadamente una excepción verificada o de runtime (e.g. falla inducida en la generación del PDF o error de inventario: `throw new RuntimeException("Error simulado de comunicación con cuadrilla de técnicos")`).
  4. Spring Boot TransactionManager captura la excepción y ejecuta la reversión (`ROLLBACK`) inmediata en la base de datos.
- **Resultado Esperado:**
  - Se propaga la excepción hacia el manejador de errores.
  - Al consultar la BD tras el fallo, la cotización permanece en estado `ACEPTADA`.
  - La tabla `ordenes_servicio` no contiene ningún registro huérfano asociado.
- **Criterio de Éxito:** Verificación directa en base de datos de cero registros parciales persistidos; reversión atómica total. *(Ver especificación de código completa en Sección 5).*

---

### TC-CRIT-010: Backend — Paginación en Servidor con `Pageable` (Semana 11)
- **Módulo:** Controladores y Repositorios Paginados (`pe.upn.sist1402a.controller.CotizacionController`).
- **Prioridad:** Media (P3).
- **Tipo de Prueba:** Rendimiento y Manejo de Grandes Volúmenes de Datos.
- **Trazabilidad:** `[RF-SEM11-01]`, `SPEC-SEM11-A`.
- **Precondiciones:** Base de datos con al menos 25 cotizaciones registradas.
- **Datos de Entrada:** `GET /api/panel/cotizaciones?page=1&size=10&sort=fechaEmision,desc` (Página 1, indexada en 0, es decir la segunda página).
- **Procedimiento Paso a Paso:**
  1. Enviar solicitud con parámetros de paginación y cabecera de autenticación de asesor.
  2. Spring Data JPA transforma la petición en una consulta SQL con `LIMIT 10 OFFSET 10`.
  3. Retornar objeto serializado `Page<CotizacionResponseDto>`.
- **Resultado Esperado:**
  - Código HTTP: `200 OK`.
  - JSON estructurado con metadatos de paginación:
    ```json
    {
      "content": [ /* 10 elementos */ ],
      "pageable": { "pageNumber": 1, "pageSize": 10 },
      "totalElements": 25,
      "totalPages": 3,
      "last": false,
      "first": false
    }
    ```
- **Criterio de Éxito:** La memoria del servidor se mantiene constante sin cargar la tabla completa; paginación ágil compatible con `MatPaginator` de Angular.

---

### TC-CRIT-011: Documentación — Swagger UI / OpenAPI 3 y Autenticación Interactiva
- **Módulo:** Documentación Interactiva de Contratos (`pe.upn.sist1402a.config.OpenApiConfig`).
- **Prioridad:** Media (P3).
- **Tipo de Prueba:** Documentación y Usabilidad de API.
- **Trazabilidad:** `[RF-SEM11-02]`, `SPEC-SEM11-B`.
- **Precondiciones:** Servidor Spring Boot activo en puerto 8080.
- **Procedimiento Paso a Paso:**
  1. Navegar en Chrome/Edge hacia `http://localhost:8080/swagger-ui/index.html`.
  2. Verificar la carga del esquema OpenAPI v3 generado en `/v3/api-docs`.
  3. Hacer clic en el botón verde "Authorize" e ingresar un token Bearer obtenido de `/api/auth/login`.
  4. Ejecutar la prueba interactiva del endpoint protegido `GET /api/panel/cotizaciones`.
- **Resultado Esperado:**
  - Swagger UI responde con HTTP 200 OK y muestra el payload JSON real formateado.
- **Criterio de Éxito:** Todos los controladores, DTOs y respuestas de error se encuentran documentados con anotaciones `@Operation`, `@ApiResponse` y esquema Bearer.

---

### TC-CRIT-012: Seguridad — Validación de Carga de Archivos Adjuntos por Magic Bytes
- **Módulo:** Subida Segura de Archivos de Visita Técnica (`pe.upn.sist1402a.service.AdjuntoService`).
- **Prioridad:** Crítica (P1).
- **Tipo de Prueba:** Seguridad / Integridad de Archivos OWASP A08.
- **Trazabilidad:** `[RF-SEM09-01]`, `SPEC-SEM09-A`.
- **Precondiciones:** Carpeta de almacenamiento local configurada en `application.properties`.
- **Datos de Entrada:** Archivo malicioso ejecutable (`malware.exe` o `script.php`) renombrado falsamente como `plano_fachada.jpg`.
- **Procedimiento Paso a Paso:**
  1. Enviar petición `POST /api/cotizaciones/publica/adjuntos` con `multipart/form-data`.
  2. El servicio lee los primeros 4 bytes del flujo binario (`Magic Bytes`).
  3. Detecta que no coincide con las firmas autorizadas (JPEG: `FF D8 FF`, PNG: `89 50 4E 47`, PDF: `25 50 44 46`).
- **Resultado Esperado:**
  - Código HTTP: `415 Unsupported Media Type` o `400 Bad Request`.
  - Mensaje: `"Firma binaria de archivo inválida. Solo se admiten archivos genuinos JPG, PNG o PDF"`.
  - El archivo no se guarda en disco y se purga del búfer temporal.
- **Criterio de Éxito:** Neutralización total de ataques de subida de ejecutables (RCE); los archivos legítimos se almacenan renombrados con un UUID aleatorio fuera del web root.

---

### TC-CRIT-013: Frontend — Protección de Rutas Administrativas con `AuthGuard` de Angular
- **Módulo:** Enrutamiento y Guardias de Seguridad (`frontend/src/app/core/guards/auth.guard.ts`).
- **Prioridad:** Alta (P2).
- **Tipo de Prueba:** Seguridad Frontend / Single Page Application.
- **Trazabilidad:** `[RF-SEM08-03]`, `SPEC-SEM08-C`.
- **Precondiciones:** Navegador web en `http://localhost:4200` con `localStorage` y `sessionStorage` vacíos (sin sesión activa).
- **Procedimiento Paso a Paso:**
  1. Ingresar directamente en la barra de direcciones: `http://localhost:4200/panel/solicitudes`.
  2. El enrutador de Angular activa el guard `canActivate: [AuthGuard]`.
  3. `AuthGuard` verifica el estado de autenticación en `AuthService`.
- **Resultado Esperado:**
  - `AuthGuard` retorna `false`.
  - El enrutador intercepta la navegación y redirige automáticamente hacia `/login?returnUrl=%2Fpanel%2Fsolicitudes`.
  - La pantalla del panel nunca se renderiza ni se emiten peticiones HTTP a la API protegida.
- **Criterio de Éxito:** Protección integral de vistas privadas en cliente ante accesos no autorizados.

---

### TC-CRIT-014: Frontend — Inyección Automática de Bearer Token mediante `JwtInterceptor`
- **Módulo:** Capa de Comunicación HTTP Frontend (`frontend/src/app/core/interceptors/jwt.interceptor.ts`).
- **Prioridad:** Alta (P2).
- **Tipo de Prueba:** Integración Frontend-Backend / Manejo de Estado Asíncrono.
- **Trazabilidad:** `[RF-SEM08-03]`, `SPEC-SEM08-C`.
- **Precondiciones:** Usuario autenticado con token almacenado en cliente.
- **Procedimiento Paso a Paso:**
  1. El asesor hace clic en "Actualizar Lista de Cotizaciones" en el panel.
  2. `CotizacionService` de Angular despacha una petición `httpClient.get('/api/panel/cotizaciones')`.
  3. `JwtInterceptor` clona la petición e inyecta la cabecera: `Authorization: Bearer <token_guardado>`.
  4. Inspeccionar la pestaña "Network" de DevTools en el navegador.
- **Resultado Esperado:**
  - La petición saliente contiene la cabecera de autorización formateada.
  - El servidor responde HTTP 200 OK con los datos.
- **Criterio de Éxito:** Token adjuntado transparentemente en el 100% de las peticiones hacia endpoints protegidos; peticiones públicas a `/api/catalogo/**` se despachan sin cabecera.

---

### TC-CRIT-015: UI/UX — Responsividad del Modal de Cotización en 3 Pasos (RNF03)
- **Módulo:** Componente Modal de Cotización (`frontend/src/app/components/modal/modal.component.ts`).
- **Prioridad:** Alta (P2).
- **Tipo de Prueba:** Verificación Visual, Ergonomía y Responsividad.
- **Trazabilidad:** `[RF-SEM07-03]`, `[RNF03]`, `SPEC-SEM07-C`.
- **Precondiciones:** Emulador de dispositivo móvil en Chrome DevTools configurado en **360 x 640 px** (Samsung Galaxy S8 / A51).
- **Procedimiento Paso a Paso:**
  1. Abrir la página principal en `http://localhost:4200` y pulsar "Cotizar en Línea".
  2. Verificar la visualización del **Paso 1 (Recomendador Térmico)**: sliders de metros cuadrados, selección de ambiente y tipo de sol.
  3. Avanzar al **Paso 2 (Detalles y Carga de Fotos/Planos)**: verificar botón de selección de archivos y cálculo de cañería.
  4. Avanzar al **Paso 3 (Datos de Contacto)**: completar formulario y enviar solicitud.
- **Resultado Esperado:**
  - El modal se ajusta al ancho completo con margen `--gf-gutter-sm: 20px`.
  - Cero desbordamiento horizontal en cualquiera de los tres pasos.
  - Botones de navegación "Anterior" y "Siguiente" accesibles con el pulgar sin necesidad de scroll vertical excesivo.
- **Criterio de Éxito:** Experiencia de usuario fluida y sin fricción técnica conforme al diseño original de Vivian.

---

## 5. Verificación Técnica de Atomicidad y Rollback Transaccional (`@Transactional`)

> [!SECURITY]
> **Fundamentación de Ingeniería para la Cátedra:**  
> En una aplicación distribuida empresarial de cotizaciones y órdenes de servicio, una falla durante la conversión de una cotización aceptada a una orden de trabajo (por ejemplo, error en el motor de reportes, desconexión del pool de conexiones HikariCP o excepción en el inventario) **jamás debe dejar la base de datos en un estado inconsistente o parcialmente actualizado**.  
> Por regla de arquitectura, `@Transactional(rollbackFor = Exception.class)` debe residir **exclusivamente en la capa de servicios (`@Service`)**, nunca en controladores ni repositorios.

### 5.1. Código Fuente del Test Automatizado en JUnit 5

A continuación se presenta la implementación real y ejecutable de la prueba de integración transaccional, ubicada canónicamente en `backend/src/test/java/pe/upn/sist1402a/service/CotizacionTransaccionalRollbackTest.java`:

```java
package pe.upn.sist1402a.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pe.upn.sist1402a.entity.Cotizacion;
import pe.upn.sist1402a.entity.EstadoCotizacion;
import pe.upn.sist1402a.entity.OrdenServicio;
import pe.upn.sist1402a.exception.SimulatedBusinessException;
import pe.upn.sist1402a.repository.CotizacionRepository;
import pe.upn.sist1402a.repository.OrdenServicioRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev") // Utiliza base de datos H2 en memoria para pruebas aisladas
public class CotizacionTransaccionalRollbackTest {

    @Autowired
    private CotizacionService cotizacionService;

    @Autowired
    private CotizacionRepository cotizacionRepository;

    @Autowired
    private OrdenServicioRepository ordenServicioRepository;

    private Long cotizacionIdExistente;

    @BeforeEach
    void setUp() {
        // Limpieza previa del entorno de persistencia
        ordenServicioRepository.deleteAll();
        cotizacionRepository.deleteAll();

        // Sembrado determinista de una cotización en estado ACEPTADA
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setCodigoCotizacion("COT-2026-TEST-ROLLBACK");
        cotizacion.setEstado(EstadoCotizacion.ACEPTADA);
        cotizacion.setSubtotal(new BigDecimal("3695.00"));
        cotizacion.setIgv(new BigDecimal("665.10"));
        cotizacion.setCostoFlete(new BigDecimal("40.00"));
        cotizacion.setTotal(new BigDecimal("4400.10"));
        cotizacion.setSnapshotClienteNombre("Clinica Dental San Miguel SAC");
        cotizacion.setSnapshotClienteDoc("20609876543");
        cotizacion.setSnapshotClienteDireccion("Av. La Marina 2450, San Miguel");
        cotizacion.setSnapshotFleteZona("LIMA_CENTRO");
        cotizacion.setFechaEmision(LocalDateTime.now());
        cotizacion.setFechaVencimiento(LocalDateTime.now().plusDays(15));

        Cotizacion guardada = cotizacionRepository.save(cotizacion);
        this.cotizacionIdExistente = guardada.getId();
    }

    @Test
    @DisplayName("TC-CRIT-009: Verificación de Rollback Transaccional Atómico ante Excepción en @Service")
    void testRollbackTransaccionalAlFallarGeneracionOrdenServicio() {
        // 1. Verificar estado inicial determinista en la base de datos
        Cotizacion cotizacionInicial = cotizacionRepository.findById(cotizacionIdExistente)
                .orElseThrow(() -> new AssertionError("La cotización de prueba no fue encontrada"));
        assertEquals(EstadoCotizacion.ACEPTADA, cotizacionInicial.getEstado(), 
                "La cotización debe iniciar en estado ACEPTADA");
        assertEquals(0, ordenServicioRepository.count(), 
                "No deben existir órdenes de servicio previas en la base de datos");

        // 2. Invocar el método transaccional configurando el flag de simulación de error
        // El método de servicio intenta:
        //   a) Actualizar cotizacion.setEstado(EstadoCotizacion.ORDEN_SERVICIO)
        //   b) Guardar nueva entidad OrdenServicio
        //   c) Simular falla crítica forzada (SimulatedBusinessException)
        assertThrows(SimulatedBusinessException.class, () -> {
            cotizacionService.generarOrdenServicioConFalloSimulado(cotizacionIdExistente);
        }, "El servicio debió arrojar SimulatedBusinessException para detonar el rollback");

        // 3. Verificación Determinista Post-Fallo (Inspección directa de la Base de Datos)
        // Spring Boot TransactionManager debió ejecutar un ROLLBACK total en el SGBD

        // A) Comprobar que el estado de la cotización NO quedó en ORDEN_SERVICIO
        Cotizacion cotizacionPostFallo = cotizacionRepository.findById(cotizacionIdExistente)
                .orElseThrow(() -> new AssertionError("La cotización desapareció de la base de datos"));
        assertEquals(EstadoCotizacion.ACEPTADA, cotizacionPostFallo.getEstado(), 
                "ROLLBACK FALLÓ: El estado de la cotización debió revertirse a ACEPTADA");

        // B) Comprobar que NO quedó registrada ninguna orden de servicio huérfana
        long totalOrdenes = ordenServicioRepository.count();
        assertEquals(0, totalOrdenes, 
                "ROLLBACK FALLÓ: Se encontró un registro huérfano en ordenes_servicio en la base de datos");
    }
}
```

---

## 6. Los 8 Quality Gates Inflexibles (`QG-01` a `QG-08`)

Para que cualquier incremento de código sea considerado apto para ser entregado a Vivian o presentado en una evaluación de la UPN, debe superar obligatoriamente los siguientes 8 Quality Gates:

> [!CHECK]
> **Secuencia Inflexible de los 8 Quality Gates (Shift-Left Pipeline)**  
> Todo cambio de código o corrección de defectos debe transitar estrictamente por la siguiente cadena de homologación antes de su entrega a Vivian:  
> **1. Compilación (`QG-01`)** ➔ **2. Pruebas 100% (`QG-02`)** ➔ **3. Lista Blanca (`QG-03`)** ➔ **4. Didáctica UPN (`QG-04`)** ➔ **5. OWASP (`QG-05`)** ➔ **6. Contrato Errores (`QG-06`)** ➔ **7. Tokens Visuales (`QG-07`)** ➔ **8. Vault & Draw.io (`QG-08`)** ➔ **Aprobación Final**.


| Quality Gate | Nombre y Alcance | Criterio de Aceptación Inflexible | Métrica / Umbral Requerido | Comando / Herramienta de Auditoría | Guardián Responsable |
|---|---|---|---|---|---|
| **`QG-01`** | **Compilación Cero Errores** | Compilación limpia de backend y frontend sin errores ni warnings bloqueantes. | Código de salida `0`; cero fallos de sintaxis o empaquetado. | `./mvnw clean compile`<br>`ng build` | Orlando / QA |
| **`QG-02`** | **Pass Rate 100% en Pruebas** | Ejecución de todas las suites unitarias, MockMvc, JPA y rollback transaccional. | **100% verde** (0 fallas, 0 errores, 0 tests ignorados). | `./mvnw test`<br>`ng test --watch=false` | Compañero QA |
| **`QG-03`** | **Lista Blanca Tecnológica Estricta** | Cero dependencias en `pom.xml` o `package.json` fuera del programa oficial del curso. | 0 dependencias no autorizadas en Sección 2 de `AGENTS.md`. | Inspección de `pom.xml` y `package.json` | Orlando / QA |
| **`QG-04`** | **Respeto a la Progresión Didáctica** | Prohibición absoluta de `JpaRepository` en Semana 03; no JWT antes de Semana 05; no Angular antes de Semana 07. | 100% de cumplimiento del cronograma académico UPN. | Auditoría de código por commits `[RF-SEMXX-YY]` | Orlando / QA |
| **`QG-05`** | **Blindaje de Seguridad OWASP** | Consultas JPQL parametrizadas (`:param`), anti-tampering de precios en backend y validación Magic Bytes. | 0 vulnerabilidades SQLi; cero confianza en totales del frontend. | Verificación de `@Param`, `@Valid` y Magic Bytes | Compañero QA |
| **`QG-06`** | **Contrato Canónico de Excepciones** | Todas las excepciones HTTP retornan la estructura JSON oficial (`timestamp`, `status`, `error`, `message`, `path`). | 100% de errores capturados en `@RestControllerAdvice`. | Pruebas MockMvc de `GlobalExceptionHandler` | Compañero QA |
| **`QG-07`** | **Fidelidad de Tokens y UI/UX** | Respeto riguroso de la paleta de colores corporativa (`#5980a6`, `#1f5fa9`, `#15181a`) y tipografía Barlow. | 100% de fidelidad con el prototipo original Vanilla de Vivian. | Inspección visual en viewports RNF03 | Vivian |
| **`QG-08`** | **Trazabilidad y Diagramación Draw.io** | Todos los diagramas en `/docs` deben ser archivos `.drawio` nativos. Cero sintaxis de diagramas de texto en `/docs`. | 0 ocurrencias de diagramas de texto; 19 archivos verificados en `/docs`. | Script PowerShell de validación integral | Compañero QA |

---

## 7. Trazabilidad con Otros Módulos del Sistema

Este documento se conecta integralmente con los demás componentes del Vault de Obsidian:
- [[00-MOC-GlobalFrio|MOC Central de Global Frío]] — Índice general del sistema.
- [[01-Requerimientos-y-Negocio/PRD-GlobalFrio-Cotizaciones|PRD Comercial y Requerimientos]] — Definición de Historias de Usuario (`HU01` a `HU08`) y criterios Gherkin.
- [[01-Requerimientos-y-Negocio/Matriz-Trazabilidad-Silabo-UPN|Matriz de Trazabilidad Sílabo UPN]] — Cruce de las 16 semanas y los hitos evaluativos.
- [[01-Requerimientos-y-Negocio/Guia-Evidencias-Vivian-y-Flujo-Equipo|Guía de Evidencias de Vivian y Flujo de Equipo]] — Matriz RACI y los 5 documentos comerciales reales.
- [[02-Arquitectura-y-Backend/HLD-Arquitectura-Sistemas|HLD Arquitectura de Sistemas]] — Topología desacoplada y contratos de red.
- [[02-Arquitectura-y-Backend/LLD-Backend-SpringBoot-Patrones|LLD Backend Spring Boot y Patrones GoF]] — Especificación del patrón Decorator y Factory.
- [[02-Arquitectura-y-Backend/Modelo-Base-Datos-y-Dummy-Data|Modelo de Base de Datos y Datos Dummy]] — DDL de 11 tablas y script semilla.
- [[02-Arquitectura-y-Backend/Seguridad-OWASP-y-AI-Risks|Seguridad OWASP y Riesgos AI]] — Mitigaciones técnicas web y LLM.
- [[03-UI-UX-Design-Handoff/UI-UX-Design-Handoff-Spec|Especificación de Handoff UI/UX]] — Tokens de diseño y componentes visuales.
- [[03-UI-UX-Design-Handoff/Guia-Angular-para-Vivian|Guía Angular para Vivian]] — Guía de desarrollo frontend para la dueña del diseño.
- [[04-QA-y-Testing/Playbook-QA-y-Arreglo-de-Errores|Playbook Operativo de QA y Arreglo de Errores]] — Manual de resolución activa de bugs para el compañero de QA.

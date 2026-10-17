---
title: Matriz de Trazabilidad Sílabo UPN — Global Frío
aliases:
  - Trazabilidad Sílabo UPN
  - Matriz UPN 16 Semanas
  - Cumplimiento Académico SIST1402A
tags:
  - trazabilidad
  - silabo
  - upn
  - ods09
  - academic
owner: Orlando (Arquitecto de Sistemas & Líder Backend)
status: Aprobado
version: 1.0.0
updated: 2026-10-17
---

# Matriz de Trazabilidad Sílabo UPN — Global Frío

> [!IMPORTANT]
> **Marco Canónico de Cumplimiento Curricular:**  
> Este documento establece la correspondencia bidireccional entre los objetivos pedagógicos de la asignatura **Soluciones Web y Aplicaciones Distribuidas (SIST1402A)** de la **Universidad Privada del Norte (UPN - Periodo 2026-2)**, cátedra del **Ing. Mg. Carlos Alberto Ponte Ramírez**, y la implementación técnica del Sistema Web de Cotización de **Global Frío S.A.C.**
> 
> Se garantiza el respeto absoluto de la **Lista Blanca Tecnológica**, la **Progresión Didáctica por Semanas**, los **4 Hitos de Evaluación Académica** y la contribución formal a las metas del **ODS 09: Industria, Innovación e Infraestructura**.

---

## 1. Esquema General de Unidades y Progresión Didáctica

| Unidad Curricular | Semanas | Enfoque Pedagógico y Tecnológico | Hitos Académicos Asociados |
| :--- | :---: | :--- | :--- |
| **Unidad I: Introducción al Desarrollo Web, Procesos y Backend Inicial** | Semanas 01 - 02 | Arquitectura N-Capas Spring Boot, Patrones GoF (Singleton, Factory, Decorator, Repository) y almacenamiento transitorio en memoria sin BD. | Configuración base del proyecto y casos de uso comerciales. |
| **Unidad II: Backend con Spring Boot, Persistencia y Gestión del Proyecto** | Semanas 03 - 06 | Persistencia relacional manual JPA (`EntityManager` en S03 $\rightarrow$ `JpaRepository` en S04), Scrum, Bean Validation y Seguridad JWT. | **Evaluación T1 (Semana 06 — Ponderación 10%)** |
| **Unidad III: Front-end Angular e Integración con API REST** | Semanas 07 - 11 | SPA con Angular 18, Data Binding, SASS con Design Tokens corporativos, Data Services RxJS, formularios reactivos y paginación `Pageable`. | **Evaluación T2 (Semana 10 — Ponderación 20%)** |
| **Unidad IV: Arquitecturas Distribuidas, Transacciones y Despliegue** | Semanas 12 - 16 | Control transaccional atómico `@Transactional` con prueba de Rollback, interoperabilidad REST vs SOAP, SSL/TLS (8443) y producción. | **Evaluación T3 (Semana 13 — 30%)**<br>**Examen Final (Semana 16 — 40%)** *(No aplica sustitutorio)* |

---

## 2. Matriz Exhaustiva de Trazabilidad: 16 Semanas Lectivas

A continuación se detalla la matriz integral que correlaciona cada semana lectiva con sus requerimientos funcionales, especificaciones técnicas de clase, reglas didácticas de cátedra y criterios de aceptación deterministas:

| Sem | Requerimiento Cátedra | Especificación Técnica | Unidad Temática | Alcance Técnico en Global Frío | Restricción Didáctica Obligatoria | Lista Blanca: Clases / Anotaciones | Criterio de Aceptación Determinista |
| :-: | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **S01** | `RF-SEM01-01` | `SPEC-SEM01-A` | Unidad I | Configuración inicial del backend cliente-servidor en N-capas desacopladas. Paquete base `pe.upn.sist1402a`. Servidor Tomcat en puerto 8080. | Spring Initializr oficial, Java 17 LTS, Maven Wrapper `./mvnw`. Prohibido añadir librerías fuera de lista blanca. | `@SpringBootApplication`, `@RestController`, `@GetMapping`, `ResponseEntity`. | Compila con `./mvnw clean compile`. Endpoint base `GET /api/saludo` responde `"Backend activo"`. |
| **S01** | `RF-SEM01-02` | `SPEC-SEM01-B` | Unidad I | Contraste arquitectónico: Renderizado server-side (Thymeleaf) frente a API REST (JSON). | Vista web clásica para reportes vs endpoint JSON para consumo por cliente SPA. | `spring-boot-starter-thymeleaf`, `@Controller`, `Model`, `resources/templates/`. | Controlador `@Controller` renderiza plantilla HTML de proforma; `@RestController` emite JSON puro. |
| **S02** | `RF-SEM02-01` | `SPEC-SEM02-A` | Unidad I | Delimitación del proceso de negocio de climatización comercial y cámaras frigoríficas. | Casos de uso de Global Frío (Ventanilla `07061`, Edgar Sabalu `904 209 489`). Entidades `Producto`, `Cotizacion`. | Java POO, encapsulamiento, getters/setters, validación de estado en dominio. | Documento formal de casos de uso y modelos de dominio estructurados. |
| **S02** | `RF-SEM02-02` | `SPEC-SEM02-B` | Unidad I | Aplicación de patrones de diseño GoF en backend: Singleton, Factory, Decorator y Repository. | `PresupuestoCalculator` (Decorator con `BigDecimal`), `CotizacionSnapshotFactory` (Factory), Servicios Spring (Singleton). | Patrones GoF puros, `@Service`, separación estricta controller-service-model. | Servicios de negocio desacoplados; cálculo acumulativo de accesorios sin modificar la clase base. |
| **S02** | `RF-SEM02-03` | `SPEC-SEM02-C` | Unidad I | Endpoints GET y POST con almacenamiento transitorio en memoria sin persistencia relacional. | Gestión de catálogo y cotizaciones temporales almacenadas en estructuras `List<T>`. | **PROHIBIDO el uso de base de datos relacional en S02**. Uso de Streams, `filter`, `findFirst`. | Endpoints `GET` y `POST /api/productos` operan en memoria respondiendo `200 OK`, `201 Created` y `404 Not Found`. |
| **S03** | `RF-SEM03-01` | `SPEC-SEM03-A` | Unidad II | Diseño técnico de la API REST bajo el marco tradicional PMI (Alcance, EDT/WBS, Contratos). | Definición formal de contratos de endpoints, estructura de payloads JSON y persistencia durable. | Metodología PMI/PMBOK. Los datos deben sobrevivir al reinicio del servidor. | Matriz de trazabilidad entre entregables EDT y firmas de endpoints REST. |
| **S03** | `RF-SEM03-02` | `SPEC-SEM03-B` | Unidad II | Persistencia relacional manual mediante JPA `EntityManager` en clase `@Repository`. | Conexión a MySQL 8.x / H2. Mapeo de entidades `Producto` y `Cotizacion`. Inserción y consulta SQL/JPQL directa. | **PROHIBIDO el uso de `JpaRepository` en Semana 03**. Obligatorio `entityManager.persist()` y `entityManager.createQuery()`. | `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@PersistenceContext EntityManager`. Registros persisten físicamente en BD. |
| **S04** | `RF-SEM04-01` | `SPEC-SEM04-A` | Unidad II | Gestión ágil de proyectos mediante marco Scrum (Product Backlog, Sprints, Criterios DoD). | Product Backlog del cotizador con historias `HU01` a `HU08` priorizadas con criterios de aceptación. | Historias de usuario en formato estándar con Definition of Done determinista. | Backlog documentado en Obsidian con estimación y dependencias entre historias. |
| **S04** | `RF-SEM04-02` | `SPEC-SEM04-B` | Unidad II | CRUD integral con Spring Data JPA mediante interfaces derivadas de `JpaRepository`. | Operaciones completas de lectura, alta, edición y baja sobre catálogo y cotizaciones. | **Autorización formal de `JpaRepository` a partir de esta semana**. Métodos derivados. | `JpaRepository<Producto, Long>`, `findAll()`, `findById()`, `save()`, `deleteById()`. | Métodos CRUD operan sobre base relacional con respuestas HTTP estandarizadas. |
| **S04** | `RF-SEM04-03` | `SPEC-SEM04-C` | Unidad II | Consultas JPQL parametrizadas seguras, `@NamedQuery` y campos calculados con `@Transient`. | Filtrado de equipos por rango de BTU y precio. Campo calculado `@Transient` para estado de stock o total. | **PROHIBIDA la concatenación de strings en JPQL**. Uso obligatorio de `:param` con `@Param`. | `@Query("SELECT p FROM Producto p WHERE p.precio BETWEEN :min AND :max")`, `@Transient`. | Consultas dinámicas inmunes a inyección SQL (SQLi). Atributos calculados no se persisten en tabla. |
| **S05** | `RF-SEM05-01` | `SPEC-SEM05-A` | Unidad II | Estandarización de endpoints REST y validación declarativa con Bean Validation API. | Validación de payloads de entrada (área $>0$, correo válido, teléfono de 9 dígitos, RUC 11 dígitos). | `spring-boot-starter-validation`. Manejo de excepciones centralizado con `@RestControllerAdvice`. | `@Valid`, `@NotBlank`, `@NotNull`, `@Min`, `@Max`, `@Email`, `MethodArgumentNotValidException`. | Payloads erróneos responden `400 Bad Request` con mapa detallado de campos y mensajes claros. |
| **S05** | `RF-SEM05-02` | `SPEC-SEM05-B` | Unidad II | Seguridad backend stateless con Spring Security 6 y tokens JSON Web Token (JWT). | Endpoint público `/api/auth/login`. Endpoints administrativos protegidos exigiendo token Bearer en cabecera. | Tokens firmados HMAC-SHA256 con tiempo de expiración (24h). Cero almacenamiento de sesión en servidor. | `spring-boot-starter-security`, `io.jsonwebtoken:jjwt-*`, `SecurityFilterChain`, `JwtFilter`. | Acceso anónimo a rutas privadas retorna `401 Unauthorized` / `403 Forbidden`. Token válido autoriza acceso. |
| **S05** | `RF-SEM05-03` | `SPEC-SEM05-C` | Unidad II | Control de políticas CORS para consumo seguro desde el cliente Single Page Application. | Habilitación de consumo cross-origin desde `http://localhost:4200` y soporte pre-flight `OPTIONS`. | Configuración en Spring Security (`CorsConfigurationSource`). | `@CrossOrigin(origins = "http://localhost:4200")`, `CorsConfig.java`. | Solicitudes pre-flight `OPTIONS` retornan `200 OK` admitiendo verbos HTTP y cabeceras de autorización. |
| **S06** | `RF-SEM06-01` | `SPEC-SEM06-A` | Unidad II | **Consolidación Backend — Evaluación T1 (10% de la nota final)**. | Integración total: API REST, persistencia JPA relacional, Bean Validation, JWT y suite cURL/Postman. | Demostración en vivo ante cátedra con datos persistentes en MySQL 8.x / H2. | Spring Boot 3.3.4, cURL Scripts, Colección Postman con variables de entorno. | Casos de prueba ejecutados satisfactoriamente; persistencia verificada tras reinicio del servidor. |
| **S06** | `RF-SEM06-02` | `SPEC-SEM06-B` | Unidad II | Alineación estratégica y justificación de impacto en el Objetivo de Desarrollo Sostenible (ODS 09). | Sustento del dimensionamiento térmico exacto para evitar desperdicio eléctrico industrial y refrigerantes limpios. | Marco Agenda 2030 de las Naciones Unidas (Metas 9.1, 9.4, 9.b). | Documento técnico formal de impacto ambiental y eficiencia energética. | Aprobación de la justificación técnica por el docente en la evaluación T1. |
| **S07** | `RF-SEM07-01` | `SPEC-SEM07-A` | Unidad III | Inicialización de la arquitectura frontend Single Page Application (SPA) con Angular CLI. | Estructura modular de vistas, componentes y servicios en puerto 4200 (`frontend/src/app/`). | **Autorización formal de Angular a partir de Semana 07**. `ng new frontend --routing --style=scss`. | Node.js LTS, Angular CLI `ng`, `@angular/core`, `package.json` oficial. | Aplicación compila y corre con `ng serve` en `localhost:4200` sin errores de consola ni advertencias. |
| **S07** | `RF-SEM07-02` | `SPEC-SEM07-B` | Unidad III | Mecanismos de comunicación de componentes, Data Binding y directivas estructurales. | Interpolación `{{ }}`, property binding `[ ]`, event binding `( )`, directivas `*ngIf`, `*ngFor`, `@Input()`, `@Output()`. | Separación estricta de lógica en `.ts`, vista en `.html` y diseño en `.scss`. | `EventEmitter`, `@Input`, `@Output`, directivas estructurales nativas. | Componentes hijos emiten eventos al componente contenedor; listas de productos se renderizan dinámicamente. |
| **S07** | `RF-SEM07-03` | `SPEC-SEM07-C` | Unidad III | Diseño responsivo con Bootstrap 5, Angular Material y SASS adaptado a Design Tokens de Global Frío. | Maquetación visual basada en los tokens corporativos: acento acero `#5980a6`, botón `#1f5fa9`, fondo oscuro `#15181a`, Barlow. | Bootstrap 5 (`styles.scss`), `@angular/material`, Flexbox y CSS Grid. Cero colisiones CSS. | Variables CSS globales (`--gf-accent`, `--gf-btn`, `--gf-bg`), `styles.scss`. | Interfaz 100% responsiva (móvil 360px a desktop 1920px); cero desbordamiento horizontal (`overflow-x`). |
| **S08** | `RF-SEM08-01` | `SPEC-SEM08-A` | Unidad III | Consumo asíncrono de API REST mediante Data Services basados en `HttpClient` y RxJS. | Servicios Angular tipados (`ProductoService`, `CotizacionService`) consumiendo endpoints Spring Boot. | `provideHttpClient()` / `HttpClientModule`. Uso de `Observable<T>`, `pipe`, `map`, `catchError`. | `HttpClient`, RxJS `Observable`, interfaces TypeScript correspondientes a DTOs. | Respuestas del backend mapeadas a modelos TypeScript tipados; datos renderizados en pantalla. |
| **S08** | `RF-SEM08-02` | `SPEC-SEM08-B` | Unidad III | Enrutamiento SPA, captura de Query Params y desarrollo de Formularios Reactivos con validaciones. | Navegación entre vistas (`/cotizar`, `/catalogo`, `/login`), formularios reactivos (`FormGroup`, `FormControl`). | `ReactiveFormsModule`. Validación reactiva síncrona en cliente previa al envío HTTP. | `RouterModule`, `Routes`, `router-outlet`, `FormBuilder`, `Validators.required`. | Navegación fluida sin recargar el navegador; formularios muestran errores de validación en tiempo real. |
| **S08** | `RF-SEM08-03` | `SPEC-SEM08-C` | Unidad III | Seguridad frontend con Interceptor HTTP de tokens JWT y protección de rutas con Route Guards. | Inyección de cabecera `Authorization: Bearer <token>` en peticiones protegidas; `AuthGuard` bloquea rutas privadas. | Almacenamiento seguro del token en cliente (`localStorage`). Redirección a `/login` si no autenticado. | `HttpInterceptor`, `HttpRequest`, `HttpHandler`, `CanActivate`, `Router`. | Acceso a `/panel/*` redirige a login; peticiones salientes incluyen automáticamente el token Bearer. |
| **S08** | `RF-SEM08-04` | `SPEC-SEM08-D` | Unidad III | Despliegue de integración local simultáneo cliente-servidor en entorno de desarrollo. | Ejecución coordinada de Backend Spring Boot (puerto 8080) y Frontend Angular (puerto 4200). | Comunicación cross-origin operativa sin bloqueos de navegador. | Terminal 1: `./mvnw spring-boot:run`. Terminal 2: `ng serve`. | La SPA consume endpoints reales del backend; datos fluyen sin errores de red en la consola del navegador. |
| **S09** | `RF-SEM09-01` | `SPEC-SEM09-A` | Unidad III | Integración Extremo a Extremo (E2E): Flujo comercial completo crear-listar-editar-eliminar. | Creación de cotización desde la SPA, persistencia en MySQL, consulta en panel asesor y cambio de estado. | Consistencia transaccional cliente-servidor-base de datos. | Angular Frontend + Spring Boot Backend + MySQL 8.x Database. | Las acciones ejecutadas en la interfaz web se reflejan de inmediato en los registros de la base de datos. |
| **S09** | `RF-SEM09-02` | `SPEC-SEM09-B` | Unidad III | Manejo robusto de errores HTTP y retroalimentación interactiva al usuario final. | Intercepción de errores 400, 401, 403, 404 y 500 mostrando alertas visuales con `MatSnackBar` o alertas Bootstrap. | Manejo con operador `catchError` de RxJS; la interfaz no debe congelarse ni quedar en blanco. | RxJS `catchError`, `throwError`, Angular Material `MatSnackBar`. | Mensajes de error legibles para el usuario; interfaz recuperable ante caídas temporales del servidor. |
| **S09** | `RF-SEM09-03` | `SPEC-SEM09-C` | Unidad III | Documentación técnica del consumo de la API REST y evidencias de pruebas de integración. | Registro de matrices de endpoints, payloads JSON de entrada/salida y capturas de prueba E2E. | Trazabilidad documental rigurosa para la defensa de la evaluación T2. | Archivos Markdown en `/docs`, capturas en Postman y consola de red de DevTools. | Carpeta documental completa con evidencias verificables de interacción cliente-servidor. |
| **S10** | `RF-SEM10-01` | `SPEC-SEM10-A` | Unidad III | **Defensa de Solución Integrada — Evaluación T2 (20% de la nota final)**. | Sustentación en vivo de la aplicación integrada completa (Frontend Angular + Backend Spring Boot + JWT + CRUD). | Demostración fluida de login, rutas privadas, validaciones reactivas, CRUD completo y persistencia. | Entorno local completo en vivo (Tomcat 8080 + Angular 4200 + MySQL). | Evaluación satisfactoria ante el docente Carlos Alberto Ponte Ramírez con rúbrica oficial. |
| **S10** | `RF-SEM10-02` | `SPEC-SEM10-B` | Unidad III | Estudio de casos de innovación sostenible en la cadena de frío comercial (ODS 09). | Sustentación del impacto de Global Frío en postas médicas, cámaras frigoríficas y conservación de vacunas/alimentos. | Relación directa con infraestructura resiliente y tecnologías limpias (Metas 9.1 y 9.4). | Estudio de caso documentado con métricas de ahorro energético y reducción de mermas. | Evidencia aceptada como parte integral de la calificación de la evaluación T2. |
| **S11** | `RF-SEM11-01` | `SPEC-SEM11-A` | Unidad III | Implementación de paginación eficiente en el servidor mediante Spring Data `Pageable`. | Grilla de cotizaciones en panel asesor con paginación (`PageRequest.of(page, size, sort)`) y `MatPaginator`. | Optimización de memoria en servidor y reducción de payload transferido sobre la red. | Spring Data `Page<T>`, `Pageable`, Angular Material `MatPaginator`, `MatTable`. | Endpoint `/api/cotizaciones?page=0&size=10` devuelve estructura `Page` con 10 registros y metadatos. |
| **S11** | `RF-SEM11-02` | `SPEC-SEM11-B` | Unidad III | Documentación interactiva de la API con Swagger UI y especificación OpenAPI 3. | Interfaz interactiva de documentación viva en `/swagger-ui/index.html` con botón de autenticación Bearer JWT. | Documentación generada automáticamente desde controladores Spring Boot sin desfases de versión. | `springdoc-openapi-starter-webmvc-ui` versión 2.5.0, anotaciones `@Operation`, `@SecurityRequirement`. | Swagger UI accesible en navegador permitiendo ejecutar pruebas interactivas con token JWT. |
| **S11** | `RF-SEM11-03` | `SPEC-SEM11-C` | Unidad III | Refinamiento de Experiencia de Usuario (UX): Spinners reactivos y modales preventivos. | Indicadores de carga asíncrona durante peticiones HTTP y modales de confirmación antes de eliminar cotizaciones. | Prevención de errores operativos humanos y retroalimentación inmediata. | Angular Material `MatProgressSpinner`, `MatDialog`, directivas reactivas. | El usuario percibe fluidez; operaciones irreversibles solicitan confirmación explícita previa. |
| **S12** | `RF-SEM12-01` | `SPEC-SEM12-A` | Unidad IV | Consultas JPQL avanzadas con parámetros nombrados y uniones entre entidades relacionales. | Búsqueda de cotizaciones asociadas a clientes, ítems de presupuesto y órdenes mediante `JOIN` en JPQL. | Parámetros nombrados obligatorios con `@Param`. Prohibición total de consultas nativas sin tipar. | `@Query("SELECT c FROM Cotizacion c JOIN FETCH c.cliente WHERE c.estado = :estado")`. | Consultas óptimas que evitan el problema de N+1 consultas en Hibernate mediante `JOIN FETCH`. |
| **S12** | `RF-SEM12-02` | `SPEC-SEM12-B` | Unidad IV | Control transaccional atómico y políticas de reversión total mediante `@Transactional`. | Conversión de cotización aceptada a Orden de Servicio con snapshot de precios bajo transacción atómica. | La anotación debe residir **exclusivamente en métodos de la capa `@Service`**. | `@Transactional(rollbackFor = Exception.class)`, `Isolation.READ_COMMITTED`. | Ante cualquier falla inducida, Hibernate ejecuta `ROLLBACK` atómico; la base de datos queda inalterada. |
| **S12** | `RF-SEM12-03` | `SPEC-SEM12-C` | Unidad IV | Estructuración del sistema bajo principios de Arquitectura Orientada a Servicios (SOA). | Contratos de servicio independientes, alta cohesión y bajo acoplamiento entre módulos de negocio. | Interfaces de servicio que desacoplan la lógica de dominio de los detalles de persistencia. | Interfaces Java en paquete `pe.upn.sist1402a.service`, DTOs de transporte. | Capa de negocio completamente reutilizable e independiente del protocolo de transporte web. |
| **S13** | `RF-SEM13-01` | `SPEC-SEM13-A` | Unidad IV | **Consolidación Transaccional — Evaluación T3 (30% de la nota final)**. | Demostración en vivo de la atomicidad del sistema y la reversión íntegra de transacciones ante fallas forzadas. | Prueba automatizada con JUnit 5 que comprueba que tras un error no quedan registros huérfanos. | `@SpringBootTest`, `assertThrows`, verificación de repositorios antes y después de la falla. | Prueba de rollback transaccional aprobada al 100% ante la cátedra con calificación sobresaliente. |
| **S14** | `RF-SEM14-01` | `SPEC-SEM14-A` | Unidad IV | Cuadro comparativo formal e interoperabilidad entre arquitecturas RESTful y servicios SOAP. | Documentación técnica de contraste entre REST (JSON, HTTP ligero) y SOAP (XML, contratos WSDL, WS-Security). | Análisis de aplicabilidad para integración con sistemas legados hospitalarios o aduaneros. | Especificación teórica y técnica documentada en `/docs/02-Arquitectura-y-Backend/`. | Sustentación técnica clara de las ventajas y desventajas de cada paradigma arquitectónico. |
| **S14** | `RF-SEM14-02` | `SPEC-SEM14-B` | Unidad IV | Desacoplamiento modular del backend con miras a una arquitectura de Microservicios distribuidos. | Separación de módulos de Cotización, Catálogo y Clientes con interfaces claras de comunicación. | Modularidad interna basada en paquetes de dominio sin acoplamiento circular. | Inyección de dependencias por constructor en Spring (`@RequiredArgsConstructor` de Lombok). | Código limpio, testeable y preparado para una eventual segregación en contenedores independientes. |
| **S14** | `RF-SEM14-03` | `SPEC-SEM14-C` | Unidad IV | Empaquetado de artefactos ejecutables para entornos empresariales de producción. | Generación del archivo `.jar` ejecutable del backend y compilación de bundles estáticos en Angular `dist/`. | Optimización de assets, minificación de JavaScript y exclusión de dependencias de desarrollo (`devtools`). | `./mvnw clean package -DskipTests=false`, `ng build --configuration production`. | Artefacto `.jar` autocontenido y directorio `dist/` listos para ser levantados en servidor productivo. |
| **S15** | `RF-SEM15-01` | `SPEC-SEM15-A` | Unidad IV | Puesta en marcha en servidores de aplicaciones distribuidos empresariales. | Despliegue en Apache Tomcat embebido/standalone, o servidores Java EE/Jakarta EE (WildFly / WebLogic). | Configuración de variables de entorno productivas (`SPRING_PROFILES_ACTIVE=prod`). | Tomcat 10+, Red Hat WildFly, Oracle WebLogic Server, `application-prod.properties`. | Servidor levanta satisfactoriamente en entorno empresarial gestionando conexiones a MySQL 8.x. |
| **S15** | `RF-SEM15-02` | `SPEC-SEM15-B` | Unidad IV | Configuración de canal de comunicación cifrado mediante certificados de seguridad SSL/TLS (HTTPS). | Habilitación de HTTPS en puerto seguro 8443 mediante keystore digital (PKCS12 / JKS). | Todo el tráfico web sensible (credenciales, datos de clientes, proformas) debe circular cifrado. | `server.port=8443`, `server.ssl.key-store`, `server.ssl.key-store-type=PKCS12`. | Conexión a `https://localhost:8443/api/*` establece túnel cifrado TLS 1.3 con candado verde. |
| **S15** | `RF-SEM15-03` | `SPEC-SEM15-C` | Unidad IV | Auditoría final de infraestructura tecnológica sostenible (Meta 9.4 del ODS 09). | Evaluación del consumo de memoria RAM y CPU del backend Spring Boot y optimización del pool HikariCP. | Dimensionamiento eficiente de hardware y reducción de consumo energético de servidores. | Métricas de Spring Boot Actuator, optimización de queries SQL y pool de conexiones Hikari. | Informe de auditoría técnica que certifica el uso racional de cómputo en la solución distribuida. |
| **S16** | `RF-SEM16-01` | `SPEC-SEM16-A` | Unidad IV | **Sustentación Integral del Proyecto Final (40% de la nota final de la asignatura)**. | Defensa formal ante el jurado y el docente de la solución web distribuida completa de Global Frío. | **REGLA MANDATARIA DE FACULTAD: NO APLICA EXAMEN SUSTITUTORIO**. | Sistema completo integrado en producción (Frontend SPA + Backend N-Capas + MySQL + JWT + HTTPS). | Sustentación exitosa demostrando código fuente, base de datos, pruebas continuas y valor de negocio. |

---

## 3. Desglose Ponderado de Evaluaciones Académicas (Periodo 2026-2)

> [!WARNING]
> **Normativa de Evaluación de la Facultad de Ingeniería UPN:**  
> Las calificaciones se consolidan de manera continua y acumulativa según la siguiente escala oficial:
> 
> $$\text{Promedio Final} = (0.10 \times \text{T1}) + (0.20 \times \text{T2}) + (0.30 \times \text{T3}) + (0.40 \times \text{Evaluación Final})$$
> 
> Al no existir examen sustitutorio en esta asignatura, cada hito es eliminatorio y demanda preparación rigurosa y código fuente 100% funcional y probado.

### Hito T1: Consolidación Backend, Persistencia y Seguridad (Semana 06 — 10%)
- **Entregables Requeridos:**
  1. Proyecto Spring Boot 3.3.4 con Java 17 LTS compilando sin errores (`./mvnw clean compile`).
  2. Implementación de controladores REST con respuestas HTTP estructuradas (`200 OK`, `201 Created`, `400 Bad Request`, `404 Not Found`).
  3. Evidencia del cumplimiento didáctico de Semana 03 (`EntityManager` manual) y evolución a `JpaRepository` en Semana 04.
  4. Consultas JPQL parametrizadas seguras mitigando ataques SQLi.
  5. Bean Validation declarativo en DTOs con captura de errores en `@RestControllerAdvice`.
  6. Endpoint `/api/auth/login` emitiendo tokens JWT firmados HMAC-SHA256 y protección de recursos administrativos.
  7. Colección de pruebas en Postman y cURL con evidencias de ejecución exitosa.
  8. Justificación inicial de alineación con el ODS 09 (Industria, Innovación e Infraestructura).

### Hito T2: Solución Integrada Frontend Angular y Backend REST (Semana 10 — 20%)
- **Entregables Requeridos:**
  1. Aplicación Angular 18 SPA modular ejecutándose en puerto 4200 y conectada al backend en puerto 8080.
  2. Implementación completa de Data Services con `HttpClient` y RxJS tipados con modelos TypeScript.
  3. Formularios Reactivos con validaciones visuales en tiempo real y diseño responsivo adaptado a los tokens de Global Frío (`#5980a6`, `#1f5fa9`, `#15181a`, `Barlow`).
  4. Seguridad frontend con `HttpInterceptor` inyectando automáticamente la cabecera `Authorization: Bearer <token>` y protección de rutas con `AuthGuard`.
  5. Flujo integral Extremo a Extremo (E2E) demostrado en vivo: cotización generada en cliente persistida en base de datos MySQL 8.x.
  6. Manejo elegante de errores de red (400, 401, 403, 404, 500) con alertas `MatSnackBar` sin pantallas en blanco.
  7. Estudio de caso de innovación sostenible documentando la optimización de la cadena de frío en postas médicas y centros de acopio.

### Hito T3: Atomicidad Transaccional, Rollback y OpenAPI (Semana 13 — 30%)
- **Entregables Requeridos:**
  1. Demostración en vivo de transacciones ACID en la capa `@Service` utilizando `@Transactional(rollbackFor = Exception.class)`.
  2. **Prueba Crítica de Rollback Forzado:** Ejecución de test unitario JUnit 5 que induce un fallo deliberado durante la conversión de cotización a Orden de Servicio, demostrando que la base de datos revierte todas las modificaciones y permanece 100% íntegra.
  3. Consultas JPQL avanzadas con `JOIN FETCH` optimizadas para evitar problemas de sobrecarga N+1 en Hibernate.
  4. Paginación en servidor con `Pageable` y su consumo eficiente en Angular con `MatPaginator`.
  5. Documentación viva de la API con Swagger UI / OpenAPI 3 (`springdoc-openapi-starter-webmvc-ui` en `/swagger-ui/index.html`) con capacidad de autenticación interactiva.

### Evaluación Final: Solución Distribuida, Producción y Sustentación (Semana 16 — 40%)
- **Entregables Requeridos:**
  1. Sistema web distribuido completo en funcionamiento, empaquetado como binario `.jar` ejecutable de Spring Boot y bundles estáticos de Angular en `dist/`.
  2. Despliegue operativo en servidor empresarial Tomcat / WildFly configurado con perfiles de producción (`application-prod.properties`).
  3. Canal de comunicación seguro cifrado mediante protocolo HTTPS en puerto 8443 con certificado SSL/TLS.
  4. Sustentación técnica integral ante el jurado y cátedra del Ing. Mg. Carlos Alberto Ponte Ramírez:
     - Defensa de arquitectura, base de datos y seguridad por **Orlando**.
     - Defensa de la estrategia de pruebas continuas y resolución en código por el **Compañero de QA**.
     - Defensa del modelo comercial, los 5 documentos reales y la experiencia frontend por **Vivian**.
  5. Auditoría final de infraestructura sostenible y cumplimiento del ODS 09.

---

## 4. Alineación Estratégica con el ODS 09 (Naciones Unidas)

El proyecto de Global Frío no es únicamente un desarrollo de software académico; constituye una herramienta tecnológica orientada a resolver problemas industriales y de salud pública en el Perú:

| Meta ODS 09 | Enfoque de la Meta (Agenda 2030) | Aplicación y Solución en Global Frío S.A.C. |
| :--- | :--- | :--- |
| **Meta 9.1** | Infraestructuras fiables, sostenibles, resilientes y de calidad | Preservación de la cadena de frío en cámaras frigoríficas y salas limpias para postas médicas (vacunas, insulina) y agroindustria alimentaria. |
| **Meta 9.4** | Modernización industrial, eficiencia de recursos y tecnologías limpias | Selección óptima de capacidad térmica (BTU/h) que previene el sobreconsumo eléctrico (15% a 25%), adopción de gases ecológicos R-410A / R-32 y digitalización cero papel. |
| **Meta 9.b** | Apoyo a la investigación, innovación y tecnología nacional para PYMEs | Automatización integral de cotización y orden de trabajo para una PYME técnica de Ventanilla frente a corporaciones transnacionales. |

### 4.1. Meta 9.1: Infraestructuras Sanitarias y Productivas Resilientes
- **Impacto Social en el Perú:** La ruptura de la cadena de frío es la principal causa de desecho de medicamentos biológicos y mermas en productos perecibles en el país. El sistema web de Global Frío calcula la potencia frigorífica exacta para cámaras de conservación ($2^\circ\text{C}$ a $8^\circ\text{C}$) y congelación (hasta $-20^\circ\text{C}$) en postas de salud y almacenes logísticos de Lima Norte y Callao, garantizando infraestructura confiable para la preservación de la vida.

### 4.2. Meta 9.4: Eficiencia Energética y Descarbonización Industrial
- **Impacto Ambiental Cuantificable:** Un compresor de aire acondicionado sobredimensionado en 12,000 BTU/h desperdicia en promedio $1.2 \text{ kWh}$ por cada hora de operación comercial. Para un local que opera 10 horas al día, esto representa un sobreconsumo de $360 \text{ kWh}$ mensuales. El algoritmo recomendador de Global Frío ajusta la capacidad exacta requerida por el volumen del recinto, mitigando el desperdicio eléctrico. Asimismo, el sistema cataloga y prioriza equipos inverter con refrigerantes R-410A y R-32 con potencial de agotamiento de ozono cero (ODP = 0).

### 4.3. Meta 9.b: Innovación Tecnológica y Competitividad para PYMEs Peruanas
- **Soberanía Tecnológica Nacional:** El desarrollo a medida permite a **Global Frío S.A.C.** (empresa emergente de Ventanilla) competir en igualdad de condiciones con contratistas multinacionales, automatizando sus procesos de proforma, cotización, dimensionamiento térmico y emisión de órdenes de servicio en una plataforma web soberana, sin costos de licenciamiento abusivos ni dependencia de software propietario externo.

---

## 5. Trazabilidad de Diagramas de Arquitectura con las Semanas del Sílabo

Todos los diagramas técnicos nativos en Draw.io mapean directamente a las semanas del sílabo:

- [[diagramas/01-arquitectura-n-capas-globalfrio.drawio|Diagrama 01: Arquitectura N-Capas]] $\longrightarrow$ Semanas 01, 07 y 15.
- [[diagramas/02-modelo-entidad-relacion-bd.drawio|Diagrama 02: Modelo Entidad-Relación BD]] $\longrightarrow$ Semanas 03, 04 y 12.
- [[diagramas/03-flujo-cotizacion-y-patron-decorator.drawio|Diagrama 03: Flujo Cotización y Decorator]] $\longrightarrow$ Semanas 02, 05 y 11.
- [[diagramas/04-maquina-estados-cotizacion-orden.drawio|Diagrama 04: Máquina de Estados FSM]] $\longrightarrow$ Semanas 04 y 12.
- [[diagramas/05-secuencia-jwt-rbac-logout.drawio|Diagrama 05: Secuencia JWT y RBAC]] $\longrightarrow$ Semanas 05 y 08.
- [[diagramas/06-mapa-navegacion-ui-ux.drawio|Diagrama 06: Mapa de Navegación UI/UX]] $\longrightarrow$ Semanas 07, 08 y 11.
- [[diagramas/07-flujo-trabajo-equipo-qa-vivian.drawio|Diagrama 07: Flujo de Trabajo Equipo, QA y Vivian]] $\longrightarrow$ Semanas 04, 06, 10, 13 y 16.

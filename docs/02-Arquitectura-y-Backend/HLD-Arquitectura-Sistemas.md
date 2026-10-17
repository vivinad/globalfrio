---
title: "HLD — Arquitectura de Sistemas de Alta Disponibilidad y Desacoplada"
aliases:
  - "HLD Arquitectura Global Frío"
  - "High Level Design Sistemas"
tags:
  - arquitectura
  - hld
  - springboot
  - angular
  - devops
  - upn
owner: "Orlando (Arquitecto de Sistemas & Líder Backend)"
status: "Completado"
version: "1.0.0"
updated: "2026-10-17"
---

# HLD — Arquitectura de Sistemas de Alta Disponibilidad y Desacoplada

El presente documento de Diseño de Alto Nivel (**High Level Design — HLD**) establece la estructura global, la topología física y lógica desacoplada, la estrategia de integración cliente-servidor y el modelo operativo colaborativo del **Sistema Web de Cotización Automatizada** de la empresa **Global Frío SAC** (Lima, Perú — Periodo 2026-2).

Esta arquitectura responde a los estándares académicos del curso **Soluciones Web y Aplicaciones Distribuidas (SIST1402A)** de la Universidad Privada del Norte (UPN), dictado por el **Ing. Mg. Carlos Alberto Ponte Ramírez**, alineado estratégicamente con el **Objetivo de Desarrollo Sostenible 09 (Industria, Innovación e Infraestructura)** de las Naciones Unidas.

---

## 1. Topología Física y Lógica Desacoplada

La solución adopta una arquitectura desacoplada de tipo **Single Page Application (SPA) + Microservicio RESTful N-Capas**, garantizando una estricta separación de responsabilidades entre la capa de presentación visual y la capa de lógica de negocio y persistencia.

![[diagramas/01-arquitectura-n-capas-globalfrio.drawio]]
*Figura 1: Arquitectura Lógica y Física N-Capas del Sistema Global Frío.*  
[[diagramas/01-arquitectura-n-capas-globalfrio.drawio|Abrir Diagrama de Arquitectura N-Capas en Draw.io]]

### 1.1. Capa de Presentación: Cliente SPA (Angular 18)
- **Tecnología y Runtime:** Angular 18 (TypeScript 5.4+), Bootstrap 5, Angular Material, SASS modular (`styles.scss`).
- **Puerto de Ejecución Local:** `http://localhost:4200` (`ng serve -o`).
- **Responsabilidad Técnica:** Captura de requerimientos térmicos mediante asistente modal reactivo en 3 pasos, visualización en tiempo real de presupuestos preliminares, gestión de autenticación administrativa, y dashboards operativos para asesores y administradores.
- **Canal de Transporte:** Comunicación asíncrona mediante `HttpClient` y flujos reactivos de RxJS (`Observable<T>`), inyección automática de cabeceras de seguridad mediante interceptor HTTP (`JwtInterceptor`) y protección de navegación con `AuthGuard`.
- **Propiedad del Componente:** Conducido y maquetado por **Vivian**, tomando como base su prototipo original en HTML/CSS/JS Vanilla ubicado en `frontend/prototipo-vanilla/`.

### 1.2. Canal de Comunicación e Integración: Red HTTP / REST
- **Protocolo de Aplicación:** HTTP/1.1 y HTTP/2 sobre TLS 1.3.
- **Formato de Carga Útil (Payload):** JSON estandarizado conforme a la especificación RFC 8259, con codificación UTF-8.
- **Control de Acceso Cruzado (CORS):** Filtro de seguridad `CorsConfig` en Spring Boot que valida estrictamente los encabezados pre-flight `OPTIONS`, autorizando exclusivamente peticiones desde el cliente web (`http://localhost:4200`) y exponiendo de manera controlada la cabecera `Authorization`.
- **Estandarización de Respuestas:** Todas las transacciones retornan instancias de `ResponseEntity<T>` con códigos de estado HTTP semánticos (200 OK, 201 Created, 204 No Content, 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found, 500 Internal Server Error).

### 1.3. Capa de Lógica de Negocio y Servicios: Backend Spring Boot
- **Tecnología y Runtime:** Java 17 LTS compilado sobre Spring Boot 3.3.4, gestionado mediante Maven Wrapper (`./mvnw`).
- **Servidor Web Embebido:** Apache Tomcat 10.1 optimizado para conexiones concurrentes.
- **Entorno de Desarrollo (Dev):** Puerto HTTP `8080` (`http://localhost:8080`), habilitado para depuración rápida y consola H2.
- **Entorno de Producción / Evaluación Oficial (Prod):** Puerto HTTPS seguro `8443` (`https://localhost:8443`) con cifrado SSL/TLS de 2048 bits respaldado por un almacén de claves en formato PKCS12 (`keystore.p12`).
- **Responsabilidad Técnica:** Validación estricta de datos (Jakarta Bean Validation), control de acceso basado en roles (**RBAC** con Spring Security y JJWT 0.11.5), cálculo automatizado de cargas térmicas BTU, cómputo financiero inmutable mediante el **Patrón Decorator**, auditoría de estados y gestión de transacciones ACID (`@Transactional(rollbackFor = Exception.class)`).
- **Propiedad del Componente:** Diseñado, implementado y mantenido de manera exclusiva por **Orlando Dorival**.

### 1.4. Capa de Persistencia y Bases de Datos
La solución implementa una estrategia de persistencia dual completamente configurable mediante perfiles de Spring Boot (`application-dev.properties` y `application-prod.properties`):
1. **Perfil de Desarrollo y Pruebas Unitarias (`dev`):**
   - **Motor:** H2 Database Engine en memoria (`jdbc:h2:mem:globalfriodb`).
   - **Comportamiento Hibernate:** `spring.jpa.hibernate.ddl-auto=update`.
   - **Propósito:** Ejecución autónoma de pruebas automatizadas por parte del **Compañero de QA**, garantizando aislamiento total sin dependencia de servidores externos.
2. **Perfil de Producción y Demostración Final (`prod`):**
   - **Motor:** MySQL Server 8.x en puerto local estándar `3306` (`jdbc:mysql://localhost:3306/globalfrio_db`).
   - **Comportamiento Hibernate:** `spring.jpa.hibernate.ddl-auto=validate`, asegurando que la estructura física provenga de scripts DDL formalmente versionados.
   - **Pool de Conexiones:** HikariCP con tamaño de pool administrado (10 conexiones activas, timeout de 30 segundos).

> [!IMPORTANT]
> **Alineación con la Progresión Didáctica del Sílabo (Semana 03 vs. Semana 04):**
> Para cumplir con la restricción académica de la Semana 03 (`SPEC-SEM03-B`), el backend mantiene componentes DAO manuales gobernados exclusivamente por `@PersistenceContext EntityManager entityManager`. A partir de la Semana 04 (`SPEC-SEM04-B`), coexisten interfaces `JpaRepository<T, ID>` para operaciones CRUD completas y consultas JPQL parametrizadas.

---

## 2. Estructura de Monorepo y Organización de Directorios

El repositorio se estructura como un **Monorepo Desacoplado**, manteniendo en una misma raíz controlada el código de backend, el código de frontend, los prototipos comerciales históricos y la documentación del sistema.

```text
c:\Users\ORLANDO\Desktop\globalfrio\
├── AGENTS.md                          <- Especificación canónica y lista blanca tecnológica estricta
├── README.md                          <- Guía de despliegue, scripts de inicio y contexto general
├── backend/                           <- Microservicio Backend Java 17 + Spring Boot 3.3.4
│   ├── AGENTS.md                      <- Reglas específicas y lista blanca de dependencias Maven
│   ├── pom.xml                        <- Descriptor de dependencias estricto (100% verificado)
│   ├── mvnw / mvnw.cmd                <- Maven Wrapper oficial
│   └── src/
│       ├── main/java/pe/upn/sist1402a/
│       │   ├── config/                <- SecurityConfig, CorsConfig, OpenApiConfig
│       │   ├── controller/            <- Controladores REST y Thymeleaf SSR
│       │   ├── service/               <- Interfaces de negocio (@Service)
│       │   ├── service/impl/          <- Implementaciones con @Transactional
│       │   ├── repository/            <- JpaRepository y DAOs EntityManager
│       │   ├── entity/                <- Entidades JPA (@Entity) con Bean Validation
│       │   ├── dto/                   <- Objetos de Transferencia de Datos Request/Response
│       │   ├── exception/             <- @RestControllerAdvice y excepciones de dominio
│       │   └── pattern/               <- Patrones GoF: Decorator, Factory, Singleton
│       └── main/resources/
│           ├── application.properties <- Configuración central y activación de perfiles
│           ├── application-dev.properties  <- Configuración H2 Database
│           ├── application-prod.properties <- Configuración MySQL 8.x + SSL
│           ├── data-seed-globalfrio.sql    <- Script semilla con datos reales en Soles S/
│           └── templates/             <- Plantillas SSR Thymeleaf (reporte-cotizacion.html)
├── frontend/                          <- Single Page Application Angular 18
│   ├── AGENTS.md                      <- Reglas específicas y lista blanca Angular CLI
│   ├── package.json                   <- Dependencias de cliente autorizadas
│   ├── angular.json                   <- Configuración de compilación y assets
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/            <- Componentes de interfaz (Header, Hero, Modal, Panels)
│   │   │   ├── services/              <- Servicios de comunicación HttpClient y estado RxJS
│   │   │   ├── guards/                <- AuthGuard para protección de rutas privadas
│   │   │   └── interceptors/          <- JwtInterceptor para transporte Bearer Token
│   │   ├── assets/                    <- Imágenes de catálogo Gree/Samsung, logos y manuales
│   │   └── styles.scss                <- Tokens de diseño industrial de Global Frío
│   └── prototipo-vanilla/             <- Prototipo original de Vivian (preservado como activo histórico)
│       ├── index.html                 <- Maquetación base de Vivian
│       ├── LEEME.md                   <- Documento fuente con datos reales de Global Frío
│       ├── css/theme.css              <- Definición original de la paleta de colores
│       └── js/                        <- Lógica Vanilla original
└── docs/                              <- Vault de Obsidian (12 documentos Markdown y 7 diagramas Draw.io)
    ├── 00-MOC-GlobalFrio.md           <- Mapa de Contenido central interconectado
    ├── 01-Requerimientos-y-Negocio/   <- PRD, Matriz de Trazabilidad UPN y Guía de Evidencias
    ├── 02-Arquitectura-y-Backend/     <- HLD, LLD Patrones GoF, Modelo BD y Seguridad OWASP
    ├── 03-UI-UX-Design-Handoff/       <- Especificación Handoff y Guía Angular para Vivian
    ├── 04-QA-y-Testing/               <- Plan de Pruebas QA y Playbook de Corrección de Bugs
    └── diagramas/                     <- Archivos fuente nativos .drawio (Draw.io XML)
```

---

## 3. Modelo Operativo de Colaboración Humana (Flujo de 3 Roles)

La ingeniería del proyecto está distribuida de forma determinista entre los tres integrantes del equipo humano, asegurando una cadena de custodia rigurosa sobre el código y las evidencias académicas:

> [!IMPORTANT]
> **Cadena de Custodia y Flujo de Trabajo Secuencial del Equipo Humano:**  
> La topología del sistema y la arquitectura técnica se integran a través del flujo de colaboración determinista de 3 etapas, esquematizado formalmente en el diagrama nativo `![[diagramas/07-flujo-trabajo-equipo-qa-vivian.drawio]]` y sustentado en la arquitectura N-capas `![[diagramas/01-arquitectura-n-capas-globalfrio.drawio]]`.

| Etapa | Integrante / Rol | Responsabilidad Técnica Principal | Criterio de Pase / Handoff |
| :---: | :--- | :--- | :--- |
| **1** | **Orlando Dorival**<br>*(Arquitecto de Sistemas & Líder Backend/BD)* | Diseña e implementa la arquitectura en N-capas (`backend/`), base de datos relacional (11 tablas 3FN), servicios RESTful Spring Boot y seguridad OWASP/JWT. | Código compilable, pruebas base listas y pase formal al repositorio para verificación de QA. |
| **2** | **Compañero de QA**<br>*(QA Engineer & Bug-Fixer Activo)* | Ejecuta las suites de pruebas (`./mvnw test`, `ng test`, cURL, Swagger UI). **Si detecta un fallo, diagnostica y corrige el código fuente directamente**. | **100% de pruebas en verde** (0 errores, 0 fallos) y Quality Gates superados antes de liberar a Vivian. |
| **3** | **Vivian**<br>*(Product Owner & Frontend Owner)* | Valida el sistema con los 5 documentos comerciales reales y programa/corrige la interfaz de usuario en Angular (`frontend/src/app/`). | Fidelidad estética absoluta con su prototipo Vanilla original y conformidad comercial. |
| **4** | **Evaluación Académica**<br>*(Cátedra UPN)* | Sustentación formal ante el docente **Ing. Mg. Carlos Alberto Ponte Ramírez** en los hitos T1, T2, T3 y Evaluación Final. | Demostración de persistencia real, trazabilidad con el sílabo y cumplimiento del ODS 09. |

![[diagramas/07-flujo-trabajo-equipo-qa-vivian.drawio]]
*Figura 2: Flujo Operativo y Swimlanes del Equipo de Ingeniería Global Frío.*  
[[diagramas/07-flujo-trabajo-equipo-qa-vivian.drawio|Abrir Diagrama de Flujo de Trabajo en Draw.io]]


### 3.1. Responsabilidades por Integrante

#### 1. Orlando Dorival (Arquitecto de Sistemas & Líder Backend/BD)
- **Alcance Operativo:**
  - Diseña la topología de alto y bajo nivel del sistema.
  - Diseña el esquema relacional en 3FN y las estrategias de snapshots inmutables.
  - Implementa la totalidad del código Spring Boot en `backend/src/` (controladores, servicios transaccionales, repositorios JPA/EntityManager y los 4 patrones GoF).
  - Configura Spring Security con autenticación stateless JWT y endurecimiento contra OWASP Top 10 Web y OWASP Top 10 for LLM/AI.
  - Efectúa la migración estructural inicial del prototipo Vanilla hacia los módulos base de Angular.

#### 2. Compañero de QA (QA Engineer & Bug-Fixer Activo en Código)
- **Alcance Operativo:**
  - Diseña y ejecuta planes de prueba exhaustivos en cada hito del curso (T1, T2, T3 y Examen Final).
  - Ejecuta pruebas continuas con `./mvnw test`, `ng test`, colecciones de cURL y Postman, y validaciones Swagger UI.
  - **Regla Mandataria de Intervención:** Ante cualquier fallo detectado (errores 400 por validación, 401 por expiración de token, 403 por roles insuficientes o 500 por transacciones fallidas), el Compañero de QA **no se limita a reportar un ticket**: analiza la traza en `GlobalExceptionHandler.java` o en los servicios, aplica la corrección técnica directamente en el código fuente, comitea la solución y re-ejecuta la suite hasta obtener 100% de pruebas en verde.
  - Garantiza que Vivian reciba una base de software totalmente blindada y libre de regresiones.

#### 3. Vivian (Product Owner, Autora Vanilla & Owner Frontend Angular)
- **Alcance Operativo:**
  - Autora intelectual del prototipo comercial inicial en `frontend/prototipo-vanilla/`.
  - Representa formalmente a la empresa Global Frío, dictando las reglas comerciales de cálculo de BTU, zonificación de fletes en Lima y protocolos de visita técnica.
  - Custodia y aporta los **5 documentos comerciales reales** (Proforma membretada, Tarifario interno BTU/zonas, Orden de Servicio física, Catálogo Gree/Samsung y Ficha técnica de visita con fotos/planos).
  - Desarrolla y ajusta los componentes de interfaz en Angular (`frontend/src/app/`), asegurando fidelidad milimétrica con los tokens visuales y la experiencia de usuario.

---

## 4. Estrategia de Ramificación Git y Flujo de Integración

Para gobernar el desarrollo colaborativo de los tres integrantes, se implementa una variante optimizada de **Git-Flow** adaptada al contexto del curso universitario.

### 4.1. Estructura de Ramas Principales
- `main` / `master`: Rama protegida de producción. Solo contiene incrementos 100% estables aprobados en las evaluaciones formales (T1, T2, T3 y Final). Requiere validación previa del Compañero de QA.
- `develop`: Rama de integración activa. Aquí confluyen las ramas de trabajo de backend, frontend y control de calidad.

### 4.2. Ramas de Trabajo por Integrante
- `feat/backend-core`: Asignada a **Orlando** para construcción de controladores, entidades, repositorios y seguridad JWT.
- `fix/qa-patching`: Asignada al **Compañero de QA** para la aplicación de parches y correcciones directas de defectos en el código.
- `feat/frontend-views`: Asignada a **Vivian** para maquetación de componentes Angular, vinculación de formularios reactivos y estilos SCSS.

---

## 5. Convención Estricta de Mensajes de Commit

Conforme a la Sección 1 de `AGENTS.md` (Regla 4: Trazabilidad Obligatoria), todo commit registrado en el repositorio debe referenciar obligatoriamente el código de requerimiento funcional del sílabo UPN (`[RF-SEMXX-YY]`), siguiendo la especificación de **Conventional Commits**:

### 5.1. Estructura Formal del Mensaje
```text
<tipo>(<alcance>): [RF-SEMXX-YY] <descripción imperativa en español>

[Cuerpo opcional detallando la justificación técnica y módulos afectados]
[Referencia a hito académico o issue: Ref #T1-Sem06]
```

### 5.2. Catálogo de Tipos Autorizados
- `feat`: Nuevas funcionalidades del sistema desarrolladas por Orlando o Vivian.
- `fix`: Corrección activa de errores o bugs aplicada por el Compañero de QA.
- `test`: Incorporación de pruebas unitarias, de integración o scripts de Postman por QA.
- `sec`: Fortalecimiento de directivas de seguridad, filtros JWT o reglas CORS por Orlando.
- `style`: Ajustes estéticos de CSS/SCSS o maquetación responsiva realizados por Vivian.
- `docs`: Modificaciones en el vault de Obsidian `/docs` o diagramas Draw.io.

### 5.3. Ejemplos de Commits Reales del Proyecto
- `feat(backend): [RF-SEM01-01] configurar inicio Spring Boot en puerto 8080 con endpoints de saludo`
- `style(views): [RF-SEM01-02] maquetar vista inicio.html con Thymeleaf para contraste SSR`
- `feat(logic): [RF-SEM02-02] implementar calculo de presupuesto base con patron Decorator`
- `feat(dao): [RF-SEM03-02] implementar persistencia manual con EntityManager y clase Repository`
- `feat(crud): [RF-SEM04-02] migrar repositorio de productos a JpaRepository con JPQL seguro`
- `fix(entity): [RF-SEM04-03] corregir calculo de getEstadoStock usando anotacion @Transient`
- `sec(auth): [RF-SEM05-02] configurar Spring Security con filtro stateless JJWT 0.11.5`
- `fix(security): [RF-SEM05-03] ajustar cabeceras de CorsConfig para pre-flight desde Angular`
- `feat(spa): [RF-SEM07-01] estructurar componentes modulares en Angular 18 con enrutamiento`
- `style(theme): [RF-SEM07-03] aplicar tokens industriales de Global Frio en styles.scss`
- `fix(service): [RF-SEM08-01] corregir desuscripcion de Observable en servicio de cotizaciones`

---

## 6. Parámetros de Configuración del Entorno de Ejecución

A continuación se documentan las propiedades clave que gobiernan el despliegue del sistema:

### 6.1. Descriptor Central (`backend/src/main/resources/application.properties`)
```properties
# ===================================================================
# CONFIGURACION CENTRAL DEL SISTEMA WEB GLOBAL FRIO SAC (UPN 2026-2)
# ===================================================================
spring.application.name=globalfrio-backend
server.servlet.context-path=/

# Perfil activo por defecto (dev = H2 en memoria, prod = MySQL 8.x)
spring.profiles.active=dev

# Configuracion de Documentacion OpenAPI 3 / Swagger UI
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui/index.html
springdoc.swagger-ui.operations-sorter=alpha
springdoc.swagger-ui.tags-sorter=alpha

# Parametros Criptograficos JWT
globalfrio.jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
globalfrio.jwt.expiration-ms=86400000

# Parametros de Carga de Archivos de Visita Tecnica (Fotos / Planos)
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=10MB
globalfrio.upload.dir=uploads/planos-visitas/
```

> [!SECURITY]
> **Blindaje Criptográfico de Producción:**
> En perfil `prod`, el servidor rechaza toda conexión en texto plano por el puerto 8080 y redirecciona el tráfico al puerto seguro 8443 con HTTPS forzado. La clave privada y el certificado digital residen en un keystore protegido, salvaguardando las credenciales transmitidas desde Angular.

---

## 7. Navegación Interconectada (Wikilinks de Obsidian)

- **MOC Central:** [[00-MOC-GlobalFrio|00-MOC-GlobalFrio.md]]
- **Requerimientos y Sílabo:**
  - [[01-Requerimientos-y-Negocio/PRD-GlobalFrio-Cotizaciones|PRD-GlobalFrio-Cotizaciones.md]]
  - [[01-Requerimientos-y-Negocio/Matriz-Trazabilidad-Silabo-UPN|Matriz-Trazabilidad-Silabo-UPN.md]]
  - [[01-Requerimientos-y-Negocio/Guia-Evidencias-Vivian-y-Flujo-Equipo|Guia-Evidencias-Vivian-y-Flujo-Equipo.md]]
- **Arquitectura y Persistencia:**
  - [[02-Arquitectura-y-Backend/LLD-Backend-SpringBoot-Patrones|LLD-Backend-SpringBoot-Patrones.md]]
  - [[02-Arquitectura-y-Backend/Modelo-Base-Datos-y-Dummy-Data|Modelo-Base-Datos-y-Dummy-Data.md]]
  - [[02-Arquitectura-y-Backend/Seguridad-OWASP-y-AI-Risks|Seguridad-OWASP-y-AI-Risks.md]]
- **Frontend y Diseño:**
  - [[03-UI-UX-Design-Handoff/UI-UX-Design-Handoff-Spec|UI-UX-Design-Handoff-Spec.md]]
  - [[03-UI-UX-Design-Handoff/Guia-Angular-para-Vivian|Guia-Angular-para-Vivian.md]]
- **Aseguramiento de Calidad:**
  - [[04-QA-y-Testing/QA-Test-Plan-Acceptance-Matrix|QA-Test-Plan-Acceptance-Matrix.md]]
  - [[04-QA-y-Testing/Playbook-QA-y-Arreglo-de-Errores|Playbook-QA-y-Arreglo-de-Errores.md]]
- **Diagramas de Arquitectura:**
  - [[diagramas/01-arquitectura-n-capas-globalfrio.drawio|01-arquitectura-n-capas-globalfrio.drawio]]

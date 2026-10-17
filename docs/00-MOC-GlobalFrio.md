---
title: Map of Content (MOC) — Sistema de Cotización Global Frío
aliases:
  - MOC Global Frío
  - Índice General de Documentación
  - Map of Content
tags:
  - moc
  - arquitectura
  - requerimientos
  - upn
  - globalfrio
owner: Orlando (Arquitecto de Sistemas & Líder Backend)
status: Aprobado
version: 1.0.0
updated: 2026-10-17
---

# Map of Content (MOC) — Sistema Web de Cotización Automatizada Global Frío

> [!IMPORTANT]
> **Propósito del Vault de Documentación:**  
> Este repositorio documental centraliza la arquitectura de software, especificación funcional, modelo relacional, estrategia de pruebas y trazabilidad académica del Sistema Web de Cotización Automatizada de **Global Frío S.A.C.** (Lima, Perú - Periodo 2026).
> 
> El proyecto satisface de forma determinista las exigencias de la asignatura **Soluciones Web y Aplicaciones Distribuidas (SIST1402A)** de la **Universidad Privada del Norte (UPN)**, cátedra del **Ing. Mg. Carlos Alberto Ponte Ramírez**, alineado estratégicamente con el **ODS 09: Industria, Innovación e Infraestructura**.

---

## 1. Ficha Técnica del Proyecto y Contacto Comercial Real

| Parámetro | Detalle Institucional y Comercial |
| :--- | :--- |
| **Empresa Cliente** | **Global Frío S.A.C.** — Especialistas en HVAC, Climatización Industrial y Cámaras Frigoríficas |
| **RUC Institucional** | 20601234567 |
| **Representante Comercial** | **Edgar Sabalu** — Teléfono: `904 209 489` |
| **Sede Operativa** | Ventanilla, Callao (Código Postal `07061`), Lima Metropolitana |
| **Institución Académica** | Universidad Privada del Norte (UPN) — Facultad de Ingeniería |
| **Curso & Cátedra** | SIST1402A — Ciclo 8° / 9° — Docente: Ing. Mg. Carlos Alberto Ponte Ramírez |
| **Alineación Estratégica** | Agenda 2030 ONU — **ODS 09: Industria, Innovación e Infraestructura** (Metas 9.1, 9.4, 9.b) |
| **Stack Backend** | Java 17 LTS, Spring Boot 3.3.4, Spring Data JPA, Hibernate, Spring Security 6, JJWT 0.11.5 |
| **Stack Frontend** | Angular 18 SPA (TypeScript, SCSS, Bootstrap 5, RxJS, Reactive Forms) |
| **Persistencia** | MySQL Server 8.x (Producción) / H2 Database (Desarrollo y Testing) |

---

## 2. Mapa Estructurado del Vault (Wikilinks de los 12 Documentos)

A continuación se detalla la red completa de especificaciones organizadas en cuatro áreas de ingeniería:

```
docs/
├── 00-MOC-GlobalFrio.md (Documento Central Activo)
├── 01-Requerimientos-y-Negocio/
│   ├── PRD-GlobalFrio-Cotizaciones.md
│   ├── Matriz-Trazabilidad-Silabo-UPN.md
│   └── Guia-Evidencias-Vivian-y-Flujo-Equipo.md
├── 02-Arquitectura-y-Backend/
│   ├── HLD-Arquitectura-Sistemas.md
│   ├── LLD-Backend-SpringBoot-Patrones.md
│   ├── Modelo-Base-Datos-y-Dummy-Data.md
│   └── Seguridad-OWASP-y-AI-Risks.md
├── 03-UI-UX-Design-Handoff/
│   ├── UI-UX-Design-Handoff-Spec.md
│   └── Guia-Angular-para-Vivian.md
└── 04-QA-y-Testing/
    ├── QA-Test-Plan-Acceptance-Matrix.md
    └── Playbook-QA-y-Arreglo-de-Errores.md
```

### 2.1. Área 01: Requerimientos, Negocio y Gobernanza Académica
- [[01-Requerimientos-y-Negocio/PRD-GlobalFrio-Cotizaciones|PRD - Global Frío Cotizaciones]]: Especificación de producto, problemática industrial, KPIs de eficiencia térmica y operativa, Requerimientos Funcionales (`RF01` a `RF12`), Requerimientos No Funcionales (`RNF01` a `RNF06` MoSCoW) e Historias de Usuario (`HU01` a `HU08`) con criterios de aceptación Gherkin.
- [[01-Requerimientos-y-Negocio/Matriz-Trazabilidad-Silabo-UPN|Matriz de Trazabilidad Sílabo UPN]]: Mapeo exhaustivo de las 16 semanas lectivas (`RF-SEM01` a `RF-SEM16`), restricciones didácticas (ej. uso estricto de `EntityManager` en S03 y `JpaRepository` a partir de S04), hitos de evaluación UPN (T1 10%, T2 20%, T3 30%, Final 40%) y alineación cuantitativa con el ODS 09.
- [[01-Requerimientos-y-Negocio/Guia-Evidencias-Vivian-y-Flujo-Equipo|Guía de Evidencias de Vivian y Flujo del Equipo]]: Matriz de roles RACI, flujo de trabajo en 3 etapas (Orlando $\rightarrow$ QA Bug-Fixer $\rightarrow$ Vivian), sustento de los 5 documentos comerciales reales y banco de 10 preguntas y respuestas de defensa ante el Ing. Carlos Alberto Ponte Ramírez.

### 2.2. Área 02: Arquitectura, Backend y Seguridad
- [[02-Arquitectura-y-Backend/HLD-Arquitectura-Sistemas|HLD - Arquitectura de Sistemas]]: Diseño de alto nivel de la topología distribuida desacoplada entre Angular (`localhost:4200`) y Spring Boot (`localhost:8080` / `8443` HTTPS), arquitectura del monorepo, estrategia Git y convención de commits `[RF-SEMXX-YY]`.
- [[02-Arquitectura-y-Backend/LLD-Backend-SpringBoot-Patrones|LLD - Backend Spring Boot y Patrones GoF]]: Diseño de bajo nivel en N-capas bajo el paquete canónico `pe.upn.sist1402a`, implementación de los 4 patrones GoF exigidos (Decorator `PresupuestoCalculator` con `BigDecimal`, Factory `CotizacionSnapshotFactory`, Singleton Spring `@Service`, y Repository `JpaRepository` + `EntityManager`), contratos REST y vista Thymeleaf `/reportes/cotizacion/{id}`.
- [[02-Arquitectura-y-Backend/Modelo-Base-Datos-y-Dummy-Data|Modelo de Base de Datos y Dummy Data]]: Esquema relacional normalizado en 3NF con 11 tablas, definición de columnas `snapshot_*` inmutables, gestión de perfiles `application-dev.properties` (H2) y `application-prod.properties` (MySQL 8.x), y script sembrador con precios reales en Soles (`S/`), zonificación de Lima y contraseñas cifradas en BCrypt.
- [[02-Arquitectura-y-Backend/Seguridad-OWASP-y-AI-Risks|Seguridad OWASP y AI Risks]]: Blindaje técnico contra OWASP Top 10 Web (RBAC `ADMIN`/`ASESOR`, anti-tampering de presupuestos en servidor, máquina de estados FSM, JPQL parametrizado con `@Param`, Magic Bytes y UUID en adjuntos, revocación de tokens JWT) y mitigación de OWASP Top 10 for LLM/AI (inyección de prompt, lista blanca anti-slopsquatting, Zero-PII en logging y limitación de agencia excesiva).

### 2.3. Área 03: Handoff UI/UX y Desarrollo Frontend Angular
- [[03-UI-UX-Design-Handoff/UI-UX-Design-Handoff-Spec|UI/UX Design Handoff Spec]]: Especificación de diseño visual con tokens corporativos invariantes (`#5980a6`, `#1f5fa9`, `#15181a`, tipografía `Barlow`), arquitectura del modal de cotización en 3 pasos (recomendador BTU $\rightarrow$ cálculo en vivo $\rightarrow$ datos de contacto), interfaz de login y paneles administrativos.
- [[03-UI-UX-Design-Handoff/Guia-Angular-para-Vivian|Guía Angular para Vivian]]: Manual de ingeniería frontend que establece la equivalencia 1 a 1 entre el prototipo original en HTML/CSS/JS Vanilla de Vivian y los componentes TypeScript/SCSS migrados a Angular, orientando la resolución directa de detalles estéticos y el uso estricto de directivas autorizadas por la lista blanca.

### 2.4. Área 04: Aseguramiento de Calidad (QA), Testing y Playbook
- [[04-QA-y-Testing/QA-Test-Plan-Acceptance-Matrix|QA Test Plan & Acceptance Matrix]]: Plan maestro de calidad bajo enfoque Shift-Left desde T1 hasta el Examen Final, matriz de compatibilidad responsiva (RNF03: 360px a 1920px), catálogo de 15 casos de prueba críticos (`TC-CRIT-001` a `TC-CRIT-015`), prueba obligatoria de rollback atómico con `@Transactional`, y los 8 Quality Gates (`QG-01` a `QG-08`).
- [[04-QA-y-Testing/Playbook-QA-y-Arreglo-de-Errores|Playbook QA y Arreglo de Errores]]: Manual operativo para el compañero de QA con procedimientos de diagnóstico y **reparación directa en código fuente** para excepciones HTTP 400, 401, 403, 404 y 500 antes de entregar incrementos a Vivian.

---

## 3. Catálogo de Diagramas de Ingeniería Draw.io

De acuerdo con la directiva institucional y las reglas de diseño del proyecto, se prohíbe el uso de diagramas basados en texto dentro de `/docs`. Toda la modelización del sistema está soportada en **archivos nativos Draw.io** (`.drawio` con formato XML estándar) ubicados en el subdirectorio `docs/diagramas/`:

| N° | Archivo Nativo Draw.io | Título del Diagrama y Alcance de Ingeniería | Vista / Enlace de Diagramación |
| :---: | :--- | :--- | :---: |
| **01** | `01-arquitectura-n-capas-globalfrio.drawio` | **Topología de Arquitectura N-Capas:** Desacoplamiento entre cliente Angular SPA (4200), canal REST/CORS y servidor Spring Boot (8080/8443) conectado a MySQL 8.x / H2. | [[diagramas/01-arquitectura-n-capas-globalfrio.drawio\|Abrir Diagrama 01]]<br>![[diagramas/01-arquitectura-n-capas-globalfrio.drawio]] |
| **02** | `02-modelo-entidad-relacion-bd.drawio` | **Modelo Entidad-Relación Relacional (3NF):** Estructura de 11 tablas con claves primarias, foráneas, índices y columnas de congelamiento `snapshot_*`. | [[diagramas/02-modelo-entidad-relacion-bd.drawio\|Abrir Diagrama 02]]<br>![[diagramas/02-modelo-entidad-relacion-bd.drawio]] |
| **03** | `03-flujo-cotizacion-y-patron-decorator.drawio` | **Cálculo Térmico y Patrón GoF Decorator:** Algoritmo recomendador de BTU y cadena acumulativa de cálculo monetario con `BigDecimal`. | [[diagramas/03-flujo-cotizacion-y-patron-decorator.drawio\|Abrir Diagrama 03]]<br>![[diagramas/03-flujo-cotizacion-y-patron-decorator.drawio]] |
| **04** | `04-maquina-estados-cotizacion-orden.drawio` | **Máquina de Estados Finita (FSM):** Ciclo de vida transaccional `PENDIENTE -> EN_REVISION -> ENVIADO -> ACEPTADO/RECHAZADO -> ORDEN_SERVICIO`. | [[diagramas/04-maquina-estados-cotizacion-orden.drawio\|Abrir Diagrama 04]]<br>![[diagramas/04-maquina-estados-cotizacion-orden.drawio]] |
| **05** | `05-secuencia-jwt-rbac-logout.drawio` | **Secuencia de Autenticación, RBAC y Cierre de Sesión:** Flujo de pre-flight `OPTIONS`, emisión de token JWT, intercepción Bearer y lista negra en memoria. | [[diagramas/05-secuencia-jwt-rbac-logout.drawio\|Abrir Diagrama 05]]<br>![[diagramas/05-secuencia-jwt-rbac-logout.drawio]] |
| **06** | `06-mapa-navegacion-ui-ux.drawio` | **Mapa de Navegación y Experiencia de Usuario (UI/UX):** Sitemap completo, flujo del modal en 3 pasos, accesos directos y rutas protegidas por `AuthGuard`. | [[diagramas/06-mapa-navegacion-ui-ux.drawio\|Abrir Diagrama 06]]<br>![[diagramas/06-mapa-navegacion-ui-ux.drawio]] |
| **07** | `07-flujo-trabajo-equipo-qa-vivian.drawio` | **Flujo Colaborativo y Ciclo de Bug-Fixing Activo:** 4 carriles de interacción (Orlando $\rightarrow$ Compañero QA Bug-Fixer $\rightarrow$ Vivian $\rightarrow$ Hitos Académicos UPN). | [[diagramas/07-flujo-trabajo-equipo-qa-vivian.drawio\|Abrir Diagrama 07]]<br>![[diagramas/07-flujo-trabajo-equipo-qa-vivian.drawio]] |

---

## 4. Estructura y Dinámica del Equipo de Ingeniería

> [!NOTE]
> **Modelo Tripartito de Responsabilidades:**  
> La asignación de tareas del proyecto refleja una división de responsabilidades estricta y orientada al cumplimiento de la calidad de software:

| Integrante | Rol Principal | Especialidad Técnica | Responsabilidad de Cierre |
| :--- | :--- | :--- | :--- |
| **1. Orlando** | Arquitecto de Sistemas & Líder Backend | Java 17, Spring Boot 3.3.4, JPA, MySQL 8.x, OWASP | Entrega arquitectura base, contratos y BD |
| **2. Compañero de QA** | QA Engineer & Bug-Fixer Activo | Pruebas continuas, diagnóstico y fix en código | Repara directamente bugs antes de entregar a Vivian |
| **3. Vivian** | Product Owner & Dueña del Frontend | Prototipo Vanilla, UI/UX Angular, 5 Docs Reales | Validación comercial y refinamiento estético en CSS |

1. **Orlando (Arquitecto de Sistemas, Líder Backend & BD):**
   - Lideró la migración inicial del prototipo Vanilla (`frontend/prototipo-vanilla/`) hacia la estructura modular en Angular (`frontend/src/app/`).
   - Construye y mantiene la arquitectura N-capas en Java 17 con Spring Boot 3.3.4, la base de datos relacional (MySQL 8.x y H2), consultas JPQL parametrizadas y el blindaje OWASP.
2. **Compañero de QA (QA Engineer & Bug-Fixer Activo):**
   - Ejecuta la suite continua de pruebas automatizadas e inspección de endpoints.
   - **Regla Mandataria:** Cuando identifica un fallo o bug, él mismo lo diagnostica y lo repara directamente en el código fuente (`fix(modulo): [RF-SEMXX-YY] ...`), verificando el 100% de éxito en las pruebas antes de transferir el build a Vivian.
3. **Vivian (Product Owner, Representante de Global Frío & Dueña de Frontend Angular):**
   - Creadora original de la interfaz en HTML/CSS Vanilla que plasmó la identidad comercial y operativa de Global Frío.
   - Garante de los 5 documentos comerciales reales para la defensa técnica.
   - **Regla Mandataria:** Encargada exclusiva de las modificaciones visuales, fidelidad de estilos SCSS y componentes de interfaz en Angular; cualquier ajuste estético es programado por ella misma.

---

## 5. Cronograma de Hitos de Evaluación Académica UPN (2026-2)

La evaluación del curso SIST1402A se rige por un cronograma continuo e incremental sin examen sustitutorio:

| Hito Académico | Semana | Ponderación | Foco de Evaluación Técnica |
| :--- | :---: | :---: | :--- |
| **Evaluación T1** | Semana 06 | 10% | Backend REST, Persistencia relacional (`EntityManager` / `JpaRepository`), Bean Validation y Seguridad JWT |
| **Evaluación T2** | Semana 10 | 20% | Integración Frontend Angular SPA + Backend REST + CRUD E2E |
| **Evaluación T3** | Semana 13 | 30% | Transacciones ACID, prueba forzada de `@Transactional` Rollback y OpenAPI Swagger |
| **Examen Final** | Semana 16 | 40% | Solución Distribuida completa, canal HTTPS (8443) y Sustentación Integral (**Sin examen sustitutorio**) |

- **Hito T1 (Semana 06 — 10%):** Consolidación de la API REST en Spring Boot, persistencia relacional con `EntityManager` (Semana 03) y `JpaRepository` (Semana 04-06), Bean Validation, autenticación stateless con JWT y suite de pruebas cURL/Postman. Ver detalle en [[01-Requerimientos-y-Negocio/Matriz-Trazabilidad-Silabo-UPN#Hito T1]].
- **Hito T2 (Semana 10 — 20%):** Consolidación del cliente Angular SPA, consumo de la API mediante servicios RxJS, formularios reactivos con validación previa, rutas privadas con `AuthGuard` e integración E2E. Ver detalle en [[01-Requerimientos-y-Negocio/Matriz-Trazabilidad-Silabo-UPN#Hito T2]].
- **Hito T3 (Semana 13 — 30%):** Consolidación transaccional con reversión atómica `@Transactional(rollbackFor = Exception.class)`, prueba de rollback inducido en JUnit 5 y documentación viva de contratos en Swagger UI. Ver detalle en [[01-Requerimientos-y-Negocio/Matriz-Trazabilidad-Silabo-UPN#Hito T3]].
- **Evaluación Final (Semana 16 — 40%):** Presentación integral de la solución distribuida en producción con empaquetado optimizado, despliegue en servidor Tomcat/WildFly bajo HTTPS (8443) y defensa técnica ante el jurado. **Nota: No aplica examen sustitutorio**. Ver detalle en [[01-Requerimientos-y-Negocio/Matriz-Trazabilidad-Silabo-UPN#Evaluación Final]].

---

## 6. Design Tokens Corporativos y Convención Estética

Tanto el prototipo Vanilla como el frontend en Angular y la documentación técnica respetan la paleta oficial de **Global Frío**:

- **Color Acero Principal:** `--gf-accent: #5980a6`
- **Acento Suave:** `--gf-accent-soft: #a9bed2`
- **Acento Profundo:** `--gf-accent-deep: #2c4560`
- **Botón Primario de Acción:** `--gf-btn: #1f5fa9` (Hover: `--gf-btn-hover: #184d8a`)
- **Modo Oscuro (Predeterminado):** Fondo `--gf-bg: #15181a`, Superficie `--gf-surface: #1c2023`, Texto `--gf-text: #e8eaeb`
- **Modo Claro (Alternativo):** Fondo `--gf-bg: #f2f2f3`, Superficie `--gf-surface: #f5f5f8`, Texto `--gf-text: #1d1f20`
- **Tipografías Oficiales:** `Barlow Condensed` (títulos y encabezados) y `Barlow` (cuerpo y datos tabulares)

---

## 7. Verificación Rápida de Navegación en Obsidian

> [!TIP]
> Para navegar eficientemente en este Vault:
> 1. Presione `Ctrl + Click` (o clic en modo lectura) sobre cualquiera de los enlaces con sintaxis `[[00-MOC-GlobalFrio|Nombre-Archivo]]` para acceder a la especificación completa.
> 2. Utilice la vista gráfica (*Graph View*) de Obsidian para visualizar la interconexión entre requerimientos, arquitectura, pruebas y diagramas Draw.io.
> 3. Los diagramas pueden ser editados o visualizados directamente en Draw.io o mediante la extensión correspondiente para Visual Studio Code / Obsidian.

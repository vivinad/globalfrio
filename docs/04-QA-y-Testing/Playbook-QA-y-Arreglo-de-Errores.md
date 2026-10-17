---
title: Playbook Operativo de QA y Arreglo Activo de Errores para el Compañero
aliases:
  - Playbook QA
  - Manual de Bug-Fixing QA
  - Guía de Reparación Rápida Global Frío
tags:
  - qa
  - bug-fixing
  - playbook
  - diagnostico
  - globalfrio
  - upn-sist1402a
owner: Compañero de QA (QA Engineer & Bug-Fixer Activo)
status: Aprobado
version: 1.0.0
updated: 2026-10-17
---

# Playbook Operativo de QA y Arreglo Activo de Errores para el Compañero

> [!NOTE]
> **Propósito Operativo y Destinatario:**  
> Este manual es la guía de acción táctica y resolución técnica para el **Compañero de QA** en el proyecto **Global Frío SAC** (Ventanilla, Callao / Lima 07061, RUC 20601234567, Teléfono: `904 209 489`, Representante: Edgar Sabalu). Su objetivo es proporcionar procedimientos estandarizados para arrancar el entorno, ejecutar pruebas unitarias/integrales y, fundamentalmente, **diagnosticar y reparar directamente en el código fuente los defectos detectados (HTTP 400, 401, 403, 404, 500)** antes de realizar el handoff oficial a Vivian.

---

## 1. Misión Operativa del Compañero de QA y Filosofía de Bug-Fixing Activo

En el modelo de trabajo del equipo, el rol de QA trasciende el testeo pasivo. No se redactan tickets burocráticos para devolver el trabajo al arquitecto; **el Compañero de QA es un Ingeniero de Calidad y Bug-Fixer Activo**:

1. **Recepción del Incremento:** Obtiene la rama de trabajo actualizada de Orlando (`orlando-docs-backend`).
2. **Ejecución y Diagnóstico:** Corre la batería automatizada de pruebas (`./mvnw test`, `ng test`, cURL y Postman).
3. **Reparación Directa en Código:** Si un test falla o un endpoint arroja una excepción, el compañero de QA abre los archivos fuente en su IDE (IntelliJ IDEA / VS Code), localiza la causa-raíz y aplica el fix exacto en Java o TypeScript.
4. **Re-verificación Obligatoria:** Ejecuta nuevamente la suite completa hasta obtener **100% de éxito (0 fallos, 0 errores)**.
5. **Commit Semántico Trazable:** Registra el cambio con la convención oficial: `fix(modulo): [RF-SEMXX-YY] descripción precisa del arreglo`.
6. **Entrega a Vivian:** Una vez estabilizado el sistema, notifica a Vivian para que ella realice las validaciones comerciales y los ajustes estéticos de diseño en Angular.

### 1.1. Diagrama de Flujo del Equipo y Rol Activo de QA

A continuación se muestra el carril operativo del Compañero de QA dentro del ciclo colaborativo:

![[diagramas/07-flujo-trabajo-equipo-qa-vivian.drawio]]
*Figura 1: Flujo de Trabajo Colaborativo en 4 Carriles: Orlando -> Compañero QA Bug-Fixer -> Vivian -> Hitos UPN.*  
[[diagramas/07-flujo-trabajo-equipo-qa-vivian.drawio|Abrir en Draw.io]]

---

## 2. Protocolo de Arranque y Verificación del Entorno Local

Antes de iniciar cualquier ciclo de pruebas o depuración, se debe garantizar que el entorno local esté levantado y saludable.

### 2.1. Requisitos Previos de Software
- **Java Development Kit (JDK):** Versión 17 LTS (verificar con `java -version`).
- **Node.js & npm:** Node.js v20 LTS o superior y npm v10 (verificar con `node -v` y `npm -v`).
- **Angular CLI:** Versión 18 global o local (`ng version`).
- **Base de Datos:** Servidor MySQL 8.x en puerto 3306 (para perfil `prod`) o motor en memoria H2 (para perfil `dev`).
- **Git:** Cliente Git configurado con rama de trabajo.

---

### 2.2. Arranque del Backend Spring Boot (Puerto 8080 / 8443)

#### A. Perfil de Desarrollo Rápido con Base de Datos H2 en Memoria (`dev`)
Recomendado para pruebas unitarias veloces y validaciones aisladas:
```powershell
# Ubicarse en el directorio backend
cd c:\Users\ORLANDO\Desktop\globalfrio\backend

# Compilar y arrancar la aplicación en perfil dev
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```
- **Puerto de escucha:** `http://localhost:8080`
- **Consola interactiva H2:** `http://localhost:8080/h2-console`
  - *JDBC URL:* `jdbc:h2:mem:globalfriodb`
  - *User Name:* `sa`
  - *Password:* *(dejar en blanco)*

#### B. Perfil de Evaluación y Persistencia Real con MySQL 8.x (`prod`)
Recomendado para pruebas de persistencia permanente y sustentaciones:
```powershell
# Asegurar que el servicio MySQL esté corriendo en Windows
Get-Service MySQL* | Start-Service

# Arrancar Spring Boot con perfil prod
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
```
- **Parámetros de conexión:** Base `globalfrio_db`, usuario `root`, contraseña `Carros312.`.
- **Puerto seguro HTTPS:** `https://localhost:8443` (o `http://localhost:8080` si SSL está en modo de prueba).

---

### 2.3. Arranque del Frontend Angular (Puerto 4200)

```powershell
# Ubicarse en el directorio frontend
cd c:\Users\ORLANDO\Desktop\globalfrio\frontend

# Instalar dependencias si es la primera vez
npm install

# Compilar y servir en caliente con apertura de navegador
ng serve -o
```
- **URL de la SPA:** `http://localhost:4200`
- **Verificación inicial:** Abrir la consola de desarrollador del navegador (F12) y comprobar que la consola esté completamente limpia de errores de compilación TypeScript o de dependencias circulares.

---

### 2.4. Ejecución de Suites de Pruebas Automatizadas

#### A. Pruebas Automatizadas de Backend (JUnit 5, MockMvc, JPA)
```powershell
cd c:\Users\ORLANDO\Desktop\globalfrio\backend

# Ejecución completa de todos los tests del backend
.\mvnw.cmd test

# Ejecución de un caso de prueba específico (e.g. Rollback Transaccional)
.\mvnw.cmd test -Dtest=CotizacionTransaccionalRollbackTest

# Ver reporte de pruebas en consola o en archivo HTML
# target/surefire-reports/index.html
```
> [!CHECK]
> **Criterio de Aprobación QA:** La salida final de Maven DEBE reportar:  
> `[INFO] Tests run: XX, Failures: 0, Errors: 0, Skipped: 0`  
> `[INFO] BUILD SUCCESS`

#### B. Pruebas Automatizadas de Frontend (Jasmine / Karma)
```powershell
cd c:\Users\ORLANDO\Desktop\globalfrio\frontend

# Ejecución de tests en modo Headless (sin abrir ventana de navegador)
ng test --watch=false --browsers=ChromeHeadless
```

---

### 2.5. Smoke Test Rápido con cURL

Ejecutar estos comandos de diagnóstico inmediato en una consola PowerShell para verificar la salud del backend en menos de 10 segundos:

```powershell
# 1. Comprobar salud básica del servidor
curl.exe -i http://localhost:8080/api/saludo

# 2. Comprobar diagnóstico estructurado ODS 9
curl.exe -i http://localhost:8080/api/saludo/detalle

# 3. Comprobar renderizado de plantilla Thymeleaf SSR
curl.exe -i http://localhost:8080/

# 4. Comprobar Swagger UI
curl.exe -i http://localhost:8080/swagger-ui/index.html
```

---

## 3. Navegación e Interacción con Swagger UI (`/swagger-ui/index.html`)

Swagger UI es la herramienta gráfica oficial del curso para auditar y demostrar en vivo ante el docente la interoperabilidad de la API REST.

> [!TIP]
> **Guía Rápida de Interacción con Swagger UI 3 (Springdoc)**  
> **URL Base:** `http://localhost:8080/swagger-ui/index.html`  
> 1. **Autenticación:** Clic en botón `[Authorize 🔓]` e ingresar `Bearer <token_jwt>`.  
> 2. **Navegación de Controladores:** Explorar los grupos `/api/auth`, `/api/cotizaciones`, `/api/panel`.  
> 3. **Prueba Dinámica:** Desplegar endpoint -> `[Try it out]` -> completar payload -> `[Execute]`.  
> 4. **Verificación:** Validar status HTTP (200, 201, 400, 401, 403, 404) y estructura JSON del contrato.


### 3.1. Procedimiento de Autenticación en Swagger UI
1. Navegar en el navegador hacia `http://localhost:8080/swagger-ui/index.html`.
2. Desplegar el endpoint `POST /api/auth/login`.
3. Hacer clic en **"Try it out"**, ingresar credenciales (`username: "asesor"`, `password: "Asesor123$"`) y pulsar **"Execute"**.
4. Del cuerpo de respuesta 200 OK, copiar el valor del campo `token`.
5. Ir a la parte superior de la página y pulsar el botón verde **"Authorize"** con ícono de candado.
6. En el campo de texto, pegar el token copiado (no es necesario anteponer "Bearer ", Springdoc lo concatena automáticamente según `OpenApiConfig.java`).
7. Pulsar **"Authorize"** y luego **"Close"**. El candado aparecerá cerrado indicando que todos los endpoints protegidos incluirán el token.
8. Ahora es posible ejecutar los endpoints protegidos como `GET /api/panel/cotizaciones`.

---

## 4. Árbol de Diagnóstico Causa-Raíz y Procedimientos de Reparación en Código

Cuando un test falle o se detecte una anomalía durante la ejecución, el Compañero de QA debe aplicar el siguiente árbol sistemático de diagnóstico y refactorización directa en caliente:

---

### CASO 1: Error HTTP 400 (Bad Request)

#### A. Síntoma Observado
- El test MockMvc falla con `status().isBadRequest()` o una llamada POST/PUT desde Angular retorna código 400 con mensaje de error en campos del formulario.

#### B. Árbol de Diagnóstico Causa-Raíz
| Fallo Potencial (HTTP 400) | Causa Raíz Probable | Archivo y Capa a Diagnosticar / Corregir |
| :--- | :--- | :--- |
| **¿Falla en Bean Validation (`@Valid`)?** | El DTO recibió un campo en blanco (`@NotBlank`), área <= 0 (`@Min`), o email malformado. | `pe.upn.sist1402a.dto.CotizacionRequestDto` / formulario reactivo frontend |
| **¿Error de deserialización JSON (Jackson)?** | Formato de fecha incompatible (no ISO-8601) o string enviado a campo `BigDecimal`. | `pe.upn.sist1402a.dto.*Dto` / anotación `@JsonFormat` |
| **¿Excepción de regla de negocio lanzada explícitamente?** | `IllegalArgumentException` o `BadRequestException` lanzada desde la capa `@Service`. | `pe.upn.sist1402a.service.impl.CotizacionServiceImpl` |


#### C. Reparación Directa en Código Fuente
- **Escenario Real:** El DTO de cotización no permitía áreas fraccionarias o el mensaje de validación estaba ausente.
- **Archivo a modificar:** `backend/src/main/java/pe/upn/sist1402a/dto/CotizacionRequestDto.java`.

*Código defectuoso:*
```java
// ERROR: @Min(1) exige entero y bloquea áreas decimales válidas (ej. 15.5 m2)
@NotNull(message = "El área es obligatoria")
@Min(value = 1, message = "Área inválida")
private Integer areaM2;
```

*Código corregido por el Compañero de QA:*
```java
// FIX: Uso de BigDecimal con validación DecimalMin para permitir medidas precisas de climatización
@NotNull(message = "El área en metros cuadrados es obligatoria")
@DecimalMin(value = "1.0", message = "El área debe ser mayor o igual a 1.0 m2")
private BigDecimal areaM2;
```

- **Commit Semántico del Arreglo:**
```bash
git add backend/src/main/java/pe/upn/sist1402a/dto/CotizacionRequestDto.java
git commit -m "fix(backend): [RF-SEM05-01] corregir tipo y validación DecimalMin en CotizacionRequestDto"
```

---

### CASO 2: Error HTTP 401 (Unauthorized)

#### A. Síntoma Observado
- La llamada a un endpoint protegido retorna 401 a pesar de que el usuario inició sesión, o el interceptor de Angular recibe respuesta de no autorizado de forma intermitente.

#### B. Árbol de Diagnóstico Causa-Raíz
| Fallo Potencial (HTTP 401) | Causa Raíz Probable | Archivo y Capa a Diagnosticar / Corregir |
| :--- | :--- | :--- |
| **¿Cabecera `Authorization` ausente o malformada?** | `JwtInterceptor` de Angular no inyecta `Bearer <token>` o la petición es a URL no contemplada. | `frontend/src/app/core/interceptors/jwt.interceptor.ts` |
| **¿Token JWT expirado o firma inválida?** | Clave secreta diferente en configuración o expiración superada (`ExpiredJwtException`). | `backend/src/main/resources/application.properties` / `JwtUtil.java` |
| **¿Token revocado en lista negra (Blacklist)?** | El usuario ejecutó logout y el token quedó invalidado en memoria. | `pe.upn.sist1402a.service.TokenBlacklistService` |


#### C. Reparación Directa en Código Fuente
- **Escenario Real:** El interceptor de Angular olvidaba el espacio después de `Bearer` o no clonaba adecuadamente la solicitud.
- **Archivo a modificar:** `frontend/src/app/core/interceptors/jwt.interceptor.ts`.

*Código defectuoso:*
```typescript
// ERROR: Falta espacio tras Bearer y altera la cabecera original directamente
if (token) {
    req.headers.set('Authorization', 'Bearer' + token);
}
return next.handle(req);
```

*Código corregido por el Compañero de QA:*
```typescript
// FIX: Clonar la petición HTTP e inyectar cabecera Authorization con formato RFC 6750 estricto
if (token) {
    const authReq = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next.handle(authReq);
}
return next.handle(req);
```

- **Commit Semántico del Arreglo:**
```bash
git add frontend/src/app/core/interceptors/jwt.interceptor.ts
git commit -m "fix(frontend): [RF-SEM08-03] asegurar clonación e inyección correcta de Bearer en JwtInterceptor"
```

---

### CASO 3: Error HTTP 403 (Forbidden)

#### A. Síntoma Observado
- El navegador bloquea la petición mostrando en la consola de JavaScript: `Access to XMLHttpRequest at 'http://localhost:8080/api/...' from origin 'http://localhost:4200' has been blocked by CORS policy`, o el backend rechaza con 403 Forbidden a un asesor que intenta ver cotizaciones.

#### B. Árbol de Diagnóstico Causa-Raíz
| Fallo Potencial (HTTP 403) | Causa Raíz Probable | Archivo y Capa a Diagnosticar / Corregir |
| :--- | :--- | :--- |
| **¿Fallo de CORS (Pre-flight `OPTIONS` bloqueado)?** | `CorsConfig` o `SecurityConfig` no autorizan origen `http://localhost:4200` o verbo `OPTIONS`. | `pe.upn.sist1402a.config.CorsConfig` / `SecurityConfig.java` |
| **¿Fallo de autorización RBAC?** | Discordancia entre `hasRole('ASESOR')` y `hasAuthority('ROLE_ASESOR')` en anotaciones `@PreAuthorize`. | `pe.upn.sist1402a.controller.*Controller.java` |


#### C. Reparación Directa en Código Fuente
- **Escenario Real:** La configuración de CORS no exponía la cabecera `Authorization` o no admitía solicitudes `OPTIONS` pre-flight en Spring Security.
- **Archivo a modificar:** `backend/src/main/java/pe/upn/sist1402a/config/CorsConfig.java` y `SecurityConfig.java`.

*Código corregido por el Compañero de QA en `CorsConfig.java`:*
```java
package pe.upn.sist1402a.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // Origen autorizado explícito del frontend Angular
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedHeaders(Arrays.asList(
                "Origin", "Content-Type", "Accept", "Authorization",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"
        ));
        config.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
```

- **Commit Semántico del Arreglo:**
```bash
git add backend/src/main/java/pe/upn/sist1402a/config/CorsConfig.java
git commit -m "fix(security): [RF-SEM05-03] habilitar pre-flight OPTIONS y exponer cabeceras en CorsConfig"
```

---

### CASO 4: Error HTTP 404 (Not Found)

#### A. Síntoma Observado
- El frontend llama a un endpoint y recibe 404, o un test MockMvc falla con `status().isNotFound()`.

#### B. Árbol de Diagnóstico Causa-Raíz
| Fallo Potencial (HTTP 404) | Causa Raíz Probable | Archivo y Capa a Diagnosticar / Corregir |
| :--- | :--- | :--- |
| **¿Desalineación de URL en `@RequestMapping`?** | El controlador expone `/api/cotizaciones` y el servicio Angular consume `/api/cotizacion` (singular/plural). | Controlador Spring Boot vs Data Service Angular |
| **¿Registro no existe en la Base de Datos?** | `findById()` retornó `Optional.empty()` y detonó `ResourceNotFoundException`. | Script `data-seed-globalfrio.sql` o IDs enviados en la petición |
| **¿Plantilla Thymeleaf no encontrada?** | Error tipográfico en el nombre del archivo en `resources/templates/`. | Carpeta `templates/` en backend resources |


#### C. Reparación Directa en Código Fuente
- **Escenario Real:** `CotizacionService` en Angular consumía `/api/cotizacion` en lugar del endpoint pluralizado canónico `/api/cotizaciones`.
- **Archivo a modificar:** `frontend/src/app/services/api.service.ts`.

*Código defectuoso:*
```typescript
// ERROR: Ruta singular desincronizada con el backend
obtenerCotizaciones(): Observable<Cotizacion[]> {
    return this.http.get<Cotizacion[]>(`${this.apiUrl}/api/cotizacion`);
}
```

*Código corregido por el Compañero de QA:*
```typescript
// FIX: Alinear a la convención RESTful pluralizada oficial de AGENTS.md
obtenerCotizaciones(): Observable<Cotizacion[]> {
    return this.http.get<Cotizacion[]>(`${this.apiUrl}/api/cotizaciones`);
}
```

- **Commit Semántico del Arreglo:**
```bash
git add frontend/src/app/services/api.service.ts
git commit -m "fix(frontend): [RF-SEM08-01] corregir URI REST pluralizada /api/cotizaciones en ApiService"
```

---

### CASO 5: Error HTTP 500 (Internal Server Error)

#### A. Síntoma Observado
- La respuesta HTTP devuelve 500 y la consola del backend muestra una traza de excepción (StackTrace) con `NullPointerException`, `DataIntegrityViolationException`, o `TransactionSystemException`.

#### B. Árbol de Diagnóstico Causa-Raíz
| Fallo Potencial (HTTP 500) | Causa Raíz Probable | Archivo y Capa a Diagnosticar / Corregir |
| :--- | :--- | :--- |
| **¿`NullPointerException` en capa `@Service`?** | Acceso a objeto no persistido o llamada directa a `.get()` de `Optional` sin verificación previa. | `pe.upn.sist1402a.service.impl.*ServiceImpl.java` |
| **¿Fallo de integridad referencial o constraint en BD?** | Intento de insertar detalle con `equipoId` o `clienteId` inexistente (*Foreign Key Violation*). | Mapeo de claves foráneas y cascadas `CascadeType.ALL` en Entidades JPA (`entity/`). |
| **¿Inconsistencia en `@Transactional`?** | Excepción checked no configurada en `rollbackFor` o transacción manual no sincronizada. | Anotación `@Transactional(rollbackFor = Exception.class)` en métodos de servicio. |
| **¿Sintaxis JPQL inválida o concatenación no autorizada?** | Query mal formulada que falla al parsearse por Hibernate. | Métodos `@Query` en interfaces `repository/` (usar parámetros `:param`). |


#### C. Reparación Directa en Código Fuente
- **Escenario Real:** El método `generarOrdenServicio` invocaba `.get()` sobre un `Optional` sin verificar existencia, o faltaba la política de rollback ante excepciones comprobadas.
- **Archivo a modificar:** `backend/src/main/java/pe/upn/sist1402a/service/impl/CotizacionServiceImpl.java`.

*Código defectuoso:*
```java
// ERROR: No captura ausencia de ID, no define rollbackFor amplio y deja transacciones a medias
@Transactional
public OrdenServicio generarOrdenServicio(Long cotizacionId) {
    Cotizacion cotizacion = cotizacionRepository.findById(cotizacionId).get(); // Peligro NPE
    cotizacion.setEstado(EstadoCotizacion.ORDEN_SERVICIO);
    cotizacionRepository.save(cotizacion);
    // Si falla lo siguiente, cotización queda modificada en la BD sin orden de servicio!
    return ordenServicioRepository.save(nuevaOrden);
}
```

*Código corregido por el Compañero de QA:*
```java
// FIX: Manejo seguro con orElseThrow y blindaje atómico total con rollbackFor = Exception.class
@Override
@Transactional(rollbackFor = Exception.class)
public OrdenServicio generarOrdenServicio(Long cotizacionId) {
    Cotizacion cotizacion = cotizacionRepository.findById(cotizacionId)
            .orElseThrow(() -> new ResourceNotFoundException("No se encontró la cotización con ID: " + cotizacionId));

    if (cotizacion.getEstado() != EstadoCotizacion.ACEPTADA) {
        throw new BadRequestException("Solo se puede generar Orden de Servicio para cotizaciones en estado ACEPTADA");
    }

    cotizacion.setEstado(EstadoCotizacion.ORDEN_SERVICIO);
    cotizacionRepository.save(cotizacion);

    OrdenServicio orden = OrdenServicioFactory.crearOrdenDesdeCotizacion(cotizacion);
    return ordenServicioRepository.save(orden);
}
```

- **Commit Semántico del Arreglo:**
```bash
git add backend/src/main/java/pe/upn/sist1402a/service/impl/CotizacionServiceImpl.java
git commit -m "fix(service): [RF-SEM12-02] blindar transaccionalidad con rollbackFor y orElseThrow en CotizacionService"
```

---

## 5. Protocolo de Handoff QA -> Vivian (Paso de Estafeta Limpio)

Una vez que el Compañero de QA ha diagnosticado y reparado todos los defectos en el código fuente, debe ejecutar el siguiente protocolo formal antes de transferir la estafeta a Vivian:

> [!CHECK]
> **Checklist Obligatorio de Handoff a Vivian (100% Verde en QA)**  
> - [x] **1. Pruebas Backend:** `./mvnw test` ejecutado exitosamente con 100% verde (0 fallos, 0 errores).  
> - [x] **2. Pruebas Frontend:** `ng test` ejecutado exitosamente con 100% verde.  
> - [x] **3. Quality Gates:** Criterios `QG-01` a `QG-08` formalmente superados.  
> - [x] **4. Trazabilidad Git:** Commits semánticos debidamente rotulados con `[RF-SEMXX-YY]`.  
> - [x] **5. Verificación de Contratos:** Smoke test con cURL y Swagger UI completados sin discrepancias.  
> - [x] **6. Notificación:** Mensaje formal de entrega transmitido a Vivian para inicio de fase estética.


### 5.1. Plantilla de Mensaje de Handoff a Vivian

El Compañero de QA envía el siguiente comunicado técnico formal a Vivian para habilitar su fase de revisión:

> **Destinataria:** Vivian (Product Owner & Frontend Owner)  
> **De:** Compañero de QA (QA Engineer & Bug-Fixer Activo)  
> **Asunto:** Pase de Estafeta: Versión Estabilizada y Lista para Validación Comercial y Ajustes Visuales  
> 
> *"Hola Vivian, te informo que he completado la fase de pruebas y estabilización activa para el incremento actual. Se detectaron y subsanaron anomalías en la serialización de cotizaciones y en el interceptor de Angular.  
> 
> **Estado Técnico de Certificación:**
> - `./mvnw test`: 100% verde (todos los tests unitarios y de rollback transaccional aprobados).
> - `ng test`: 100% verde.
> - Quality Gates QG-01 a QG-08: Aprobados sin observaciones.
> - Commits de fix registrados en el repositorio bajo convención semántica `[RF-SEMXX-YY]`.
> 
> La API REST y los servicios en `http://localhost:8080` y `http://localhost:4200` se encuentran plenamente operativos y listos para que realices la revisión comercial contra los 5 documentos reales de Global Frío y apliques cualquier refinamiento visual o componente pendiente en Angular. Quedo atento a tus observaciones."*

---

## 6. Trazabilidad con el Vault de Obsidian

- [[00-MOC-GlobalFrio|MOC Central de Global Frío]] — Mapa de contenidos de la documentación técnica.
- [[01-Requerimientos-y-Negocio/Guia-Evidencias-Vivian-y-Flujo-Equipo|Guía de Evidencias de Vivian y Flujo de Equipo]] — Matriz de responsabilidades y flujo colaborativo.
- [[02-Arquitectura-y-Backend/HLD-Arquitectura-Sistemas|HLD Arquitectura de Sistemas]] — Arquitectura física y lógica desacoplada.
- [[02-Arquitectura-y-Backend/LLD-Backend-SpringBoot-Patrones|LLD Backend Spring Boot y Patrones GoF]] — Especificación de clases y patrones de diseño.
- [[04-QA-y-Testing/QA-Test-Plan-Acceptance-Matrix|Plan Maestro de Pruebas QA y Matriz de Aceptación]] — Catálogo de los 15 casos de prueba críticos y Quality Gates.

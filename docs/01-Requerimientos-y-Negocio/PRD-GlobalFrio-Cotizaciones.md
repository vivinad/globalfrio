---
title: PRD — Sistema Web de Cotización Automatizada Global Frío
aliases:
  - PRD Global Frío
  - Documento de Requisitos de Producto
  - Product Requirements Document
tags:
  - prd
  - requerimientos
  - historias-usuario
  - gherkin
  - globalfrio
owner: Vivian (Product Owner & Representante Global Frío)
status: Aprobado
version: 1.0.0
updated: 2026-10-17
---

# PRD — Sistema Web de Cotización Automatizada Global Frío

> [!IMPORTANT]
> **Alineación Estratégica y Propósito Comercial:**  
> Este Documento de Requisitos de Producto (PRD) especifica las capacidades funcionales, reglas termodinámicas, restricciones operativas y criterios de calidad para la plataforma web de cotizaciones de **Global Frío S.A.C.**, empresa contratista líder en climatización comercial, HVAC y refrigeración industrial en Lima y Callao (Ventanilla `07061`, Contacto: **Edgar Sabalu**, Tel: `904 209 489`).
> 
> El desarrollo cumple con la progresión de la cátedra de **Soluciones Web y Aplicaciones Distribuidas (SIST1402A - UPN)** dictada por el **Ing. Mg. Carlos Alberto Ponte Ramírez**, garantizando trazabilidad total con el **ODS 09: Industria, Innovación e Infraestructura**.

---

## 1. Problemática del Negocio y Objetivos Estratégicos

### 1.1. Contexto Operativo y Diagnóstico del Problema
En el mercado de climatización comercial e industrial de Lima Metropolitana, **Global Frío S.A.C.** enfrenta los siguientes cuellos de botella en su flujo comercial tradicional:

1. **Tiempos de Respuesta Prolongados (48 a 72 horas):**  
   Los prospectos solicitan cotizaciones vía telefónica o WhatsApp. Un asesor técnico debe calcular la carga térmica en hojas de cálculo no estandarizadas o coordinar una visita física preliminar, demorando entre 2 y 3 días hábiles en emitir una propuesta formal. Esto genera una tasa de abandono de prospectos del 42%.
2. **Visitas Técnicas en Falso y Costos Operativos Ineficientes:**  
   Aproximadamente el 35% de las visitas técnicas realizadas en distritos alejados (como Lurín, Ate o Asia) no se concretan porque el cliente buscaba únicamente un costo referencial o no contaba con la acometida eléctrica requerida (ej. 220V trifásica para equipos de más de 36,000 BTU/h), consumiendo combustible y horas-hombre de técnicos calificados.
3. **Errores en el Cálculo de Carga Térmica (Sobredimensionamiento / Subdimensionamiento):**  
   El uso de reglas empíricas imprecisas ("tantos metros cuadrados equivalen a tantos BTU") sin considerar el volumen cúbico, tipo de actividad (oficinas, servidores, cámaras de alimentos) y exposición solar conduce a:
   - *Sobredimensionamiento:* Incrementa el costo de inversión inicial en un 25% y provoca ciclos cortos de encendido/apagado del compresor, elevando el consumo eléctrico (afectando la meta de sostenibilidad ambiental).
   - *Subdimensionamiento:* El equipo opera forzado al 100% sin alcanzar el setpoint de temperatura, provocando congelamiento de serpentines y fallas mecánicas prematuras.
4. **Falta de Trazabilidad y Discrepancias en Precios:**  
   Los presupuestos en papel o correos aislados impiden congelar los precios de suministros (cobre, gas refrigerante R-410A / R-32 importados y tasados en dólares). Si el cliente acepta la propuesta tres semanas después, los costos de materiales han variado, mermando el margen de ganancia de la empresa.

### 1.2. Objetivos del Sistema Web
- Proveer a clientes residenciales, comerciales e industriales una herramienta digital de autoservicio que calcule la carga térmica y emita una cotización estimada formal en **menos de 3 minutos**.
- Permitir la carga digital de evidencias de obra (fotos, planos, croquis acotados) en la solicitud para que el equipo de ingeniería evalúe pases de viga y metrajes de tubería de cobre antes de despachar una cuadrilla técnica.
- Establecer un flujo transaccional centralizado para que los asesores de Global Frío revisen, ajusten factores zonales y aprueben la emisión de la **Proforma Oficial Membretada** y la posterior **Orden de Servicio**.
- Proporcionar un canal seguro contra la alteración de precios en el cliente (Zero-Trust) mediante recálculo estricto en el servidor.

---

## 2. Indicadores Clave de Rendimiento (KPIs)

| Identificador | Indicador de Éxito | Línea Base (Manual) | Meta con Sistema Web | Impacto ODS 09 |
| :--- | :--- | :---: | :---: | :--- |
| **KPI-01** | **Tiempo Promedio de Emisión de Cotización** | 48 - 72 horas | **$\le$ 3 minutos** (estimada)<br>$\le$ 24 horas (formal asesor) | Meta 9.b (Eficiencia operativa en PYMEs) |
| **KPI-02** | **Precisión en el Dimensionamiento Térmico (BTU/h)** | 78% precisión empírica | **$\ge$ 99.4%** conforme a estándares ASHRAE / Gree / Samsung | Meta 9.4 (Eficiencia energética, previene desperdicio eléctrico de 15-25%) |
| **KPI-03** | **Reducción de Visitas Técnicas en Falso** | 35% de visitas infructuosas | **$\le$ 8%** mediante carga previa de fotos y planos | Meta 9.4 (Reducción de huella de carbono por transporte de cuadrillas) |
| **KPI-04** | **Tasa de Conversión de Cotización a Orden de Servicio** | 18% de cierre comercial | **$\ge$ 35%** en los primeros 6 meses | Meta 9.b (Crecimiento industrial sostenible) |
| **KPI-05** | **Digitalización y Consumo Cero Papel** | 100% talonarios impresos | **100% digital** en PDF y reportes web | Meta 9.4 (Cero desperdicio de recursos físicos) |

---

## 3. Matriz de Priorización de Requerimientos (MoSCoW)

| Nivel MoSCoW | Requerimientos Asociados | Alcance y Justificación Comercial |
| :--- | :--- | :--- |
| **MUST HAVE**<br>(Mandatorios T1 / T2) | - `RF01`: Algoritmo recomendador de carga térmica (BTU/h)<br>- `RF02`: Cotizador dinámico en servidor con Decorator en `BigDecimal`<br>- `RF04`: Registro y validación de clientes con Bean Validation<br>- `RF05`: Seguridad stateless JWT y control de acceso RBAC<br>- `RF06`: Máquina de estados finita de la cotización<br>- `RF07`: Conversión atómica a Orden de Servicio con `@Transactional`<br>- `RNF03`: Front-end SPA responsive con tokens oficiales | Base operacional y comercial indispensable para emitir cotizaciones válidas y seguras sin tampering. |
| **SHOULD HAVE**<br>(Compromiso T3 / Final) | - `RF03`: Carga segura de fotos/planos con verificación Magic Bytes<br>- `RF08`: Panel asesor con paginación server-side (`Pageable`) y filtros JPQL<br>- `RF09`: Panel de administración de tarifarios y fletes zonales<br>- `RF10`: Catálogo técnico con especificaciones SEER y refrigerantes ecológicos<br>- `RF11`: Generación de proforma oficial membretada en Thymeleaf/PDF | Optimización operativa, soporte documental de obra y digitalización formal para la evaluación final. |
| **COULD HAVE**<br>(Valor Agregado) | - `RF12`: Registro de auditoría de cambios de estado y bitácora técnica<br>- Notificación asíncrona simulada al confirmar orden de servicio | Monitoreo administrativo avanzado y trazabilidad post-venta. |
| **WON'T HAVE**<br>(Excluido de este ciclo) | - Pasarela de pagos con tarjeta de crédito en tiempo real<br>- Geolocalización satelital en vivo de técnicos en ruta | Exceden el alcance del sílabo y demandan pasarelas comerciales externas no cubiertas. |

---

## 4. Requerimientos Funcionales Detallados (`RF01` a `RF12`)

### RF01: Algoritmo Recomendador de Carga Térmica (BTU/h)
- **Descripción:** El sistema calculará la capacidad frigorífica nominal requerida para el recinto a climatizar, tomando como variables de entrada el área ($m^2$), altura de techo ($m$), tipo de uso del recinto (habitacional, oficina corporativa, sala de cómputo/servidores, consultorio médico/vacunatorio, cámara de conservación de alimentos) y nivel de exposición solar (baja, media, alta).
- **Fórmula de Ingeniería Aplicada:**
  $$\text{Volumen } V = \text{Área } (m^2) \times \text{Altura } (m)$$
  $$\text{Carga Base } (BTU/h) = V \times \text{Factor de Uso } (K_{uso})$$
  Donde:
  - $K_{uso} = 150 \text{ BTU}/m^3$ para recintos estándar de baja carga.
  - $K_{uso} = 200 \text{ BTU}/m^3$ para oficinas con computadoras y personas.
  - $K_{uso} = 300 \text{ BTU}/m^3$ para consultorios/posta médica y almacenes comerciales.
  - $K_{uso} \ge 400 \text{ BTU}/m^3$ para salas de servidores y cuartos de control.
  - Multiplicador solar: $+15\%$ ante radiación solar directa intensa.
- **Salida:** Capacidad nominal recomendada en BTU/h comercial más cercana (9,000, 12,000, 18,000, 24,000, 36,000 o 60,000 BTU/h) y selección de tecnología sugerida (Split Muro, Piso Techo, Cassette o Paquete).
- **Trazabilidad:** Semana 02 (`RF-SEM02-01`), Semana 07 (`RF-SEM07-02`).

### RF02: Cotizador Dinámico en Servidor con Desglose de Componentes
- **Descripción:** Cálculo monetario acumulativo del presupuesto total desglosado en: (a) Equipo evaporador/condensador base, (b) Servicio de instalación estándar (incluye hasta 5 metros de tubería de cobre y ménsulas), (c) Metros adicionales de tubería de cobre aislada con armaflex y cable vulcanizado, y (d) Flete zonal según el distrito de Lima Metropolitana o Callao.
- **Regla Mandataria Anti-Tampering:** El cálculo debe ejecutarse **exclusivamente en el backend de Spring Boot** mediante el patrón GoF **Decorator** (`PresupuestoCalculator`) empleando variables `BigDecimal` con escala de 2 decimales y modo de redondeo `RoundingMode.HALF_UP`. Cualquier precio enviado desde el cliente web será ignorado y sobrescrito en el servidor.
- **Trazabilidad:** Semana 02 (`RF-SEM02-02`), Semana 05 (`RF-SEM05-01`), Semana 12 (`RF-SEM12-03`). Ver diagrama: [[diagramas/03-flujo-cotizacion-y-patron-decorator.drawio|Diagrama 03: Flujo Cotización y Decorator]].

### RF03: Carga Segura de Ficha Técnica, Planos y Fotos de Obra
- **Descripción:** El cliente podrá adjuntar hasta 3 archivos digitales (máximo 5 MB por archivo) en formato PDF, PNG o JPG que evidencien el espacio físico: croquis acotado, fotografía de la pared donde se colocará el evaporador, y ubicación prevista del condensador exterior (fachada, azotea o patio).
- **Regla de Seguridad OWASP:** El sistema verificará los Magic Bytes de la cabecera binaria del archivo (`%PDF`, `\xFF\xD8\xFF`, `\x89PNG`) rechazando scripts ejecutables maliciosos camuflados. Cada archivo será renombrado en el servidor con un `UUID.randomUUID()` aleatorio y almacenado en un directorio aislado fuera del web root.
- **Trazabilidad:** Semana 05 (`RF-SEM05-01`), Semana 09 (`RF-SEM09-01`).

### RF04: Registro y Validación Declarativa de Datos de Clientes
- **Descripción:** Captura de datos del cliente solicitante distinguiendo entre Persona Natural (DNI 8 dígitos, Nombres, Apellidos) y Persona Jurídica (RUC 11 dígitos, Razón Social, Contacto Responsable).
- **Validaciones Bean Validation Backend:**
  - `@NotBlank` en nombres, dirección y distrito.
  - `@Email` válido para envío de proforma.
  - `@Pattern(regexp = "^9[0-9]{8}$")` para teléfono celular peruano (9 dígitos iniciando con 9).
  - `@Size(min = 8, max = 11)` para documento de identidad.
- **Trazabilidad:** Semana 05 (`RF-SEM05-01`).

### RF05: Autenticación Stateless y Control de Acceso RBAC
- **Descripción:** Mecanismo de autenticación para el personal de Global Frío en el endpoint `/api/auth/login`. Emisión de JSON Web Tokens (JWT) firmados con algoritmo HMAC-SHA256 y vigencia de 24 horas.
- **Roles y Permisos:**
  - `ROLE_ASESOR`: Permiso de lectura y modificación del estado de cotizaciones, agendamiento de visitas y conversión a orden de servicio.
  - `ROLE_ADMIN`: Acceso total, incluyendo modificación de tarifarios base, fletes zonales, catálogo de equipos y gestión de usuarios.
- **Trazabilidad:** Semana 05 (`RF-SEM05-02`), Semana 08 (`RF-SEM08-03`). Ver diagrama: [[diagramas/05-secuencia-jwt-rbac-logout.drawio|Diagrama 05: Secuencia JWT y RBAC]].

### RF06: Máquina de Estados Finita (FSM) de Cotizaciones
- **Descripción:** Cada cotización transitará por un ciclo de vida formal y determinista:
  $$\text{PENDIENTE} \longrightarrow \text{EN\_REVISION} \longrightarrow \text{ENVIADO} \longrightarrow \begin{cases} \text{ACEPTADO} \longrightarrow \text{ORDEN\_SERVICIO} \\ \text{RECHAZADO} \end{cases}$$
- **Reglas de Transición:**
  - Solo cotizaciones en estado `PENDIENTE` pueden ser tomadas por un asesor y pasar a `EN_REVISION`.
  - Solo cotizaciones en estado `ENVIADO` con acuse formal del cliente pueden ser marcadas como `ACEPTADO`.
  - La transición de `ACEPTADO` a `ORDEN_SERVICIO` dispara la generación de la orden de trabajo.
- **Trazabilidad:** Semana 04 (`RF-SEM04-02`), Semana 12 (`RF-SEM12-02`). Ver diagrama: [[diagramas/04-maquina-estados-cotizacion-orden.drawio|Diagrama 04: Máquina de Estados FSM]].

### RF07: Conversión Atómica a Orden de Servicio y Congelamiento de Snapshots
- **Descripción:** Cuando una cotización es aceptada, el sistema genera la entidad `OrdenServicio` asociada a la cuadrilla de instalación. En ese instante, se copian de forma inmutable los precios unitarios, subtotales, fletes e impuestos en campos dedicados (`snapshot_precio_equipo`, `snapshot_costo_instalacion`, `snapshot_total_soles`).
- **Regla de Integridad Transaccional:** Toda la operación se ejecutará bajo `@Transactional(rollbackFor = Exception.class)`. Si falla la generación del snapshot o la inserción de la orden, se revierte íntegramente la transacción en la base de datos, evitando inconsistencias contables.
- **Trazabilidad:** Semana 12 (`RF-SEM12-02`), Semana 13 (`RF-SEM13-01`).

### RF08: Panel de Asesor con Paginación en Servidor y Filtros Dinámicos
- **Descripción:** Interfaz para el asesor técnico con grilla de cotizaciones registradas. Para optimizar el rendimiento ante altos volúmenes de solicitudes, la consulta utilizará `Pageable` (`PageRequest.of(page, size, sort)`) en Spring Data JPA y `MatPaginator` en Angular.
- **Filtros Soportados:** Búsqueda por número de correlativo (`COT-2026-XXXX`), filtro por estado, rango de fechas y distrito de instalación mediante consultas JPQL parametrizadas seguras.
- **Trazabilidad:** Semana 11 (`RF-SEM11-01`), Semana 12 (`RF-SEM12-01`).

### RF09: Panel de Administración de Tarifarios y Zonificación de Lima
- **Descripción:** Módulo reservado para el administrador (`ROLE_ADMIN`) que permite actualizar:
  - Costo base de equipos por capacidad BTU y fabricante (Gree, Samsung, York).
  - Costo de mano de obra de instalación estándar.
  - Precio por metro lineal adicional de tubería de refrigeración de cobre (aislada y con cables).
  - Recargos de transporte zonales (Zona Norte: Ventanilla/Puente Piedra/Comas; Zona Centro: San Isidro/Miraflores; Zona Este/Sur: Lurín/Ate/Chorrillos/Asia).
- **Trazabilidad:** Semana 04 (`RF-SEM04-02`), Semana 05 (`RF-SEM05-01`).

### RF10: Catálogo Técnico Oficial de Equipos de Refrigeración y Climatización
- **Descripción:** Registro centralizado de especificaciones técnicas de los equipos disponibles:
  - Capacidad nominal (BTU/h y kW térmicos).
  - Eficiencia energética: Ratio SEER (Seasonal Energy Efficiency Ratio) $\ge 16$ y clasificación energética A/Inverter.
  - Tipo de gas refrigerante ecológico: R-410A o R-32 (bajo potencial de calentamiento global GWP).
  - Tensión eléctrica de alimentación: 220V Monofásico o 220V/380V Trifásico.
  - Nivel de presión sonora interior y exterior (dB).
- **Trazabilidad:** Semana 02 (`RF-SEM02-01`), Semana 06 (`RF-SEM06-02`).

### RF11: Emisión de Proforma Membretada Oficial en Vista Thymeleaf / PDF
- **Descripción:** Generación del documento comercial oficial con el membrete institucional de **Global Frío S.A.C.** (RUC 20601234567, Ventanilla 07061, Edgar Sabalu 904 209 489). Se implementa un controlador server-side `@Controller` con plantilla Thymeleaf en `/reportes/cotizacion/{id}` que renderiza la proforma idéntica al formato físico, con desglose de ítems, condiciones de validez comercial (7 días) y cálculo de IGV (18%).
- **Trazabilidad:** Semana 01 (`RF-SEM01-02`), Semana 14 (`RF-SEM14-03`).

### RF12: Registro de Trazabilidad y Bitácora de Eventos Comerciales
- **Descripción:** Cada acción relevante sobre una cotización (creación de solicitud, cambio de estado, edición de notas técnicas, asignación de asesor, descarga de proforma) quedará registrada en una tabla de auditoría `cotizacion_bitacora` con marca de tiempo UTC, usuario responsable y descripción del evento.
- **Trazabilidad:** Semana 12 (`RF-SEM12-01`), Semana 15 (`RF-SEM15-03`).

---

## 5. Requerimientos No Funcionales (`RNF01` a `RNF06` MoSCoW)

### RNF01: Rendimiento y Tiempos de Respuesta (Must Have)
- El tiempo de respuesta del backend para el cálculo de cotización en el endpoint `/api/cotizaciones/calcular` debe ser inferior a **300 milisegundos** bajo una carga concurrente típica.
- El tiempo de carga inicial de la aplicación Angular SPA en redes 4G estándar debe ser inferior a **1.5 segundos**, utilizando compresión gzip/brotli y lazy loading de rutas.

### RNF02: Seguridad, Anti-Tampering y Blindaje OWASP (Must Have)
- **Principio de Confianza Cero en el Cliente:** El backend nunca aceptará montos totales ni subtotales calculados por el cliente. Todos los importes monetarios se calculan en `@Service` usando `BigDecimal`.
- **Mitigación SQLi:** Todas las consultas dinámicas a la base de datos se ejecutarán mediante JPQL parametrizado con `@Query` y parámetros enlazados con `@Param`. Prohibida la concatenación de cadenas.
- **Criptografía:** Contraseñas de usuarios almacenadas con algoritmo de derivación de claves BCrypt (factor de costo 12). Tokens JWT cifrados con HMAC-SHA256.

### RNF03: Responsividad y Fidelidad Visual Mobile-First (Must Have)
- La interfaz de usuario debe ser 100% responsiva y operable en pantallas móviles desde **360px de ancho** (smartphones compactos) hasta pantallas 4K (**1920px o superior**), sin provocar scroll horizontal (`overflow-x: hidden`).
- Adherencia estricta a los Design Tokens corporativos:
  - Color acero principal: `#5980a6`
  - Botón de acción: `#1f5fa9` (Hover: `#184d8a`)
  - Fondo oscuro: `#15181a` (Superficie: `#1c2023`, Texto: `#e8eaeb`)
  - Tipografía oficial: `Barlow Condensed` en encabezados y `Barlow` en textos regulares.

### RNF04: Alta Disponibilidad, Resiliencia y Manejo de Errores (Should Have)
- El servidor de aplicaciones debe mantener una disponibilidad del **99.5%** durante horario de atención comercial.
- Todos los errores y excepciones en la API REST deben ser interceptados por un manejador global `@RestControllerAdvice`, emitiendo respuestas JSON normalizadas con estructura estándar:
  ```json
  {
    "timestamp": "2026-10-17T22:30:00Z",
    "status": 400,
    "error": "Bad Request",
    "message": "El área en metros cuadrados debe ser mayor a cero",
    "path": "/api/cotizaciones"
  }
  ```

### RNF05: Integridad Transaccional ACID (Must Have)
- Las operaciones compuestas (creación de cotización con ítems, aprobación de cotización y generación de orden de servicio) deben ejecutarse de forma atómica mediante `@Transactional(rollbackFor = Exception.class)`. Ante cualquier falla inesperada de I/O, base de datos o lógica de negocio, se forzará la reversión completa de los cambios.

### RNF06: Mantenibilidad y Cumplimiento de la Lista Blanca UPN (Must Have)
- El código fuente debe apegarse estrictamente a la lista blanca de dependencias del sílabo del curso SIST1402A (Java 17, Spring Boot 3.3.4, Angular 18, Bootstrap 5). Queda terminantemente prohibido incorporar frameworks o librerías externas no homologadas por la cátedra del Ing. Carlos Alberto Ponte Ramírez.

---

## 6. Historias de Usuario (`HU01` a `HU08`) con Criterios Gherkin

### HU01: Selección de Equipo y Dimensionamiento Térmico (Recomendador BTU)
**Como** cliente potencial que necesita climatizar un ambiente comercial o médico,  
**Quiero** ingresar las dimensiones de mi local y el tipo de uso en el recomendador web,  
**Para** conocer la capacidad exacta en BTU/h requerida y evitar el sobredimensionamiento energético.

> [!CHECK]
> **Criterios de Aceptación (Gherkin):**
> 
> ```gherkin
> Escenario: Cálculo exitoso de carga térmica para consultorio médico
>   Dado que el usuario abre el modal de cotización en el Paso 1
>   Y el usuario ingresa un área de "30" metros cuadrados y altura de "2.6" metros
>   Y selecciona el tipo de ambiente "Consultorio Médico / Posta"
>   Y selecciona radiación solar "Media"
>   Cuando el usuario hace clic en el botón "Calcular Carga Térmica"
>   Entonces el sistema calcula un volumen de 78 metros cúbicos
>   Y recomienda un equipo comercial de "18000 BTU/h" con tecnología Inverter
>   Y habilita el botón para avanzar al Paso 2
> 
> Escenario: Ingreso de área no válida (menor o igual a cero)
>   Dado que el usuario se encuentra en el Paso 1 del recomendador
>   Cuando ingresa un valor de "0" o negativo en el campo "Área (m2)"
>   Entonces el sistema muestra una advertencia visual "El área debe ser mayor a 0 m²"
>   Y mantiene deshabilitado el botón de avance
> ```

---

### HU02: Cotización Interactiva en Tiempo Real con Selección Zonal
**Como** usuario interesado en los servicios de Global Frío,  
**Quiero** seleccionar el metraje estimado de tuberías de cobre y el distrito de instalación en Lima/Callao,  
**Para** visualizar el presupuesto desglosado en Soles (S/) antes de enviar mi solicitud.

> [!CHECK]
> **Criterios de Aceptación (Gherkin):**
> 
> ```gherkin
> Escenario: Cotización en vivo con recargo zonal y tubería adicional
>   Dado que el usuario seleccionó un equipo Split Muro de 12000 BTU/h (precio base S/ 1,850.00)
>   Y el servicio incluye 5 metros estándar de tubería de cobre
>   Cuando el usuario especifica una distancia de "9" metros (4 metros adicionales a S/ 45.00 c/u)
>   Y selecciona como distrito de instalación "San Isidro" (flete zonal estándar S/ 70.00)
>   Entonces el cotizador muestra un subtotal de S/ 2,100.00
>   Y calcula el IGV (18%) equivalente a S/ 378.00
>   Y muestra el Total General en pantalla de "S/ 2,478.00"
>   Y avisa claramente que el monto final será ratificado por el backend de Global Frío
> ```

---

### HU03: Carga de Ficha Técnica y Fotos de Obra para Validación de Factibilidad
**Como** cliente que posee un local con estructura compleja (vigas peraltadas o fachada de vidrio),  
**Quiero** subir fotografías y planos del lugar de instalación,  
**Para** que el personal técnico de Global Frío verifique la factibilidad antes de acudir al terreno.

> [!CHECK]
> **Criterios de Aceptación (Gherkin):**
> 
> ```gherkin
> Escenario: Carga exitosa de fotografía del área técnica
>   Dado que el usuario se encuentra en la sección de anexos del cotizador
>   Cuando selecciona un archivo "fachada_condensador.jpg" con peso de 2.4 MB
>   Entonces el sistema valida que los Magic Bytes correspondan a una imagen JPEG válida
>   Y muestra una vista previa miniatura con el nombre del archivo
>   Y permite continuar al formulario de contacto
> 
> Escenario: Rechazo de archivo ejecutable malicioso
>   Dado que un usuario intenta subir un archivo "script_danino.php" o "virus.exe"
>   Cuando adjunta el archivo en el selector
>   Entonces el sistema detecta cabeceras no autorizadas
>   Y rechaza la subida mostrando el mensaje de error "Formato de archivo no admitido. Suba solo JPG, PNG o PDF"
>   Y no almacena ningún binario en el servidor
> ```

---

### HU04: Solicitud Formal de Proforma con Generación de Correlativo Único
**Como** cliente que completó la configuración de su equipo,  
**Quiero** ingresar mis datos de contacto (RUC/DNI, nombres, correo, teléfono) y enviar la solicitud,  
**Para** recibir formalmente mi proforma numerada y ser atendido por un asesor comercial.

> [!CHECK]
> **Criterios de Aceptación (Gherkin):**
> 
> ```gherkin
> Escenario: Emisión de solicitud de cotización formal
>   Dado que el usuario completó la configuración técnica de un equipo de 24000 BTU
>   Cuando ingresa datos válidos: RUC "20551234561", Razón Social "Inversiones Médicas SAC", Email "logistica@inversionesmedicas.pe", Celular "987654321"
>   Y hace clic en "Solicitar Cotización Formal"
>   Entonces el backend genera un código correlativo con formato "COT-2026-XXXX"
>   Y guarda el registro en la base de datos con estado "PENDIENTE"
>   Y muestra en pantalla la pantalla de confirmación con el código asignado y opción de descargar PDF estimado
> ```

---

### HU05: Autenticación Segura de Asesores y Administradores con JWT
**Como** asesor comercial o administrador de Global Frío,  
**Quiero** autenticarme en la plataforma con mi usuario y contraseña institucional,  
**Para** acceder al panel privado de gestión de solicitudes y proteger la información comercial.

> [!CHECK]
> **Criterios de Aceptación (Gherkin):**
> 
> ```gherkin
> Escenario: Inicio de sesión exitoso de un asesor
>   Dado que el usuario navega a la ruta "/login"
>   Cuando ingresa su usuario "esabalu" y su contraseña válida
>   Entonces el backend valida el hash BCrypt en la base de datos
>   Y emite un token JWT con el claim de rol "ROLE_ASESOR"
>   Y el interceptor frontend almacena el token en el almacenamiento seguro
>   Y redirige automáticamente al usuario al panel "/panel/solicitudes"
> 
> Escenario: Intento de acceso a ruta protegida sin autenticación
>   Dado que un usuario anónimo intenta acceder directamente a "/panel/solicitudes"
>   Cuando carga la URL en su navegador
>   Entonces el AuthGuard de Angular intercepta la navegación
>   Y redirige inmediatamente a la vista "/login" con el parámetro "?returnUrl=/panel/solicitudes"
> ```

---

### HU06: Gestión de Estados y Aprobación Comercial de Cotizaciones
**Como** asesor comercial de Global Frío,  
**Quiero** revisar las solicitudes pendientes, inspeccionar las fotos adjuntas y actualizar su estado a "ENVIADO" o "ACEPTADO",  
**Para** coordinar con el cliente las condiciones técnicas y comerciales finales.

> [!CHECK]
> **Criterios de Aceptación (Gherkin):**
> 
> ```gherkin
> Escenario: Transición de estado de PENDIENTE a EN_REVISION y ENVIADO
>   Dado que el asesor tiene abierta la solicitud "COT-2026-0042" en estado "PENDIENTE"
>   Cuando el asesor hace clic en "Tomar Solicitud"
>   Entonces el sistema actualiza el estado a "EN_REVISION" y registra al asesor asignado
>   Y cuando el asesor valida los costos y hace clic en "Enviar Proforma al Cliente"
>   Entonces el estado cambia a "ENVIADO" y se registra la fecha de despacho
> ```

---

### HU07: Generación Atómica de Orden de Servicio con Congelamiento de Snapshots
**Como** asesor comercial,  
**Quiero** convertir una cotización aceptada por el cliente en una Orden de Servicio de instalación,  
**Para** asignar una cuadrilla técnica en terreno y asegurar que los precios pactados no se alteren en el futuro.

> [!CHECK]
> **Criterios de Aceptación (Gherkin):**
> 
> ```gherkin
> Escenario: Generación atómica de Orden de Servicio y snapshots inmutables
>   Dado que la cotización "COT-2026-0042" se encuentra en estado "ACEPTADO"
>   Cuando el asesor hace clic en "Generar Orden de Servicio" asignando técnico "Carlos Méndez" y fecha "2026-10-25"
>   Entonces el servicio transaccional @Transactional crea la orden con correlativo "OS-2026-0019"
>   Y congela de forma inmutable los precios unitarios y fletes en las columnas snapshot de la base de datos
>   Y actualiza el estado de la cotización a "ORDEN_SERVICIO"
>   Y si ocurriese un error de base de datos durante la creación, se ejecuta rollback total sin alterar ninguna tabla
> ```

---

### HU08: Ajuste Administrativo de Tarifarios y Parámetros Comerciales
**Como** administrador general del sistema,  
**Quiero** modificar los precios base de equipos, costo por metro de tubería de cobre y recargos zonales de transporte,  
**Para** mantener el cotizador alineado con las listas de precios de importación de Gree, Samsung y los proveedores de cobre.

> [!CHECK]
> **Criterios de Aceptación (Gherkin):**
> 
> ```gherkin
> Escenario: Modificación del flete para la Zona Sur de Lima
>   Dado que el usuario autenticado posee el rol "ROLE_ADMIN" en el panel "/panel/tarifario"
>   Cuando modifica el flete de transporte para "Zona Sur (Lurín, Chorrillos)" de S/ 120.00 a S/ 140.00
>   Y presiona "Guardar Cambios"
>   Entonces el backend actualiza la entidad en la base de datos MySQL
>   Y las nuevas cotizaciones calculadas reflejan inmediatamente el flete de S/ 140.00
>   Y las cotizaciones emitidas previamente con snapshots no sufren modificación alguna
> ```

---

## 7. Diagramas de Apoyo Arquitectónico

Para una comprensión visual exhaustiva de los requerimientos y la lógica del negocio, consulte los siguientes diagramas nativos en Draw.io:
- [[diagramas/03-flujo-cotizacion-y-patron-decorator.drawio|Diagrama 03: Algoritmo de Carga Térmica y Decorator]]
- [[diagramas/04-maquina-estados-cotizacion-orden.drawio|Diagrama 04: Ciclo de Vida y Transición de Estados FSM]]
- [[diagramas/06-mapa-navegacion-ui-ux.drawio|Diagrama 06: Mapa de Navegación UI/UX del Cotizador]]

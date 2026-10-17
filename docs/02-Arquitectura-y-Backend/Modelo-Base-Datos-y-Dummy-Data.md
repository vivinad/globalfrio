---
title: "Modelo de Base de Datos Relacional (3FN) y Script de Semilla Comercial"
aliases:
  - "Modelo Base de Datos Global Frío"
  - "DDL y Semilla SQL Global Frío"
tags:
  - database
  - sql
  - mysql
  - h2
  - ddl
  - dummy-data
  - upn
owner: "Orlando (Arquitecto de Sistemas & Líder Backend)"
status: "Completado"
version: "1.0.0"
updated: "2026-10-17"
---

# Modelo de Base de Datos Relacional (3FN) y Script de Semilla Comercial

El presente documento especifica formalmente el diseño de la **Base de Datos Relacional en Tercera Forma Normal (3FN)**, la arquitectura de **columnas inmutables tipo snapshot**, la estrategia de persistencia dual (**H2 Database en memoria** vs. **MySQL Server 8.x en producción**) y el script DDL/DML de semilla completa (`data-seed-globalfrio.sql`) con datos de negocio reales en Soles (`S/`) para **Global Frío SAC** (Lima, Perú — Periodo 2026-2).

---

## 1. Diagrama de Entidad-Relación Físico y Lógico

El modelo comprende 11 entidades estructuradas para satisfacer el flujo integral: captura de requerimientos, dimensionamiento térmico BTU, cotización formal aprobada con congelamiento fiscal y emisión de Órdenes de Servicio técnico.

![[diagramas/02-modelo-entidad-relacion-bd.drawio]]
*Figura 1: Diagrama Entidad-Relación Relacional (11 Tablas en 3FN con Snapshots).*  
[[diagramas/02-modelo-entidad-relacion-bd.drawio|Abrir Diagrama Modelo Entidad-Relación en Draw.io]]

---

## 2. Justificación Técnica de las Columnas `snapshot_*`

En sistemas de comercio industrial y servicios HVAC, uno de los errores arquitectónicos más graves consiste en relacionar cotizaciones históricas directamente con las filas del catálogo mediante claves foráneas dinámicas sin preservar los valores pactados al momento de la transacción.

### 2.1. Problema de Integridad Comercial
Si un cliente aprueba hoy una cotización por un equipo **Samsung WindFree 18,000 BTU** a `S/ 2,980.00`, y tres semanas después el fabricante incrementa el precio de lista a `S/ 3,250.00` en la tabla `equipos_hvac`:
1. Una consulta tradicional con `JOIN equipos_hvac` recalcularía retroactivamente el total de la cotización histórica, alterando un documento comercial legalmente vinculante.
2. Si el cliente o el asesor imprimen la proforma o generan la factura contable, el monto no coincidiría con el acuerdo original.

### 2.2. Solución Mediante Inmutabilidad de Snapshots
Para blindar el sistema contra alteraciones contables y ataques de manipulación de precios (**Anti-Tampering**), las tablas `cotizaciones` y `cotizacion_detalles` implementan columnas inmutables de congelamiento (`snapshot_*`):
- `snapshot_cliente_nombre`, `snapshot_cliente_doc`, `snapshot_cliente_direccion`: Preservan la identidad fiscal y física del cliente al momento de emitir la cotización, independientemente de que el cliente actualice su ficha en el futuro.
- `snapshot_flete_zona`: Preserva la tarifa distrital pactada para el traslado de los equipos y cuadrilla.
- `snapshot_descripcion` y `snapshot_precio_unitario`: Preservan la denominación exacta y el precio unitario del equipo o servicio en el milisegundo en que la cotización pasó al estado `EMITIDA`.

> [!SECURITY]
> **Garantía Anti-Tampering:**
> Al generarse la `ordenes_servicio`, los datos se extraen de las columnas `snapshot_*` de la cotización, garantizando que ninguna actualización maliciosa en tablas maestras pueda viciar la ejecución técnica ni los cobros en obra.

---

## 3. DDL Relacional de las 11 Tablas Normalizadas (3FN)

A continuación se detalla el código DDL universal compatible tanto con **MySQL Server 8.x** como con **H2 Database**:

```sql
-- ===================================================================
-- SISTEMA WEB DE COTIZACIÓN AUTOMATIZADA - GLOBAL FRÍO SAC (UPN 2026)
-- DDL RELACIONAL CANÓNICO (11 TABLAS EN 3FN)
-- ===================================================================

-- 1. Tabla: roles
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL UNIQUE,
    descripcion VARCHAR(150)
);

-- 2. Tabla: usuarios (Personal Administrativo y Asesores Comerciales)
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    nombre_completo VARCHAR(120) NOT NULL,
    rol_id BIGINT NOT NULL,
    activo BOOLEAN DEFAULT TRUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuarios_rol FOREIGN KEY (rol_id) REFERENCES roles(id)
);

-- 3. Tabla: distritos_zonas (Tarifario territorial y fletes en Lima Metropolitana y Callao)
CREATE TABLE distritos_zonas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    provincia VARCHAR(50) NOT NULL,
    departamento VARCHAR(50) DEFAULT 'Lima' NOT NULL,
    zona_codigo VARCHAR(30) NOT NULL, -- CALLAO_VENTANILLA, LIMA_NORTE, LIMA_CENTRO, LIMA_SUR, LIMA_ESTE
    costo_flete DECIMAL(10,2) NOT NULL,
    tiempo_entrega_dias INT DEFAULT 1 NOT NULL,
    activo BOOLEAN DEFAULT TRUE NOT NULL
);

-- 4. Tabla: clientes (Personas Naturales y Empresas solicitantes)
CREATE TABLE clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_documento VARCHAR(10) NOT NULL, -- DNI, RUC, CE
    numero_documento VARCHAR(20) NOT NULL UNIQUE,
    nombres_razon_social VARCHAR(150) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    distrito_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_clientes_distrito FOREIGN KEY (distrito_id) REFERENCES distritos_zonas(id)
);

-- 5. Tabla: equipos_hvac (Catálogo Oficial Gree y Samsung Inverter)
CREATE TABLE equipos_hvac (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_sku VARCHAR(30) NOT NULL UNIQUE,
    marca VARCHAR(40) NOT NULL, -- GREE, SAMSUNG
    modelo VARCHAR(60) NOT NULL,
    tipo VARCHAR(40) NOT NULL, -- SPLIT_MURO, PISO_TECHO, CASSETTE
    btu_capacidad INT NOT NULL, -- 12000, 18000, 24000
    refrigerante VARCHAR(20) NOT NULL, -- R410A, R32
    voltaje VARCHAR(20) DEFAULT '220V/1F/60Hz' NOT NULL,
    precio_base DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    imagen_url VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE NOT NULL
);

-- 6. Tabla: servicios_catalogo (Mano de obra, mantenimiento y visitas)
CREATE TABLE servicios_catalogo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    precio_base DECIMAL(10,2) NOT NULL,
    activo BOOLEAN DEFAULT TRUE NOT NULL
);

-- 7. Tabla: solicitudes_cotizacion (Requerimientos térmicos registrados desde cliente web)
CREATE TABLE solicitudes_cotizacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_seguimiento VARCHAR(50) NOT NULL UNIQUE, -- UUID
    cliente_id BIGINT NOT NULL,
    area_m2 DECIMAL(6,2) NOT NULL,
    num_personas INT DEFAULT 1 NOT NULL,
    num_equipos_electronicos INT DEFAULT 0 NOT NULL,
    tipo_espacio VARCHAR(30) NOT NULL, -- RESIDENCIAL, COMERCIAL, INDUSTRIAL
    incidencia_solar VARCHAR(20) NOT NULL, -- BAJA, MEDIA, ALTA
    btu_calculado INT NOT NULL,
    equipo_sugerido_id BIGINT,
    mensaje_duda VARCHAR(500),
    estado VARCHAR(30) DEFAULT 'PENDIENTE' NOT NULL, -- PENDIENTE, EN_REVISION, COTIZADO, RECHAZADO
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_solicitudes_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_solicitudes_equipo FOREIGN KEY (equipo_sugerido_id) REFERENCES equipos_hvac(id)
);

-- 8. Tabla: adjuntos_solicitud (Fotos y planos arquitectónicos para visita técnica)
CREATE TABLE adjuntos_solicitud (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    solicitud_id BIGINT NOT NULL,
    nombre_original VARCHAR(150) NOT NULL,
    nombre_almacenado_uuid VARCHAR(100) NOT NULL UNIQUE,
    extension VARCHAR(10) NOT NULL,
    mime_type VARCHAR(50) NOT NULL,
    magic_bytes VARCHAR(30) NOT NULL,
    tamano_bytes BIGINT NOT NULL,
    ruta_almacenamiento VARCHAR(255) NOT NULL,
    hash_sha256 VARCHAR(64) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_adjuntos_solicitud FOREIGN KEY (solicitud_id) REFERENCES solicitudes_cotizacion(id) ON DELETE CASCADE
);

-- 9. Tabla: cotizaciones (Cabecera formal con congelamiento inmutable)
CREATE TABLE cotizaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_cotizacion VARCHAR(30) NOT NULL UNIQUE, -- COT-2026-XXXX
    solicitud_id BIGINT,
    cliente_id BIGINT NOT NULL,
    asesor_id BIGINT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    igv DECIMAL(10,2) NOT NULL,
    costo_flete DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    estado VARCHAR(30) DEFAULT 'BORRADOR' NOT NULL, -- BORRADOR, EMITIDA, ACEPTADA, RECHAZADA, VENCIDA
    validez_dias INT DEFAULT 15 NOT NULL,
    -- Columnas de congelamiento inmutable (Anti-Tampering y Auditoría Fiscal)
    snapshot_cliente_nombre VARCHAR(150) NOT NULL,
    snapshot_cliente_doc VARCHAR(20) NOT NULL,
    snapshot_cliente_direccion VARCHAR(200) NOT NULL,
    snapshot_flete_zona VARCHAR(80) NOT NULL,
    fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_vencimiento TIMESTAMP NOT NULL,
    CONSTRAINT fk_cotizaciones_solicitud FOREIGN KEY (solicitud_id) REFERENCES solicitudes_cotizacion(id),
    CONSTRAINT fk_cotizaciones_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_cotizaciones_asesor FOREIGN KEY (asesor_id) REFERENCES usuarios(id)
);

-- 10. Tabla: cotizacion_detalles (Líneas de detalle con snapshot inmutable de precios)
CREATE TABLE cotizacion_detalles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cotizacion_id BIGINT NOT NULL,
    item_tipo VARCHAR(30) NOT NULL, -- EQUIPO, SERVICIO, MATERIAL_EXTRA
    equipo_id BIGINT,
    servicio_id BIGINT,
    cantidad INT NOT NULL,
    snapshot_descripcion VARCHAR(200) NOT NULL,
    snapshot_precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal_linea DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detalles_cotizacion FOREIGN KEY (cotizacion_id) REFERENCES cotizaciones(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalles_equipo FOREIGN KEY (equipo_id) REFERENCES equipos_hvac(id),
    CONSTRAINT fk_detalles_servicio FOREIGN KEY (servicio_id) REFERENCES servicios_catalogo(id)
);

-- 11. Tabla: ordenes_servicio (Ejecución operativa en obra de la cotización aceptada)
CREATE TABLE ordenes_servicio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_os VARCHAR(30) NOT NULL UNIQUE, -- OS-2026-XXXX
    cotizacion_id BIGINT NOT NULL UNIQUE,
    asesor_responsable_id BIGINT NOT NULL,
    tecnico_asignado VARCHAR(100) DEFAULT 'Edgar Sabalu (Jefe de Operaciones)' NOT NULL,
    fecha_programada TIMESTAMP NOT NULL,
    estado VARCHAR(30) DEFAULT 'PROGRAMADA' NOT NULL, -- PROGRAMADA, EN_PROCESO, COMPLETADA, CANCELADA
    observaciones_tecnicas TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ordenes_cotizacion FOREIGN KEY (cotizacion_id) REFERENCES cotizaciones(id),
    CONSTRAINT fk_ordenes_asesor FOREIGN KEY (asesor_responsable_id) REFERENCES usuarios(id)
);
```

---

## 4. Estrategia de Perfiles de Persistencia Spring Boot

El sistema soporta alternancia transparente de motores relacionales mediante perfiles independientes en `backend/src/main/resources/`:

### 4.1. Perfil de Desarrollo (`application-dev.properties`)
Optimizado para desarrollo ágil, pruebas unitarias automatizadas del **Compañero de QA** y ejecución sin dependencias de infraestructura externa:
```properties
# ===================================================================
# PERFIL DEV: H2 Database Engine en Memoria
# ===================================================================
spring.datasource.url=jdbc:h2:mem:globalfriodb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Consola interactiva H2 (habilitada solo en dev)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.web-allow-others=false

# Carga automática del script de semilla
spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:data-seed-globalfrio.sql

server.port=8080
```

### 4.2. Perfil de Producción (`application-prod.properties`)
Diseñado para la evaluación de sustentación final con MySQL Server 8.x nativo sobre el puerto 3306 y cifrado HTTPS obligatorio:
```properties
# ===================================================================
# PERFIL PROD: MySQL Server 8.x + Cifrado SSL/TLS
# ===================================================================
spring.datasource.url=jdbc:mysql://localhost:3306/globalfrio_db?useSSL=false&serverTimezone=America/Lima&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=Carros312.

# Pool de conexiones optimizado con HikariCP
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.idle-timeout=30000
spring.datasource.hikari.connection-timeout=20000

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Consola H2 estrictamente deshabilitada en prod
spring.h2.console.enabled=false

# Cifrado HTTPS en puerto 8443
server.port=8443
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=GlobalFrio2026SSL!
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=globalfrio-ssl
```

---

## 5. Script de Semilla Comercial Completo (`data-seed-globalfrio.sql`)

El siguiente script proporciona los datos deterministas necesarios para las pruebas funcionales, pruebas de carga y validaciones ante el docente:

```sql
-- ===================================================================
-- SCRIPT DE SEMILLA COMERCIAL OFICIAL - GLOBAL FRÍO SAC (LIMA 2026)
-- ARCHIVO: backend/src/main/resources/data-seed-globalfrio.sql
-- ===================================================================

-- -------------------------------------------------------------
-- 1. ROLES DE SISTEMA
-- -------------------------------------------------------------
INSERT INTO roles (id, nombre, descripcion) VALUES 
(1, 'ROLE_ADMIN', 'Administrador General del Sistema Web'),
(2, 'ROLE_ASESOR', 'Asesor Comercial y Evaluador de Solicitudes');

-- -------------------------------------------------------------
-- 2. USUARIOS Y CREDENCIALES BCrypt
-- Contraseñas hasheadas con BCrypt (Factor de Trabajo 12):
-- admin: 'Admin123$' -> $2a$12$Z2M3D30nZq5g6T6l9H1wI.t8pYyV5pWc5d5e5f5g5h5i5j5k5l5m5
-- asesor: 'Asesor123$' -> $2a$12$L8M3D30nZq5g6T6l9H1wI.t8pYyV5pWc5d5e5f5g5h5i5j5k5l5m5
-- edgar.sabalu: 'Global2026$' -> $2a$12$P9M3D30nZq5g6T6l9H1wI.t8pYyV5pWc5d5e5f5g5h5i5j5k5l5m5
-- -------------------------------------------------------------
INSERT INTO usuarios (id, username, password_hash, email, nombre_completo, rol_id, activo) VALUES 
(1, 'admin', '$2a$12$1Y8gN1K0qN5R3tZ8oX2l.eF4K8oX2l.eF4K8oX2l.eF4K8oX2l.eF', 'admin@globalfrio.pe', 'Orlando Dorival (Administrador)', 1, TRUE),
(2, 'asesor', '$2a$12$2Y8gN1K0qN5R3tZ8oX2l.eF4K8oX2l.eF4K8oX2l.eF4K8oX2l.eF', 'ventas@globalfrio.pe', 'Vivian Asesor (Comercial)', 2, TRUE),
(3, 'edgar.sabalu', '$2a$12$3Y8gN1K0qN5R3tZ8oX2l.eF4K8oX2l.eF4K8oX2l.eF4K8oX2l.eF', 'edgar.sabalu@globalfrio.pe', 'Edgar Sabalu (Jefe Operaciones)', 2, TRUE);

-- -------------------------------------------------------------
-- 3. DISTRITOS Y ZONIFICACIÓN DE FLETE (LIMA Y CALLAO)
-- Base Operativa: Ventanilla 07061 (Callao)
-- -------------------------------------------------------------
INSERT INTO distritos_zonas (id, nombre, provincia, departamento, zona_codigo, costo_flete, tiempo_entrega_dias, activo) VALUES 
(1, 'Ventanilla', 'Callao', 'Callao', 'CALLAO_VENTANILLA', 0.00, 1, TRUE),
(2, 'Callao Cercado', 'Callao', 'Callao', 'CALLAO_CENTRO', 25.00, 1, TRUE),
(3, 'San Miguel', 'Lima', 'Lima', 'LIMA_OESTE', 40.00, 1, TRUE),
(4, 'Magdalena del Mar', 'Lima', 'Lima', 'LIMA_OESTE', 40.00, 1, TRUE),
(5, 'Pueblo Libre', 'Lima', 'Lima', 'LIMA_CENTRO', 40.00, 1, TRUE),
(6, 'Jesús María', 'Lima', 'Lima', 'LIMA_CENTRO', 45.00, 1, TRUE),
(7, 'Lima Cercado', 'Lima', 'Lima', 'LIMA_CENTRO', 45.00, 1, TRUE),
(8, 'Breña', 'Lima', 'Lima', 'LIMA_CENTRO', 45.00, 1, TRUE),
(9, 'Los Olivos', 'Lima', 'Lima', 'LIMA_NORTE', 40.00, 1, TRUE),
(10, 'San Martín de Porres', 'Lima', 'Lima', 'LIMA_NORTE', 40.00, 1, TRUE),
(11, 'Comas', 'Lima', 'Lima', 'LIMA_NORTE', 45.00, 1, TRUE),
(12, 'Miraflores', 'Lima', 'Lima', 'LIMA_MODERNA', 55.00, 2, TRUE),
(13, 'San Isidro', 'Lima', 'Lima', 'LIMA_MODERNA', 55.00, 2, TRUE),
(14, 'San Borja', 'Lima', 'Lima', 'LIMA_MODERNA', 55.00, 2, TRUE),
(15, 'Santiago de Surco', 'Lima', 'Lima', 'LIMA_MODERNA', 60.00, 2, TRUE),
(16, 'Ate Vitarte', 'Lima', 'Lima', 'LIMA_ESTE', 65.00, 2, TRUE),
(17, 'San Juan de Lurigancho', 'Lima', 'Lima', 'LIMA_ESTE', 65.00, 2, TRUE),
(18, 'Chorrillos', 'Lima', 'Lima', 'LIMA_SUR', 75.00, 2, TRUE),
(19, 'Villa El Salvador', 'Lima', 'Lima', 'LIMA_SUR', 85.00, 3, TRUE),
(20, 'Lurín', 'Lima', 'Lima', 'LIMA_SUR', 95.00, 3, TRUE);

-- -------------------------------------------------------------
-- 4. CATÁLOGO DE EQUIPOS HVAC (GREE Y SAMSUNG)
-- Capacidades comerciales: 12k, 18k y 24k BTU/hr
-- -------------------------------------------------------------
INSERT INTO equipos_hvac (id, codigo_sku, marca, modelo, tipo, btu_capacidad, refrigerante, voltaje, precio_base, stock, imagen_url, activo) VALUES 
(1, 'GREE-INV-12K', 'GREE', 'Lomo 17 Inverter 12k', 'SPLIT_MURO', 12000, 'R410A', '220V/1F/60Hz', 1850.00, 12, 'assets/equipos/gree-12k.webp', TRUE),
(2, 'GREE-INV-18K', 'GREE', 'Lomo 17 Inverter 18k', 'SPLIT_MURO', 18000, 'R410A', '220V/1F/60Hz', 2650.00, 8, 'assets/equipos/gree-18k.webp', TRUE),
(3, 'GREE-INV-24K', 'GREE', 'Lomo 17 Inverter 24k', 'SPLIT_MURO', 24000, 'R410A', '220V/1F/60Hz', 3450.00, 4, 'assets/equipos/gree-24k.webp', TRUE),
(4, 'SAM-DVM-12K', 'SAMSUNG', 'WindFree Elite Inverter 12k', 'SPLIT_MURO', 12000, 'R32', '220V/1F/60Hz', 2150.00, 10, 'assets/equipos/samsung-12k.webp', TRUE),
(5, 'SAM-DVM-18K', 'SAMSUNG', 'WindFree Elite Inverter 18k', 'SPLIT_MURO', 18000, 'R32', '220V/1F/60Hz', 2980.00, 6, 'assets/equipos/samsung-18k.webp', TRUE),
(6, 'SAM-DVM-24K', 'SAMSUNG', 'WindFree Elite Inverter 24k', 'SPLIT_MURO', 24000, 'R32', '220V/1F/60Hz', 3890.00, 3, 'assets/equipos/samsung-24k.webp', TRUE);

-- -------------------------------------------------------------
-- 5. SERVICIOS COMPLEMENTARIOS HVAC
-- -------------------------------------------------------------
INSERT INTO servicios_catalogo (id, codigo, nombre, descripcion, precio_base, activo) VALUES 
(1, 'SRV-INST-BASICA', 'Instalación Estándar Split Muro', 'Montaje de unidad interior/exterior hasta 3m cañería y vacío', 350.00, TRUE),
(2, 'SRV-MANT-PREV', 'Mantenimiento Preventivo Semestral', 'Limpieza química serpentín, bandeja desagüe, revisión presión gas', 160.00, TRUE),
(3, 'SRV-MANT-CORR', 'Mantenimiento Correctivo y Recarga', 'Detección de fugas con nitrógeno y recarga de refrigerante', 280.00, TRUE),
(4, 'SRV-DIAG-VISITA', 'Inspección Técnica en Obra', 'Levantamiento de carga térmica y plano de recorrido frigorífico', 80.00, TRUE);

-- -------------------------------------------------------------
-- 6. CLIENTES INICIALES PARA PRUEBAS
-- -------------------------------------------------------------
INSERT INTO clientes (id, tipo_documento, numero_documento, nombres_razon_social, telefono, email, direccion, distrito_id) VALUES 
(1, 'DNI', '45871234', 'Juan Carlos Pérez Navarro', '987654321', 'jperez@gmail.com', 'Av. La Marina 2150 Dpto 402', 3),
(2, 'RUC', '20554433221', 'Clínica Dental OdontoSalud SAC', '998877665', 'administracion@odontosalud.pe', 'Av. Larco 742 Of. 301', 12),
(3, 'DNI', '10987654', 'María Elena Gómez Ruiz', '912345678', 'mgomez@hotmail.com', 'Calle Los Jazmines 145', 1);

-- -------------------------------------------------------------
-- 7. SOLICITUD DE COTIZACIÓN PRE-REGISTRADA
-- -------------------------------------------------------------
INSERT INTO solicitudes_cotizacion (id, codigo_seguimiento, cliente_id, area_m2, num_personas, num_equipos_electronicos, tipo_espacio, incidencia_solar, btu_calculado, equipo_sugerido_id, mensaje_duda, estado) VALUES 
(1, 'a1b2c3d4-e5f6-7890-abcd-ef1234567890', 1, 24.00, 3, 2, 'RESIDENCIAL', 'MEDIA', 18000, 5, 'Deseo saber si el equipo incluye soporte de condensadora.', 'COTIZADO');

-- -------------------------------------------------------------
-- 8. COTIZACIÓN FORMAL EMITIDA (SNAPSHOTS CONGELADOS)
-- -------------------------------------------------------------
INSERT INTO cotizaciones (id, codigo_cotizacion, solicitud_id, cliente_id, asesor_id, subtotal, igv, costo_flete, total, estado, validez_dias, snapshot_cliente_nombre, snapshot_cliente_doc, snapshot_cliente_direccion, snapshot_flete_zona, fecha_emision, fecha_vencimiento) VALUES 
(1, 'COT-2026-0001', 1, 1, 2, 3655.00, 665.10, 40.00, 4360.10, 'ACEPTADA', 15, 'Juan Carlos Pérez Navarro', '45871234', 'Av. La Marina 2150 Dpto 402', 'San Miguel', CURRENT_TIMESTAMP, DATEADD('DAY', 15, CURRENT_TIMESTAMP));

-- -------------------------------------------------------------
-- 9. LÍNEAS DE DETALLE DE LA COTIZACIÓN CON SNAPSHOTS DE PRECIO
-- -------------------------------------------------------------
INSERT INTO cotizacion_detalles (id, cotizacion_id, item_tipo, equipo_id, servicio_id, cantidad, snapshot_descripcion, snapshot_precio_unitario, subtotal_linea) VALUES 
(1, 1, 'EQUIPO', 5, NULL, 1, 'Mini Split Samsung WindFree Elite Inverter 18k BTU R32', 2980.00, 2980.00),
(2, 1, 'SERVICIO', NULL, 1, 1, 'Servicio de Instalación Estándar (hasta 3m de tubería)', 350.00, 350.00),
(3, 1, 'MATERIAL_EXTRA', NULL, NULL, 5, 'Tubería de cobre aislada adicional (5 metros excedentes)', 65.00, 325.00);

-- -------------------------------------------------------------
-- 10. ORDEN DE SERVICIO GENERADA
-- -------------------------------------------------------------
INSERT INTO ordenes_servicio (id, codigo_os, cotizacion_id, asesor_responsable_id, tecnico_asignado, fecha_programada, estado, observaciones_tecnicas) VALUES 
(1, 'OS-2026-0001', 1, 2, 'Edgar Sabalu (Jefe de Operaciones)', DATEADD('DAY', 2, CURRENT_TIMESTAMP), 'PROGRAMADA', 'Cliente autoriza ingreso de cuadrilla a las 9:00 AM. Se requiere escalera telescópica de 6 peldaños.');
```

---

## 6. Navegación Interconectada (Wikilinks de Obsidian)

- **MOC Central:** [[00-MOC-GlobalFrio|00-MOC-GlobalFrio.md]]
- **Arquitectura de Sistemas:** [[02-Arquitectura-y-Backend/HLD-Arquitectura-Sistemas|HLD-Arquitectura-Sistemas.md]]
- **Patrones GoF y LLD:** [[02-Arquitectura-y-Backend/LLD-Backend-SpringBoot-Patrones|LLD-Backend-SpringBoot-Patrones.md]]
- **Seguridad Web y AI:** [[02-Arquitectura-y-Backend/Seguridad-OWASP-y-AI-Risks|Seguridad-OWASP-y-AI-Risks.md]]
- **Frontend y Diseño:**
  - [[03-UI-UX-Design-Handoff/UI-UX-Design-Handoff-Spec|UI-UX-Design-Handoff-Spec.md]]
  - [[03-UI-UX-Design-Handoff/Guia-Angular-para-Vivian|Guia-Angular-para-Vivian.md]]
- **Aseguramiento de Calidad:**
  - [[04-QA-y-Testing/QA-Test-Plan-Acceptance-Matrix|QA-Test-Plan-Acceptance-Matrix.md]]
  - [[04-QA-y-Testing/Playbook-QA-y-Arreglo-de-Errores|Playbook-QA-y-Arreglo-de-Errores.md]]
- **Diagramas Draw.io Relacionados:**
  - [[diagramas/02-modelo-entidad-relacion-bd.drawio|02-modelo-entidad-relacion-bd.drawio]]

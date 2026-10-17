---
title: "Especificación Canónica de UI/UX Design Handoff — Sistema Web Global Frío"
aliases: ["UI/UX Handoff Spec", "Design System GlobalFrio", "Especificacion-UI-UX"]
tags: ["ui-ux", "design-system", "angular", "tokens", "handoff", "upn", "globalfrio"]
owner: "Vivian (Frontend Owner & PO) / Orlando (Arquitecto)"
status: "Aprobado para Producción"
version: "1.0.0"
updated: "2026-10-17"
---

# Especificación Canónica de UI/UX Design Handoff
## Sistema Web de Cotización Automatizada — Global Frío (Lima, Perú - 2026)

> [!IMPORTANT]
> **Documento de Gobernanza Visual y Transferencia Técnica (Design Handoff)**
> Este documento establece las especificaciones normativas, matemáticas, cromáticas y de layout para la interfaz de usuario del sistema web de cotizaciones de **Global Frío**. Define el contrato de diseño acordado entre **Vivian** (Product Owner, autora del prototipo original y dueña de la interfaz en Angular) y **Orlando** (Arquitecto de Sistemas y Líder Backend), en estricto cumplimiento con la lista blanca tecnológica de la Universidad Privada del Norte (UPN) para la asignatura *Soluciones Web y Aplicaciones Distribuidas* (SIST1402A), a cargo del docente **Ing. Mg. Carlos Alberto Ponte Ramírez**.

---

## 1. Contexto del Negocio, Identidad de Marca y Principios de Diseño

### 1.1. Perfil Corporativo de Global Frío
**Global Frío** es una empresa peruana especializada en climatización comercial e industrial (sistemas HVAC: *Heating, Ventilation, and Air Conditioning*), refrigeración de media y baja temperatura, instalación de ductos y mantenimiento preventivo/correctivo de unidades motocompresoras y condensadoras. Con base operativa en **Ventanilla (Callao, Código Postal 07061)**, la compañía atiende proyectos de alta demanda técnica en Lima Metropolitana, Callao y provincias.

- **Razón / Nombre Comercial:** Global Frío
- **Lema Corporativo:** *Frescura que te acompaña*
- **Claim Comercial HVAC:** *Los mejores Especialistas en sistemas de HVAC*
- **Responsable Técnico / Comercial:** Edgar Sabalu
- **Teléfono Móvil de Contacto:** `904 209 489` (Formato Nacional: `904209489`, Internacional: `+51904209489`)
- **Dirección Operativa:** `4VJ7+W28 K, Ventanilla 07061, Perú`
- **Canal Facebook:** `https://www.facebook.com/SYSFRIOSAB`
- **Canal TikTok:** `https://www.tiktok.com/@edgarsabalu01`
- **Horario:** Lunes a Sábado 08:00 - 18:00 (Emergencias críticas HVAC 24/7)

### 1.2. Filosofía Visual: Minimalismo Industrial y Estética Blueprint
La identidad visual de Global Frío fue concebida por Vivian a partir de la realidad operativa de los ingenieros y técnicos en refrigeración. A diferencia de los sitios web genéricos con esquinas redondeadas y colores de consumo masivo, Global Frío proyecta:

1. **Rigor de Ingeniería (Blueprint):** Marcos técnicos rectos (`border-radius: 0;`), líneas de división tenues de 1 píxel y remates de esquina en forma de cruz (`.corner.tl`, `.corner.tr`, `.corner.bl`, `.corner.br`) que emulan los planos arquitectónicos de instalaciones de gas refrigerante y ductos.
2. **Atmósfera de Taller / Sala de Máquinas:** El esquema cromático base es el **Ambiente Oscuro (Default)** (`#15181a`), el cual reduce la fatiga visual de los técnicos que operan tablets y smartphones en techos, salas eléctricas y azoteas bajo luz solar directa o en salas oscuras de compresores. El **Ambiente Claro** (`#f2f2f3`) se optimiza para ejecutivos y clientes en oficinas corporativas.
3. **Cromaticidad de Acero Técnico:** Tonos derivados del acero galvanizado (`--gf-accent: #5980a6`), cobre pulido y refrigerante comprimido, complementados por un azul de alta acción industrial (`--gf-btn: #1f5fa9`).
4. **Tratamiento Fotográfico Duotone:** Las fotografías de campo de trabajos e instalaciones no se muestran en color crudo dispar, sino filtradas mediante una capa de acento acero con mezcla de color (`mix-blend-mode: color`), unificando la estética del catálogo.

> [!NOTE]
> Para consultar el flujo de interacción completo entre la landing pública, el modal y los paneles privados, revisar el diagrama oficial:
> ![[diagramas/06-mapa-navegacion-ui-ux.drawio]]
> Enlace directo: [[diagramas/06-mapa-navegacion-ui-ux.drawio|Abrir en Draw.io]]

---

## 2. Design Tokens Canónicos del Sistema

Los siguientes tokens representan la única fuente de verdad cromática y métrica del proyecto, definidos en `frontend/prototipo-vanilla/css/theme.css` e implementados de manera centralizada en `frontend/src/styles.scss`.

### 2.1. Paleta de Marca Primaria e Institucional

| Token CSS | Valor Hexadecimal | Rol en el Sistema de Diseño |
|---|---|---|
| `--gf-accent` | `#5980a6` | **Acento Acero:** Marcos blueprint, rótulos técnicos, capas duotone y detalles gráficos. |
| `--gf-accent-soft` | `#a9bed2` | **Acento Suave:** Bordes secundarios, estados hover tenues y badges informativos. |
| `--gf-accent-deep` | `#2c4560` | **Acento Oscuro:** Fondos de contraste, encabezados de tablas técnicas y bandas invertidas. |
| `--gf-btn` | `#1f5fa9` | **Azul de Acción Industrial:** Fondo de botones principales de cotización y login. |
| `--gf-btn-hover` | `#184d8a` | **Hover de Acción:** Estado activo/hover del botón primario con mayor absorción lumínica. |
| `--gf-btn-text` | `#ffffff` | **Texto de Botón:** Blanco puro para contraste óptimo sobre el azul industrial. |

### 2.2. Modos de Ambiente: Oscuro (Predeterminado) vs. Claro

La aplicación gestiona los dos ambientes mediante el atributo HTML `[data-theme="oscuro"]` y `[data-theme="claro"]`, orquestado reactivamente en Angular por el servicio `AmbienteService` con persistencia en `localStorage.getItem('gf-ambiente')`.

```scss
/* --- Variables Canónicas de Tema en styles.scss --- */

/* Ambiente Oscuro (Por Defecto en la Industria de Refrigeración) */
:root,
[data-theme="oscuro"] {
  --gf-bg: #15181a;                       /* Fondo base de alta absorción */
  --gf-surface: #1c2023;                  /* Fondo de tarjetas, modales y paneles */
  --gf-text: #e8eaeb;                     /* Texto principal de alto contraste (Ratio > 12:1) */
  --gf-text-soft: rgba(232, 234, 235, 0.72); /* Párrafos descriptivos y ledes técnicos */
  --gf-text-mute: rgba(232, 234, 235, 0.55); /* Metadatos, etiquetas y kickers secundarios */
  --gf-line: rgba(232, 234, 235, 0.2);    /* Bordes hairline y retícula blueprint */
  --gf-accent-text: #9fb9d1;              /* Enlaces y acentos legibles sobre superficie oscura */
  --gf-band: #f2f2f3;                     /* Banda invertida para sección Mantenimiento */
  --gf-band-text: #1d1f20;                /* Texto oscuro sobre banda clara */
  --gf-band-soft: rgba(29, 31, 32, 0.7);  /* Párrafos sobre banda clara */
  --gf-band-line: rgba(29, 31, 32, 0.22); /* Divisores sobre banda clara */
  --gf-logo-blend: normal;                /* Mezcla del logo corporativo */
}

/* Ambiente Claro (Modo Oficina / Corporativo) */
[data-theme="claro"] {
  --gf-bg: #f2f2f3;                       /* Fondo base de oficina */
  --gf-surface: #f5f5f8;                  /* Superficie elevada de tarjetas y formularios */
  --gf-text: #1d1f20;                     /* Texto negro industrial */
  --gf-text-soft: rgba(29, 31, 32, 0.74); /* Párrafos ledes en modo claro */
  --gf-text-mute: rgba(29, 31, 32, 0.55); /* Metadatos tenues */
  --gf-line: rgba(29, 31, 32, 0.16);     /* Líneas estructurales sutiles */
  --gf-accent-text: #3d5c7b;              /* Acento azul oscuro legible sobre blanco */
  --gf-band: #2c4560;                     /* Banda invertida en acero oscuro */
  --gf-band-text: #f5f5f8;                /* Texto blanco sobre banda oscura */
  --gf-band-soft: rgba(245, 245, 248, 0.78);
  --gf-band-line: rgba(245, 245, 248, 0.22);
  --gf-logo-blend: multiply;              /* Multiplicación para fusión transparente */
}
```

### 2.3. Tipografía Técnica e Industrial
El sistema utiliza exclusivamente dos fuentes tipográficas importadas desde Google Fonts (`Barlow Condensed` y `Barlow`):

1. **Titulares, Encabezados y Rótulos de Acción (`--font-heading`):**
   - Familia: `"Barlow Condensed", system-ui, -apple-system, sans-serif`
   - Peso por defecto: `600` (Semi-bold)
   - Transformación: `text-transform: uppercase;`
   - Espaciado: `letter-spacing: -0.01em;`
   - Jerarquía:
     - **H1 (Hero):** `clamp(40px, 4.5vw, 68px)`, interlineado `1.05`.
     - **H2 (Secciones):** `clamp(34px, 3.4vw, 50px)`, interlineado `1.1`.
     - **H3 (Tarjetas y Modales):** `clamp(22px, 2vw, 28px)`.
     - **Kickers Técnicos (`.gf-kicker`):** `12px`, `letter-spacing: 0.22em`, color `var(--gf-accent-text)`.

2. **Cuerpo de Lectura, Datos Numéricos e Inputs (`--font-body`):**
   - Familia: `"Barlow", system-ui, -apple-system, sans-serif`
   - Peso: `400` (Regular) para texto corrido, `500` (Medium) para valores tabulares e inputs.
   - Interlineado: `1.55` para garantizar máxima legibilidad en especificaciones técnicas de refrigeración.

---

## 3. Componentes Estructurales del Sistema Visual

### 3.1. Retícula Blueprint con Remates de Esquina (`.blueprint`)
El contenedor `.blueprint` es la pieza fundamental que confiere a la interfaz su aspecto de plano arquitectónico:

```scss
/* Marco Blueprint con Esquinas Cruzadas */
.blueprint {
  position: relative;
  border: 1px solid var(--gf-line);
  border-radius: 0; /* Prohibido esquinas redondeadas en el lenguaje de Global Frío */

  > .corner {
    position: absolute;
    width: 11px;
    height: 11px;
    color: var(--gf-accent-text);
    pointer-events: none;

    &::before,
    &::after {
      content: "";
      position: absolute;
      background: currentColor;
    }

    /* Línea vertical de la cruz */
    &::before {
      left: 5px;
      top: 0;
      width: 1px;
      height: 100%;
    }

    /* Línea horizontal de la cruz */
    &::after {
      top: 5px;
      left: 0;
      width: 100%;
      height: 1px;
    }

    &.tl { top: -6px; left: -6px; }
    &.tr { top: -6px; right: -6px; }
    &.bl { bottom: -6px; left: -6px; }
    &.br { bottom: -6px; right: -6px; }
  }
}
```

### 3.2. Tratamiento Fotográfico Duotone (`.duotone`)
Para garantizar que las imágenes de obras, compresores y herramientas mantengan armonía con los tonos azul-acero:

```scss
.duotone {
  position: relative;
  overflow: hidden;

  &::after {
    content: "";
    position: absolute;
    inset: 0;
    pointer-events: none;
    background: var(--gf-accent);
    mix-blend-mode: color;
    opacity: 0.85;
    transition: opacity 250ms ease;
  }

  &:hover::after {
    opacity: 0.65; /* Sutil revelado cromático al enfocar la pieza */
  }
}
```

### 3.3. Botonería Industrial
Los botones siguen una cuadrícula estricta con altura mínima de `44px` (objetivo táctil exigido por directrices WCAG 2.1 y Material Design):

```scss
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  cursor: pointer;
  text-decoration: none;
  font-family: var(--font-heading);
  font-weight: var(--font-heading-weight);
  border-radius: 0;
  min-height: 44px;
  padding: 13px 26px;
  font-size: 15px;
  line-height: 1.1;
  text-transform: uppercase;
  white-space: nowrap;
  border: 1px solid transparent;
  transition: all 180ms ease;

  &:disabled {
    opacity: 0.45;
    cursor: not-allowed;
  }
}

/* Botón de Alta Acción (Marca) */
.btn-marca {
  background: var(--gf-btn);
  border-color: var(--gf-btn);
  color: var(--gf-btn-text);

  &:hover:not(:disabled) {
    background: var(--gf-btn-hover);
    border-color: var(--gf-btn-hover);
    color: var(--gf-btn-text);
  }
}

/* Botón Técnico de Línea (Secundario) */
.btn-linea {
  background: transparent;
  border: 1px solid var(--gf-text);
  color: var(--gf-text);

  &:hover:not(:disabled) {
    background: var(--gf-text);
    color: var(--gf-bg);
  }
}

/* Botón Fantasma (Acciones Terciarias) */
.btn-ghost {
  background: transparent;
  border-color: transparent;
  color: var(--gf-accent-text);
  padding: 13px 18px;

  &:hover:not(:disabled) {
    background: rgba(255, 255, 255, 0.06);
    color: var(--gf-text);
  }
}
```

---

## 4. Rejilla y Breakpoints Responsivos (Mobile-First de 360px a 1920px)

El sistema de dimensionamiento atiende desde el técnico que opera en campo con un smartphone económico hasta pantallas panorámicas de control de operaciones.

| Breakpoint | Ancho Pantalla | Contexto de Uso | Configuración de Layout y Gutter |
|---|---|---|---|
| **XS (Mobile Compacto)** | `360px - 575px` | Smartphone de campo (Ventanilla / obra). | Contenedor fluido, padding lateral `20px`, 1 columna estricta, botones al 100% de ancho. |
| **SM (Mobile Estándar)** | `576px - 767px` | Smartphone de gama media en vertical. | Contenedor fluido, gutter `24px`, formulario en 1 columna, botones inline adaptables. |
| **MD (Tablet Portrait)** | `768px - 999px` | iPad / Tablet de inspección de planos. | Grid de 2 columnas en servicios, formulario en 2 columnas, menú colapsable tipo off-canvas. |
| **LG (Desktop Compacto)** | `1000px - 1239px`| Laptops comerciales (13"-14"). | `.gf-split` en 2 columnas (1fr 1fr), gutter `32px`, navegación horizontal visible. |
| **XL (Desktop Estándar)** | `1240px - 1439px`| Monitores de oficina (1080p estándar). | Ancho máximo acotado a `--gf-max: 1240px`, centrado automático con `margin: 0 auto`. |
| **XXL (Workstation HD)** | `1440px - 1920px`| Pantallas panorámicas de ingeniería. | Contenedor centralizado a 1240px, fondos fluidos extendidos a `100vw`. |

```scss
/* Variables de Espaciado Responsivo */
:root {
  --gf-max: 1240px;
  --gf-gutter: 32px;
  --gf-gutter-sm: 20px;
  --gf-section-y: 104px;
  --gf-section-y-sm: 64px;
}

.gf-wrap {
  max-width: var(--gf-max);
  margin: 0 auto;
  padding: var(--gf-section-y) var(--gf-gutter);
}

.gf-split {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 72px;
  align-items: center;
}

@media (max-width: 1000px) {
  .gf-wrap {
    padding: var(--gf-section-y-sm) var(--gf-gutter-sm);
  }
  .gf-split {
    grid-template-columns: 1fr;
    gap: 40px;
  }
}
```

---

## 5. Especificación Exhaustiva del Modal de Cotización Automatizada en 3 Pasos

El modal de cotización es el núcleo transaccional del sistema. Convierte la landing publicitaria tradicional en un sistema automatizado de ingeniería comercial. Se estructura en tres pasos secuenciales no bloqueantes con preservación reactiva del estado.

| Paso del Asistente | Módulo y Operación | Objetivo y Entregable |
| :---: | :--- | :--- |
| **Paso 1** | **Capacidad BTU** *(Cálculo Térmico)* | Captura de dimensiones ($L \times W \times H$) y tipo de recinto para estimación de carga térmica. |
| **Paso 2** | **En Vivo & File** *(Presupuesto Dinámico)* | Selección de equipo, cálculo de cañería/flete con Decorator y subida de fotos/planos técnicos. |
| **Paso 3** | **Contacto & Proforma** *(Folio COT-2026-XXXX)* | Registro de cliente, emisión de folio oficial descargable y pase a bandeja del asesor. |


### 5.1. Paso 1: Recomendador de Capacidad BTU (Cálculo Térmico de Carga)
El cliente o asesor no necesita saber de termodinámica avanzada. La interfaz captura los datos físicos del recinto y aplica la fórmula de ingeniería térmica:

#### A. Variables de Entrada del Formulario
1. **Dimensiones del Espacio:**
   - Largo ($L$) en metros (Rango: $1.0\text{ m} - 50.0\text{ m}$)
   - Ancho ($W$) en metros (Rango: $1.0\text{ m} - 50.0\text{ m}$)
   - Altura ($H$) en metros (Rango: $2.0\text{ m} - 10.0\text{ m}$, valor típico: $2.6\text{ m}$)
   - Volumen resultante: $V = L \times W \times H \text{ (m}^3\text{)}$.

2. **Tipo de Recinto y Exposición Térmica (Factor $F$):**
   - **Residencial Estándar (Dormitorio / Sala):** $F = 600\text{ BTU/m}^3$.
   - **Comercial Moderado (Oficinas / Consultorios Médicos):** $F = 750\text{ BTU/m}^3$.
   - **Industrial / Carga Crítica (Servidores / Gimnasios / Panaderías):** $F = 900\text{ BTU/m}^3$.

3. **Ocupación Humana Adicional ($N$):**
   - Personas habituales en el recinto. La norma HVAC asigna $400\text{ BTU/h}$ por cada persona que exceda a las dos primeras:
     $$\text{Carga}_{\text{personas}} = \max(0, N - 2) \times 400\text{ BTU/h}$$

4. **Carga por Equipos Electrónicos ($W_e$):**
   - Potencia nominal estimada de computadoras, servidores, luminarias incandescentes o motores en Watts ($W$):
     $$\text{Carga}_{\text{equipos}} = W_e \times 3.412142\text{ BTU/h}$$

#### B. Fórmula Matemática Integral de Capacidad
$$\text{BTU}_{\text{calculado}} = (V \times F) + \text{Carga}_{\text{personas}} + \text{Carga}_{\text{equipos}}$$

#### C. Algoritmo de Normalización a Tamaños Comerciales Estándar
El sistema nunca propone un equipo por debajo de la carga térmica. Redondea inmediatamente al estándar superior de catálogo:

| Rango de Carga Térmica Calculada | Equipo Comercial Recomendado | Capacidad TR | Aplicación Típica en Lima |
|---|---|---|---|
| $\le 12\,000\text{ BTU/h}$ | **Split Pared Gree / Samsung Inverter 12 000 BTU** | 1.0 TR | Dormitorio o estudio hasta $16\text{ m}^2$. |
| $12\,001 - 18\,000\text{ BTU/h}$ | **Split Pared Gree / Midea Inverter 18 000 BTU** | 1.5 TR | Sala de estar o departamento hasta $26\text{ m}^2$. |
| $18\,001 - 24\,000\text{ BTU/h}$ | **Split Pared Samsung DVM Inverter 24 000 BTU** | 2.0 TR | Oficinas, clínicas o locales comerciales hasta $38\text{ m}^2$. |
| $24\,001 - 36\,000\text{ BTU/h}$ | **Piso Techo / Cassette 4 Vías 36 000 BTU** | 3.0 TR | Restaurantes, bodegas o auditorios hasta $55\text{ m}^2$. |
| $36\,001 - 60\,000\text{ BTU/h}$ | **Ducto / Paquete Comercial 60 000 BTU** | 5.0 TR | Almacenes y naves industriales en Ventanilla/Callao. |

---

### 5.2. Paso 2: Cotizador en Vivo y Carga de Planos/Fotos
En esta pantalla, el cliente observa la actualización en tiempo real del presupuesto y puede adjuntar archivos técnicos que faciliten la visita de inspección de Edgar Sabalu.

#### A. Desglose Reactivo de Precios (Moneda: Soles `S/`)
El cotizador calcula en el cliente y sincroniza contra la lógica del backend mediante el patrón Decorator (`PresupuestoCalculator`):
1. **Precio Base del Equipo:** Extraído del catálogo activo (ej. Gree Inverter 18 000 BTU: `S/ 2,450.00`).
2. **Kit Básico de Instalación:** Incluye 4 metros de tubería de cobre deshidratado, manguera corrugada de drenaje, cinta armaflex, cableado vulcanizado y soporte metálico para unidad exterior: `S/ 380.00`.
3. **Mano de Obra Calificada y Transporte según Zona Geográfica:**
   - *Zona 1 (Ventanilla / Puente Piedra / Mi Perú):* `S/ 280.00` (Recargo de desplazamiento: `S/ 0.00`).
   - *Zona 2 (Callao Cercado / Bellavista / San Miguel / Los Olivos):* `S/ 320.00`.
   - *Zona 3 (Miraflores / San Isidro / Surco / La Molina / Ate):* `S/ 380.00`.
4. **Subtotal, Impuesto General a las Ventas (IGV 18%) y Total Final:**
   - $\text{Subtotal} = \text{Equipo} + \text{Materiales} + \text{Instalación}$
   - $\text{IGV (18\%)} = \text{Subtotal} \times 0.18$
   - $\text{Total} = \text{Subtotal} + \text{IGV}$

#### B. Componente Drag & Drop para Carga de Planos y Fotos
- **Formatos Permitidos:** PDF (planos CAD exportados), JPG / JPEG / PNG (fotografías de fachada, techo o punto eléctrico).
- **Límite de Carga:** Hasta 3 archivos de máximo $10\text{ MB}$ por archivo.
- **Micro-interacción UI:**
  - Área activa con marco `.blueprint` discontinuo (`border-style: dashed;`).
  - Al arrastrar un archivo, el borde cambia a `--gf-accent: #5980a6` con fondo `rgba(89, 128, 166, 0.08)`.
  - Barra de progreso porcentual técnica animada con clase `.blueprint-progress`.
  - Lista de archivos cargados con ícono de extensión, peso en KB/MB y botón de remoción rápida.

---

### 5.3. Paso 3: Contacto, Confirmación y Emisión de Folio Oficial
El último paso captura la información comercial del solicitante, persiste la transacción en la base de datos relacional y genera el folio inmutable.

#### A. Campos de Validación Estricta
- **Tipo de Documento:** DNI (8 dígitos numéricos) o RUC (11 dígitos numéricos, validando que inicie con 10 o 20).
- **Nombre Completo / Razón Social:** Obligatorio, mínimo 3 caracteres.
- **Teléfono Celular:** 9 dígitos, validando formato peruano que inicia con `9`.
- **Correo Electrónico:** Validación de estructura RFC 5322 (`@` y dominio válido).
- **Dirección de Obra:** Calle/Avenida, Número/Lote, Distrito y Referencia de llegada.

#### B. Respuesta Transaccional y Pantalla de Éxito
Al pulsar el botón `.btn-marca` ("GENERAR COTIZACIÓN OFICIAL"):
1. Se despacha la petición `POST /api/cotizaciones`.
2. El backend emite código HTTP 201 Created y retorna el payload con el folio inmutable: `COT-2026-0842`.
3. La interfaz transiciona hacia la tarjeta de confirmación técnica que presenta:
   - **Folio Destacado:** Rótulo en tipografía `Barlow Condensed` de $36\text{ px}$: `FOLIO: COT-2026-0842`.
   - **Botón de Descarga PDF:** Enlace directo a la Proforma Oficial membretada (`GET /api/cotizaciones/COT-2026-0842/pdf`).
   - **Botón de WhatsApp Corporativo:** Abre chat directo con Edgar Sabalu (`+51 904 209 489`) con mensaje pre-armado:
     ```
     https://wa.me/51904209489?text=Hola%20Edgar%20Sabalu,%20acabo%20de%20generar%20la%20cotizaci%C3%B3n%20COT-2026-0842%20para%20un%20equipo%20de%2018000%20BTU%20en%20el%20distrito%20de%20Ventanilla.%20Deseo%20coordinar%20la%20visita%20t%C3%A9cnica.
     ```

---

## 6. Especificación de Vistas Protegidas y Paneles de Control

Para responder a las exigencias académicas de las Semanas 08, 09 y 10 (Evaluación T2 - 20%), el sistema cuenta con interfaces privadas protegidas por `AuthGuard` y `JwtInterceptor`.

### 6.1. Vista de Autenticación (`/login`)
- **Ubicación:** `src/app/views/login/login.component.html/.scss/.ts`
- **Estructura Visual:** Tarjeta centralizada `.blueprint` con remates en esquina, fondo `--gf-surface`, isotipo de Global Frío y conmutador de tema oscuro/claro en la esquina superior derecha.
- **Formulario Reactivo:** Campos `username` y `password` con validación en tiempo real.
- **Manejo de Respuestas:**
  - **HTTP 200 OK:** Almacena token JWT en `localStorage.getItem('token')`. Decodifica el payload para extraer el Claim `roles`:
    - Si contiene `ROLE_ADMIN`: Redirige automáticamente a `/panel/tarifario`.
    - Si contiene `ROLE_ASESOR`: Redirige automáticamente a `/panel/solicitudes`.
  - **HTTP 401 Unauthorized:** Muestra alerta visual técnica con clase `.blueprint-alert` indicando credenciales erróneas sin recargar la página.

### 6.2. Panel del Asesor Comercial (`/panel/solicitudes` y `/panel/ordenes`)
Diseñado para la gestión diaria de las solicitudes generadas por clientes:

#### A. Bandeja de Solicitudes (`/panel/solicitudes`)
- **Barra de Herramientas Superior:**
  - Buscador reactivo por folio (`COT-2026-...`) o nombre de cliente con operador `debounceTime(300)` de RxJS.
  - Filtro por estado: `TODAS`, `PENDIENTE`, `ENVIADO`, `ACEPTADO`, `RECHAZADO`.
- **Tabla Técnica Industrial (`.table-blueprint`):**
  - Columnas: Folio, Fecha/Hora, Cliente, Distrito, Capacidad BTU, Monto Total (`S/`), Estado, Acciones.
  - Badges de Estado:
    - `PENDIENTE`: Borde amarillo industrial (`#eab308`), fondo tenue.
    - `ENVIADO`: Borde azul marca (`#5980a6`).
    - `ACEPTADO`: Borde verde técnico (`#22c55e`).
    - `RECHAZADO`: Borde rojo industrial (`#ef4444`).
- **Acciones Disponibles por Fila:**
  - *Ver Detalle:* Abre modal lateral (Drawer) con las fotos y planos adjuntos.
  - *Convertir en Orden:* Dispara la mutación a estado `ORDEN_SERVICIO` solicitando fecha de visita.

#### B. Gestión de Órdenes de Servicio (`/panel/ordenes`)
- Visualización de contratos en ejecución para Edgar Sabalu.
- Asignación de técnicos responsables de la instalación física.
- Descarga del formato de Acta de Entrega y Recepción de Obra.

### 6.3. Panel del Administrador (`/panel/tarifario` y `/panel/catalogo`)
Diseñado para el control maestro de parámetros económicos y stock de equipos:

#### A. Mantenimiento de Tarifario (`/panel/tarifario`)
- Edición del costo base de mano de obra por hora y factor de dificultad en altura.
- Tabla de recargos por desplazamiento geográfico en los distritos de Lima y Callao.
- Costo de insumos unitarios: metro lineal de tubería de cobre de 1/4", 3/8", 1/2" y gas R410A por kilogramo.

#### B. Catálogo Maestro de Equipos HVAC (`/panel/catalogo`)
- Formulario de alta y edición con validación de precios mínimos (`@Min(1)`).
- Campos: Marca (Gree, Samsung, Midea), Modelo exacto, Capacidad BTU (12k, 18k, 24k, 36k, 60k), Eficiencia SEER, Precio de Lista y Unidades en Stock.
- Indicador visual del campo transitorio `@Transient` de JPA (`estadoStock`):
  - Verde: `DISPONIBLE` (Stock > 3 unidades).
  - Ámbar: `POR_AGOTAR` (1 a 3 unidades).
  - Rojo: `AGOTADO` (0 unidades).

---

## 7. Diagrama Oficial de Navegación UI/UX

La siguiente arquitectura de navegación y sitemap ilustra los flujos de interacción del usuario visitante, el asesor y el administrador:

> [!CHECK]
> **Integración Nativa de Diagrama Draw.io:**
> ![[diagramas/06-mapa-navegacion-ui-ux.drawio]]
> Para inspeccionar o editar la estructura vectorial del mapa de navegación, utilizar el archivo fuente: [[diagramas/06-mapa-navegacion-ui-ux.drawio|Abrir en Draw.io]].

---

## 8. Criterios de Aceptación y Checklist de Calidad UI/UX para Vivian

Para dar por concluida la implementación de cualquier vista en Angular, Vivian debe certificar los siguientes puntos en su entorno local:

1. [ ] **Consistencia de Tokens:** Todas las clases de color utilizan `var(--gf-accent)`, `var(--gf-surface)`, `var(--gf-text)` y `var(--gf-btn)`. Prohibido el uso de colores en formato `#hex` quemados en archivos `.component.scss`.
2. [ ] **Esquinas Blueprint Rectas:** Todo contenedor técnico posee `border-radius: 0;` y las esquinas `.corner` presentan sus 4 cruces alineadas sin desfase de píxeles.
3. [ ] **Touch Target Accesible:** Todos los botones y controles interactivos tienen una altura no menor a `44px`.
4. [ ] **Validación Reactiva de Formularios:** Los inputs muestran borde rojo tenue y mensaje explicativo únicamente cuando el campo ha sido tocado (`touched`) y permanece inválido (`invalid`).
5. [ ] **Modo Claro / Oscuro sin Parpadeo (FOUC):** La alternancia de tema no provoca destellos en blanco ni pérdida de legibilidad en ningún componente.
6. [ ] **Compilación Cero Errores:** La ejecución de `npm run build` en `frontend/` finaliza con código 0 y sin advertencias de tipado TypeScript.

---

## 9. Navegación e Interconexión en el Vault de Obsidian

Este documento se conecta bidireccionalmente con los demás componentes del sistema de documentación técnica de Global Frío:

- **Eje Central de Documentación:** [[00-MOC-GlobalFrio|00 - Map of Content (MOC) Central de Global Frío]]
- **Guía de Transferencia para Vivian:** [[Guia-Angular-para-Vivian|Guía Práctica de Angular 18 para Vivian]]
- **Requerimientos y Reglas de Negocio:** [[PRD-GlobalFrio-Cotizaciones|PRD Comercial y Requerimientos de Software]]
- **Trazabilidad Académica UPN:** [[Matriz-Trazabilidad-Silabo-UPN|Matriz de Trazabilidad con el Sílabo UPN]]
- **Gobernanza y Evidencias de Equipo:** [[Guia-Evidencias-Vivian-y-Flujo-Equipo|Guía de Evidencias de Vivian y Flujo de Equipo]]
- **Arquitectura de Sistemas:** [[HLD-Arquitectura-Sistemas|HLD - Arquitectura de Alto Nivel de Sistemas]]
- **Diseño Detallado Backend:** [[LLD-Backend-SpringBoot-Patrones|LLD - Backend Spring Boot y Patrones GoF]]
- **Garantía de Calidad y Pruebas:** [[QA-Test-Plan-Acceptance-Matrix|Plan de Pruebas QA y Matriz de Aceptación]]
- **Sitemap y Diagrama UI/UX:** [[diagramas/06-mapa-navegacion-ui-ux.drawio|Diagrama 06: Mapa de Navegación UI/UX (Draw.io)]]


---
title: "Guía Práctica de Angular 18 para Vivian — Transferencia Tecnológica y Mantenimiento UI/UX"
aliases: ["Guia Angular Vivian", "Manual Frontend Vivian", "Guia-Angular-Vivian"]
tags: ["angular", "frontend", "vivian", "guia-practica", "upn", "transferencia-tecnologica"]
owner: "Orlando (Arquitecto) / Vivian (Frontend Owner)"
status: "Aprobado para Producción"
version: "1.0.0"
updated: "2026-10-17"
---

# Guía Práctica de Angular 18 para Vivian
## Transferencia Tecnológica, Mantenimiento UI/UX y Buenas Prácticas de Frontend

> [!IMPORTANT]
> **Mensaje de Bienvenida y Transferencia Técnica para Vivian**
> Vivian: como **Product Owner**, representante de **Global Frío** y autora del prototipo original en HTML/CSS/JS Vanilla (`frontend/prototipo-vanilla/`), tú eres la **dueña indiscutible del Frontend**. Tú definiste la identidad visual, los colores industriales, las retículas blueprint y el lenguaje de marca que distingue a Global Frío.
>
> Mi rol como Arquitecto (Orlando) consistió en estructurar el cascarón de Angular 18 (`frontend/src/app/`), configurar el enrutamiento, conectar los servicios base con RxJS y habilitar la seguridad con JWT hacia nuestro backend en Spring Boot. Sin embargo, **cualquier ajuste estético, afinamiento de estilos CSS/SCSS o desarrollo de nuevas vistas en Angular te compete directamente a ti**. 
>
> Esta guía ha sido escrita pensando en tu flujo de trabajo, para que la transición desde tu prototipo en Vanilla hacia los componentes de Angular sea totalmente transparente, cómoda y sin frustraciones técnicas.

---

## 1. El Flujo de Trabajo en Equipo y la División de Responsabilidades

Para que nuestro proyecto funcione con máxima fluidez y obtengamos la más alta calificación con el docente **Ing. Mg. Carlos Alberto Ponte Ramírez** en la asignatura *Soluciones Web y Aplicaciones Distribuidas* (SIST1402A), respetamos este flujo riguroso:

| Rol / Integrante | Responsabilidad Técnica Principal | Compromiso de Entrega y Calidad |
| :--- | :--- | :--- |
| **1. Orlando Dorival**<br>*(Arquitectura & BD)* | • Construye la API RESTful en Java 17 + Spring Boot 3.3.4.<br>• Gestiona base de datos relacional MySQL 8.x y H2.<br>• Configura seguridad JWT stateless y servicios reactivos. | Entrega código fuente estructurado, compilable y con pruebas base listas para homologación de QA. |
| **2. Compañero de QA**<br>*(Bug-Fixing Activo en Código)* | • Ejecuta pruebas unitarias, de integración, cURL y E2E.<br>• **Si detecta un bug, él mismo lo diagnostica y lo corrige directamente en el código fuente**.<br>• Re-ejecuta pruebas de regresión. | **Entrega 100% verde** antes de cualquier pase a frontend. Vivian nunca recibe código con errores. |
| **3. Vivian**<br>*(Frontend Owner & PO)* | • Diseña y ajusta la UI/UX en Angular 18 con SASS modular.<br>• Valida la fidelidad visual absoluta contra su prototipo Vanilla original.<br>• Aporta los **5 documentos comerciales reales** de Global Frío. | Interfaz pulida, responsiva y lista para sustentación formal ante el docente Ing. Mg. Carlos Alberto Ponte Ramírez. |


1. **Orlando (Líder Backend & BD):** Provee los endpoints REST (`/api/...`), gestiona los modelos de persistencia JPA y asegura la integridad transaccional.
2. **Compañero de QA (Ingeniero de Calidad y Corrector Activo):** Prueba cada endpoint y componente en Angular. Si detecta un fallo (error 400, 401, 500 o desalineación), él mismo analiza el código y realiza la corrección (*commit `fix(modulo): ...`*). **Nunca te entregará código con errores**.
3. **Vivian (Product Owner y Dueña de Frontend):** Recibe el sistema probado, valida el cumplimiento comercial con Edgar Sabalu (`904 209 489`, Ventanilla `07061`), aplica sus ajustes finos en las hojas de estilo SCSS y programa las vistas que requieran refinamiento estético.

> [!NOTE]
> Para visualizar el mapa de navegación general de la aplicación que estás liderando, consulta:
> ![[diagramas/06-mapa-navegacion-ui-ux.drawio]]
> Enlace interactivo: [[diagramas/06-mapa-navegacion-ui-ux.drawio|Abrir en Draw.io]]

---

## 2. Matriz de Equivalencia 1 a 1: De Vanilla JS a Angular 18

En tu prototipo Vanilla creaste una arquitectura modular admirable utilizando `data-gf-incluir`, `fetch()` y el espacio de nombres global `window.GF`. En Angular 18 hacemos exactamente lo mismo, pero aprovechando el compilador oficial de Angular para que el sitio cargue al instante y sin peticiones asíncronas lentas.

A continuación tienes la tabla de equivalencias exacta:

| Qué hacías en tu Prototipo Vanilla (`prototipo-vanilla`) | Cómo se hace ahora en Angular 18 (`frontend/src/app`) | Por qué es mucho mejor y más fácil |
|---|---|---|
| `<div data-gf-incluir="Modulo-hero/..."></div>` | Escribes la etiqueta HTML: `<app-hero></app-hero>` | No requiere `fetch()` en tiempo de ejecución. Angular compila el componente antes de cargarlo. |
| Declarar `window.GF.Services.ConfigService = { ... }` | Clase con `@Injectable({ providedIn: 'root' })` | Inyección de dependencias limpia sin contaminar el objeto global `window`. |
| `document.getElementById('tel').innerText = config.telefono` | Interpolación directa en HTML: `{{ config.telefono }}` | Si el teléfono cambia, la pantalla se actualiza sola automáticamente (reactividad). |
| `elemento.classList.add('oculto')` | Directiva condicional: `*ngIf="mostrarElemento"` | Elimina el elemento del DOM si no se cumple la condición, liberando memoria. |
| `for (let item of lista) { contenedor.innerHTML += ... }` | Directiva de repetición: `*ngFor="let item of lista"` | Creas una sola tarjeta en HTML y Angular la multiplica tantas veces como elementos haya. |
| `boton.addEventListener('click', () => { ... })` | Event Binding nativo: `(click)="miMetodo()"` | Sintaxis declarativa clara directamente en la etiqueta del botón. |
| `form.addEventListener('submit', (e) => { e.preventDefault() })` | Formularios reactivos: `[formGroup]="miFormulario"` y `(ngSubmit)="enviar()"` | Validación automática de campos (DNI, RUC, Teléfono) con alertas en tiempo real. |
| Cargar archivos CSS sueltos en el `<head>` | Archivo `.component.scss` privado por cada componente | Los estilos de un componente jamás afectarán ni dañarán a otros componentes (*View Encapsulation*). |

---

## 3. Guía Práctica de Directivas y Patrones Clave en Angular

### 3.1. Interpolación y Enlace de Propiedades (Property Binding)

#### ¿Cómo mostrar datos dinámicos? (Interpolación `{{ }}`)
En cualquier archivo `.component.html`, para imprimir una variable declarada en tu archivo `.component.ts`, colócala entre dobles llaves:

```html
<!-- Mostrar el claim y el teléfono de Edgar Sabalu -->
<h1 class="gf-title">{{ tituloPrincipal }}</h1>
<p class="gf-lede">{{ claimHvac }}</p>
<a [href]="'tel:' + telefonoEdgar" class="btn btn-linea">
  LLAMAR AHORA: {{ telefonoEdgar }}
</a>
```

#### ¿Cómo enlazar atributos HTML dinámicos? (Property Binding `[ ]`)
Cuando quieras que el valor de una imagen (`src`), un enlace (`href`) o un estado de botón (`disabled`) dependa de una variable, enciérralo entre corchetes:

```html
<!-- La imagen se asigna dinámicamente desde el objeto TypeScript -->
<div class="blueprint duotone">
  <span class="corner tl"></span><span class="corner tr"></span>
  <span class="corner bl"></span><span class="corner br"></span>
  <img [src]="servicio.rutaImagen" [alt]="servicio.nombre">
</div>

<!-- Deshabilitar botón si el formulario no está completamente lleno -->
<button class="btn btn-marca" [disabled]="formularioInvalido">
  CONFIRMAR Y COTIZAR
</button>
```

---

### 3.2. Directiva Estructural `*ngIf`: Mostrar u Ocultar Elementos

En lugar de manipular clases CSS (`display: none` o `.oculto`), en Angular usas `*ngIf`. Si la condición es verdadera, el bloque existe; si es falsa, se elimina limpiamente.

#### Caso 1: Condicional Básico
```html
<!-- Muestra el aviso de stock agotado solo si el stock es 0 -->
<div *ngIf="equipo.stock === 0" class="badge-agotado">
  EQUIPO AGOTADO — CONSULTAR IMPORTACIÓN
</div>
```

#### Caso 2: Estructura `if - else` con `<ng-template>`
Muy útil para alternar entre la vista de carga (spinner) y la tabla de datos:

```html
<div *ngIf="!cargando; else bloqueCargando">
  <!-- Contenido real cuando ya cargaron los datos -->
  <h3>EQUIPO RECOMENDADO: {{ equipoSeleccionado.nombre }}</h3>
  <p class="precio">S/ {{ equipoSeleccionado.precioTotal }}</p>
</div>

<!-- Bloque alternativo que se muestra mientras se calculan los BTU -->
<ng-template #bloqueCargando>
  <div class="blueprint-cargando">
    <p>CALCULANDO CARGA TÉRMICA EN TIEMPO REAL...</p>
  </div>
</ng-template>
```

---

### 3.3. Directiva Estructural `*ngFor`: Renderizado de Listas y Catálogos

En tu prototipo tenías que armar largos strings HTML dentro de un bucle `for`. En Angular escribes la tarjeta una sola vez y usas `*ngFor`:

```html
<!-- Recorrer el catálogo de equipos de climatización -->
<div class="servicios-grid">
  <article 
    *ngFor="let equipo of catalogoEquipos; let i = index; trackBy: rastrearPorId" 
    class="card blueprint">
    
    <!-- Remates industriales blueprint de Vivian -->
    <span class="corner tl"></span><span class="corner tr"></span>
    <span class="corner bl"></span><span class="corner br"></span>

    <header class="card-header">
      <span class="gf-kicker">ÍTEM #0{{ i + 1 }} · {{ equipo.marca }}</span>
      <h3>{{ equipo.modelo }}</h3>
    </header>

    <div class="card-body">
      <p class="capacidad">{{ equipo.capacidadBtu }} BTU/h ({{ equipo.toneladasRefrigeracion }} TR)</p>
      <p class="precio-soles">S/ {{ equipo.precioBase | number:'1.2-2' }}</p>
    </div>

    <footer class="card-footer">
      <button class="btn btn-marca" (click)="seleccionarEquipo(equipo)">
        SELECCIONAR EQUIPO
      </button>
    </footer>
  </article>
</div>
```

> [!TIP]
> **Rendimiento con `trackBy`:** Observa la propiedad `trackBy: rastrearPorId`. Añadir este método en tu TypeScript permite que Angular solo redibuje los elementos que cambiaron en lugar de toda la lista:
> ```typescript
> rastrearPorId(index: number, item: Equipo): number {
>   return item.id;
> }
> ```

---

### 3.4. Formularios Reactivos (`ReactiveFormsModule`)

Para el modal de cotización y la pantalla de login, utilizamos **Formularios Reactivos**. Son robustos, seguros y permiten validar datos en tiempo real (por ejemplo, que el DNI tenga exactamente 8 dígitos y el teléfono 9 dígitos).

#### En tu archivo TypeScript (`.component.ts`):
```typescript
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-contacto-cotizacion',
  templateUrl: './contacto-cotizacion.component.html',
  styleUrls: ['./contacto-cotizacion.component.scss']
})
export class ContactoCotizacionComponent implements OnInit {
  formCliente!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.formCliente = this.fb.group({
      nombreCompleto: ['', [Validators.required, Validators.minLength(3)]],
      tipoDocumento: ['DNI', Validators.required],
      numeroDocumento: ['', [Validators.required, Validators.pattern(/^[0-9]{8}$/)]],
      telefono: ['', [Validators.required, Validators.pattern(/^9[0-9]{8}$/)]],
      correo: ['', [Validators.required, Validators.email]],
      direccion: ['', Validators.required]
    });
  }

  // Atajo para consultar los campos en la plantilla HTML
  get f() {
    return this.formCliente.controls;
  }

  enviarCotizacion(): void {
    if (this.formCliente.invalid) {
      this.formCliente.markAllAsTouched();
      return;
    }
    console.log('Datos listos para enviar al backend:', this.formCliente.value);
  }
}
```

#### En tu plantilla HTML (`.component.html`):
```html
<form [formGroup]="formCliente" (ngSubmit)="enviarCotizacion()" class="form-blueprint">
  <!-- Campo Teléfono Móvil -->
  <div class="campo-grupo">
    <label for="telefono" class="gf-kicker">TELÉFONO DE CONTACTO (PERÚ)</label>
    <input 
      id="telefono"
      type="tel" 
      formControlName="telefono" 
      placeholder="Ej. 904209489"
      class="input-industrial"
      [class.invalido]="f['telefono'].touched && f['telefono'].invalid">
    
    <!-- Mensaje de error visible solo si el usuario interactuó y falló -->
    <span *ngIf="f['telefono'].touched && f['telefono'].errors?.['required']" class="error-msg">
      El teléfono es obligatorio.
    </span>
    <span *ngIf="f['telefono'].touched && f['telefono'].errors?.['pattern']" class="error-msg">
      Debe ser un celular válido de 9 dígitos que empiece con 9.
    </span>
  </div>

  <button type="submit" class="btn btn-marca" [disabled]="formCliente.invalid">
    GENERAR PROFORMA OFICIAL
  </button>
</form>
```

---

### 3.5. Consumo de Servicios y Manejo de Datos Reactivos con RxJS

En Angular, los datos se consultan a través de servicios inyectables (`@Injectable`).

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CotizacionRespuesta } from '../models/cotizacion.model';

@Injectable({
  providedIn: 'root'
})
export class CotizacionService {
  private readonly API_URL = 'http://localhost:8080/api/cotizaciones';

  constructor(private http: HttpClient) {}

  // Consulta al backend de Orlando
  crearCotizacion(datos: any): Observable<CotizacionRespuesta> {
    return this.http.post<CotizacionRespuesta>(this.API_URL, datos);
  }
}
```

#### ¿Cómo consumirlo en tu componente con `subscribe`?
```typescript
this.cotizacionService.crearCotizacion(payload).subscribe({
  next: (respuesta) => {
    // Éxito: Guardamos el folio (ej. COT-2026-0842) para mostrar la proforma
    this.folioGenerado = respuesta.folio;
    this.pasoActual = 3;
  },
  error: (err) => {
    // Si algo falla, mostramos una alerta amigable sin romper la página
    console.error('Error al cotizar:', err);
    this.mensajeError = 'No se pudo generar la cotización. Intente nuevamente.';
  }
});
```

---

## 4. Paso a Paso: Cómo Crear un Nuevo Componente sin Romper Nada

Si necesitas agregar una nueva pantalla (por ejemplo, el visor del PDF de la proforma o una tarjeta de promociones de temporada), sigue estos sencillos pasos desde la terminal de tu máquina:

### Paso 1: Abrir la terminal en la carpeta frontend
Asegúrate de estar en `c:\Users\ORLANDO\Desktop\globalfrio\frontend`.

### Paso 2: Generar el componente con Angular CLI
Ejecuta el comando oficial del curso:
```bash
npx ng g c components/mi-nuevo-componente --skip-tests
```
Esto creará automáticamente la carpeta con sus 3 archivos listos (`.html`, `.scss`, `.ts`) y lo registrará en `app.module.ts`.

### Paso 3: Aplicar tus estilos industriales
Abre el archivo `mi-nuevo-componente.component.scss` y programa tus estilos usando las variables de `src/styles.scss`:
```scss
/* Usar siempre las variables de Vivian para respetar modo oscuro y claro */
:host {
  display: block;
  background: var(--gf-surface);
  color: var(--gf-text);
  border: 1px solid var(--gf-line);
  padding: 24px;
}

h2 {
  font-family: var(--font-heading);
  color: var(--gf-text);
}
```

### Paso 4: Probar en vivo en tu navegador
Inicia el servidor local con:
```bash
npm start
# O también:
npx ng serve -o
```
Abre tu navegador en `http://localhost:4200`. Si modificas una línea en tu HTML o SCSS, la página se actualizará sola en menos de un segundo (*Hot Module Replacement*).

---

## 5. Cumplimiento Estricto de la Lista Blanca Académica UPN

El docente **Ing. Mg. Carlos Alberto Ponte Ramírez** evalúa rigurosamente que el código fuente se mantenga dentro de lo enseñado en las diapositivas oficiales de la asignatura *Soluciones Web y Aplicaciones Distribuidas*.

Para garantizar nuestra calificación perfecta (20/20), ten presente esta tabla:

| Categoría | Tecnologías y Librerías AUTORIZADAS (Lista Blanca) | Elementos ESTRICTAMENTE PROHIBIDOS (Rechazo Inmediato) |
|---|---|---|
| **Estructura** | Angular 18 CLI (`ng g c`, `ng g s`), TypeScript estricto, Módulos (`@NgModule`). | Prohibido usar React, Vue, Svelte o Vanilla scripts sueltos en el `index.html`. |
| **Estilos** | SCSS / SASS (`styles.scss`), Bootstrap 5 (`container`, `row`, `col`, grillas), Angular Material (`@angular/material`). | **PROHIBIDO TAILWIND CSS** u otros frameworks utilitarios no autorizados. |
| **Formularios** | `ReactiveFormsModule` (`FormGroup`, `FormControl`, `Validators`). | Prohibido manipular el DOM con `document.getElementById` o jQuery. |
| **Comunicación** | `HttpClientModule`, RxJS (`Observable`, `Subject`, `BehaviorSubject`, `pipe`). | Prohibido usar `fetch()` directo o Axios dentro de los componentes. |
| **Seguridad** | Interceptor HTTP para adjuntar `Authorization: Bearer <token>`, Guards `canActivate`. | Prohibido quemar contraseñas o tokens en texto plano dentro del código. |

---

## 6. Comandos Operativos Clave para el Día a Día de Vivian

| Qué deseas hacer | Comando exacto a ejecutar | Resultado esperado |
|---|---|---|
| **Encender el Frontend** | `npm start` | Inicia Angular en `http://localhost:4200`. |
| **Verificar Errores de Tipado** | `npm run build` | Compila todo el proyecto y te avisa si hay errores en el código. |
| **Verificar Estado de Git** | `git status` | Muestra qué archivos HTML o SCSS has modificado. |
| **Guardar tus Ajustes de UI** | `git commit -m "feat(ui): [RF-SEM08-02] ajuste de estilos blueprint"` | Registra tus cambios bajo el estándar formal del curso UPN. |

---

## 7. Navegación e Interconexión en el Vault de Obsidian

Esta guía se articula orgánicamente con los demás documentos de diseño y gobernanza del proyecto:

- **Eje Central de Documentación:** [[00-MOC-GlobalFrio|00 - Map of Content (MOC) Central de Global Frío]]
- **Especificación de Diseño UI/UX:** [[UI-UX-Design-Handoff-Spec|Especificación Canónica de UI/UX Design Handoff]]
- **Gobernanza y Evidencias de Equipo:** [[Guia-Evidencias-Vivian-y-Flujo-Equipo|Guía de Evidencias de Vivian y Flujo de Equipo]]
- **Requerimientos y Reglas de Negocio:** [[PRD-GlobalFrio-Cotizaciones|PRD Comercial y Requerimientos de Software]]
- **Playbook de Calidad para el Compañero:** [[Playbook-QA-y-Arreglo-de-Errores|Playbook Operativo de QA y Arreglo de Errores]]
- **Plan Maestro de Pruebas:** [[QA-Test-Plan-Acceptance-Matrix|Plan de Pruebas QA y Matriz de Aceptación]]
- **Sitemap y Flujo de Navegación:** [[diagramas/06-mapa-navegacion-ui-ux.drawio|Diagrama 06: Mapa de Navegación UI/UX (Draw.io)]]


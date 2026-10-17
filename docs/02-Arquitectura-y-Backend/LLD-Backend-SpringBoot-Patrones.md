---
title: "LLD — Diseño de Bajo Nivel del Backend Spring Boot y Patrones GoF"
aliases:
  - "LLD Backend Spring Boot Patrones"
  - "Patrones GoF Global Frio"
tags:
  - backend
  - lld
  - springboot
  - patrones-gof
  - jpa
  - rest
  - thymeleaf
  - upn
owner: "Orlando (Arquitecto de Sistemas & Líder Backend)"
status: "Completado"
version: "1.0.0"
updated: "2026-10-17"
---

# LLD — Diseño de Bajo Nivel del Backend Spring Boot y Patrones GoF

El presente documento de Diseño de Bajo Nivel (**Low Level Design — LLD**) formaliza la estructura de paquetes, la implementación detallada de los 4 patrones de diseño GoF obligatorios por el sílabo, los contratos canónicos de la API RESTful y la vista server-side con Thymeleaf para el microservicio de **Global Frío SAC**, desarrollado sobre **Java 17 LTS** y **Spring Boot 3.3.4**.

---

## 1. Arquitectura de Paquetes en N-Capas (`pe.upn.sist1402a.*`)

En estricto cumplimiento del paquete oficial de cátedra asignado por la Universidad Privada del Norte, el código backend se organiza bajo una arquitectura modular de capas desacopladas:

```text
pe.upn.sist1402a
├── config/                  <- Configuración de Spring Security, CORS, Swagger OpenAPI y Beans globales
│   ├── SecurityConfig.java
│   ├── CorsConfig.java
│   └── OpenApiConfig.java
├── controller/              <- Controladores REST (@RestController) y Controladores MVC (@Controller)
│   ├── AuthController.java
│   ├── CotizacionPublicaController.java
│   ├── CotizacionPanelController.java
│   ├── OrdenServicioController.java
│   ├── CatalogoController.java
│   ├── SaludoController.java
│   └── CotizacionReporteController.java  <- Controlador SSR con Thymeleaf
├── service/                 <- Contratos de servicios de lógica de negocio (@Service)
│   ├── CotizacionService.java
│   ├── OrdenServicioService.java
│   ├── DimensionamientoTermicoService.java
│   ├── CatalogoService.java
│   ├── TokenBlacklistService.java
│   └── UsuarioAuthService.java
├── service.impl/            <- Implementaciones de negocio con transaccionalidad declarativa
│   ├── CotizacionServiceImpl.java
│   ├── OrdenServicioServiceImpl.java
│   ├── DimensionamientoTermicoServiceImpl.java
│   ├── CatalogoServiceImpl.java
│   └── UsuarioAuthServiceImpl.java
├── repository/              <- Interfaces Spring Data JPA y DAOs manuales con EntityManager
│   ├── ProductoRepository.java
│   ├── CotizacionRepository.java
│   ├── CotizacionManualDao.java          <- Coexistencia Semana 03 (EntityManager)
│   ├── SolicitudCotizacionRepository.java
│   ├── OrdenServicioRepository.java
│   ├── EquipoHvacRepository.java
│   ├── DistritoZonaRepository.java
│   ├── ClienteRepository.java
│   └── UsuarioRepository.java
├── entity/                  <- Entidades relacionales mapeadas con JPA (@Entity) y Bean Validation
│   ├── Producto.java                     <- Entidad base con @Transient getEstadoStock()
│   ├── Rol.java
│   ├── Usuario.java
│   ├── DistritoZona.java
│   ├── Cliente.java
│   ├── EquipoHvac.java
│   ├── ServicioCatalogo.java
│   ├── SolicitudCotizacion.java
│   ├── AdjuntoSolicitud.java
│   ├── Cotizacion.java                   <- Cabecera con columnas snapshot_*
│   ├── CotizacionDetalle.java            <- Detalle con columnas snapshot_*
│   └── OrdenServicio.java
├── dto/                     <- Data Transfer Objects para entrada y salida tipada
│   ├── request/
│   │   ├── LoginRequestDto.java
│   │   ├── SolicitudCotizacionRequestDto.java
│   │   ├── AprobarCotizacionRequestDto.java
│   │   └── CrearOrdenServicioRequestDto.java
│   └── response/
│       ├── JwtAuthResponseDto.java
│       ├── CotizacionResumenDto.java
│       ├── CotizacionDetalleDto.java
│       ├── OrdenServicioResponseDto.java
│       ├── SaludoResponseDto.java
│       └── ErrorResponseDto.java
├── exception/               <- Manejador global centralizado y excepciones de dominio
│   ├── GlobalExceptionHandler.java       <- @RestControllerAdvice
│   ├── BadRequestException.java
│   ├── ResourceNotFoundException.java
│   ├── UnauthorizedException.java
│   └── BusinessRuleException.java
└── pattern/                 <- Los 4 Patrones de Diseño GoF requeridos por el sílabo
    ├── decorator/           <- Patrón Decorator: Cálculo de presupuesto en BigDecimal
    │   ├── PresupuestoCalculator.java
    │   ├── CostoBaseEquipoCalculator.java
    │   ├── PresupuestoDecorator.java
    │   ├── InstalacionEstandarDecorator.java
    │   ├── MetrosAdicionalesTuberiaDecorator.java
    │   ├── RecargoZonaDistritoDecorator.java
    │   ├── DescuentoComercialDecorator.java
    │   └── IGVCalculatorDecorator.java
    ├── factory/             <- Patrón Factory: Fabricación de órdenes y snapshots
    │   ├── CotizacionSnapshotFactory.java
    │   └── OrdenServicioFactory.java
    └── singleton/           <- Patrón Singleton: Almacén de tarifas y parámetros comerciales
        └── ParametrosComercialesSingleton.java
```

---

## 2. Los 4 Patrones de Diseño GoF (Alineados al Sílabo UPN)

El diseño del backend incorpora 4 patrones fundamentales del catálogo GoF (*Gang of Four*), exigidos en las semanas teóricas y prácticas del curso:

### 2.1. Patrón 1: Decorator — Cómputo Financiero de Presupuestos en `BigDecimal`
- **Problema de Dominio:** El cálculo de una cotización de refrigeración industrial y climatización HVAC no es estático. Requiere componer dinámicamente un precio base según múltiples conceptos agregados (instalación estándar, metros adicionales de cañería de cobre frigorífica, recargo por flete territorial en Lima y Callao, descuentos promocionales y cálculo fiscal del IGV). Además, se prohíbe el uso de tipos primitivos flotantes (`double`, `float`) por riesgo de pérdida de precisión en transacciones financieras.

> [!IMPORTANT]
> **Decisión de Arquitectura Financiera: Cero Pérdida de Precisión con `BigDecimal`**  
> Para todos los cálculos monetarios de cotización (mano de obra, metros adicionales de cañería, recargo de flete por zona e IGV 18%), se prohíbe el uso de tipos flotantes primitivos (`double`, `float`). Se exige `java.math.BigDecimal` con escala a dos decimales y estrategia de redondeo bancario determinista `RoundingMode.HALF_UP`.


![[diagramas/03-flujo-cotizacion-y-patron-decorator.drawio]]
*Figura 2: Estructura y Flujo de Cálculo del Patrón Decorator.*  
[[diagramas/03-flujo-cotizacion-y-patron-decorator.drawio|Abrir Diagrama Flujo Cotización y Decorator]]

#### Implementación del Componente y Decoradores:

```java
package pe.upn.sist1402a.pattern.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Componente Base del Patrón Decorator.
 * Define las operaciones comunes para el cálculo de cotizaciones.
 */
public interface PresupuestoCalculator {
    BigDecimal calcularSubtotal();
    String obtenerDescripcionDetallada();
}
```

```java
package pe.upn.sist1402a.pattern.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Componente Concreto: Costo base del equipo HVAC seleccionado del catálogo.
 */
public class CostoBaseEquipoCalculator implements PresupuestoCalculator {
    private final String sku;
    private final String descripcion;
    private final BigDecimal precioBase;

    public CostoBaseEquipoCalculator(String sku, String descripcion, BigDecimal precioBase) {
        this.sku = sku;
        this.descripcion = descripcion;
        this.precioBase = precioBase.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calcularSubtotal() {
        return this.precioBase;
    }

    @Override
    public String obtenerDescripcionDetallada() {
        return "Equipo HVAC [" + sku + "] " + descripcion + " (S/ " + precioBase + ")";
    }
}
```

```java
package pe.upn.sist1402a.pattern.decorator;

import java.math.BigDecimal;

/**
 * Decorador Abstracto base.
 */
public abstract class PresupuestoDecorator implements PresupuestoCalculator {
    protected final PresupuestoCalculator envoltorio;

    public PresupuestoDecorator(PresupuestoCalculator envoltorio) {
        this.envoltorio = envoltorio;
    }

    @Override
    public BigDecimal calcularSubtotal() {
        return envoltorio.calcularSubtotal();
    }

    @Override
    public String obtenerDescripcionDetallada() {
        return envoltorio.obtenerDescripcionDetallada();
    }
}
```

```java
package pe.upn.sist1402a.pattern.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Decorador Concreto 1: Instalación Estándar (hasta 3 metros de cañería básica).
 */
public class InstalacionEstandarDecorator extends PresupuestoDecorator {
    private final BigDecimal costoManoObra;

    public InstalacionEstandarDecorator(PresupuestoCalculator envoltorio, BigDecimal costoManoObra) {
        super(envoltorio);
        this.costoManoObra = costoManoObra.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calcularSubtotal() {
        return envoltorio.calcularSubtotal().add(this.costoManoObra);
    }

    @Override
    public String obtenerDescripcionDetallada() {
        return envoltorio.obtenerDescripcionDetallada() + 
               " + Instalación Estándar (3m tubería básica) (S/ " + costoManoObra + ")";
    }
}
```

```java
package pe.upn.sist1402a.pattern.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Decorador Concreto 2: Tubería de cobre aislada adicional (excedente a los 3 metros).
 */
public class MetrosAdicionalesTuberiaDecorator extends PresupuestoDecorator {
    private final int metrosExcedentes;
    private final BigDecimal costoPorMetro;
    private final BigDecimal recargoTotal;

    public MetrosAdicionalesTuberiaDecorator(PresupuestoCalculator envoltorio, 
                                             int metrosTotales, 
                                             BigDecimal costoPorMetro) {
        super(envoltorio);
        this.metrosExcedentes = Math.max(0, metrosTotales - 3);
        this.costoPorMetro = costoPorMetro.setScale(2, RoundingMode.HALF_UP);
        this.recargoTotal = this.costoPorMetro.multiply(new BigDecimal(this.metrosExcedentes))
                                              .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calcularSubtotal() {
        return envoltorio.calcularSubtotal().add(this.recargoTotal);
    }

    @Override
    public String obtenerDescripcionDetallada() {
        if (metrosExcedentes == 0) {
            return envoltorio.obtenerDescripcionDetallada();
        }
        return envoltorio.obtenerDescripcionDetallada() + 
               " + Tuberia adicional (" + metrosExcedentes + "m x S/ " + costoPorMetro + " = S/ " + recargoTotal + ")";
    }
}
```

```java
package pe.upn.sist1402a.pattern.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Decorador Concreto 3: Recargo por flete territorial según zona de Lima / Callao.
 */
public class RecargoZonaDistritoDecorator extends PresupuestoDecorator {
    private final String distritoNombre;
    private final BigDecimal costoFlete;

    public RecargoZonaDistritoDecorator(PresupuestoCalculator envoltorio, 
                                        String distritoNombre, 
                                        BigDecimal costoFlete) {
        super(envoltorio);
        this.distritoNombre = distritoNombre;
        this.costoFlete = costoFlete.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calcularSubtotal() {
        return envoltorio.calcularSubtotal().add(this.costoFlete);
    }

    @Override
    public String obtenerDescripcionDetallada() {
        return envoltorio.obtenerDescripcionDetallada() + 
               " + Flete Zona [" + distritoNombre + "] (S/ " + costoFlete + ")";
    }
}
```

```java
package pe.upn.sist1402a.pattern.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Decorador Concreto 4: Cálculo fiscal obligatorio de Impuesto General a las Ventas (IGV 18%).
 */
public class IGVCalculatorDecorator extends PresupuestoDecorator {
    private static final BigDecimal FACTOR_IGV = new BigDecimal("0.18");

    public IGVCalculatorDecorator(PresupuestoCalculator envoltorio) {
        super(envoltorio);
    }

    public BigDecimal calcularSoloIgv() {
        BigDecimal baseImponible = envoltorio.calcularSubtotal();
        return baseImponible.multiply(FACTOR_IGV).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calcularSubtotal() {
        BigDecimal subtotal = envoltorio.calcularSubtotal();
        BigDecimal igv = calcularSoloIgv();
        return subtotal.add(igv).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String obtenerDescripcionDetallada() {
        BigDecimal base = envoltorio.calcularSubtotal();
        BigDecimal igv = calcularSoloIgv();
        return envoltorio.obtenerDescripcionDetallada() + 
               " | Subtotal Base: S/ " + base + " | IGV (18%): S/ " + igv + " | TOTAL FINAL: S/ " + calcularSubtotal();
    }
}
```

#### Traza de Cómputo Financiero Determinista:
Considerando una instalación comercial en **San Miguel** con un equipo **Samsung WindFree Inverter 18,000 BTU** y **8 metros** de cañería requeridos:
1. `CostoBaseEquipoCalculator`: Equipo `SAM-DVM-18K` = `S/ 2,980.00`
2. `InstalacionEstandarDecorator`: Mano de obra básica = `+ S/ 350.00` (Acumulado: `S/ 3,330.00`)
3. `MetrosAdicionalesTuberiaDecorator`: 5 metros extra (8m - 3m) * S/ 65.00 = `+ S/ 325.00` (Acumulado: `S/ 3,655.00`)
4. `RecargoZonaDistritoDecorator`: Flete San Miguel = `+ S/ 40.00` (Subtotal Imponible: `S/ 3,695.00`)
5. `IGVCalculatorDecorator`: IGV 18% = `3,695.00 * 0.18` = `S/ 665.10`
6. **Total General Calculado:** `3,695.00 + 665.10 = S/ 4,360.10` (redondeo `HALF_UP` en centavos).

---

### 2.2. Patrón 2: Factory — Fábrica de Órdenes y Congelamiento de Snapshots
- **Problema de Dominio:** Garantizar la integridad referencial y fiscal en dos momentos clave:
  1. *Congelamiento de Cotización (`CotizacionSnapshotFactory`):* Al emitirse una proforma formal, los precios de lista del catálogo deben copiarse en columnas inmutables (`snapshot_*`).
  2. *Generación de Orden de Servicio (`OrdenServicioFactory`):* Al aceptarse una cotización, se debe instanciar la orden operativa con su numeración correlativa (`OS-2026-XXXX`), asignando al responsable técnico de Global Frío (**Edgar Sabalu**) y programando la fecha de cuadrilla.

![[diagramas/04-maquina-estados-cotizacion-orden.drawio]]
*Figura 3: Ciclo de Vida y Máquina de Estados Gobernado por Fábricas.*  
[[diagramas/04-maquina-estados-cotizacion-orden.drawio|Abrir Diagrama Máquina de Estados]]

> [!CHECK]
> **Garantía de Inmutabilidad Fiscal y Correlativos Deterministas**  
> El patrón Factory asegura la congelación inmutable de precios (`CotizacionSnapshotFactory`) copiando los valores del catálogo a columnas `snapshot_*`, protegiendo la proforma contra futuros incrementos de tarifa. Asimismo, `OrdenServicioFactory` garantiza correlativos únicos deterministas con formato `OS-2026-XXXX`.


```java
package pe.upn.sist1402a.pattern.factory;

import pe.upn.sist1402a.entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Fábrica para la generación de Órdenes de Servicio a partir de cotizaciones formalmente aprobadas.
 */
@Component
public class OrdenServicioFactory {

    public OrdenServicio crearOrdenDesdeCotizacion(Cotizacion cotizacion, Usuario asesor, LocalDateTime fechaProgramada) {
        if (!"ACEPTADA".equalsIgnoreCase(cotizacion.getEstado())) {
            throw new IllegalStateException("Solo se puede generar una Orden de Servicio para cotizaciones en estado ACEPTADA.");
        }

        OrdenServicio orden = new OrdenServicio();
        // Generación de correlativo determinista OS-2026-XXXX
        String correlativo = String.format("OS-2026-%04d", cotizacion.getId());
        orden.setCodigoOs(correlativo);
        orden.setCotizacion(cotizacion);
        orden.setAsesorResponsable(asesor);
        orden.setTecnicoAsignado("Edgar Sabalu (Jefe de Operaciones)");
        orden.setFechaProgramada(fechaProgramada != null ? fechaProgramada : LocalDateTime.now().plusDays(2));
        orden.setEstado("PROGRAMADA");
        orden.setObservacionesTecnicas("Protocolo HVAC: Verificación de presiones R410A/R32, vacío de cañería con bomba de 2 etapas y prueba de drenaje.");
        orden.setCreatedAt(LocalDateTime.now());

        return orden;
    }
}
```

```java
package pe.upn.sist1402a.pattern.factory;

import pe.upn.sist1402a.entity.*;
import pe.upn.sist1402a.pattern.decorator.PresupuestoCalculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Fábrica para la construcción de cotizaciones formales con congelamiento de snapshots inmutables.
 */
@Component
public class CotizacionSnapshotFactory {

    public Cotizacion crearCotizacionFormal(SolicitudCotizacion solicitud, 
                                           Usuario asesor, 
                                           PresupuestoCalculator calculador, 
                                           BigDecimal flete, 
                                           BigDecimal igv, 
                                           BigDecimal total) {
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setCodigoCotizacion(String.format("COT-2026-%04d", System.currentTimeMillis() % 10000));
        cotizacion.setSolicitud(solicitud);
        cotizacion.setCliente(solicitud.getCliente());
        cotizacion.setAsesor(asesor);
        cotizacion.setCostoFlete(flete);
        cotizacion.setSubtotal(total.subtract(igv).subtract(flete));
        cotizacion.setIgv(igv);
        cotizacion.setTotal(total);
        cotizacion.setEstado("EMITIDA");
        cotizacion.setValidezDias(15);
        cotizacion.setFechaEmision(LocalDateTime.now());
        cotizacion.setFechaVencimiento(LocalDateTime.now().plusDays(15));

        // Congelamiento inmutable de datos fiscales del cliente
        cotizacion.setSnapshotClienteNombre(solicitud.getCliente().getNombresRazonSocial());
        cotizacion.setSnapshotClienteDoc(solicitud.getCliente().getNumeroDocumento());
        cotizacion.setSnapshotClienteDireccion(solicitud.getCliente().getDireccion());
        cotizacion.setSnapshotFleteZona(solicitud.getCliente().getDistrito().getNombre());

        return cotizacion;
    }
}
```

---

### 2.3. Patrón 3: Singleton — Almacén Central de Tarifas y Parámetros Comerciales
- **Problema de Dominio:** Mantenimiento de constantes normativas del negocio en memoria con seguridad de hilos (*thread-safety*), evitando lecturas reiterativas a disco o bases de datos para reglas inmutables como la tasa del IGV, distancias base o factores de carga térmica.

```java
package pe.upn.sist1402a.pattern.singleton;

import java.math.BigDecimal;

/**
 * Patrón Singleton Thread-Safe para constantes operativas de Global Frío SAC.
 */
public final class ParametrosComercialesSingleton {

    private static volatile ParametrosComercialesSingleton instancia;

    // Constantes operativas de Global Frío
    private final BigDecimal tasaIgv;
    private final int metrosBaseKitInstalacion;
    private final BigDecimal precioMetroAdicionalTuberia;
    private final int factorBtuPorMetroCuadrado;
    private final int garantiaMesesEquipo;
    private final int garantiaMesesInstalacion;

    private ParametrosComercialesSingleton() {
        this.tasaIgv = new BigDecimal("0.18");
        this.metrosBaseKitInstalacion = 3;
        this.precioMetroAdicionalTuberia = new BigDecimal("65.00");
        this.factorBtuPorMetroCuadrado = 600; // Estándar Lima Residencial
        this.garantiaMesesEquipo = 12;
        this.garantiaMesesInstalacion = 6;
    }

    public static ParametrosComercialesSingleton getInstancia() {
        if (instancia == null) {
            synchronized (ParametrosComercialesSingleton.class) {
                if (instancia == null) {
                    instancia = new ParametrosComercialesSingleton();
                }
            }
        }
        return instancia;
    }

    public BigDecimal getTasaIgv() { return tasaIgv; }
    public int getMetrosBaseKitInstalacion() { return metrosBaseKitInstalacion; }
    public BigDecimal getPrecioMetroAdicionalTuberia() { return precioMetroAdicionalTuberia; }
    public int getFactorBtuPorMetroCuadrado() { return factorBtuPorMetroCuadrado; }
    public int getGarantiaMesesEquipo() { return garantiaMesesEquipo; }
    public int getGarantiaMesesInstalacion() { return garantiaMesesInstalacion; }
}
```

---

### 2.4. Patrón 4: Repository — Coexistencia `EntityManager` y `JpaRepository`
En estricta correspondencia con los requerimientos pedagógicos del curso:
1. **Semana 03 (`SPEC-SEM03-B`):** Persistencia manual mediante clase `@Repository` inyectando `@PersistenceContext EntityManager entityManager`, sin usar `JpaRepository`.
2. **Semana 04 (`SPEC-SEM04-B` y `SPEC-SEM04-C`):** Migración e integración de interfaces `JpaRepository<T, ID>` con consultas JPQL parametrizadas y campos `@Transient`.

> [!TIP]
> **Coexistencia Pedagógica UPN: `EntityManager` (Semana 03) vs. `JpaRepository` (Semana 04)**  
> En estricto respeto a las reglas de la cátedra del docente Ing. Mg. Carlos Alberto Ponte Ramírez, `CotizacionManualDao` demuestra la persistencia manual con `EntityManager` y JPQL nativo sin interfaces automáticas (Semana 03), mientras que `CotizacionRepository` implementa el CRUD completo y consultas derivadas con `JpaRepository` a partir de la Semana 04.

#### Implementación Semana 03 (DAO Manual con EntityManager):

```java
package pe.upn.sist1402a.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import pe.upn.sist1402a.entity.Cotizacion;

import java.util.List;
import java.util.Optional;

/**
 * Cumplimiento estricto Semana 03 (SPEC-SEM03-B):
 * Operaciones directas con EntityManager sin JpaRepository.
 */
@Repository
public class CotizacionManualDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void persistir(Cotizacion cotizacion) {
        entityManager.persist(cotizacion);
    }

    public Optional<Cotizacion> buscarPorId(Long id) {
        return Optional.ofNullable(entityManager.find(Cotizacion.class, id));
    }

    public List<Cotizacion> listarTodas() {
        TypedQuery<Cotizacion> query = entityManager.createQuery(
            "SELECT c FROM Cotizacion c ORDER BY c.fechaEmision DESC", 
            Cotizacion.class
        );
        return query.getResultList();
    }

    public List<Cotizacion> buscarPorEstado(String estado) {
        TypedQuery<Cotizacion> query = entityManager.createQuery(
            "SELECT c FROM Cotizacion c WHERE c.estado = :estado", 
            Cotizacion.class
        );
        query.setParameter("estado", estado);
        return query.getResultList();
    }
}
```

#### Implementación Semana 04+ (Spring Data JPA y JPQL Parametrizado):
```java
package pe.upn.sist1402a.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upn.sist1402a.entity.Producto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Cumplimiento Semana 04 (SPEC-SEM04-B & SPEC-SEM04-C):
 * JpaRepository completo con métodos derivados y consultas JPQL parametrizadas con @Param.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT p FROM Producto p WHERE p.precio BETWEEN :minimo AND :maximo ORDER BY p.precio ASC")
    List<Producto> buscarPorRangoDePrecio(@Param("minimo") BigDecimal minimo, 
                                          @Param("maximo") BigDecimal maximo);
}
```

#### Entidad Base con Campo Calculado `@Transient`:
```java
package pe.upn.sist1402a.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto no puede estar vacio")
    @Size(min = 3, max = 120, message = "El nombre debe contener entre 3 y 120 caracteres")
    @Column(nullable = false, length = 120)
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    /**
     * Campo calculado en memoria conforme a SPEC-SEM04-C.
     * No se persiste como columna física en la base de datos.
     */
    @Transient
    public String getEstadoStock() {
        if (this.stock == null || this.stock <= 0) {
            return "AGOTADO";
        } else if (this.stock < 5) {
            return "POR_AGOTAR";
        } else {
            return "DISPONIBLE";
        }
    }
}
```

---

## 3. Contratos de la API RESTful (Endpoints Principales)

| Verbo | Ruta / Endpoint | Autenticación / Rol | Request Body | Códigos HTTP | Propósito de Negocio |
|---|---|---|---|---|---|
| `GET` | `/api/saludo` | Anónimo (Público) | Ninguno | `200 OK` | Conectividad de servidor (`RF-SEM01-01`). |
| `POST` | `/api/auth/login` | Anónimo (Público) | `LoginRequestDto` `{username, password}` | `200 OK`, `401 Unauthorized` | Emisión de Token JWT Bearer (`RF-SEM05-02`). |
| `POST` | `/api/auth/logout` | Autenticado (`Bearer`) | Ninguno (extrae de header) | `200 OK`, `401 Unauthorized` | Invalidación de token en `TokenBlacklistService`. |
| `POST` | `/api/cotizaciones/publica` | Anónimo (Público) | `SolicitudCotizacionRequestDto` | `201 Created`, `400 Bad Request` | Registro anónimo desde modal de cliente en Angular. |
| `GET` | `/api/cotizaciones/publica/{uuid}` | Anónimo (Público) | Ninguno | `200 OK`, `404 Not Found` | Consulta pública de estado de solicitud por UUID. |
| `GET` | `/api/catalogo/equipos` | Anónimo (Público) | Parámetros query opcionales | `200 OK` | Listado de equipos Gree y Samsung disponibles. |
| `GET` | `/api/catalogo/distritos` | Anónimo (Público) | Ninguno | `200 OK` | Zonificación urbana y costos de flete en Lima. |
| `GET` | `/api/panel/cotizaciones` | `ROLE_ASESOR`, `ROLE_ADMIN` | Paginación `?page=0&size=10` | `200 OK`, `403 Forbidden` | Bandeja de cotizaciones para el Asesor Comercial. |
| `PUT` | `/api/panel/cotizaciones/{id}/aprobar` | `ROLE_ASESOR` | `AprobarCotizacionRequestDto` | `200 OK`, `400 Bad Request`, `404 Not Found` | Aprobación comercial, cálculo Decorator y congelamiento. |
| `POST` | `/api/panel/ordenes/generar/{idCotizacion}` | `ROLE_ASESOR`, `ROLE_ADMIN` | `CrearOrdenServicioRequestDto` | `201 Created`, `400 Bad Request` | Generación de Orden de Servicio formal (`OS-2026-XXXX`). |

### 3.1. Esquemas JSON de Intercambio

#### Request: `POST /api/cotizaciones/publica`
```json
{
  "tipoDocumento": "DNI",
  "numeroDocumento": "45871234",
  "nombresRazonSocial": "Juan Carlos Pérez",
  "telefono": "987654321",
  "email": "jperez@gmail.com",
  "direccion": "Av. La Marina 2150 Dpto 402",
  "distritoId": 3,
  "areaM2": 24.50,
  "numPersonas": 3,
  "numEquiposElectronicos": 2,
  "tipoEspacio": "RESIDENCIAL",
  "incidenciaSolar": "MEDIA",
  "mensajeDuda": "Deseo saber si el equipo incluye soporte para techo."
}
```

#### Response: `201 Created`
```json
{
  "codigoSeguimiento": "d3b07384-d113-4f44-8d4e-6e274b1234a1",
  "estado": "PENDIENTE",
  "btuCalculado": 18000,
  "equipoRecomendado": {
    "sku": "SAM-DVM-18K",
    "marca": "SAMSUNG",
    "modelo": "WindFree Inverter 18k BTU",
    "precioBase": 2980.00
  },
  "mensaje": "Solicitud de cotización registrada exitosamente. Un asesor se comunicará con usted."
}
```

---

## 4. Vista Server-Side con Thymeleaf (`/reportes/cotizacion/{id}`)

En estricta observancia de los requerimientos de la **Semana 01 (`RF-SEM01-02` / `SPEC-SEM01-B`)**, el backend implementa un controlador MVC tradicional con `@Controller` para contrastar formalmente el renderizado del lado del servidor (**SSR**) frente al consumo desacoplado de la SPA en Angular.

### 4.1. Controlador MVC (`CotizacionReporteController.java`)
```java
package pe.upn.sist1402a.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.upn.sist1402a.entity.Cotizacion;
import pe.upn.sist1402a.service.CotizacionService;

@Controller
@RequestMapping("/reportes")
public class CotizacionReporteController {

    private final CotizacionService cotizacionService;

    public CotizacionReporteController(CotizacionService cotizacionService) {
        this.cotizacionService = cotizacionService;
    }

    /**
     * Endpoint SSR con Thymeleaf conforme a RF-SEM01-02.
     * Genera la proforma comercial formal imprimible de Global Frío SAC.
     */
    @GetMapping("/cotizacion/{id}")
    public String emitirReporteCotizacionThymeleaf(@PathVariable("id") Long id, Model model) {
        Cotizacion cotizacion = cotizacionService.obtenerPorIdConSnapshots(id);
        
        model.addAttribute("cotizacion", cotizacion);
        model.addAttribute("detalles", cotizacion.getDetalles());
        model.addAttribute("empresaNombre", "Global Frío SAC");
        model.addAttribute("empresaRuc", "20608945123");
        model.addAttribute("empresaContacto", "Edgar Sabalu (904 209 489)");
        model.addAttribute("empresaDireccion", "4VJ7+W28 K, Ventanilla 07061, Callao, Perú");

        // Retorna la plantilla resources/templates/reporte-cotizacion.html
        return "reporte-cotizacion";
    }
}
```

### 4.2. Plantilla Thymeleaf (`resources/templates/reporte-cotizacion.html`)
La plantilla renderiza una proforma membretada apta para impresión física y descarga directa en PDF, con el branding industrial de Global Frío (colores `#5980a6`, `#1f5fa9` y tipografía `Barlow`):

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="es">
<head>
    <meta charset="UTF-8">
    <title>Proforma Comercial - Global Frío SAC</title>
    <style>
        body { font-family: 'Barlow', Arial, sans-serif; color: #1d1f20; margin: 30px; background: #fff; }
        .header { border-bottom: 3px solid #1f5fa9; padding-bottom: 15px; margin-bottom: 20px; display: flex; justify-content: space-between; }
        .brand-title { color: #1f5fa9; font-size: 24px; font-weight: bold; margin: 0; }
        .brand-sub { color: #5980a6; font-size: 14px; margin: 2px 0; }
        .doc-box { border: 2px solid #5980a6; padding: 10px 20px; text-align: center; border-radius: 6px; }
        .table-data { width: 100%; border-collapse: collapse; margin-top: 15px; }
        .table-data th { background-color: #2c4560; color: #fff; padding: 10px; text-align: left; }
        .table-data td { border-bottom: 1px solid #ddd; padding: 9px; }
        .total-box { margin-top: 20px; float: right; width: 300px; text-align: right; }
        .total-line { font-size: 16px; margin: 4px 0; }
        .total-bold { font-size: 20px; font-weight: bold; color: #1f5fa9; }
    </style>
</head>
<body>
    <div class="header">
        <div>
            <h1 class="brand-title" th:text="${empresaNombre}">Global Frío SAC</h1>
            <p class="brand-sub">Especialistas en Refrigeración Industrial y Climatización HVAC</p>
            <p class="brand-sub" th:text="'RUC: ' + ${empresaRuc} + ' | Base: ' + ${empresaDireccion}">RUC: 20608945123</p>
            <p class="brand-sub" th:text="'Atención Técnica: ' + ${empresaContacto}">Edgar Sabalu</p>
        </div>
        <div class="doc-box">
            <h3 style="margin: 0; color: #1f5fa9;">PROFORMA COMERCIAL</h3>
            <p style="font-size: 18px; font-weight: bold; margin: 5px 0;" th:text="${cotizacion.codigoCotizacion}">COT-2026-0001</p>
            <small th:text="'Validez: ' + ${cotizacion.validezDias} + ' días'">Validez: 15 días</small>
        </div>
    </div>

    <h3>Datos del Cliente (Registro Snapshot Inmutable)</h3>
    <table style="width: 100%; margin-bottom: 20px;">
        <tr>
            <td><strong>Razón Social / Nombre:</strong> <span th:text="${cotizacion.snapshotClienteNombre}">Juan Pérez</span></td>
            <td><strong>DNI / RUC:</strong> <span th:text="${cotizacion.snapshotClienteDoc}">45871234</span></td>
        </tr>
        <tr>
            <td><strong>Dirección de Obra:</strong> <span th:text="${cotizacion.snapshotClienteDireccion}">Av. La Marina 2150</span></td>
            <td><strong>Zona de Flete:</strong> <span th:text="${cotizacion.snapshotFleteZona}">San Miguel</span></td>
        </tr>
    </table>

    <table class="table-data">
        <thead>
            <tr>
                <th>Ítem / Concepto</th>
                <th>Cant.</th>
                <th>Precio Unit. (S/)</th>
                <th>Subtotal (S/)</th>
            </tr>
        </thead>
        <tbody>
            <tr th:each="det : ${detalles}">
                <td th:text="${det.snapshotDescripcion}">Equipo HVAC Samsung WindFree</td>
                <td th:text="${det.cantidad}">1</td>
                <td th:text="${'S/ ' + det.snapshotPrecioUnitario}">S/ 2980.00</td>
                <td th:text="${'S/ ' + det.subtotalLinea}">S/ 2980.00</td>
            </tr>
        </tbody>
    </table>

    <div class="total-box">
        <p class="total-line">Subtotal: <strong th:text="${'S/ ' + cotizacion.subtotal}">S/ 3655.00</strong></p>
        <p class="total-line">Flete Distrital: <strong th:text="${'S/ ' + cotizacion.costoFlete}">S/ 40.00</strong></p>
        <p class="total-line">I.G.V. (18%): <strong th:text="${'S/ ' + cotizacion.igv}">S/ 665.10</strong></p>
        <p class="total-bold">TOTAL: <span th:text="${'S/ ' + cotizacion.total}">S/ 4360.10</span></p>
    </div>
</body>
</html>
```

---

## 5. Navegación Interconectada (Wikilinks de Obsidian)

- **MOC Central:** [[00-MOC-GlobalFrio|00-MOC-GlobalFrio.md]]
- **Arquitectura de Sistemas:** [[02-Arquitectura-y-Backend/HLD-Arquitectura-Sistemas|HLD-Arquitectura-Sistemas.md]]
- **Modelo de Base de Datos y Semilla:** [[02-Arquitectura-y-Backend/Modelo-Base-Datos-y-Dummy-Data|Modelo-Base-Datos-y-Dummy-Data.md]]
- **Seguridad Web y AI:** [[02-Arquitectura-y-Backend/Seguridad-OWASP-y-AI-Risks|Seguridad-OWASP-y-AI-Risks.md]]
- **Frontend y Diseño:**
  - [[03-UI-UX-Design-Handoff/UI-UX-Design-Handoff-Spec|UI-UX-Design-Handoff-Spec.md]]
  - [[03-UI-UX-Design-Handoff/Guia-Angular-para-Vivian|Guia-Angular-para-Vivian.md]]
- **Aseguramiento de Calidad:**
  - [[04-QA-y-Testing/QA-Test-Plan-Acceptance-Matrix|QA-Test-Plan-Acceptance-Matrix.md]]
  - [[04-QA-y-Testing/Playbook-QA-y-Arreglo-de-Errores|Playbook-QA-y-Arreglo-de-Errores.md]]
- **Diagramas Draw.io Relacionados:**
  - [[diagramas/03-flujo-cotizacion-y-patron-decorator.drawio|03-flujo-cotizacion-y-patron-decorator.drawio]]
  - [[diagramas/04-maquina-estados-cotizacion-orden.drawio|04-maquina-estados-cotizacion-orden.drawio]]

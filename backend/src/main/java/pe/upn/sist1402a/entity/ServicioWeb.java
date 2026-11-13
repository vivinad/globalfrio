package pe.upn.sist1402a.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Tarjeta de la sección "01 — Servicios" de la página. Tabla servicio_web.
 */
@Entity
@Table(name = "servicio_web")
@Getter
@Setter
@NoArgsConstructor
public class ServicioWeb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String titulo;

    @Column(nullable = false, length = 255)
    private String descripcion;

    /** Nombre del ícono de Material Icons (ej. ac_unit). */
    @Column(nullable = false, length = 40)
    private String icono;

    @Column(nullable = false)
    private Integer orden;

    @Column(nullable = false)
    private Boolean activo = true;
}

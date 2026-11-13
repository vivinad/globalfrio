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
 * Foto de la galería "02 — Trabajos". Tabla trabajo.
 */
@Entity
@Table(name = "trabajo")
@Getter
@Setter
@NoArgsConstructor
public class Trabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Texto que se ve sobre la foto. */
    @Column(nullable = false, length = 120)
    private String pie;

    /** Ruta de la foto en el frontend (assets/...). */
    @Column(nullable = false, length = 200)
    private String imagen;

    /** Descripción de la foto para accesibilidad. */
    @Column(name = "texto_alternativo", nullable = false, length = 200)
    private String textoAlternativo;

    @Column(nullable = false)
    private Integer orden;

    @Column(nullable = false)
    private Boolean activo = true;
}

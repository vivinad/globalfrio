package pe.upn.sist1402a.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entidad JPA canónica que representa un producto o equipo de refrigeración.
 * Alineada a la lista blanca de anotaciones de AGENTS.md (Sección 2.3 y RF-SEM04-03).
 */
@Entity
@Table(name = "productos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    @Column(length = 255)
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    @NotBlank(message = "La categoría es obligatoria")
    @Column(nullable = false, length = 50)
    private String categoria;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    /**
     * Campo calculado transitorio conforme a la regla técnica SPEC-SEM04-C.
     * No se persiste en la base de datos relacional.
     */
    @Transient
    private String estadoStock;

    public String getEstadoStock() {
        if (stock == null || stock <= 0) {
            return "AGOTADO";
        } else if (stock < 5) {
            return "POR_AGOTAR";
        } else {
            return "DISPONIBLE";
        }
    }
}

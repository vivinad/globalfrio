package pe.upn.sist1402a.dto;

import java.math.BigDecimal;

public record ZonaDto(Long id, String nombre, String distritos, BigDecimal costoTraslado) {
}

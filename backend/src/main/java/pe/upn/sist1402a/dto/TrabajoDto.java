package pe.upn.sist1402a.dto;

/** Foto de Trabajos tal como la usa el frontend (modelo Trabajo). */
public record TrabajoDto(Long id, String imagen, String alt, String pie) {
}

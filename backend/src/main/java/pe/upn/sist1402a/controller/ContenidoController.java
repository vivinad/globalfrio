package pe.upn.sist1402a.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.upn.sist1402a.dto.ItemMantenimientoDto;
import pe.upn.sist1402a.dto.ServicioWebDto;
import pe.upn.sist1402a.dto.TrabajoDto;
import pe.upn.sist1402a.service.ContenidoService;

import java.util.List;

/**
 * API pública con el contenido de la página de inicio (no requiere login).
 */
@RestController
@RequestMapping("/api/contenido")
@Tag(name = "Contenido", description = "Servicios, trabajos y mantenimiento que muestra la página")
public class ContenidoController {

    private final ContenidoService contenidoService;

    public ContenidoController(ContenidoService contenidoService) {
        this.contenidoService = contenidoService;
    }

    @Operation(summary = "Tarjetas de la sección Servicios")
    @GetMapping("/servicios")
    public ResponseEntity<List<ServicioWebDto>> servicios() {
        return ResponseEntity.ok(contenidoService.listarServicios());
    }

    @Operation(summary = "Fotos de la sección Trabajos")
    @GetMapping("/trabajos")
    public ResponseEntity<List<TrabajoDto>> trabajos() {
        return ResponseEntity.ok(contenidoService.listarTrabajos());
    }

    @Operation(summary = "Elementos de la sección Mantenimiento")
    @GetMapping("/mantenimiento")
    public ResponseEntity<List<ItemMantenimientoDto>> mantenimiento() {
        return ResponseEntity.ok(contenidoService.listarMantenimiento());
    }
}

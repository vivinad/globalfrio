package pe.upn.sist1402a.service;

import pe.upn.sist1402a.dto.ItemMantenimientoDto;
import pe.upn.sist1402a.dto.ServicioWebDto;
import pe.upn.sist1402a.dto.TrabajoDto;

import java.util.List;

/**
 * Contenido de la página de inicio leído de la base de datos.
 */
public interface ContenidoService {

    List<ServicioWebDto> listarServicios();

    List<TrabajoDto> listarTrabajos();

    List<ItemMantenimientoDto> listarMantenimiento();
}

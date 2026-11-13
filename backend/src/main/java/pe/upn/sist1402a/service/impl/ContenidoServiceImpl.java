package pe.upn.sist1402a.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upn.sist1402a.dto.ItemMantenimientoDto;
import pe.upn.sist1402a.dto.ServicioWebDto;
import pe.upn.sist1402a.dto.TrabajoDto;
import pe.upn.sist1402a.repository.ItemMantenimientoRepository;
import pe.upn.sist1402a.repository.ServicioWebRepository;
import pe.upn.sist1402a.repository.TrabajoRepository;
import pe.upn.sist1402a.service.ContenidoService;

import java.util.List;

/**
 * Lee de la base de datos las secciones Servicios, Trabajos y Mantenimiento.
 * Así el contenido se cambia en la base sin tocar el código.
 */
@Service
@Transactional(readOnly = true)
public class ContenidoServiceImpl implements ContenidoService {

    private final ServicioWebRepository servicioWebRepository;
    private final TrabajoRepository trabajoRepository;
    private final ItemMantenimientoRepository itemMantenimientoRepository;

    public ContenidoServiceImpl(ServicioWebRepository servicioWebRepository,
                                TrabajoRepository trabajoRepository,
                                ItemMantenimientoRepository itemMantenimientoRepository) {
        this.servicioWebRepository = servicioWebRepository;
        this.trabajoRepository = trabajoRepository;
        this.itemMantenimientoRepository = itemMantenimientoRepository;
    }

    @Override
    public List<ServicioWebDto> listarServicios() {
        return servicioWebRepository.listarActivos().stream()
                .map(s -> new ServicioWebDto(s.getId(), s.getTitulo(), s.getDescripcion(), s.getIcono()))
                .toList();
    }

    @Override
    public List<TrabajoDto> listarTrabajos() {
        return trabajoRepository.listarActivos().stream()
                .map(t -> new TrabajoDto(t.getId(), t.getImagen(), t.getTextoAlternativo(), t.getPie()))
                .toList();
    }

    @Override
    public List<ItemMantenimientoDto> listarMantenimiento() {
        return itemMantenimientoRepository.listarActivos().stream()
                .map(i -> new ItemMantenimientoDto(i.getId(), i.getTitulo(), i.getDescripcion()))
                .toList();
    }
}

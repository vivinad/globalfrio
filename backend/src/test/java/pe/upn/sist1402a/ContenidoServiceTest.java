package pe.upn.sist1402a;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pe.upn.sist1402a.dto.ItemMantenimientoDto;
import pe.upn.sist1402a.dto.ServicioWebDto;
import pe.upn.sist1402a.dto.TrabajoDto;
import pe.upn.sist1402a.service.ContenidoService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * El contenido de la página (Servicios, Trabajos y Mantenimiento) se lee de la base de datos.
 */
@SpringBootTest
@ActiveProfiles("dev")
class ContenidoServiceTest {

    @Autowired
    private ContenidoService contenidoService;

    @Test
    @DisplayName("Servicios: 4 tarjetas en orden, la primera es Instalación")
    void servicios() {
        List<ServicioWebDto> servicios = contenidoService.listarServicios();
        assertEquals(4, servicios.size());
        assertEquals("Instalación", servicios.get(0).titulo());
        assertEquals("construction", servicios.get(0).icono());
    }

    @Test
    @DisplayName("Trabajos: 5 fotos con su ruta y texto alternativo")
    void trabajos() {
        List<TrabajoDto> trabajos = contenidoService.listarTrabajos();
        assertEquals(5, trabajos.size());
        assertEquals("assets/samsung-dvm.jpg", trabajos.get(0).imagen());
    }

    @Test
    @DisplayName("Mantenimiento: 4 elementos en orden")
    void mantenimiento() {
        List<ItemMantenimientoDto> items = contenidoService.listarMantenimiento();
        assertEquals(4, items.size());
        assertEquals("Limpieza profunda", items.get(0).titulo());
    }
}

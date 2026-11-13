package pe.upn.sist1402a;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pe.upn.sist1402a.dto.PaginaDto;
import pe.upn.sist1402a.dto.ProductoCotizadorDto;
import pe.upn.sist1402a.exception.BadRequestException;
import pe.upn.sist1402a.repository.ProductoRepository;
import pe.upn.sist1402a.service.CotizadorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Semana 11: el catálogo se entrega por páginas (Pageable) con la metadata que necesita Angular.
 */
@SpringBootTest
@ActiveProfiles("dev")
class CatalogoPaginadoTest {

    @Autowired
    private CotizadorService cotizadorService;

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    @DisplayName("La primera página trae 5 productos y la metadata para navegar")
    void primeraPagina() {
        long activos = productoRepository.findByCategoriaAndActivo("EQUIPO", true).size()
                + productoRepository.findByCategoriaAndActivo("SERVICIO", true).size();

        PaginaDto<ProductoCotizadorDto> pagina = cotizadorService.listarCatalogo(null, 0, 5);

        assertEquals(5, pagina.contenido().size());
        assertEquals(0, pagina.pagina());
        assertEquals(activos, pagina.totalElementos());
        assertEquals((int) Math.ceil(activos / 5.0), pagina.totalPaginas());
        assertTrue(pagina.primera());
        assertFalse(pagina.ultima());
    }

    @Test
    @DisplayName("Filtrar por EQUIPO devuelve solo los 3 equipos en una sola página")
    void filtrarEquipos() {
        PaginaDto<ProductoCotizadorDto> pagina = cotizadorService.listarCatalogo("EQUIPO", 0, 5);

        assertEquals(3, pagina.totalElementos());
        assertTrue(pagina.contenido().stream().allMatch(p -> "EQUIPO".equals(p.categoria())));
        assertTrue(pagina.ultima());
    }

    @Test
    @DisplayName("Un tamaño de página fuera de rango o una categoría inválida responden 400")
    void parametrosInvalidos() {
        assertThrows(BadRequestException.class, () -> cotizadorService.listarCatalogo(null, 0, 50));
        assertThrows(BadRequestException.class, () -> cotizadorService.listarCatalogo(null, -1, 5));
        assertThrows(BadRequestException.class, () -> cotizadorService.listarCatalogo("OTRA", 0, 5));
    }
}

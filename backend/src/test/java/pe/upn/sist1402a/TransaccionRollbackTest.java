package pe.upn.sist1402a;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import pe.upn.sist1402a.dto.CotizacionRequestDto;
import pe.upn.sist1402a.dto.CotizacionResponseDto;
import pe.upn.sist1402a.dto.ItemCotizacionDto;
import pe.upn.sist1402a.dto.ZonaDto;
import pe.upn.sist1402a.exception.BadRequestException;
import pe.upn.sist1402a.exception.ResourceNotFoundException;
import pe.upn.sist1402a.repository.CotizacionRepository;
import pe.upn.sist1402a.repository.ProductoRepository;
import pe.upn.sist1402a.service.CotizadorService;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Semana 12: transacciones y rollback.
 * Transacción → Operación A (guardar la cotización) → Operación B (falla) → Rollback.
 * Si algo falla, no debe quedar ninguna cotización a medio guardar.
 */
@SpringBootTest
@ActiveProfiles("dev")
class TransaccionRollbackTest {

    @Autowired
    private CotizadorService cotizadorService;

    @Autowired
    private CotizacionRepository cotizacionRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private CotizacionRequestDto solicitud(String producto) {
        Long zonaId = cotizadorService.obtenerCatalogo().zonas().stream()
                .filter(z -> z.nombre().equals("Lima Norte")).map(ZonaDto::id).findFirst().orElseThrow();
        Long productoId = productoRepository.findByNombreContainingIgnoreCase(producto).get(0).getId();
        return new CotizacionRequestDto("REPARACION", "HOGAR", zonaId, null,
                List.of(new ItemCotizacionDto(productoId, 1)));
    }

    @Test
    @DisplayName("Si una operación falla después de guardar, la cotización se revierte (rollback)")
    void fallaDespuesDeGuardar() {
        long antes = cotizacionRepository.count();
        AtomicReference<String> codigo = new AtomicReference<>();
        TransactionTemplate transaccion = new TransactionTemplate(transactionManager);

        assertThrows(IllegalStateException.class, () -> transaccion.executeWithoutResult(estado -> {
            // Operación A: se guarda la cotización y se obtiene su código
            CotizacionResponseDto guardada = cotizadorService.guardar(solicitud("Diagnóstico técnico"));
            codigo.set(guardada.codigo());
            assertNotNull(guardada.codigo());
            // Operación B: falla (por ejemplo, no se pudo generar el documento)
            throw new IllegalStateException("Fallo simulado en la operación B");
        }));

        // Rollback: no quedó la cotización ni sus líneas
        assertEquals(antes, cotizacionRepository.count());
        assertThrows(ResourceNotFoundException.class, () -> cotizadorService.obtenerPorCodigo(codigo.get()));
    }

    @Test
    @DisplayName("Si los datos no son válidos, guardar() no registra nada")
    void datosInvalidosNoGuardan() {
        long antes = cotizacionRepository.count();
        // Un servicio de instalación no corresponde a una reparación: BadRequestException
        assertThrows(BadRequestException.class, () -> cotizadorService.guardar(solicitud("Instalación básica 12 000")));
        assertEquals(antes, cotizacionRepository.count());
    }
}

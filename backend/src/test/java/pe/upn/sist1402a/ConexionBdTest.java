package pe.upn.sist1402a;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pe.upn.sist1402a.entity.Producto;
import pe.upn.sist1402a.repository.ProductoRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prueba de integración: verifica la conexión real con SQL Server (perfil "sqlserver").
 * Requiere que SQL Server esté encendido, que se hayan ejecutado los scripts de
 * backend/database y que las variables DB_USUARIO / DB_CLAVE estén configuradas.
 */
@SpringBootTest
@ActiveProfiles("sqlserver")
class ConexionBdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    @DisplayName("La aplicación abre una conexión válida con SQL Server y la base globalfrio_db")
    void conexionConSqlServer() throws Exception {
        try (Connection conexion = dataSource.getConnection()) {
            DatabaseMetaData meta = conexion.getMetaData();
            System.out.println("Conectado a: " + meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion());
            System.out.println("Base de datos: " + conexion.getCatalog());

            assertTrue(conexion.isValid(2), "La conexión no es válida");
            assertTrue(meta.getDatabaseProductName().contains("SQL Server"), "No es SQL Server");
            assertTrue("globalfrio_db".equalsIgnoreCase(conexion.getCatalog()), "No es la base globalfrio_db");
        }
    }

    @Test
    @DisplayName("La entidad Producto lee el catálogo de equipos desde la tabla productos (RF01)")
    void leerCatalogoDesdeSqlServer() {
        long total = productoRepository.count();
        System.out.println("Registros en la tabla productos (equipos y servicios): " + total);

        List<Producto> equipos = productoRepository.findByCategoriaAndActivo("EQUIPO", true);
        equipos.forEach(p -> System.out.println(" - " + p.getNombre() + " | S/ " + p.getPrecio()));

        assertFalse(equipos.isEmpty(), "Ejecuta el script 03_datos_iniciales.sql antes de esta prueba");
    }
}

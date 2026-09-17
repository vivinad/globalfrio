package pe.upn.sist1402a.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upn.sist1402a.entity.Producto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositorio JPA para la entidad Producto.
 * Cumple con las directivas de RF-SEM04-02 y SPEC-SEM04-C (JPQL parametrizado, sin concatenaciones).
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Búsqueda por coincidencia de nombre ignorando mayúsculas y minúsculas.
     * @param nombre Término a buscar
     * @return Lista de productos coincidentes
     */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Búsqueda por categoría exacta y estado activo.
     * @param categoria Categoría del producto
     * @param activo Estado de actividad
     * @return Lista de productos filtrados
     */
    List<Producto> findByCategoriaAndActivo(String categoria, Boolean activo);

    /**
     * Consulta JPQL parametrizada para rango de precios (Mitigación estricta de SQL Injection).
     * @param minimo Precio límite inferior
     * @param maximo Precio límite superior
     * @return Lista de productos dentro del rango
     */
    @Query("SELECT p FROM Producto p WHERE p.precio BETWEEN :minimo AND :maximo ORDER BY p.precio ASC")
    List<Producto> findByRangoPrecio(@Param("minimo") BigDecimal minimo, @Param("maximo") BigDecimal maximo);
}

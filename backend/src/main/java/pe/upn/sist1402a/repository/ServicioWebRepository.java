package pe.upn.sist1402a.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.upn.sist1402a.entity.ServicioWeb;

import java.util.List;

@Repository
public interface ServicioWebRepository extends JpaRepository<ServicioWeb, Long> {

    /** Registros activos en el orden en que se muestran en la página. */
    @Query("SELECT x FROM ServicioWeb x WHERE x.activo = true ORDER BY x.orden ASC")
    List<ServicioWeb> listarActivos();
}

package pe.upn.sist1402a.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upn.sist1402a.entity.TipoCliente;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoClienteRepository extends JpaRepository<TipoCliente, Long> {

    List<TipoCliente> findByActivoTrueOrderByIdAsc();

    Optional<TipoCliente> findByCodigoAndActivoTrue(String codigo);
}

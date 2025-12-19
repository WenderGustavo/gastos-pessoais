package io.github.wendergustavo.gastospessoais.repository;

import io.github.wendergustavo.gastospessoais.entity.Gasto;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface GastoRepository extends JpaRepository<Gasto, UUID>, JpaSpecificationExecutor<Gasto> {

    boolean existsByUsuario(Usuario usuario);

    List<Gasto> findByUsuarioEmailOrderByCreatedAtDesc(String email);

    List<Gasto> findByUsuarioId(UUID id);
}

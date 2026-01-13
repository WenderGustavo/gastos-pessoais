package io.github.wendergustavo.gastospessoais.repository;

import io.github.wendergustavo.gastospessoais.entity.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface GastoRepository extends JpaRepository<Gasto, UUID>, JpaSpecificationExecutor<Gasto> {

    boolean existsByUsuarioId(UUID id);

    List<Gasto> findByUsuarioEmailOrderByCreatedAtDesc(String email);

    List<Gasto> findByUsuarioId(UUID id);

    boolean existsByIdAndUsuarioId(UUID gastoId, UUID usuarioId);
}

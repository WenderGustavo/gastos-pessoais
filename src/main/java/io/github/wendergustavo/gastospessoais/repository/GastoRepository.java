package io.github.wendergustavo.gastospessoais.repository;

import io.github.wendergustavo.gastospessoais.model.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GastoRepository extends JpaRepository<Gasto, UUID> {
}

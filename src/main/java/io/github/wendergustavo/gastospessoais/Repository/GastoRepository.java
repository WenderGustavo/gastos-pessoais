package io.github.wendergustavo.gastospessoais.Repository;

import io.github.wendergustavo.gastospessoais.Model.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GastoRepository extends JpaRepository<Gasto, UUID> {
}

package io.github.wendergustavo.gastospessoais.repository;

import io.github.wendergustavo.gastospessoais.entity.Gasto;
import io.github.wendergustavo.gastospessoais.entity.GastoTipo;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface GastoRepository extends JpaRepository<Gasto, UUID> {

    boolean existsByUsuario(Usuario usuario);

    boolean existsByDescricaoAndGastoTipoAndValorAndUsuario(
            String descricao,
            GastoTipo gastoTipo,
            BigDecimal valor,
            Usuario usuario
    );

    boolean existsByDescricaoAndGastoTipoAndValorAndUsuarioAndIdNot(
            String descricao,
            GastoTipo gastoTipo,
            BigDecimal valor,
            Usuario usuario,
            UUID id
    );
    List<Gasto> findByUsuarioEmailOrderByCreatedAtDesc(String email);

    List<Gasto> findByUsuarioId(UUID id);
}

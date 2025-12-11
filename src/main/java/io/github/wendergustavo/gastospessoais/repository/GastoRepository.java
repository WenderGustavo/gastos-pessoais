package io.github.wendergustavo.gastospessoais.repository;

import io.github.wendergustavo.gastospessoais.entity.Gasto;
import io.github.wendergustavo.gastospessoais.entity.GastoTipo;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GastoRepository extends JpaRepository<Gasto, UUID> {

    boolean existsByUsuario(Usuario usuario);

    boolean existsByDescricaoAndGastoTipoAndValorAndDataGastoAndUsuario(
            String descricao,
            GastoTipo gastoTipo,
            BigDecimal valor,
            LocalDate dataGasto,
            Usuario usuario
    );

    boolean existsByDescricaoAndGastoTipoAndValorAndDataGastoAndUsuarioAndIdNot(
            String descricao, GastoTipo gastoTipo, BigDecimal valor,
            LocalDate dataGasto, Usuario usuario, UUID id
    );
    List<Gasto> findByUsuarioEmailOrderByDataGastoDesc(String email);

    List<Gasto> findByUsuarioId(UUID id);
}

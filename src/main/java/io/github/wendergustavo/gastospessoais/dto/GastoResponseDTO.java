package io.github.wendergustavo.gastospessoais.dto;

import io.github.wendergustavo.gastospessoais.model.GastoTipo;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GastoResponseDTO(

        String descricao,
        GastoTipo gastoTipo,
        BigDecimal valor,
        LocalDate dataGasto,
        UsuarioResponseDTO usuario

) {
}

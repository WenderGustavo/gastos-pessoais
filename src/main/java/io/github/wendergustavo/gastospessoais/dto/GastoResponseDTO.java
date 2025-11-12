package io.github.wendergustavo.gastospessoais.dto;

import io.github.wendergustavo.gastospessoais.entity.GastoTipo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record GastoResponseDTO(

        UUID id,
        String descricao,
        GastoTipo gastoTipo,
        BigDecimal valor,
        LocalDate dataGasto

) {
}

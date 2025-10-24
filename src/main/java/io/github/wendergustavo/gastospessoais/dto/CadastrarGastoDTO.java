package io.github.wendergustavo.gastospessoais.dto;

import io.github.wendergustavo.gastospessoais.entity.GastoTipo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CadastrarGastoDTO(

        @Size(min = 0, max = 150)
        String descricao,

        @NotNull(message = "Campo obrigatorio")
        GastoTipo gastoTipo,

        @NotNull(message = "Campo obrigatorio")
        BigDecimal valor,

        @PastOrPresent(message = "A data não pode ser uma data futura")
        LocalDate dataGasto,

        @NotNull(message = "campo obrigatorio")
        UUID idUsuario
) {
}

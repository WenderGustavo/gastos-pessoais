package io.github.wendergustavo.gastospessoais.dto.gasto;

import io.github.wendergustavo.gastospessoais.entity.GastoTipo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CadastrarGastoDTO(

        @Size(min = 0, max = 150)
        String descricao,

        @NotNull(message = "Campo obrigatorio")
        GastoTipo gastoTipo,

        @NotNull(message = "Campo obrigatorio")
        BigDecimal valor,

        @NotNull(message = "campo obrigatorio")
        UUID idUsuario
) {
}

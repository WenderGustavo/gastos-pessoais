package io.github.wendergustavo.gastospessoais.dto;

import java.util.List;

public record ListaGastosResponseDTO(

        List<GastoResponseDTO> gastos
) {
}

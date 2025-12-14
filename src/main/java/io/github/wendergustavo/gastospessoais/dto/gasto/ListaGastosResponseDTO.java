package io.github.wendergustavo.gastospessoais.dto.gasto;

import java.util.List;

public record ListaGastosResponseDTO(

        List<GastoResponseDTO> gastos
) {
}

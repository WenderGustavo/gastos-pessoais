package io.github.wendergustavo.gastospessoais.dto;

import java.util.UUID;

public record UsuarioResponseDTO(

        UUID id,
        String nome,
        String email
) {
}

package io.github.wendergustavo.gastospessoais.dto.usuario;

import io.github.wendergustavo.gastospessoais.entity.Roles;

import java.util.UUID;
public record UsuarioResponseDTO(

        UUID id,
        String nome,
        String email,
        Roles role
) {
}

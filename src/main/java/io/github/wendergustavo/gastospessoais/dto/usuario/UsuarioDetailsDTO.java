package io.github.wendergustavo.gastospessoais.dto.usuario;

import java.util.UUID;

public record UsuarioDetailsDTO(UUID id, String email, String role) {
}

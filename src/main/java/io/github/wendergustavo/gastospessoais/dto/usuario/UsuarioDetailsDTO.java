package io.github.wendergustavo.gastospessoais.dto;

import java.util.UUID;

public record UsuarioDetailsDTO(UUID id, String email, String role) {
}

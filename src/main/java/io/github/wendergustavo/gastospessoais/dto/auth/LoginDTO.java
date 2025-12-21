package io.github.wendergustavo.gastospessoais.dto.auth;

import jakarta.validation.constraints.NotNull;

public record LoginDTO(

        @NotNull(message = "Campo obrigatorio.")
        String email,
        @NotNull(message = "Campo obrigatorio.")
        String senha
) {}
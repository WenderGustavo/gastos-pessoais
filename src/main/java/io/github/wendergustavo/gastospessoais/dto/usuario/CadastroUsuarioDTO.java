package io.github.wendergustavo.gastospessoais.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastroUsuarioDTO(

        @NotBlank
        String nome,
        @Email
        String email,
        @NotBlank
        @Size(min = 8, max = 128)
        String senha
) {
}

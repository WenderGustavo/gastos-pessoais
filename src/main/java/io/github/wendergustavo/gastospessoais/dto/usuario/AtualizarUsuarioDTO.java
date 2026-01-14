package io.github.wendergustavo.gastospessoais.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record AtualizarUsuarioDTO(

        @Size(min = 3, max = 100, message = "Campo fora do tamanho padrao.")
        String nome,
        @Email(message = "Email invalido.")
        String email,

        @Size(min = 8, max = 128, message = "Campo fora do tamanho padrao.")
        String senha) {
}

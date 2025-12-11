package io.github.wendergustavo.gastospessoais.dto.usuario;

import io.github.wendergustavo.gastospessoais.entity.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioDTO(

        @NotNull(message = "Campo obrigatorio.")
        @Size(min = 3, max = 100, message = "Campo fora do tamanho padrao.")
        String nome,

        @NotBlank(message = "Campo obrigatorio.")
        @Email(message = "Email invalido.")
        String email,

        @NotNull(message = "Campo obrigatorio.")
        @Size(min = 8, max = 128, message = "Campo fora do tamanho padrao.")
        String senha,

        @NotNull(message = "Campo obrigatorio.")
        Roles role

){
}

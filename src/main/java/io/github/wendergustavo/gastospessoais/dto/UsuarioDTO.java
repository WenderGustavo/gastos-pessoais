package io.github.wendergustavo.gastospessoais.dto;

import io.github.wendergustavo.gastospessoais.model.Gasto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record UsuarioDTO(

        UUID id,

        @NotNull(message = "Campo obrigatorio")
        @Size(min = 3, max = 100, message = "Campo fora do tamanho padrao.")
        String nome,

        @NotBlank(message = "Campo obrigatorio")
        @Email(message = "Email invalido")
        String email,

        @NotNull(message = "Campo obrigatorio")
        String senha,

        List<Gasto> gastos) {
}

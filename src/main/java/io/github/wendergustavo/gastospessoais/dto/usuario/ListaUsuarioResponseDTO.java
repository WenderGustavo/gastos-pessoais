package io.github.wendergustavo.gastospessoais.dto.usuario;

import java.util.List;

public record ListaUsuarioResponseDTO(

        List<UsuarioResponseDTO> usuarios

) {
}

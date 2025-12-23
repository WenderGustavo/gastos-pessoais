package io.github.wendergustavo.gastospessoais.mapper;


import io.github.wendergustavo.gastospessoais.dto.usuario.*;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO usuarioDTO);

    Usuario toEntitySimple(CadastroUsuarioDTO usuarioDTO);

    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    default ListaUsuarioResponseDTO toListResponseDTO(List<Usuario> usuarios) {
        List<UsuarioResponseDTO> dtos = usuarios.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return new ListaUsuarioResponseDTO(dtos);
    }

    void updateEntityFromDTO(AtualizarUsuarioDTO atualizarUsuarioDTO, @MappingTarget Usuario usuario);
}

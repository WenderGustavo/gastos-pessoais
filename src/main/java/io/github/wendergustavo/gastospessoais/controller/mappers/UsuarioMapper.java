package io.github.wendergustavo.gastospessoais.controller.mappers;


import io.github.wendergustavo.gastospessoais.dto.UsuarioDTO;
import io.github.wendergustavo.gastospessoais.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO usuarioDTO);

    UsuarioDTO toDTO(Usuario usuario);

}

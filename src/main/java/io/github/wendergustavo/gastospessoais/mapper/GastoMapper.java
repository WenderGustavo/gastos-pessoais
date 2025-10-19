package io.github.wendergustavo.gastospessoais.mapper;

import io.github.wendergustavo.gastospessoais.dto.CadastrarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.GastoResponseDTO;
import io.github.wendergustavo.gastospessoais.model.Gasto;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public abstract class GastoMapper
 {
     @Autowired
     UsuarioRepository usuarioRepository;

     @Mapping(target = "usuario", expression = "java( usuarioRepository.findById(dto.idUsuario()).orElse(null) )")
     @Mapping(target = "gastoTipo", source = "gastoTipo")
     public abstract Gasto toEntity(CadastrarGastoDTO dto);

     public abstract GastoResponseDTO toDTO(Gasto gasto);
}

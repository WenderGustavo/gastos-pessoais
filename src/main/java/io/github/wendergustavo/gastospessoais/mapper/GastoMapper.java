package io.github.wendergustavo.gastospessoais.mapper;

import io.github.wendergustavo.gastospessoais.dto.AtualizarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.CadastrarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.GastoSimplesDTO;
import io.github.wendergustavo.gastospessoais.entity.Gasto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface GastoMapper {

//     @Mapping(target = "usuario", expression = "java( usuarioRepository.findById(dto.idUsuario()).orElse(null) )")
//     @Mapping(target = "gastoTipo", source = "gastoTipo")


    Gasto toEntity(CadastrarGastoDTO dto);

    GastoResponseDTO toDTO(Gasto gasto);

    void updateEntityFromDTO(AtualizarGastoDTO gastoDTO, @MappingTarget Gasto gasto) ;

}

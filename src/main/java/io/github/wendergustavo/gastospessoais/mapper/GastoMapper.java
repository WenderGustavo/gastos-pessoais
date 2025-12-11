package io.github.wendergustavo.gastospessoais.mapper;

import io.github.wendergustavo.gastospessoais.dto.gasto.AtualizarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.gasto.CadastrarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.gasto.GastoResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.gasto.ListaGastosResponseDTO;
import io.github.wendergustavo.gastospessoais.entity.Gasto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface GastoMapper {

//     @Mapping(target = "usuario", expression = "java( usuarioRepository.findById(dto.idUsuario()).orElse(null) )")
//     @Mapping(target = "gastoTipo", source = "gastoTipo")


    Gasto toEntity(CadastrarGastoDTO dto);

    GastoResponseDTO toDTO(Gasto gasto);

    default ListaGastosResponseDTO toListResponseDTO(List<Gasto> gastos) {
        List<GastoResponseDTO> dtos = gastos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new ListaGastosResponseDTO(dtos);
    }

    void updateEntityFromDTO(AtualizarGastoDTO gastoDTO, @MappingTarget Gasto gasto) ;

}

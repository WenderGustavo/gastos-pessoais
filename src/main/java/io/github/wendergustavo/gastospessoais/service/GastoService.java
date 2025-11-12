package io.github.wendergustavo.gastospessoais.service;


import io.github.wendergustavo.gastospessoais.dto.AtualizarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.CadastrarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.GastoSimplesDTO;
import io.github.wendergustavo.gastospessoais.entity.Gasto;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import io.github.wendergustavo.gastospessoais.exceptions.GastoNaoEncontradoException;
import io.github.wendergustavo.gastospessoais.mapper.GastoMapper;
import io.github.wendergustavo.gastospessoais.repository.GastoRepository;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import io.github.wendergustavo.gastospessoais.validador.GastoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GastoService {

    private final GastoRepository gastoRepository;
    private final GastoValidator gastoValidator;
    private final GastoMapper gastoMapper;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public GastoSimplesDTO salvar(CadastrarGastoDTO dto) {

        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        Gasto gasto = gastoMapper.toEntity(dto);
        gasto.setUsuario(usuario);
        gastoValidator.validar(gasto);
        Gasto gastoSalvo = gastoRepository.save(gasto);
        return gastoMapper.toDTO(gastoSalvo);
    }

    public GastoSimplesDTO buscarPorId(UUID id){

        if(id == null){
            throw new IllegalArgumentException(id +"Gasto ID must not be null.");
        }

        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new GastoNaoEncontradoException(id));

        return gastoMapper.toDTO(gasto);
    }

    @Transactional
    public GastoResponseDTO atualizar(UUID id, AtualizarGastoDTO gastoDTO){

        if(id == null){
            throw  new IllegalArgumentException(id +"Gasto ID must not be null.");
        }

        Gasto gastoExistente = gastoRepository.findById(id)
                .orElseThrow(() -> new GastoNaoEncontradoException(id));

        gastoMapper.updateEntityFromDTO(gastoDTO,gastoExistente);


        if (gastoExistente.getDataGasto() == null) {
            gastoExistente.setDataGasto(LocalDate.now());
        }

        gastoValidator.validar(gastoExistente);
        gastoRepository.save(gastoExistente);

        return gastoMapper.toDTO(gastoExistente);

    }

    @Transactional
    public void deletar(UUID id){

        if(id == null){
            throw new IllegalArgumentException(id + "Gasto ID must not be null.");
        }

        Gasto gasto = gastoRepository.findById(id)
                        .orElseThrow(() -> new GastoNaoEncontradoException(id));

        gastoRepository.delete(gasto);
    }

}

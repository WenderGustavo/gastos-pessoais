package io.github.wendergustavo.gastospessoais.service;

import io.github.wendergustavo.gastospessoais.dto.GastoSimplesDTO;
import io.github.wendergustavo.gastospessoais.dto.UsuarioDTO;
import io.github.wendergustavo.gastospessoais.dto.UsuarioResponseDTO;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import io.github.wendergustavo.gastospessoais.exceptions.OperacaoNaoPermitidaException;
import io.github.wendergustavo.gastospessoais.exceptions.UsuarioNaoEncontradoException;
import io.github.wendergustavo.gastospessoais.mapper.GastoMapper;
import io.github.wendergustavo.gastospessoais.mapper.UsuarioMapper;
import io.github.wendergustavo.gastospessoais.repository.GastoRepository;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import io.github.wendergustavo.gastospessoais.validador.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioValidator usuarioValidator;
    private final UsuarioMapper usuarioMapper;
    private final GastoRepository gastoRepository;
    private final GastoMapper gastoMapper;


    @Transactional
    public UsuarioResponseDTO salvar(UsuarioDTO usuarioDTO){

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        usuarioValidator.validar(usuario);
        Usuario salvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(salvo);
    }

    public UsuarioResponseDTO buscarPorId(UUID id){

        if(id == null){
            throw new IllegalArgumentException(id + "User ID must not be null.");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        return usuarioMapper.toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(UUID id, UsuarioDTO usuarioDTO){

        if(id == null){
            throw  new IllegalArgumentException(id + "User ID must not be null.");
        }

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        usuarioMapper.updateEntityFromDTO(usuarioDTO,usuarioExistente);

        usuarioValidator.validar(usuarioExistente);

        Usuario salvo = usuarioRepository.save(usuarioExistente);

        return usuarioMapper.toResponseDTO(salvo);
    }

    @Transactional
    public void deletar(UUID id){

        if(id == null){
            throw new IllegalArgumentException(id + "User ID must not be null.");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        if(possuiGasto(usuario)){
            throw new OperacaoNaoPermitidaException("It is not allowed to delete a user who has expenses.");
        }

        usuarioRepository.delete(usuario);
    }

    public List<GastoSimplesDTO> listarGastosPorEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }

        return gastoRepository.findByUsuarioEmailOrderByDataGastoDesc(email)
                .stream()
                .map(gastoMapper::toDTO)
                .toList();
    }


    public boolean possuiGasto(Usuario usuario){
        return gastoRepository.existsByUsuario(usuario);
    }


}

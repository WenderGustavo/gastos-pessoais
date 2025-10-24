package io.github.wendergustavo.gastospessoais.service;

import io.github.wendergustavo.gastospessoais.exceptions.OperacaoNaoPermitidaException;
import io.github.wendergustavo.gastospessoais.model.Gasto;
import io.github.wendergustavo.gastospessoais.model.Usuario;
import io.github.wendergustavo.gastospessoais.repository.GastoRepository;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import io.github.wendergustavo.gastospessoais.validador.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioValidator usuarioValidator;
    private final UsuarioMapper usuarioMapper;
    private final GastoRepository gastoRepository;


    @Transactional
    public Usuario salvar(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        usuarioValidator.validar(usuario);
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorId(UUID id){

        if(id == null){
            throw new IllegalArgumentException("User ID must not be null.");
        }
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toResponseDTO);
    }

    @Transactional
    public void deletar(Usuario usuario){

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        if(possuiGasto(usuario)){
            throw new OperacaoNaoPermitidaException("It is not allowed to delete a user who has expenses.");
        }

        usuarioRepository.delete(usuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(UUID id, UsuarioDTO usuarioDTO){

        if(id == null){
            throw  new IllegalArgumentException("To update, the User must already be registered.");
        }

        Usuario usuarioExistente = usuarioRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("User not found."));

        usuarioMapper.updateEntityFromDTO(usuarioDTO,usuarioExistente);

        usuarioValidator.validar(usuarioExistente);

        Usuario salvo = usuarioRepository.save(usuarioExistente);

        return usuarioMapper.toResponseDTO(salvo);
    }

    public List<Gasto> listarGastosPorEmail(String email){
        if(email == null || email.isBlank()){
            throw new IllegalArgumentException("Email must not be null or empty");
        }

        return usuarioRepository.listarGastosPorEmail(email)
                .stream()
                .map(gastoMapper::toDTO)
                .toList();
    }

        usuarioValidator.validar(usuario);
        usuarioRepository.save(usuario);
    }

    public boolean possuiGasto(Usuario usuario){
        return gastoRepository.existsByUsuario(usuario);
    }


}

package io.github.wendergustavo.gastospessoais.service;

import io.github.wendergustavo.gastospessoais.dto.GastoResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.ListaUsuarioResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.UsuarioDTO;
import io.github.wendergustavo.gastospessoais.dto.UsuarioResponseDTO;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import io.github.wendergustavo.gastospessoais.exceptions.OperacaoNaoPermitidaException;
import io.github.wendergustavo.gastospessoais.exceptions.UsuarioEmailNaoEncontradoException;
import io.github.wendergustavo.gastospessoais.exceptions.UsuarioIdNaoEncontradoException;
import io.github.wendergustavo.gastospessoais.mapper.GastoMapper;
import io.github.wendergustavo.gastospessoais.mapper.UsuarioMapper;
import io.github.wendergustavo.gastospessoais.repository.GastoRepository;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import io.github.wendergustavo.gastospessoais.validador.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class  UsuarioService {

    private final UsuarioRepository  usuarioRepository;
    private final UsuarioValidator usuarioValidator;
    private final UsuarioMapper usuarioMapper;
    private final GastoRepository gastoRepository;
    private final GastoMapper gastoMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponseDTO salvar(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        usuarioValidator.validar(usuario);
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        Usuario salvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(salvo);
    }

    public UsuarioResponseDTO buscarPorId(UUID id){

        if(id == null){
            throw new IllegalArgumentException(id + "User ID must not be null.");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioIdNaoEncontradoException(id));

        return usuarioMapper.toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(UUID id, UsuarioDTO usuarioDTO){

        if(id == null){
            throw  new IllegalArgumentException(id + "User ID must not be null.");
        }

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioIdNaoEncontradoException(id));

        usuarioMapper.updateEntityFromDTO(usuarioDTO,usuarioExistente);

        if (usuarioDTO.senha() != null && !usuarioDTO.senha().isBlank()
                && !usuarioExistente.getSenha().equals(usuarioDTO.senha())) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioDTO.senha()));
        }

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
                .orElseThrow(() -> new UsuarioIdNaoEncontradoException(id));

        if(possuiGasto(usuario)){
            throw new OperacaoNaoPermitidaException("It is not allowed to delete a user who has expenses.");
        }

        usuarioRepository.delete(usuario);
    }

    public List<GastoResponseDTO> listarGastosPorEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }

        return gastoRepository.findByUsuarioEmailOrderByDataGastoDesc(email)
                .stream()
                .map(gastoMapper::toDTO)
                .toList();
    }

    public ListaUsuarioResponseDTO buscarTodosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarioMapper.toListResponseDTO(usuarios);
    }

    public Usuario obterPorEmail(String email){

        if (email == null || email.isBlank()){
            throw new IllegalArgumentException("Email must not be null or empty");
        }

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioEmailNaoEncontradoException("User not found with this email"));
    }

    public boolean possuiGasto(Usuario usuario){
        return gastoRepository.existsByUsuario(usuario);
    }
}

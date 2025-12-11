package io.github.wendergustavo.gastospessoais.service;

import io.github.wendergustavo.gastospessoais.dto.gasto.GastoResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.usuario.AtualizarUsuarioDTO;
import io.github.wendergustavo.gastospessoais.dto.usuario.ListaUsuarioResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.usuario.UsuarioDTO;
import io.github.wendergustavo.gastospessoais.dto.usuario.UsuarioResponseDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioValidator usuarioValidator;
    private final UsuarioMapper usuarioMapper;
    private final GastoRepository gastoRepository;
    private final GastoMapper gastoMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponseDTO salvar(UsuarioDTO usuarioDTO){

        log.info("Criando novo usuário: email={}", usuarioDTO.email());
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        usuarioValidator.validar(usuario);
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        Usuario salvo = usuarioRepository.save(usuario);
        log.info("Usuário criado com sucesso: id={}", salvo.getId());
        return usuarioMapper.toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(UUID id){

        log.info("Buscando usuário por id={}", id);
        if(id == null){
            log.error("ID informado é nulo");
            throw new IllegalArgumentException(id + "User ID must not be null.");
        }
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado: id={}", id);
                    return new UsuarioIdNaoEncontradoException(id);
                });
        log.info("Usuário encontrado: id={}", id);
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(UUID id, AtualizarUsuarioDTO usuarioDTO){

        log.info("Atualizando usuário: id={}", id);
        if(id == null){
            log.error("ID informado é nulo");
            throw  new IllegalArgumentException(id + "User ID must not be null.");
        }
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado para atualização: id={}", id);
                    return new UsuarioIdNaoEncontradoException(id);
                });

        usuarioMapper.updateEntityFromDTO(usuarioDTO, usuarioExistente);

        if (usuarioDTO.senha() != null && !usuarioDTO.senha().isBlank()
                && !usuarioExistente.getSenha().equals(usuarioDTO.senha())) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioDTO.senha()));
        }

        usuarioValidator.validar(usuarioExistente);

        String senhaCriptografada = passwordEncoder.encode(usuarioExistente.getSenha());
        usuarioExistente.setSenha(senhaCriptografada);

        Usuario salvo = usuarioRepository.save(usuarioExistente);
        log.info("Usuário atualizado com sucesso: id={}", salvo.getId());
        return usuarioMapper.toResponseDTO(salvo);
    }

    @Transactional
    public void deletar(UUID id){

        log.info("Deletando usuário: id={}", id);

        if(id == null){
            log.error("ID informado é nulo");

            throw new IllegalArgumentException(id + "User ID must not be null.");
        }
        Usuario usuario = usuarioRepository.findById(id)

                .orElseThrow(() -> {

                    log.warn("Usuário não encontrado para deleção: id={}", id);
                    return new UsuarioIdNaoEncontradoException(id);
                });

        if(possuiGasto(usuario)){

            log.warn("Tentativa de deletar usuário com gastos: id={}", id);
            throw new OperacaoNaoPermitidaException("It is not allowed to delete a user who has expenses.");
        }

        usuarioRepository.delete(usuario);
        log.info("Usuário deletado: id={}", id);
    }

    @Transactional(readOnly = true)
    public List<GastoResponseDTO> listarGastosPorEmail(String email) {

        log.info("Listando gastos para email={}", email);
        if (email == null || email.isBlank()) {

            log.error("Email informado é nulo ou vazio");
            throw new IllegalArgumentException("Email must not be null or empty");
        }
        return gastoRepository.findByUsuarioEmailOrderByDataGastoDesc(email)
                .stream()
                .map(gastoMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ListaUsuarioResponseDTO buscarTodosUsuarios() {

        log.info("Listando todos os usuários");

        List<Usuario> usuarios = usuarioRepository.findAll();

        log.info("Total de usuários encontrados={}", usuarios.size());

        return usuarioMapper.toListResponseDTO(usuarios);
    }

    @Transactional(readOnly = true)
    public Usuario obterPorEmail(String email){

        log.info("Buscando usuário por email={}", email);

        if (email == null || email.isBlank()){

            log.error("Email informado é nulo ou vazio");
            throw new IllegalArgumentException("Email must not be null or empty");
        }
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado: email={}", email);
                    return new UsuarioEmailNaoEncontradoException("User not found with this email");
                });
    }

    public boolean possuiGasto(Usuario usuario){
        return gastoRepository.existsByUsuario(usuario);
    }
}

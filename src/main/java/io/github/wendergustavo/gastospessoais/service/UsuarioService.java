package io.github.wendergustavo.gastospessoais.service;

import io.github.wendergustavo.gastospessoais.dto.gasto.GastoResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.usuario.*;
import io.github.wendergustavo.gastospessoais.entity.Roles;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import io.github.wendergustavo.gastospessoais.exceptions.CampoInvalidoException;
import io.github.wendergustavo.gastospessoais.exceptions.OperacaoNaoPermitidaException;
import io.github.wendergustavo.gastospessoais.exceptions.RegistroDuplicadoException;
import io.github.wendergustavo.gastospessoais.exceptions.UsuarioIdNaoEncontradoException;
import io.github.wendergustavo.gastospessoais.mapper.GastoMapper;
import io.github.wendergustavo.gastospessoais.mapper.UsuarioMapper;
import io.github.wendergustavo.gastospessoais.repository.GastoRepository;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import io.github.wendergustavo.gastospessoais.validador.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    public UsuarioResponseDTO cadastrarProprioUsuario(CadastroUsuarioDTO dto) {
        log.info("Novo auto-cadastro solicitado: email={}", dto.email());

        Usuario usuario = usuarioMapper.toEntitySimple(dto);

        usuario.setRole(Roles.USER);

        return salvarInterno(usuario);
    }


    @Transactional
    public UsuarioResponseDTO cadastrarPeloAdmin(UsuarioDTO dto) {
        log.info("Admin criando usuário: email={}", dto.email());

        Usuario usuario = usuarioMapper.toEntity(dto);

        if (usuario.getRole() == null) {
            usuario.setRole(Roles.USER);
        }

        return salvarInterno(usuario);
    }

    private UsuarioResponseDTO salvarInterno(Usuario usuario) {
        usuarioValidator.validarParaCadastro(usuario);

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        Usuario salvo = usuarioRepository.save(usuario);
        log.info("Usuário persistido com sucesso: id={}", salvo.getId());

        return usuarioMapper.toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(UUID id){

        log.info("Buscando usuário por id={}", id);
        if(id == null){
            log.error("ID informado é nulo");
            throw new IllegalArgumentException(id + "O id do usuario não pode ser nulo.");
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
    @CacheEvict(value = "usuarios", key = "#id")
    public UsuarioResponseDTO atualizar(UUID id, AtualizarUsuarioDTO dto) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioIdNaoEncontradoException(id));

        if (dto.nome() != null && !dto.nome().isBlank()) {
            usuario.setNome(dto.nome());
        }

        if (dto.email() != null && !dto.email().isBlank()
                && !dto.email().equals(usuario.getEmail())) {

            if (usuarioValidator.emailJaExiste(dto.email(), usuario.getId())) {
                throw new RegistroDuplicadoException("O email já existe.");
            }

            usuario.setEmail(dto.email());
        }

        if (dto.senha() != null && !dto.senha().isBlank()) {

            if (!usuarioValidator.senhaValida(dto.senha())) {
                throw new CampoInvalidoException(
                        "senha",
                        "A senha deve ter entre 8 a 128 caracteres."
                );
            }

            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }

        Usuario salvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(salvo);
    }

    @Transactional
    @CacheEvict(value = "usuarios", key = "#id")
    public void deletar(UUID id){

        log.info("Deletando usuário: id={}", id);

        if(id == null){
            log.error("ID informado é nulo");

            throw new IllegalArgumentException(id + "O id do usuario não pode ser nulo.");
        }
        Usuario usuario = usuarioRepository.findById(id)

                .orElseThrow(() -> {

                    log.warn("Usuário não encontrado para deleção: id={}", id);
                    return new UsuarioIdNaoEncontradoException(id);
                });

        if(possuiGasto(usuario)){

            log.warn("Tentativa de deletar usuário com gastos: id={}", id);
            throw new OperacaoNaoPermitidaException("Não é permitido deletar um usuario com gastos.");
        }

        usuarioRepository.delete(usuario);
        log.info("Usuário deletado: id={}", id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "gastos", key = "#email")
    public List<GastoResponseDTO> listarGastosPorEmail(String email) {

        log.info("Listando gastos para email={}", email);
        if (email == null || email.isBlank()) {

            log.error("Email informado é nulo ou vazio");
            throw new IllegalArgumentException("O Email não pode ser nulo");
        }
        return gastoRepository.findByUsuarioEmailOrderByCreatedAtDesc(email)
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
    public Optional<Usuario> obterPorEmail(String email) {

        log.info("Buscando usuário por email={}", email);

        if (email == null || email.isBlank()) {
            log.error("Email informado é nulo ou vazio");
            return Optional.empty();
        }

        return usuarioRepository.findByEmail(email);
    }

    public boolean possuiGasto(Usuario usuario){
        return gastoRepository.existsByUsuarioId(usuario.getId());
    }
}

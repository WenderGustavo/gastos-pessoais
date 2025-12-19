package io.github.wendergustavo.gastospessoais.service;

import io.github.wendergustavo.gastospessoais.dto.gasto.AtualizarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.gasto.CadastrarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.gasto.GastoResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.gasto.ListaGastosResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.usuario.UsuarioDetailsDTO;
import io.github.wendergustavo.gastospessoais.entity.Gasto;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import io.github.wendergustavo.gastospessoais.exceptions.GastoNaoEncontradoException;
import io.github.wendergustavo.gastospessoais.exceptions.OperacaoNaoPermitidaException;
import io.github.wendergustavo.gastospessoais.mapper.GastoMapper;
import io.github.wendergustavo.gastospessoais.repository.GastoRepository;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import io.github.wendergustavo.gastospessoais.security.CustomAuthentication;
import io.github.wendergustavo.gastospessoais.validador.GastoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GastoService {

    private final GastoRepository gastoRepository;
    private final GastoValidator gastoValidator;
    private final GastoMapper gastoMapper;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public GastoResponseDTO salvar(CadastrarGastoDTO dto) {
        UsuarioDetailsDTO usuarioLogado = getUsuarioLogado();
        boolean isAdmin = "ADMIN".equals(usuarioLogado.role());

        UUID idDonoGasto = (isAdmin && dto.idUsuario() != null)
                ? dto.idUsuario()
                : usuarioLogado.id();

        log.info("Criando gasto para usuário {}", idDonoGasto);

        Usuario usuarioDono = usuarioRepository.findById(idDonoGasto)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado: {}", idDonoGasto);
                    return new IllegalArgumentException("User not found (ID: " + idDonoGasto + ")");
                });

        Gasto gasto = gastoMapper.toEntity(dto);
        gasto.setUsuario(usuarioDono);

        gastoValidator.validar(gasto);

        gastoRepository.save(gasto);
        log.info("Gasto criado com sucesso. ID: {}", gasto.getId());

        return gastoMapper.toDTO(gasto);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "gastos", key = "#id")
    public GastoResponseDTO buscarPorId(UUID id) {
        if (id == null) {
            log.error("ID de gasto nulo");
            throw new IllegalArgumentException("Gasto ID must not be null.");
        }

        log.info("Buscando gasto {}", id);
        Gasto gasto = buscarGastoEValidarPermissao(id);

        return gastoMapper.toDTO(gasto);
    }

    @Transactional
    @CacheEvict(value = "gastos", key = "#id")
    public GastoResponseDTO atualizar(UUID id, AtualizarGastoDTO gastoDTO) {
        if (id == null) {
            log.error("ID de gasto nulo");
            throw new IllegalArgumentException("Gasto ID must not be null.");
        }

        log.info("Atualizando gasto {}", id);
        Gasto gastoExistente = buscarGastoEValidarPermissao(id);

        gastoMapper.updateEntityFromDTO(gastoDTO, gastoExistente);

        gastoValidator.validar(gastoExistente);
        gastoRepository.save(gastoExistente);
        log.info("Gasto atualizado com sucesso {}", id);

        return gastoMapper.toDTO(gastoExistente);
    }

    @Transactional
    @CacheEvict(value = "gastos", key = "#id")
    public void deletar(UUID id) {
        if (id == null) {
            log.error("ID de gasto nulo");
            throw new IllegalArgumentException("Gasto ID must not be null.");
        }

        log.info("Deletando gasto {}", id);
        Gasto gasto = buscarGastoEValidarPermissao(id);

        gastoRepository.delete(gasto);
        log.info("Gasto deletado {}", id);
    }

    @Transactional(readOnly = true)
    public ListaGastosResponseDTO buscarTodosGastos() {
        UsuarioDetailsDTO usuarioLogado = getUsuarioLogado();

        List<Gasto> gastos;

        if ("ADMIN".equals(usuarioLogado.role())) {
            log.info("Listando todos os gastos (ADMIN)");
            gastos = gastoRepository.findAll();
        } else {
            log.info("Listando gastos do usuário {}", usuarioLogado.id());
            gastos = gastoRepository.findByUsuarioId(usuarioLogado.id());
        }

        return gastoMapper.toListResponseDTO(gastos);
    }

    private void validarPermissao(Gasto gasto, UsuarioDetailsDTO usuario) {
        boolean isAdmin = "ADMIN".equals(usuario.role());
        boolean donoDoGasto = gasto.getUsuario().getId().equals(usuario.id());

        if (!isAdmin && !donoDoGasto) {
            log.warn("Acesso negado ao gasto {} pelo usuário {}", gasto.getId(), usuario.id());
            throw new OperacaoNaoPermitidaException("Você não tem permissão para acessar este recurso.");
        }
    }

    private Gasto buscarGastoEValidarPermissao(UUID idGasto) {
        log.debug("Verificando permissão para acesso ao gasto {}", idGasto);

        Gasto gasto = gastoRepository.findById(idGasto)
                .orElseThrow(() -> {
                    log.error("Gasto não encontrado: {}", idGasto);
                    return new GastoNaoEncontradoException(idGasto);
                });

        validarPermissao(gasto, getUsuarioLogado());
        return gasto;
    }

    private UsuarioDetailsDTO getUsuarioLogado() {
        var auth = (CustomAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return (UsuarioDetailsDTO) auth.getPrincipal();
    }
}

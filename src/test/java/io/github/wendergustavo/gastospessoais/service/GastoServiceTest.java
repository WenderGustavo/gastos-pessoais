package io.github.wendergustavo.gastospessoais.service;

import io.github.wendergustavo.gastospessoais.dto.gasto.AtualizarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.gasto.CadastrarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.gasto.GastoResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.usuario.UsuarioDetailsDTO;
import io.github.wendergustavo.gastospessoais.entity.Gasto;
import io.github.wendergustavo.gastospessoais.entity.GastoTipo;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import io.github.wendergustavo.gastospessoais.exceptions.GastoNaoEncontradoException;
import io.github.wendergustavo.gastospessoais.exceptions.OperacaoNaoPermitidaException;
import io.github.wendergustavo.gastospessoais.mapper.GastoMapper;
import io.github.wendergustavo.gastospessoais.repository.GastoRepository;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import io.github.wendergustavo.gastospessoais.security.CustomAuthentication;
import io.github.wendergustavo.gastospessoais.validador.GastoValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GastoServiceTest {

    @Mock
    private GastoRepository gastoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private GastoValidator gastoValidator;
    @Mock
    private GastoMapper gastoMapper;

    @InjectMocks
    private GastoService gastoService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void mockSecurityContext(UUID userId, String role) {
        UsuarioDetailsDTO usuarioDTO = new UsuarioDetailsDTO(userId, "teste@gmail.com", role);
        Authentication auth = mock(CustomAuthentication.class);
        SecurityContext context = mock(SecurityContext.class);

        when(context.getAuthentication()).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(usuarioDTO);

        SecurityContextHolder.setContext(context);
    }

    @Test
    @DisplayName("Deve salvar gasto vinculando ao usuário logado (USER)")
    void deveSalvarGastoParaUsuarioLogado() {
        UUID idUsuario = UUID.randomUUID();
        mockSecurityContext(idUsuario, "USER");

        CadastrarGastoDTO dto = new CadastrarGastoDTO("Almoco", GastoTipo.MORADIA, BigDecimal.TEN,LocalDate.now(), null);

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        Gasto gasto = new Gasto();
        Gasto gastoSalvo = new Gasto();
        GastoResponseDTO response = new GastoResponseDTO(UUID.randomUUID(), "Almoco", GastoTipo.ALIMENTACAO, BigDecimal.TEN, LocalDate.now());

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(gastoMapper.toEntity(dto)).thenReturn(gasto);
        when(gastoRepository.save(gasto)).thenReturn(gastoSalvo);
        when(gastoMapper.toDTO(gastoSalvo)).thenReturn(response);

        GastoResponseDTO result = gastoService.salvar(dto);

        assertThat(result).isNotNull();
        verify(usuarioRepository).findById(idUsuario);
        verify(gastoValidator).validar(gasto);
        verify(gastoRepository).save(gasto);
    }

    @Test
    @DisplayName("Admin deve conseguir salvar gasto para outro usuário")
    void adminDeveSalvarGastoParaOutroID() {

        UUID idAdmin = UUID.randomUUID();
        UUID idOutroUsuario = UUID.randomUUID();
        mockSecurityContext(idAdmin, "ADMIN");

        CadastrarGastoDTO dto = new CadastrarGastoDTO("Presente", GastoTipo.OUTROS, BigDecimal.TEN, LocalDate.now(), idOutroUsuario);

        Usuario outroUsuario = new Usuario();
        outroUsuario.setId(idOutroUsuario);

        when(usuarioRepository.findById(idOutroUsuario)).thenReturn(Optional.of(outroUsuario));
        when(gastoMapper.toEntity(dto)).thenReturn(new Gasto());
        when(gastoRepository.save(any())).thenReturn(new Gasto());
        when(gastoMapper.toDTO(any())).thenReturn(new GastoResponseDTO(null, null, null, null, null));

        gastoService.salvar(dto);

        verify(usuarioRepository).findById(idOutroUsuario);
    }

    @Test
    @DisplayName("Deve atualizar gasto se for o dono")
    void deveAtualizarGastoSeForDono() {

        UUID idUsuario = UUID.randomUUID();
        UUID idGasto = UUID.randomUUID();
        mockSecurityContext(idUsuario, "USER");

        AtualizarGastoDTO dto = new AtualizarGastoDTO("Novo Nome", GastoTipo.LAZER, BigDecimal.TEN, LocalDate.now());

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        Gasto gastoExistente = new Gasto();
        gastoExistente.setUsuario(usuario);

        when(gastoRepository.findById(idGasto)).thenReturn(Optional.of(gastoExistente));
        when(gastoMapper.toDTO(any())).thenReturn(new GastoResponseDTO(idGasto, "Novo Nome", null, null, null));

        gastoService.atualizar(idGasto, dto);

        verify(gastoRepository).save(gastoExistente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar gasto de outro usuário (Sem ser Admin)")
    void deveLancarErroAoAtualizarGastoDeOutro() {

        UUID idEu = UUID.randomUUID();
        UUID idOutro = UUID.randomUUID();
        UUID idGasto = UUID.randomUUID();
        mockSecurityContext(idEu, "USER");

        Usuario donoDoGasto = new Usuario();
        donoDoGasto.setId(idOutro);

        Gasto gastoDeOutro = new Gasto();
        gastoDeOutro.setUsuario(donoDoGasto);

        when(gastoRepository.findById(idGasto)).thenReturn(Optional.of(gastoDeOutro));

        AtualizarGastoDTO dto = new AtualizarGastoDTO("Hacker", GastoTipo.LAZER, BigDecimal.TEN, LocalDate.now());

        assertThatThrownBy(() -> gastoService.atualizar(idGasto, dto))
                .isInstanceOf(OperacaoNaoPermitidaException.class)
                .hasMessageContaining("permissão");

        verify(gastoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar gasto se for ADMIN, mesmo não sendo dono")
    void adminDeveDeletarGastoDeQualquerUm() {

        UUID idAdmin = UUID.randomUUID();
        UUID idUsuarioComum = UUID.randomUUID();
        UUID idGasto = UUID.randomUUID();
        mockSecurityContext(idAdmin, "ADMIN");

        Usuario dono = new Usuario();
        dono.setId(idUsuarioComum);

        Gasto gasto = new Gasto();
        gasto.setUsuario(dono);

        when(gastoRepository.findById(idGasto)).thenReturn(Optional.of(gasto));

        gastoService.deletar(idGasto);

        verify(gastoRepository).delete(gasto);
    }

    @Test
    @DisplayName("Deve lançar exceção se Gasto não existe")
    void deveLancarErroSeGastoNaoExiste() {
        UUID id = UUID.randomUUID();
        when(gastoRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gastoService.buscarPorId(id))
                .isInstanceOf(GastoNaoEncontradoException.class);
    }
}
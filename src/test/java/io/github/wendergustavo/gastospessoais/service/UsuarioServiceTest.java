package io.github.wendergustavo.gastospessoais.service;

import io.github.wendergustavo.gastospessoais.dto.GastoResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.ListaUsuarioResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.UsuarioDTO;
import io.github.wendergustavo.gastospessoais.dto.UsuarioResponseDTO;
import io.github.wendergustavo.gastospessoais.entity.Gasto;
import io.github.wendergustavo.gastospessoais.entity.GastoTipo;
import io.github.wendergustavo.gastospessoais.entity.Roles;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import io.github.wendergustavo.gastospessoais.exceptions.OperacaoNaoPermitidaException;
import io.github.wendergustavo.gastospessoais.exceptions.RegistroDuplicadoException;
import io.github.wendergustavo.gastospessoais.exceptions.UsuarioIdNaoEncontradoException;
import io.github.wendergustavo.gastospessoais.mapper.GastoMapper;
import io.github.wendergustavo.gastospessoais.mapper.UsuarioMapper;
import io.github.wendergustavo.gastospessoais.repository.GastoRepository;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import io.github.wendergustavo.gastospessoais.validador.UsuarioValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UsuarioValidator usuarioValidator;
    @Mock
    private UsuarioMapper usuarioMapper;
    @Mock
    private GastoRepository gastoRepository;
    @Mock
    private GastoMapper gastoMapper;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private ListaUsuarioResponseDTO listaUsuarioResponseDTO;

    @Mock
    private UsuarioResponseDTO usuarioResponseDTO;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve salvar um usuário com sucesso")
    void deveSalvarUmUsuarioComSucesso() {
        var dto = new UsuarioDTO("Wender", "teste@gmail.com", "123456789", Roles.USER);

        var usuario = new Usuario();
        usuario.setSenha("123456789");

        var usuarioSalvo = new Usuario();
        usuarioSalvo.setSenha("senhaEncriptada");

        var response = new UsuarioResponseDTO(UUID.randomUUID(), "Wender", "teste@gmail.com", Roles.USER);

        when(usuarioMapper.toEntity(dto)).thenReturn(usuario);

        when(encoder.encode("123456789")).thenReturn("senhaEncriptada");

        when(usuarioRepository.save(usuario)).thenReturn(usuarioSalvo);

        when(usuarioMapper.toResponseDTO(usuarioSalvo)).thenReturn(response);

        var result = usuarioService.salvar(dto);

        assertThat(result)
                .isNotNull()
                .extracting(UsuarioResponseDTO::email)
                .isEqualTo("teste@gmail.com");

        verify(usuarioValidator).validar(usuario);

        assertThat(usuario.getSenha()).isEqualTo("senhaEncriptada");

        verify(usuarioRepository).save(usuario);

        verify(encoder).encode("123456789");
    }


    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar usuário inválido")
    void deveLancarExcecaoAoSalvarUsuarioInvalido() {
        var dto = new UsuarioDTO("Wender", "teste@gmail.com", "123456789",Roles.USER);
        var usuario = new Usuario();

        when(usuarioMapper.toEntity(dto)).thenReturn(usuario);
        Mockito.doThrow(new RegistroDuplicadoException("Email already in use."))
                .when(usuarioValidator).validar(usuario);

        assertThatThrownBy(() -> usuarioService.salvar(dto))
                .isInstanceOf(RegistroDuplicadoException.class);
    }

    @Test
    @DisplayName("Deve retornar usuário quando encontrado")
    void deveBuscarUsuarioPorId() {
        var id = UUID.randomUUID();
        var usuario = new Usuario();
        var response = new UsuarioResponseDTO(id, "Wender", "teste@gmail.com",Roles.USER);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toResponseDTO(usuario)).thenReturn(response);

        var result = usuarioService.buscarPorId(id);

        assertThat(result)
                .isNotNull()
                .extracting(UsuarioResponseDTO::id)
                .isEqualTo(id);
    }

    @Test
    @DisplayName("Deve lançar exceção se usuário não for encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        var id = UUID.randomUUID();
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscarPorId(id))
                .isInstanceOf(UsuarioIdNaoEncontradoException.class);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException se o ID for nulo")
    void deveLancarExcecaoQuandoIdForNulo() {
        assertThatThrownBy(() -> usuarioService.buscarPorId(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve atualizar um usuário e encodar nova senha quando alterada")
    void deveAtualizarUsuarioComSenhaAlterada() {
        UUID id = UUID.randomUUID();

        var dto = new UsuarioDTO("Novo Nome", "novo@email.com",
                "senhaNova123", Roles.USER);

        var usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setNome("Antigo Nome");
        usuarioExistente.setEmail("antigo@email.com");
        usuarioExistente.setSenha("senhaAntigaHASH");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(encoder.encode("senhaNova123")).thenReturn("senhaNovaHASH");

        var usuarioSalvo = new Usuario();
        usuarioSalvo.setId(id);
        usuarioSalvo.setNome("Novo Nome");
        usuarioSalvo.setEmail("novo@email.com");
        usuarioSalvo.setSenha("senhaNovaHASH");

        when(usuarioRepository.save(usuarioExistente)).thenReturn(usuarioSalvo);
        when(usuarioMapper.toResponseDTO(usuarioSalvo))
                .thenReturn(new UsuarioResponseDTO(id, "Novo Nome", "novo@email.com", Roles.USER));

        var result = usuarioService.atualizar(id, dto);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("novo@email.com");

        verify(encoder).encode("senhaNova123");

        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test
    @DisplayName("Deve lançar exceção se o ID for nulo")
    void deveLancarExcecaoSeIdForNulo() {
        UsuarioDTO usuarioDTO = new UsuarioDTO("Nome", "email@email.com", "senha",Roles.USER);
        assertThatThrownBy(() -> usuarioService.atualizar(null, usuarioDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must not be null");
    }

    @Test
    @DisplayName("Deve lançar exceção se o usuário não for encontrado")
    void deveLancarExcecaoSeUsuarioNaoForEncontrado() {
        UUID id = UUID.randomUUID();
        UsuarioDTO usuarioDTO = new UsuarioDTO("Nome", "email@email.com", "senha",Roles.USER);

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.atualizar(id, usuarioDTO))
                .isInstanceOf(UsuarioIdNaoEncontradoException.class);
    }

    @Test
    @DisplayName("Não deve encodar senha se ela não foi alterada")
    void naoDeveEncodarSenhaQuandoNaoAlterada() {

        UUID id = UUID.randomUUID();

        var dto = new UsuarioDTO("Wender", "teste@gmail.com",
                "senhaAntigaHASH", Roles.USER); // mesma senha

        var usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setNome("Wender");
        usuarioExistente.setEmail("teste@gmail.com");
        usuarioExistente.setSenha("senhaAntigaHASH"); // igual

        when(usuarioRepository.findById(id))
                .thenReturn(Optional.of(usuarioExistente));

        when(usuarioRepository.save(usuarioExistente))
                .thenReturn(usuarioExistente);

        when(usuarioMapper.toResponseDTO(usuarioExistente))
                .thenReturn(new UsuarioResponseDTO(id, "Wender", "teste@gmail.com", Roles.USER));

        var result = usuarioService.atualizar(id, dto);

        assertThat(result).isNotNull();

        verify(encoder, never()).encode(anyString());
    }


    @Test
    @DisplayName("Deve deletar usuário sem gastos com sucesso")
    void deveDeletarUsuarioComSucesso() {
        var id = UUID.randomUUID();
        var usuario = new Usuario();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(gastoRepository.existsByUsuario(usuario)).thenReturn(false);

        usuarioService.deletar(id);

        verify(usuarioRepository).delete(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar usuário com gastos")
    void deveLancarExcecaoAoDeletarUsuarioComGastos() {
        var id = UUID.randomUUID();
        var usuario = new Usuario();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(gastoRepository.existsByUsuario(usuario)).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.deletar(id))
                .isInstanceOf(OperacaoNaoPermitidaException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção se tentar deletar usuário inexistente")
    void deveLancarExcecaoAoDeletarUsuarioNaoEncontrado() {
        var id = UUID.randomUUID();

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.deletar(id))
                .isInstanceOf(UsuarioIdNaoEncontradoException.class);
    }

    @Test
    @DisplayName("Deve retornar true quando usuário possuir gastos")
    void deveRetornarTrueQuandoPossuiGasto() {
        var usuario = new Usuario();
        when(gastoRepository.existsByUsuario(usuario)).thenReturn(true);

        var result = usuarioService.possuiGasto(usuario);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando usuário não possuir gastos")
    void deveRetornarFalseQuandoNaoPossuiGasto() {
        var usuario = new Usuario();
        when(gastoRepository.existsByUsuario(usuario)).thenReturn(false);

        var result = usuarioService.possuiGasto(usuario);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Deve retornar lista de gastos quando houver registros para o email")
    void deveListarGastosPorEmailComSucesso() {
        String email = "teste@gmail.com";

        var usuario = new Usuario();
        usuario.setNome("Wender");
        usuario.setEmail(email);
        usuario.setSenha("123456789");

        var gastoEntity = new Gasto();
        gastoEntity.setDescricao("Almoço");
        gastoEntity.setGastoTipo(GastoTipo.ALIMENTACAO);
        gastoEntity.setValor(BigDecimal.valueOf(35.0));
        gastoEntity.setDataGasto(LocalDate.now());
        gastoEntity.setUsuario(usuario);

        var gastoDTO = new GastoResponseDTO(
                UUID.randomUUID(),
                "Almoço",
                GastoTipo.ALIMENTACAO,
                BigDecimal.valueOf(35.0),
                LocalDate.now()
        );

        when(gastoRepository.findByUsuarioEmailOrderByDataGastoDesc(email))
                .thenReturn(List.of(gastoEntity));

        when(gastoMapper.toDTO(gastoEntity)).thenReturn(gastoDTO);

        var result = usuarioService.listarGastosPorEmail(email);

        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .containsExactly(gastoDTO);

        verify(gastoRepository).findByUsuarioEmailOrderByDataGastoDesc(email);
        verify(gastoMapper).toDTO(gastoEntity);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o email for nulo")
    void deveLancarQuandoEmailForNulo() {
        assertThatThrownBy(() -> usuarioService.listarGastosPorEmail(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must not be null");
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o email for vazio ou em branco")
    void deveLancarQuandoEmailForVazio() {
        assertThatThrownBy(() -> usuarioService.listarGastosPorEmail(""))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> usuarioService.listarGastosPorEmail("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve buscar todos os usuários com sucesso")
    void deveBuscarTodosUsuariosComSucesso() {
        var id = UUID.randomUUID();

        var usuario = new Usuario(id, "Wender", "teste@gmail.com", "senha123", Roles.USER,null);
        usuarioResponseDTO = new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole());
        listaUsuarioResponseDTO = new ListaUsuarioResponseDTO(List.of(usuarioResponseDTO));

        List<Usuario> usuarios = List.of(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        when(usuarioMapper.toListResponseDTO(usuarios)).thenReturn(listaUsuarioResponseDTO);


        ListaUsuarioResponseDTO resultado = usuarioService.buscarTodosUsuarios();


        verify(usuarioRepository).findAll();
        verify(usuarioMapper).toListResponseDTO(usuarios);
        assertThat(resultado).isNotNull();
        assertThat(resultado.usuarios()).hasSize(1);
        assertThat(resultado.usuarios().get(0).nome()).isEqualTo("Wender");
        assertThat(resultado.usuarios().get(0).email()).isEqualTo("teste@gmail.com");
    }
}

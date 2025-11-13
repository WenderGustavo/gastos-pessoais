package io.github.wendergustavo.gastospessoais.service;

import io.github.wendergustavo.gastospessoais.dto.AtualizarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.CadastrarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.GastoResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.ListaGastosResponseDTO;
import io.github.wendergustavo.gastospessoais.entity.Gasto;
import io.github.wendergustavo.gastospessoais.entity.GastoTipo;
import io.github.wendergustavo.gastospessoais.entity.Roles;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import io.github.wendergustavo.gastospessoais.exceptions.CampoInvalidoException;
import io.github.wendergustavo.gastospessoais.exceptions.GastoNaoEncontradoException;
import io.github.wendergustavo.gastospessoais.mapper.GastoMapper;
import io.github.wendergustavo.gastospessoais.repository.GastoRepository;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import io.github.wendergustavo.gastospessoais.validador.GastoValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Test
    @DisplayName("Deve salvar um gasto com sucesso")
    void deveSalvarUmGasto(){

        var id = UUID.randomUUID();

        var usuario = new Usuario(id,
                "Wender",
                "teste@gmail.com",
                "123456789",
                Roles.USER,
                null);

        var dto = new CadastrarGastoDTO("Almoco",
                GastoTipo.ALIMENTACAO,
                BigDecimal.valueOf(35.0),
                LocalDate.now(),
                usuario.getId());

        var gasto = new Gasto();
        var gastoSalvo = new Gasto();
        var response = new GastoResponseDTO(UUID.randomUUID(),"Almoco",GastoTipo.ALIMENTACAO,BigDecimal.valueOf(35.0),LocalDate.now());

        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(gastoMapper.toEntity(dto)).thenReturn(gasto);
        when(gastoRepository.save(gasto)).thenReturn(gastoSalvo);
        when(gastoMapper.toDTO(gastoSalvo)).thenReturn(response);

        var result = gastoService.salvar(dto);

        assertThat(result)
                .isNotNull()
                .extracting(GastoResponseDTO::descricao)
                .isEqualTo("Almoco");

        assertThat(result.gastoTipo()).isEqualTo(GastoTipo.ALIMENTACAO);
        assertThat(result.valor()).isEqualByComparingTo(BigDecimal.valueOf(35.0));


        verify(gastoValidator).validar(gasto);
        verify(gastoRepository).save(gasto);

    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar gasto sem passar usuario")
    void deveLancarExcecaoAoProcurarUsuarioNaoEncontrado() {

        var id = UUID.randomUUID();
        var usuario = new Usuario(id,
                "Wender",
                "teste@gmail.com",
                "123456789",
                Roles.USER,
                null);

        var dto = new CadastrarGastoDTO("Carro",
                GastoTipo.TRANSPORTE,
                BigDecimal.valueOf(50.0),
                LocalDate.now(),
                usuario.getId());

        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gastoService.salvar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found.");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar gasto com valor menor ou igual a zero")
    void deveLancarExcecaoAoSalvarGastoComValorInvalido() {

        var id = UUID.randomUUID();
        var usuario = new Usuario(id, "Wender", "teste@gmail.com", "123456789", Roles.USER,null);

        var dto = new CadastrarGastoDTO(
                "Transporte",
                GastoTipo.TRANSPORTE,
                BigDecimal.ZERO,
                LocalDate.now(),
                usuario.getId()
        );

        var gasto = new Gasto();
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(gastoMapper.toEntity(dto)).thenReturn(gasto);

        Mockito.doThrow(new CampoInvalidoException("valor", "Value must be greater than zero."))
                .when(gastoValidator).validar(gasto);

        assertThatThrownBy(() -> gastoService.salvar(dto))
                .isInstanceOf(CampoInvalidoException.class)
                .hasMessageContaining("Value must be greater than zero.");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar gasto com data futura")
    void deveLancarExcecaoAoSalvarGastoComDataFutura() {
        var id = UUID.randomUUID();
        var usuario = new Usuario(id, "Wender", "teste@gmail.com", "123456789",Roles.USER, null);

        var dto = new CadastrarGastoDTO(
                "Viagem",
                GastoTipo.LAZER,
                BigDecimal.valueOf(100.0),
                LocalDate.now().plusDays(2),
                usuario.getId()
        );

        var gasto = new Gasto();
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(gastoMapper.toEntity(dto)).thenReturn(gasto);

        Mockito.doThrow(new CampoInvalidoException("data", "Expenditure date cannot be in the future."))
                .when(gastoValidator).validar(gasto);

        assertThatThrownBy(() -> gastoService.salvar(dto))
                .isInstanceOf(CampoInvalidoException.class)
                .hasMessageContaining("Expenditure date cannot be in the future.");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar gasto sem usuário informado")
    void deveLancarExcecaoAoSalvarGastoSemUsuario() {
        var dto = new CadastrarGastoDTO(
                "Supermercado",
                GastoTipo.ALIMENTACAO,
                BigDecimal.valueOf(80.0),
                LocalDate.now(),
                null
        );

        assertThatThrownBy(() -> gastoService.salvar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found.");
    }

    @Test
    @DisplayName("Deve retornar gasto ao buscar por ID existente")
    void deveBuscarGastoPorIdComSucesso() {
        var id = UUID.randomUUID();
        var gasto = new Gasto();
        var gastoDTO = new GastoResponseDTO(id, "Almoço", GastoTipo.ALIMENTACAO, BigDecimal.valueOf(30), LocalDate.now());

        when(gastoRepository.findById(id)).thenReturn(Optional.of(gasto));
        when(gastoMapper.toDTO(gasto)).thenReturn(gastoDTO);

        var result = gastoService.buscarPorId(id);

        assertThat(result)
                .isNotNull()
                .extracting(GastoResponseDTO::descricao)
                .isEqualTo("Almoço");

        verify(gastoRepository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar gasto inexistente")
    void deveLancarExcecaoAoBuscarGastoInexistente() {
        var id = UUID.randomUUID();
        when(gastoRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gastoService.buscarPorId(id))
                .isInstanceOf(GastoNaoEncontradoException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar gasto com ID nulo")
    void deveLancarExcecaoAoBuscarGastoComIdNulo() {
        assertThatThrownBy(() -> gastoService.buscarPorId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Gasto ID must not be null");
    }

    @Test
    @DisplayName("Deve atualizar um gasto com sucesso")
    void deveAtualizarGastoComSucesso() {
        var id = UUID.randomUUID();
        var gastoExistente = new Gasto();
        var gastoAtualizado = new GastoResponseDTO(id, "Mercado", GastoTipo.ALIMENTACAO, BigDecimal.valueOf(120), LocalDate.now());
        var atualizarDTO = new AtualizarGastoDTO("Mercado",GastoTipo.ALIMENTACAO, BigDecimal.valueOf(120), LocalDate.now());

        when(gastoRepository.findById(id)).thenReturn(Optional.of(gastoExistente));
        when(gastoMapper.toDTO(gastoExistente)).thenReturn(gastoAtualizado);

        var result = gastoService.atualizar(id, atualizarDTO);

        assertThat(result)
                .isNotNull()
                .extracting(GastoResponseDTO::descricao)
                .isEqualTo("Mercado");

        verify(gastoRepository).save(gastoExistente);
        verify(gastoValidator).validar(gastoExistente);
    }
    @Test
    @DisplayName("Deve lançar exceção ao atualizar gasto inexistente")
    void deveLancarExcecaoAoAtualizarGastoInexistente() {
        var id = UUID.randomUUID();
        var atualizarDTO = new AtualizarGastoDTO("Aluguel", GastoTipo.MORADIA, BigDecimal.valueOf(800), LocalDate.now());

        when(gastoRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gastoService.atualizar(id, atualizarDTO))
                .isInstanceOf(GastoNaoEncontradoException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar gasto com ID nulo")
    void deveLancarExcecaoAoAtualizarGastoComIdNulo() {
        var dto = new AtualizarGastoDTO("Aluguel", GastoTipo.MORADIA, BigDecimal.valueOf(800),  LocalDate.now());

        assertThatThrownBy(() -> gastoService.atualizar(null, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Gasto ID must not be null");
    }

    @Test
    @DisplayName("Deve deletar um gasto com sucesso")
    void deveDeletarGastoComSucesso() {
        var id = UUID.randomUUID();
        var gasto = new Gasto();

        when(gastoRepository.findById(id)).thenReturn(Optional.of(gasto));

        gastoService.deletar(id);

        verify(gastoRepository).delete(gasto);
    }


    @Test
    @DisplayName("Deve lançar exceção ao deletar gasto inexistente")
    void deveLancarExcecaoAoDeletarGastoInexistente() {
        var id = UUID.randomUUID();
        when(gastoRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gastoService.deletar(id))
                .isInstanceOf(GastoNaoEncontradoException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar gasto com ID nulo")
    void deveLancarExcecaoAoDeletarGastoComIdNulo() {
        assertThatThrownBy(() -> gastoService.deletar(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Gasto ID must not be null");
    }

    @Test
    void deveBuscarTodosOsGastosComSucesso() {

        List<Gasto> listaDeGastos = List.of(
                new Gasto(),
                new Gasto()
        );

        ListaGastosResponseDTO responseDTO = new ListaGastosResponseDTO(List.of());

        when(gastoRepository.findAll()).thenReturn(listaDeGastos);

        when(gastoMapper.toListResponseDTO(listaDeGastos)).thenReturn(responseDTO);

        ListaGastosResponseDTO resultado = gastoService.buscarTodosGastos();

        assertNotNull(resultado);
        assertEquals(responseDTO, resultado);

        verify(gastoRepository, times(1)).findAll();
        verify(gastoMapper, times(1)).toListResponseDTO(listaDeGastos);
    }


}

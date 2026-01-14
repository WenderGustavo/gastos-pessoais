package io.github.wendergustavo.gastospessoais.repository;

import io.github.wendergustavo.gastospessoais.entity.Gasto;
import io.github.wendergustavo.gastospessoais.entity.GastoTipo;
import io.github.wendergustavo.gastospessoais.entity.Roles;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@ActiveProfiles("test")
class GastoRepositoryTest {

    @Autowired
    GastoRepository gastoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    Usuario usuario;

    Gasto gasto;


    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNome("Wender");
        usuario.setEmail("wender@gmail.com");
        usuario.setSenha("12345678");
        usuario.setRole(Roles.USER);
        usuarioRepository.save(usuario);

        gasto = new Gasto();
        gasto.setDescricao("Almoço");
        gasto.setValor(BigDecimal.valueOf(35.5));
        gasto.setGastoTipo(GastoTipo.ALIMENTACAO);
        gasto.setUsuario(usuario);
    }

    @Test
    @DisplayName("Deve salvar gasto com usuario ")
    void deveSalvarGastoComUsuario() {

        Gasto gastSalvo = gastoRepository.save(gasto);

        assertNotNull(gastSalvo.getId());
        assertThat(gastSalvo.getDescricao()).isEqualTo("Almoço");
        assertThat(gastSalvo.getUsuario().getEmail()).isEqualTo("wender@gmail.com");
    }

    @Test
    @DisplayName("Deve buscar gasto por id com sucesso")
    void deveBuscarGastoPorId(){

        Gasto gastSalvo = gastoRepository.save(gasto);

        Optional<Gasto> gastoEncontrado = gastoRepository.findById(gastSalvo.getId());

        assertThat(gastoEncontrado).isPresent();
        assertThat(gastoEncontrado.get().getUsuario().getNome()).isEqualTo("Wender");
        assertThat(gastoEncontrado.get().getGastoTipo()).isEqualTo(GastoTipo.ALIMENTACAO);
        assertThat(gastoEncontrado.get().getDescricao()).isEqualTo("Almoço");
    }

    @Test
    @DisplayName("Deve deletar um gasto por id com sucesso")
    void deveDeletarUmGastoPorId(){

        Gasto gastSalvo = gastoRepository.save(gasto);

        gastoRepository.deleteById(gasto.getId());

        boolean existe = gastoRepository.existsById(gasto.getId());
        assertThat(existe).isFalse();

    }

    @Test
    @DisplayName("Deve atualizar um gasto por id com sucesso")
    void deveAtualizarUmGastoPorId(){

        Gasto gastSalvo = gastoRepository.save(gasto);

        assertThat(gastSalvo.getDescricao()).isEqualTo("Almoço");

        gastSalvo.setDescricao("Taxi");
        gastSalvo.setGastoTipo(GastoTipo.TRANSPORTE);

        Gasto gastoAtualizado = gastoRepository.save(gastSalvo);

        assertThat(gastoAtualizado.getDescricao()).isEqualTo("Taxi");
        assertThat(gastoAtualizado.getGastoTipo()).isEqualTo(GastoTipo.TRANSPORTE);

    }

    @Test
    @DisplayName("Deve verificar se usuario possui gasto")
    void deveVerificarSeUsuarioPossuiGasto() {

        Gasto gastSalvo = gastoRepository.save(gasto);

        boolean existe = gastoRepository.existsByUsuarioId(gastSalvo.getUsuario().getId());
        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Deve retorna falso se usuario não possui gastos")
    void deveRetornarFalsoSeUsuarioNaoPossuiGasto() {

        boolean existe = gastoRepository.existsByUsuarioId(usuario.getId());
        assertThat(existe).isFalse();
    }


}
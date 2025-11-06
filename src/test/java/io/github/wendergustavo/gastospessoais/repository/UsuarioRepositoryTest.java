package io.github.wendergustavo.gastospessoais.repository;

import io.github.wendergustavo.gastospessoais.entity.Gasto;
import io.github.wendergustavo.gastospessoais.entity.GastoTipo;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    GastoRepository gastoRepository;

    Usuario usuario;


    @BeforeEach
    void setUp(){
        usuario = new Usuario();
        usuario.setNome("Wender");
        usuario.setEmail("teste@gmail.com");
        usuario.setSenha("123456789");
    }

    @Test
    @DisplayName("Deve salvar um usuario com sucesso")
    void deveSalvarUmUsuario(){

        Usuario usuarioSalvo= usuarioRepository.save(usuario);

        assertNotNull(usuarioSalvo.getId());
        assertThat(usuarioSalvo.getNome()).isEqualTo("Wender");
        assertThat(usuarioSalvo.getEmail()).isEqualTo("teste@gmail.com");
    }

    @Test
    @DisplayName("Deve buscar um usuario por id com sucesso")
    void deveBuscarUsuarioPorId() {

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(usuarioSalvo.getId());

        assertThat(usuarioEncontrado).isPresent();
        assertThat(usuarioEncontrado.get().getEmail()).isEqualTo("teste@gmail.com");
        assertThat(usuarioEncontrado.get().getNome()).isEqualTo("Wender");
    }

    @Test
    @DisplayName("Deve deletar um usuario por id com sucesso")
    void deveDeletarUmUsuarioPorId(){

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        usuarioRepository.deleteById(usuarioSalvo.getId());

        boolean existe = usuarioRepository.existsById(usuarioSalvo.getId());
        assertThat(existe).isFalse();

    }

    @Test
    @DisplayName("Deve atualizar um usuario por id com sucesso")
    void deveAtualizarUmUsuarioPorId(){

        Usuario usuarioSalvo= usuarioRepository.save(usuario);

        assertThat(usuarioSalvo.getNome()).isEqualTo("Wender");

        usuarioSalvo.setNome("Wender Gustavo");
        usuarioSalvo.setEmail("teste2@gmail.com");
        usuarioSalvo.setSenha("123456789");

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioSalvo);

        assertThat(usuarioAtualizado.getNome()).isEqualTo("Wender Gustavo");
        assertThat(usuarioAtualizado.getEmail()).isEqualTo("teste2@gmail.com");


    }

    @Test
    @DisplayName("Deve listar gastos do usuario por email com sucesso")
    void deveListarGastosDoUsuarioPorEmail() {

      Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Gasto gasto1 = new Gasto();
        gasto1.setDescricao("Almoço");
        gasto1.setGastoTipo(GastoTipo.ALIMENTACAO);
        gasto1.setValor(BigDecimal.valueOf(15.0));
        gasto1.setDataGasto(LocalDate.of(2025, 10, 1));
        gasto1.setUsuario(usuarioSalvo);

        Gasto gasto2 = new Gasto();
        gasto2.setDescricao("Transporte");
        gasto2.setGastoTipo(GastoTipo.TRANSPORTE);
        gasto2.setValor(BigDecimal.valueOf(10.0));
        gasto2.setDataGasto(LocalDate.of(2025, 10, 5));
        gasto2.setUsuario(usuarioSalvo);

        gastoRepository.saveAll(List.of(gasto1, gasto2));

        List<Gasto> resultado = gastoRepository.findByUsuarioEmailOrderByDataGastoDesc("teste@gmail.com");

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getDescricao()).isEqualTo("Transporte");
        assertThat(resultado.get(1).getDescricao()).isEqualTo("Almoço");
        assertThat(resultado)
                .allMatch(g -> g.getUsuario().getEmail().equals("teste@gmail.com"));
    }

    @Test
    @DisplayName("Deve da falha ao salvar um usuario com email null")
    void deveFalharAoSalvarUsuarioSemEmail() {

        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setEmail(null);
        usuarioInvalido.setSenha(null);

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(usuarioInvalido))
                .isInstanceOf(Exception.class);

    }
    @Test
    @DisplayName("Deve retornar vazio com id que não existe")
    void deveRetornarVazioQuandoIdNaoExiste() {

        UUID id = UUID.randomUUID();
        Optional<Usuario> resultado = usuarioRepository.findById(id);
        assertThat(resultado).isEmpty();

    }

}
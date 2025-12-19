package io.github.wendergustavo.gastospessoais.repository;

import io.github.wendergustavo.gastospessoais.entity.Gasto;
import io.github.wendergustavo.gastospessoais.entity.GastoTipo;
import io.github.wendergustavo.gastospessoais.entity.Roles;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import io.github.wendergustavo.gastospessoais.repository.specs.GastoSpecs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
    class GastoRepositorySpecTest {

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void deveEncontrarGastoPorDescricaoETipoEUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("wender");
        usuario.setEmail("teste@email.com");
        usuario.setSenha("123456789");
        usuario.setRole(Roles.ADMIN);
        em.persist(usuario);

        Gasto gasto = new Gasto();
        gasto.setDescricao("Aluguel");
        gasto.setGastoTipo(GastoTipo.OUTROS);
        gasto.setValor(new BigDecimal("1200"));
        gasto.setUsuario(usuario);
        em.persist(gasto);

        em.flush();

        Specification<Gasto> spec = Specification.allOf(
                GastoSpecs.descricaoIgual("Aluguel"),
                GastoSpecs.tipoIgual(GastoTipo.OUTROS),
                GastoSpecs.usuarioIgual(usuario)
        );

        boolean exists = gastoRepository.exists(spec);

        assertTrue(exists);
    }
}
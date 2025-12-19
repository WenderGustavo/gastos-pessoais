package io.github.wendergustavo.gastospessoais.repository.specs;

import io.github.wendergustavo.gastospessoais.entity.Gasto;
import io.github.wendergustavo.gastospessoais.entity.GastoTipo;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class GastoSpecs {

    public static Specification<Gasto> descricaoIgual(String descricao) {
        return (root, query, builder) ->
                descricao == null ? null :
                        builder.equal(root.get("descricao"), descricao);
    }

    public static Specification<Gasto> tipoIgual(GastoTipo tipo) {
        return (root, query, builder) ->
                tipo == null ? null :
                        builder.equal(root.get("gastoTipo"), tipo);
    }

    public static Specification<Gasto> valorIgual(BigDecimal valor) {
        return (root, query, builder) ->
                valor == null ? null :
                        builder.equal(root.get("valor"), valor);
    }

    public static Specification<Gasto> dataIgual(LocalDate data) {
        return (root, query, builder) ->
                data == null ? null :
                        builder.equal(root.get("createdAt"), data);
    }

    public static Specification<Gasto> usuarioIgual(Usuario usuario) {
        return (root, query, builder) ->
                usuario == null ? null :
                        builder.equal(root.get("usuario"), usuario);
    }

    public static Specification<Gasto> idDiferenteDe(UUID id) {
        return (root, query, builder) ->
                id == null ? null :
                        builder.notEqual(root.get("id"), id);
    }
}

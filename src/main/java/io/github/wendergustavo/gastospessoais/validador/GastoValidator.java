package io.github.wendergustavo.gastospessoais.validador;

import io.github.wendergustavo.gastospessoais.entity.Gasto;
import io.github.wendergustavo.gastospessoais.exceptions.CampoInvalidoException;
import io.github.wendergustavo.gastospessoais.exceptions.RegistroDuplicadoException;
import io.github.wendergustavo.gastospessoais.repository.GastoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static io.github.wendergustavo.gastospessoais.repository.specs.GastoSpecs.*;

@RequiredArgsConstructor
@Component
public class GastoValidator {

    private final GastoRepository gastoRepository;

    public void validar(Gasto gasto) {

        if (!validarValorPositivo(gasto)) {
            throw new CampoInvalidoException("valor", "Valor deve ser maior que 0.");
        }

        if (!validarUsuarioPreenchido(gasto)) {
            throw new CampoInvalidoException("usuario", "Usuario não foi informado.");
        }

        if (!validarGastoDuplicado(gasto)) {
            throw new RegistroDuplicadoException("Gasto duplicado.");
        }
    }

    private boolean validarValorPositivo(Gasto gasto) {
        return gasto != null
                && gasto.getValor() != null
                && gasto.getValor().compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean validarUsuarioPreenchido(Gasto gasto) {
        return gasto.getUsuario() != null
                && gasto.getUsuario().getId() != null;
    }

    private boolean validarGastoDuplicado(Gasto gasto) {

        Specification<Gasto> spec = Specification.allOf(
                descricaoIgual(gasto.getDescricao()),
                tipoIgual(gasto.getGastoTipo()),
                valorIgual(gasto.getValor()),
                usuarioIgual(gasto.getUsuario()),
                gasto.getId() != null ? idDiferenteDe(gasto.getId()) : null
        );

        return !gastoRepository.exists(spec);
    }


}

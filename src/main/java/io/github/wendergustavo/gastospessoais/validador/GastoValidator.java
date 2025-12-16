package io.github.wendergustavo.gastospessoais.validador;

import io.github.wendergustavo.gastospessoais.entity.Gasto;
import io.github.wendergustavo.gastospessoais.exceptions.CampoInvalidoException;
import io.github.wendergustavo.gastospessoais.exceptions.RegistroDuplicadoException;
import io.github.wendergustavo.gastospessoais.repository.GastoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class GastoValidator {

    private final GastoRepository gastoRepository;

    public void validar(Gasto gasto) {

        if (!validarValorPositivo(gasto)) {
            throw new CampoInvalidoException("valor", "Value must be greater than zero.");
        }

        if (!validarUsuarioPreenchido(gasto)) {
            throw new CampoInvalidoException("usuario", "User not found or not informed.");
        }

        if (!validarGastoDuplicado(gasto)) {
            throw new RegistroDuplicadoException("Duplicate spending is not allowed.");
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

        if (gasto.getId() != null) {
            return !gastoRepository.existsByDescricaoAndGastoTipoAndValorAndUsuarioAndIdNot(
                    gasto.getDescricao(),
                    gasto.getGastoTipo(),
                    gasto.getValor(),
                    gasto.getUsuario(),
                    gasto.getId()
            );
        }

        return !gastoRepository.existsByDescricaoAndGastoTipoAndValorAndUsuario(
                gasto.getDescricao(),
                gasto.getGastoTipo(),
                gasto.getValor(),
                gasto.getUsuario()
        );
    }
}

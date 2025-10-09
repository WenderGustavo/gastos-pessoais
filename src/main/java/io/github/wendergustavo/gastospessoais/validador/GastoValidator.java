package io.github.wendergustavo.gastospessoais.validador;

import io.github.wendergustavo.gastospessoais.exceptions.CampoInvalidoException;
import io.github.wendergustavo.gastospessoais.model.Gasto;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class GastoValidator {

    private final UsuarioRepository usuarioRepository;

    public void validarGasto(Gasto gasto) {

        if (!validarValorPositivo(gasto)){
            throw new CampoInvalidoException("Value must be greater than zero.");
        }

        if(!validarUsuarioExistente(gasto)){
            throw new CampoInvalidoException("User not found or not informed.");
        }

        if (!validarData(gasto)){
            throw new CampoInvalidoException("Expenditure date cannot be in the future.");
        }

    }

    private boolean validarValorPositivo(Gasto gasto) {
        return gasto != null && gasto.getValor() != null && gasto.getValor().compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean validarUsuarioExistente(Gasto gasto) {
        if (gasto.getUsuario() == null || gasto.getUsuario().getId() == null) {
            return false;
        }
        return usuarioRepository.existsById(gasto.getUsuario().getId());
    }

    private boolean validarData(Gasto gasto) {
        if (gasto.getDataGasto() == null) {
            gasto.setDataGasto(LocalDate.now());
            return true;
        }
        return !gasto.getDataGasto().isAfter(LocalDate.now());
    }


}

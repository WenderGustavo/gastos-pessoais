package io.github.wendergustavo.gastospessoais.validador;

import io.github.wendergustavo.gastospessoais.exceptions.CampoInvalidoException;
import io.github.wendergustavo.gastospessoais.entity.Gasto;
import io.github.wendergustavo.gastospessoais.exceptions.RegistroDuplicadoException;
import io.github.wendergustavo.gastospessoais.repository.GastoRepository;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class GastoValidator {

    private final UsuarioRepository usuarioRepository;
    private final GastoRepository gastoRepository;

    public void validar(Gasto gasto) {

        if (!validarValorPositivo(gasto)) {
            throw new CampoInvalidoException("valor","Value must be greater than zero.");
        }

        if (!validarUsuarioExistente(gasto)) {
            throw new CampoInvalidoException("usuario","User not found or not informed.");
        }

        if (!dataValida(gasto)) {
            throw new CampoInvalidoException("data","Expenditure date cannot be in the future.");
        }

        if(validarGastoDuplicado(gasto)){
            throw new RegistroDuplicadoException("Duplicate spending is not allowed");

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

    private boolean dataValida(Gasto gasto) {
        return gasto.getDataGasto() != null && !gasto.getDataGasto().isAfter(LocalDate.now());
    }

    private boolean validarGastoDuplicado(Gasto gasto){

        return !gastoRepository.existsByDescricaoAndGastoTipoAndValorAndDataGastoAndUsuario(
                gasto.getDescricao(),
                gasto.getGastoTipo(),
                gasto.getValor(),
                gasto.getDataGasto(),
                gasto.getUsuario()
        );
    }
}

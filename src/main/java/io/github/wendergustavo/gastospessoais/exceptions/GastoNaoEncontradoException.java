package io.github.wendergustavo.gastospessoais.exceptions;

import java.util.UUID;

public class GastoNaoEncontradoException extends RuntimeException {
    public GastoNaoEncontradoException(UUID id) {
        super("Gasto com ID " + id + " não encontrado.");
    }
}
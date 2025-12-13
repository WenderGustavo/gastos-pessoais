package io.github.wendergustavo.gastospessoais.exceptions;

import java.util.UUID;

public class UsuarioIdNaoEncontradoException extends RuntimeException {
    public UsuarioIdNaoEncontradoException(UUID id) {
        super("Usuário com ID " + id + " não encontrado.");
    }
}

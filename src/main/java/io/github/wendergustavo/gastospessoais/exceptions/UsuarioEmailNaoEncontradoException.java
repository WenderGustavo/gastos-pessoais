package io.github.wendergustavo.gastospessoais.exceptions;

public class UsuarioEmailNaoEncontradoException extends RuntimeException {
  public UsuarioEmailNaoEncontradoException(String email) {
    super("Usuário com e-mail " + email + " não encontrado.");
  }
}

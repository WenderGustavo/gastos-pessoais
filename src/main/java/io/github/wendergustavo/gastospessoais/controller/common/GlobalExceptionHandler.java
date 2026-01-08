package io.github.wendergustavo.gastospessoais.controller.common;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.github.wendergustavo.gastospessoais.dto.commom.ErroCampo;
import io.github.wendergustavo.gastospessoais.dto.commom.ErroResposta;
import io.github.wendergustavo.gastospessoais.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleMethodArgumentNotValid(MethodArgumentNotValidException e){
        log.warn("Erro de validação encontrado: {}", e.getMessage());

        List<ErroCampo> erros = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new ErroCampo(fe.getField(), fe.getDefaultMessage()))
                .toList();

        return new ErroResposta(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error.",
                erros
        );
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta handleRegistroDuplicado(RegistroDuplicadoException e){
        log.warn("Registro duplicado: {}", e.getMessage());
        return ErroResposta.conflito(e.getMessage());
    }

    @ExceptionHandler(UsuarioIdNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResposta handleUsuarioNaoEncontrado(UsuarioIdNaoEncontradoException e){
        log.warn("Usuário não encontrado: {}", e.getMessage());

        return new ErroResposta(
                HttpStatus.NOT_FOUND.value(),
                "Usuário não encontrado.",
                List.of(new ErroCampo("idUsuario", e.getMessage()))
        );
    }

    @ExceptionHandler(UsuarioEmailNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResposta handleUsuarioEmailNaoEncontrado(
            UsuarioEmailNaoEncontradoException e) {

        log.warn("Usuário não encontrado por e-mail: {}", e.getMessage());

        return new ErroResposta(
                HttpStatus.NOT_FOUND.value(),
                "Usuário não encontrado.",
                List.of(
                        new ErroCampo("email", e.getMessage())
                )
        );
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleOperacaoNaoPermitida(OperacaoNaoPermitidaException e){
        log.warn("Operação não permitida: {}", e.getMessage());
        return ErroResposta.respostaPadrao(e.getMessage());
    }

    @ExceptionHandler(CampoInvalidoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleCampoInvalido(CampoInvalidoException e){
        log.warn("Campo inválido '{}' - motivo: {}", e.getCampo(), e.getMessage());
        return new ErroResposta(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error.",
                List.of(new ErroCampo(e.getCampo(), e.getMessage()))
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErroResposta handleAccessDenied(AccessDeniedException e){
        log.error("Acesso negado: {}", e.getMessage());
        return new ErroResposta(
                HttpStatus.FORBIDDEN.value(),
                "Access Denied.",
                List.of(new ErroCampo("autorizacao", "Você não tem permissão para acessar este recurso."))
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErroResposta handleBadCredentials(BadCredentialsException e) {
        log.warn("Falha de autenticação (credenciais inválidas): {}", e.getMessage());

        return new ErroResposta(
                HttpStatus.UNAUTHORIZED.value(),
                "Credenciais inválidas",
                List.of(new ErroCampo(
                        "autenticacao",
                        "Email ou senha incorretos"
                ))
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleHttpMessageNotReadable(HttpMessageNotReadableException e) {

        Throwable causa = e.getCause();

        if (causa instanceof InvalidFormatException ife) {

            String campo = ife.getPath().isEmpty()
                    ? "payload"
                    : ife.getPath().get(0).getFieldName();

            String mensagem = String.format(
                    "Valor inválido para o campo '%s'. Formato esperado: %s",
                    campo,
                    ife.getTargetType().getSimpleName()
            );

            log.warn("Erro de desserialização no campo '{}': {}", campo, ife.getOriginalMessage());

            return new ErroResposta(
                    HttpStatus.BAD_REQUEST.value(),
                    "Payload inválido.",
                    List.of(new ErroCampo(campo, mensagem))
            );
        }

        log.warn("Erro de leitura do corpo da requisição: {}", e.getMessage());

        return new ErroResposta(
                HttpStatus.BAD_REQUEST.value(),
                "Payload inválido.",
                List.of(new ErroCampo("payload", "O corpo da requisição está malformado."))
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta handleErroInesperado(Exception e){
        log.error("Erro inesperado no sistema", e);
        return new ErroResposta(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error.",
                List.of(new ErroCampo("erro-interno", "Ocorreu um erro interno no servidor."))
        );
    }
}
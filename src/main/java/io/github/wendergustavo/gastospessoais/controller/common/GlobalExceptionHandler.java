package io.github.wendergustavo.gastospessoais.controller.common;

import io.github.wendergustavo.gastospessoais.dto.commom.ErroCampo;
import io.github.wendergustavo.gastospessoais.dto.commom.ErroResposta;
import io.github.wendergustavo.gastospessoais.exceptions.CampoInvalidoException;
import io.github.wendergustavo.gastospessoais.exceptions.OperacaoNaoPermitidaException;
import io.github.wendergustavo.gastospessoais.exceptions.RegistroDuplicadoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
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

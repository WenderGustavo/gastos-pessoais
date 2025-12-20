package io.github.wendergustavo.gastospessoais.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.wendergustavo.gastospessoais.dto.commom.ErroCampo;
import io.github.wendergustavo.gastospessoais.dto.commom.ErroResposta;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {

        log.warn("Acesso negado à rota {} - motivo: {}",
                request.getRequestURI(),
                accessDeniedException.getMessage());

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErroResposta erro = new ErroResposta(
                HttpServletResponse.SC_FORBIDDEN,
                "Access Denied.",
                List.of(new ErroCampo(
                        "autorizacao",
                        "Você não tem permissão para acessar este recurso."
                ))
        );

        new ObjectMapper().writeValue(response.getOutputStream(), erro);
    }
}

package io.github.wendergustavo.gastospessoais.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.wendergustavo.gastospessoais.dto.commom.ErroResposta;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        log.error("Acesso negado à rota {} - motivo: {}", request.getRequestURI(), authException.getMessage());


        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErroResposta erro = new ErroResposta(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Acesso negado",
                Collections.emptyList()
        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), erro);
    }
}
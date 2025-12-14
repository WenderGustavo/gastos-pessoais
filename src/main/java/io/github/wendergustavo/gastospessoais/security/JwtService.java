package io.github.wendergustavo.gastospessoais.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.github.wendergustavo.gastospessoais.dto.usuario.UsuarioDetailsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration}")
    private long expiration;

    public String gerarToken(UsuarioDetailsDTO usuario) {
        log.info("Gerando token para usuário {}", usuario.email());

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        String token = JWT.create()
                .withIssuer("gastos-api")
                .withSubject(usuario.email())
                .withClaim("id", usuario.id().toString())
                .withClaim("role", usuario.role())
                .withExpiresAt(gerarDataExpiracao())
                .sign(algorithm);

        log.debug("Token gerado com sucesso para {}", usuario.email());

        return token;
    }

    public String validarToken(String token) {
        try {
            log.debug("Validando token recebido");
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            String email = JWT.require(algorithm)
                    .withIssuer("gastos-api")
                    .build()
                    .verify(token)
                    .getSubject();

            log.debug("Token válido para {}", email);

            return email;

        } catch (JWTVerificationException exception) {
            log.warn("Falha na validação do token: {}", exception.getMessage());
            return "";
        }
    }

    private Instant gerarDataExpiracao() {
        return Instant.now().plusMillis(expiration);
    }
}

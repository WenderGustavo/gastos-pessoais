package io.github.wendergustavo.gastospessoais.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key:segredo-padrao-se-nao-tiver-no-properties}")
    private String secretKey;

    public String gerarToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withIssuer("gastos-api")
                .withSubject(usuario.getEmail())
                .withClaim("id", usuario.getId().toString())
                .withClaim("role", usuario.getRole().name())
                .withExpiresAt(gerarDataExpiracao())
                .sign(algorithm);
    }

    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("gastos-api")
                    .build().verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant gerarDataExpiracao() {
        return LocalDateTime.now().plusHours(8).atZone(ZoneId.systemDefault()).toInstant();

    }

}
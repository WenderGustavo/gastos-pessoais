package io.github.wendergustavo.gastospessoais.security;

import io.github.wendergustavo.gastospessoais.dto.usuario.UsuarioDetailsDTO;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var token = recuperarToken(request);

        if (token == null) {
            log.debug("Nenhum token enviado na requisição {}", request.getRequestURI());
        }

        if (token != null) {
            log.debug("Token recebido. Validando.");

            var email = jwtService.validarToken(token);

            if (email.isEmpty()) {
                log.warn("Token inválido ou expirado na rota {}", request.getRequestURI());
            }

            if (!email.isEmpty()) {
                Usuario entidade = usuarioRepository.findByEmail(email).orElse(null);

                if (entidade == null) {
                    log.warn("Token válido, porém usuário não encontrado no banco: {}", email);
                }

                if (entidade != null) {
                    log.info("Autenticação via token concluída para o usuário {}", email);

                    UsuarioDetailsDTO dto = new UsuarioDetailsDTO(
                            entidade.getId(),
                            entidade.getEmail(),
                            entidade.getRole().name()
                    );

                    var authentication = new CustomAuthentication(dto, List.of(new SimpleGrantedAuthority(dto.role())));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        return authHeader.replace("Bearer ", "");
    }
}
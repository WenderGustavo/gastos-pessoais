package io.github.wendergustavo.gastospessoais.security;

import io.github.wendergustavo.gastospessoais.dto.usuario.UsuarioDetailsDTO;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import io.github.wendergustavo.gastospessoais.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsuarioService usuarioService;
    private final PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String senhaDigitada = authentication.getCredentials().toString();

        log.info("Tentando autenticar usuário {}", login);

        Usuario usuarioEncontrado = usuarioService.obterPorEmail(login);

        if (usuarioEncontrado == null) {
            log.warn("Tentativa de login com email inexistente: {}", login);
            throw new BadCredentialsException("Usuário e/ou senha incorretos!");
        }

        boolean senhaBate = encoder.matches(senhaDigitada, usuarioEncontrado.getSenha());

        if (!senhaBate) {
            log.warn("Senha incorreta para o usuário {}", login);
            throw new BadCredentialsException("Usuário e/ou senha incorretos!");
        }

        log.info("Login bem-sucedido para o usuário {}", login);

        UsuarioDetailsDTO dto = new UsuarioDetailsDTO(
                usuarioEncontrado.getId(),
                usuarioEncontrado.getEmail(),
                usuarioEncontrado.getRole().name()
        );

        return new CustomAuthentication(dto, List.of(new SimpleGrantedAuthority(dto.role())));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}

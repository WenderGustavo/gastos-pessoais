package io.github.wendergustavo.gastospessoais.service;

import io.github.wendergustavo.gastospessoais.dto.auth.LoginDTO;
import io.github.wendergustavo.gastospessoais.dto.auth.TokenDTO;
import io.github.wendergustavo.gastospessoais.dto.usuario.UsuarioDetailsDTO;
import io.github.wendergustavo.gastospessoais.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public TokenDTO login(LoginDTO dto) {

        log.info("Tentando autenticar usuário: {}", dto.email());

        try {
            var dadosLogin = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());

            Authentication auth = authenticationManager.authenticate(dadosLogin);

            UsuarioDetailsDTO usuarioDTO = (UsuarioDetailsDTO) auth.getPrincipal();

            log.info("Usuário autenticado com sucesso: {} (role: {})", usuarioDTO.email(), usuarioDTO.role());

            String token = jwtService.gerarToken(usuarioDTO);

            log.debug("Token JWT gerado para usuário {}", usuarioDTO.id());

            return new TokenDTO(token);

        } catch (AuthenticationException e) {
            log.error("DEBUG - Exceção capturada no AuthService: {}", e.getClass().getSimpleName());
            log.error("DEBUG - Mensagem: {}", e.getMessage());
            throw e;
        }
    }
}

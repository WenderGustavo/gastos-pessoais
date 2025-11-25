package io.github.wendergustavo.gastospessoais.controller;

import io.github.wendergustavo.gastospessoais.dto.LoginDTO;
import io.github.wendergustavo.gastospessoais.dto.TokenDTO;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import io.github.wendergustavo.gastospessoais.security.JwtService;
import io.github.wendergustavo.gastospessoais.service.JwtService;
import io.github.wendergustavo.gastospessoais.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService; // Vamos precisar buscar o usuário real

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO data) {
        // 1. Valida login e senha (Isso chama o seu CustomUserDetailsService internamente)
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // 2. Se chegou aqui, a senha está certa. Vamos buscar o usuário para gerar o token
        // Nota: Poderíamos pegar do 'auth.getPrincipal()', mas seu UserDetailsService retorna um User do Spring.
        // Buscando do banco garantimos o objeto Usuario completo da sua Entity.
        Usuario usuario = usuarioService.obterPorEmail(data.email());

        // 3. Gera o token
        String token = jwtService.gerarToken(usuario);

        return ResponseEntity.ok(new TokenDTO(token));
    }
}
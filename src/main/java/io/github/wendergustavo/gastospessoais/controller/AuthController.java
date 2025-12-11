package io.github.wendergustavo.gastospessoais.controller;

import io.github.wendergustavo.gastospessoais.dto.auth.LoginDTO;
import io.github.wendergustavo.gastospessoais.dto.auth.TokenDTO;
import io.github.wendergustavo.gastospessoais.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Operações relacionadas ao login do usuario")

public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Realiza login e retorna o token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO dto) {
        TokenDTO token = authService.login(dto);
        return ResponseEntity.ok(token);
    }
}
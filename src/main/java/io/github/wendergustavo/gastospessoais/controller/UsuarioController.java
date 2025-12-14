package io.github.wendergustavo.gastospessoais.controller;


import io.github.wendergustavo.gastospessoais.dto.gasto.GastoResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.usuario.AtualizarUsuarioDTO;
import io.github.wendergustavo.gastospessoais.dto.usuario.ListaUsuarioResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.usuario.UsuarioDTO;
import io.github.wendergustavo.gastospessoais.dto.usuario.UsuarioResponseDTO;
import io.github.wendergustavo.gastospessoais.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Operações relacionadas a usuários do sistema")
public class UsuarioController implements GenericController {

    private final UsuarioService usuarioService;


    @PostMapping @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Salva um novo usuario", description = "Cadastra um novo usuário. Apenas ADMINs podem executar esta operação.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Erro de validação nos dados enviados"),
            @ApiResponse(responseCode = "409", description = "Usuário já cadastrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Void> salvarUsuario(@RequestBody @Valid UsuarioDTO dto){

        UsuarioResponseDTO usuarioResponseDTO = usuarioService.salvar(dto);
        URI location = gerarHeaderLocation(usuarioResponseDTO.id());
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna os dados de um usuário específico. Admins podem buscar qualquer usuário; o usuário comum só pode buscar a si mesmo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable("id") UUID id){

       UsuarioResponseDTO usuarioResponseDTO = usuarioService.buscarPorId(id);
       return ResponseEntity.ok(usuarioResponseDTO);

    }

    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza as informações de um usuário. Admins podem atualizar qualquer usuário; usuário comum só pode atualizar a si mesmo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "422", description = "Erro de validação"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable UUID id, @RequestBody @Valid AtualizarUsuarioDTO dto) {

        UsuarioResponseDTO usuarioResponseDTO = usuarioService.atualizar(id,dto);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar usuário",
            description = "Remove um usuário do sistema. Não é permitido excluir usuários que possuem gastos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não é permitido excluir este usuário"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Void> deletar(@PathVariable("id") UUID id){

        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/email/{email}")
    @Operation(
            summary = "Listar gastos do usuário",
            description = "Retorna todos os gastos associados ao email informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<List<GastoResponseDTO>> listarGastosPorEmail(@PathVariable String email) {

        List<GastoResponseDTO>  gastos = usuarioService.listarGastosPorEmail(email);
        return ResponseEntity.ok(gastos);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    @Operation(
            summary = "Listar usuários",
            description = "Retorna uma lista com todos os usuários cadastrados. Apenas administradores podem acessar."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<ListaUsuarioResponseDTO> listarUsuarios() {
        ListaUsuarioResponseDTO usuarios = usuarioService.buscarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

}

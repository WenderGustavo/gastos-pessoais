package io.github.wendergustavo.gastospessoais.controller;

import io.github.wendergustavo.gastospessoais.dto.gasto.AtualizarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.gasto.CadastrarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.gasto.GastoResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.gasto.ListaGastosResponseDTO;
import io.github.wendergustavo.gastospessoais.service.GastoService;
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
import java.util.UUID;

@RestController
@RequestMapping("/gastos")
@RequiredArgsConstructor
@Tag(name = "Gastos", description = "Operações relacionadas ao gerenciamento de gastos")
public class GastoController implements GenericController {

    private final GastoService gastoService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @Operation(
            summary = "Cadastrar gasto",
            description = "Registra um novo gasto para o usuário logado ou para outro usuário, caso o solicitante seja ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Gasto cadastrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou usuário não encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    public ResponseEntity<Void> salvarGasto(@RequestBody @Valid CadastrarGastoDTO gastoDTO) {
        GastoResponseDTO gastoSalvo = gastoService.salvar(gastoDTO);
        URI location = gerarHeaderLocation(gastoSalvo.id());
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar gasto por ID",
            description = "Retorna os detalhes de um gasto específico, desde que o usuário tenha permissão."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gasto encontrado."),
            @ApiResponse(responseCode = "404", description = "Gasto não encontrado."),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este gasto.")
    })
    public ResponseEntity<GastoResponseDTO> buscarPorID(@PathVariable("id") UUID id) {
        GastoResponseDTO gastoSimplesDTO = gastoService.buscarPorId(id);
        return ResponseEntity.ok(gastoSimplesDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar gasto",
            description = "Atualiza os dados de um gasto existente, caso o usuário tenha permissão."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gasto atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Gasto não encontrado."),
            @ApiResponse(responseCode = "422", description = "Erro de validação."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    public ResponseEntity<GastoResponseDTO> atualizar(@PathVariable("id") UUID id, @RequestBody @Valid AtualizarGastoDTO dto) {
        GastoResponseDTO gastoSimplesDTO = gastoService.atualizar(id, dto);
        return ResponseEntity.ok(gastoSimplesDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir gasto",
            description = "Remove um gasto do sistema, desde que o usuário tenha permissão."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Gasto removido com sucesso."),
            @ApiResponse(responseCode = "404", description = "Gasto não encontrado."),
            @ApiResponse(responseCode = "403", description = "Sem permissão para remover este gasto.")
    })
    public ResponseEntity<Void> deletar(@PathVariable("id") UUID id) {
        gastoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(
            summary = "Listar gastos",
            description = "Retorna todos os gastos do usuário logado ou todos os gastos do sistema, caso o solicitante seja ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    public ResponseEntity<ListaGastosResponseDTO> listarGastos() {
        ListaGastosResponseDTO gastos = gastoService.buscarTodosGastos();
        return ResponseEntity.ok(gastos);
    }
}
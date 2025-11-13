package io.github.wendergustavo.gastospessoais.controller;


import io.github.wendergustavo.gastospessoais.dto.AtualizarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.CadastrarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.GastoResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.ListaGastosResponseDTO;
import io.github.wendergustavo.gastospessoais.service.GastoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/gastos")
@RequiredArgsConstructor
public class GastoController implements GenericController{

    private final GastoService gastoService;

    @PostMapping
    public ResponseEntity<Void> salvarGasto(@RequestBody @Valid CadastrarGastoDTO gastoDTO){

        GastoResponseDTO gastoSalvo = gastoService.salvar(gastoDTO);

        URI location = gerarHeaderLocation( gastoSalvo.id());
        return ResponseEntity.created(location).build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoResponseDTO> buscarPorID(@PathVariable("id") UUID id){

        GastoResponseDTO gastoSimplesDTO = gastoService.buscarPorId(id);
        return ResponseEntity.ok(gastoSimplesDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GastoResponseDTO> atualizar(@PathVariable("id")  UUID id, @RequestBody @Valid AtualizarGastoDTO dto) {

        GastoResponseDTO gastoSimplesDTO = gastoService.atualizar(id,dto);
        return ResponseEntity.ok(gastoSimplesDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") UUID  id){

        gastoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ListaGastosResponseDTO> listarGastos(){
        ListaGastosResponseDTO gastos = gastoService.buscarTodosGastos();
        return ResponseEntity.ok(gastos);
    }

    }


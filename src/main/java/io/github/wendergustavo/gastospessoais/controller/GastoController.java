package io.github.wendergustavo.gastospessoais.controller;


import io.github.wendergustavo.gastospessoais.dto.AtualizarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.CadastrarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.GastoSimplesDTO;
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
    public ResponseEntity<GastoSimplesDTO> salvarGasto(@RequestBody @Valid CadastrarGastoDTO gastoDTO){

        GastoSimplesDTO gastoSalvo = gastoService.salvar(gastoDTO);

        URI location = URI.create("/gastos/" + gastoSalvo.id()); // precisa incluir getId() no DTO
        return ResponseEntity.created(location).body(gastoSalvo);

    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoSimplesDTO> buscarPorID(@PathVariable("id") UUID id){

        GastoSimplesDTO gastoSimplesDTO = gastoService.buscarPorId(id);
        return ResponseEntity.ok(gastoSimplesDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GastoSimplesDTO> atualizar(@PathVariable("id")  UUID id, @RequestBody @Valid AtualizarGastoDTO dto) {

        GastoSimplesDTO gastoSimplesDTO = gastoService.atualizar(id,dto);
        return ResponseEntity.ok(gastoSimplesDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") UUID  id){

        gastoService.deletar(id);
        return ResponseEntity.noContent().build();
    }



    }


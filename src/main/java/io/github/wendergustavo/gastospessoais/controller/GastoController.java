package io.github.wendergustavo.gastospessoais.controller;


import io.github.wendergustavo.gastospessoais.mapper.GastoMapper;
import io.github.wendergustavo.gastospessoais.dto.AtualizarGastoDTO;
import io.github.wendergustavo.gastospessoais.dto.GastoSimplesDTO;
import io.github.wendergustavo.gastospessoais.model.Gasto;
import io.github.wendergustavo.gastospessoais.service.GastoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService gastoService;
    private final GastoMapper mapper;

    @PostMapping
    public ResponseEntity<Void> salvarGasto(@RequestBody @Valid Gasto gasto){

        gastoService.salvar(gasto);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoSimplesDTO> buscarPorID(@PathVariable("id") String id){

        return gastoService.buscarPorId(UUID.fromString(id))
                .map(gasto -> {
                    var gastoResponseDTO = mapper.toDTO(gasto);
                    return ResponseEntity.ok(gastoResponseDTO);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable("id")  String id, @RequestBody @Valid AtualizarGastoDTO dto) {

        var idGasto = UUID.fromString(id);
        return gastoService.buscarPorId(idGasto)
                .map(gasto -> {
                    gasto.setDescricao(dto.descricao());
                    gasto.setGastoTipo(dto.gastoTipo());
                    gasto.setValor(dto.valor());
                    gasto.setDataGasto(dto.dataGasto());
                    gastoService.atualizar(gasto);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") String id){
        var idGasto = UUID.fromString(id);

        Optional<Gasto> gastoOptional = gastoService.buscarPorId(idGasto);

        if(idGasto == null){
            return ResponseEntity.notFound().build();
        }

        gastoService.deletar(gastoOptional.get());
        return ResponseEntity.noContent().build();
    }



    }


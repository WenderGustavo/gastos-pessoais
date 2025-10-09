package io.github.wendergustavo.gastospessoais.controller;


import io.github.wendergustavo.gastospessoais.controller.mappers.GastoMapper;
import io.github.wendergustavo.gastospessoais.dto.GastoResponseDTO;
import io.github.wendergustavo.gastospessoais.model.Gasto;
import io.github.wendergustavo.gastospessoais.service.GastoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<GastoResponseDTO> buscarPorID(@PathVariable("id") String id){

        return gastoService.buscarPorId(UUID.fromString(id))
                .map(gasto -> {
                    var gastoResponseDTO = mapper.toDTO(gasto);
                    return ResponseEntity.ok(gastoResponseDTO);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    }


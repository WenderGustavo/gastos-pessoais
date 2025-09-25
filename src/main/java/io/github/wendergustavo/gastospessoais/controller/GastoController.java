package io.github.wendergustavo.gastospessoais.controller;

public class GastoController {

    private final GastoService gastoService;
    private final GastoMapper mapper;

    @PostMapping
    public ResponseEntity<Void> salvarGasto(@RequestBody @Valid Gasto gasto){

        gastoService.salvar(gasto);

        return ResponseEntity.ok().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultadoPesquisaDTO> buscarPorID(@PathVariable("id") UUID id){

        return gastoService.buscarPorId(id)
                .map(usuario -> {
                    var resultadoPesquisaDTO = mapper.toDTO(usuario);
                    return ResponseEntity.ok(resultadoPesquisaDTO);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

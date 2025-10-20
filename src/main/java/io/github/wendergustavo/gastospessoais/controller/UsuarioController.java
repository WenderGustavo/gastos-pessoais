package io.github.wendergustavo.gastospessoais.controller;


import io.github.wendergustavo.gastospessoais.mapper.UsuarioMapper;
import io.github.wendergustavo.gastospessoais.dto.UsuarioDTO;
import io.github.wendergustavo.gastospessoais.dto.UsuarioResponseDTO;
import io.github.wendergustavo.gastospessoais.model.Usuario;
import io.github.wendergustavo.gastospessoais.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper mapper;


    @PostMapping
    public ResponseEntity<Void> salvarUsuario(@RequestBody @Valid UsuarioDTO dto){
        Usuario usuario = mapper.toEntity(dto);
        usuarioService.salvar(usuario);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable("id") String id){

        var idUsuario = UUID.fromString(id);

        return usuarioService.buscarPorId(idUsuario)
                .map(usuario -> {
                    UsuarioResponseDTO usuarioResponseDTO = mapper.toResponseDTO(usuario);
                    return ResponseEntity.ok(usuarioResponseDTO);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") String id){

        var idUsuario = UUID.fromString(id);

        Optional<Usuario> usuarioOptional = usuarioService.buscarPorId(idUsuario);

        if(usuarioOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        usuarioService.deletar(usuarioOptional.get());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable String id, @RequestBody @Valid UsuarioDTO dto) {

        var idUsuario = UUID.fromString(id);
        return usuarioService.buscarPorId(idUsuario)
                .map(usuario -> {
                    usuario.setNome(dto.nome());
                    usuario.setEmail(dto.email());
                    usuario.setSenha(dto.senha());
                    usuarioService.atualizar(usuario);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<List<GastoSimplesDTO>> listarGastosPorEmail(@PathVariable String email) {
        List<GastoSimplesDTO> gastos = usuarioService.listarGastosPorEmail(email)
                .stream()
                .map(gasto -> new GastoSimplesDTO(
                        gasto.getDescricao(),
                        gasto.getGastoTipo(),
                        gasto.getValor(),
                        gasto.getDataGasto()))
                .toList();

        return ResponseEntity.ok(gastos);
    }

}

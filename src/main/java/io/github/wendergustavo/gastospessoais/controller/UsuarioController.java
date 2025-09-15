package io.github.wendergustavo.gastospessoais.controller;


import io.github.wendergustavo.gastospessoais.controller.mappers.UsuarioMapper;
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
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable("id") UUID id){

        return usuarioService.buscarPorId(id)
                .map(usuario -> {
                    UsuarioResponseDTO usuarioResponseDTO = mapper.toResponseDTO(usuario);
                    return ResponseEntity.ok(usuarioResponseDTO);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") UUID id){

        Optional<Usuario> usuarioOptional = usuarioService.buscarPorId(id);

        if(usuarioOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable UUID id, @RequestBody @Valid UsuarioDTO dto) {
        return usuarioService.buscarPorId(id)
                .map(usuario -> {
                    usuario.setNome(dto.nome());
                    usuario.setEmail(dto.email());
                    usuario.setSenha(dto.senha());
                    usuarioService.atualizar(usuario);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

package io.github.wendergustavo.gastospessoais.controller;


import io.github.wendergustavo.gastospessoais.dto.GastoResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.ListaUsuarioResponseDTO;
import io.github.wendergustavo.gastospessoais.dto.UsuarioDTO;
import io.github.wendergustavo.gastospessoais.dto.UsuarioResponseDTO;
import io.github.wendergustavo.gastospessoais.service.UsuarioService;
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
public class UsuarioController implements GenericController {

    private final UsuarioService usuarioService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> salvarUsuario(@RequestBody @Valid UsuarioDTO dto){

        UsuarioResponseDTO usuarioResponseDTO = usuarioService.salvar(dto);
        URI location = gerarHeaderLocation(usuarioResponseDTO.id());
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable("id") UUID id){

       UsuarioResponseDTO usuarioResponseDTO = usuarioService.buscarPorId(id);
       return ResponseEntity.ok(usuarioResponseDTO);

    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable UUID id, @RequestBody @Valid UsuarioDTO dto) {

        UsuarioResponseDTO usuarioResponseDTO = usuarioService.atualizar(id,dto);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") UUID id){

        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

   @GetMapping("/email/{email}")
    public ResponseEntity<List<GastoResponseDTO>> listarGastosPorEmail(@PathVariable String email) {

        List<GastoResponseDTO>  gastos = usuarioService.listarGastosPorEmail(email);
        return ResponseEntity.ok(gastos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ListaUsuarioResponseDTO> listarUsuarios() {
        ListaUsuarioResponseDTO usuarios = usuarioService.buscarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

}

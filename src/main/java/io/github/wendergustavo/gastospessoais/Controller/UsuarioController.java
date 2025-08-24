package io.github.wendergustavo.gastospessoais.Controller;


import io.github.wendergustavo.gastospessoais.Model.Usuario;
import io.github.wendergustavo.gastospessoais.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    public ResponseEntity<Void> salvarUsuario(Usuario usuario){
        usuarioService.salvar(usuario);
        return ResponseEntity.ok().build();
    }


}

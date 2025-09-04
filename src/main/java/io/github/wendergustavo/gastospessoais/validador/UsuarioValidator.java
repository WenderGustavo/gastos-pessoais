package io.github.wendergustavo.gastospessoais.validador;


import io.github.wendergustavo.gastospessoais.model.Usuario;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UsuarioValidator {

    private final UsuarioRepository repository;

    public UsuarioValidator(UsuarioRepository usuarioRepository) {
        this.repository = usuarioRepository;
    }

    public void validarEmail(String email, UUID id){

        Optional<Usuario> usuario = repository.findByEmail(email);

        if(usuario.isPresent()){
            if (id == null || !id.equals(usuario.get().getId())){
                throw new RuntimeException("Email ja em uso!");
            }
        }

    }


}

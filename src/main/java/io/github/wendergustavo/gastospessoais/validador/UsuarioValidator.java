package io.github.wendergustavo.gastospessoais.validador;


import io.github.wendergustavo.gastospessoais.exceptions.RegistroDuplicadoException;
import io.github.wendergustavo.gastospessoais.model.Usuario;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UsuarioValidator {

    private final UsuarioRepository repository;

    public void validar(Usuario usuario){

        if(validarEmail(usuario.getEmail(),usuario.getId())){
            throw new RegistroDuplicadoException("Email already in use.");
        }
    }

    public boolean validarEmail(String email, UUID id) {
        return repository.findByEmail(email)
                .map(u -> id == null || !id.equals(u.getId()))
                .orElse(false);
    }


}

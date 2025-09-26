package io.github.wendergustavo.gastospessoais.validador;


import io.github.wendergustavo.gastospessoais.exceptions.CampoInvalidoException;
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

        if(emailJaExiste(usuario.getEmail(),usuario.getId())){
            throw new RegistroDuplicadoException("Email already in use.");
        }

        if (!senhaValida(usuario.getSenha())) {
            throw new CampoInvalidoException("Password must be between 8 and 128 characters.");
        }
    }

    public boolean emailJaExiste(String email, UUID id) {
        return repository.findByEmail(email)
                .map(u -> id == null || !id.equals(u.getId()))
                .orElse(false);
    }

    public boolean senhaValida(String senha){
        return senha != null && !senha.isBlank()
                && senha.length() >= 8 && senha.length() <= 128;
    }


}

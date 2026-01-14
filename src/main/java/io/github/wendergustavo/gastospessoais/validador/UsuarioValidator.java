package io.github.wendergustavo.gastospessoais.validador;


import io.github.wendergustavo.gastospessoais.exceptions.CampoInvalidoException;
import io.github.wendergustavo.gastospessoais.exceptions.RegistroDuplicadoException;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UsuarioValidator {

    private final UsuarioRepository repository;

    public void validarParaCadastro(Usuario usuario){

        if(emailJaExiste(usuario.getEmail(),usuario.getId())){
            throw new RegistroDuplicadoException("O email já existe.");
        }

        if (!senhaValida(usuario.getSenha())) {
            throw new CampoInvalidoException("senha","A senha deve ter entre 8 a 128 caracteres.");
        }
    }

    public boolean emailJaExiste(String email, UUID id) {
        return repository.existsByEmailAndIdNot(email, id);
    }

    public boolean senhaValida(String senha){
        return senha != null && !senha.isBlank()
                && senha.length() >= 8 && senha.length() <= 128;
    }


}

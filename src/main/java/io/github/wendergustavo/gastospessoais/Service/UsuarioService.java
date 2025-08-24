package io.github.wendergustavo.gastospessoais.Service;

import io.github.wendergustavo.gastospessoais.Model.Usuario;
import io.github.wendergustavo.gastospessoais.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario salvar(Usuario usuario){
        return usuarioRepository.save(usuario);
    }
}

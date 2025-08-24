package io.github.wendergustavo.gastospessoais.Service;

import io.github.wendergustavo.gastospessoais.Model.Usuario;
import io.github.wendergustavo.gastospessoais.Repository.UsuarioRepository;

public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public Usuario salvar(Usuario usuario){
        return usuarioRepository.save(usuario);
    }
}

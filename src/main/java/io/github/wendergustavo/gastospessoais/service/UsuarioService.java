package io.github.wendergustavo.gastospessoais.service;

import io.github.wendergustavo.gastospessoais.model.Usuario;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario salvar(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorId(UUID id){
        return usuarioRepository.findById(id);
    }

    public void deletar(UUID id){
        usuarioRepository.deleteById(id);
    }

    public void atualizar(Usuario usuario){
        usuarioRepository.save(usuario);
    }


}

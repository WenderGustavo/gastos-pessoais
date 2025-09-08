package io.github.wendergustavo.gastospessoais.service;

import io.github.wendergustavo.gastospessoais.exceptions.ResourceNotFoundException;
import io.github.wendergustavo.gastospessoais.model.Usuario;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import io.github.wendergustavo.gastospessoais.validador.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioValidator usuarioValidator;


    public Usuario salvar(Usuario usuario){
        usuarioValidator.validar(usuario);
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorId(UUID id){

        if(id == null){
            throw new ResourceNotFoundException("Usuario não encontrado");
        }

        return usuarioRepository.findById(id);
    }

    public void deletar(UUID id){

        if(id == null){
            throw  new ResourceNotFoundException("Usuario não encontrado");
        }

        usuarioRepository.deleteById(id);
    }

    public void atualizar(Usuario usuario){
        usuarioValidator.validar(usuario);
        usuarioRepository.save(usuario);
    }


}

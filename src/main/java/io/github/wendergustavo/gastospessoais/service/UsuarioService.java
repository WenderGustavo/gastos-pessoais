package io.github.wendergustavo.gastospessoais.service;

import io.github.wendergustavo.gastospessoais.exceptions.OperacaoNaoPermitidaException;
import io.github.wendergustavo.gastospessoais.model.Gasto;
import io.github.wendergustavo.gastospessoais.model.Usuario;
import io.github.wendergustavo.gastospessoais.repository.GastoRepository;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import io.github.wendergustavo.gastospessoais.validador.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioValidator usuarioValidator;
    private final GastoRepository gastoRepository;


    @Transactional
    public Usuario salvar(Usuario usuario){
        usuarioValidator.validar(usuario);
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorId(UUID id){

        if(id == null){
            throw new IllegalArgumentException("User ID must not be null.");
        }

        return usuarioRepository.findById(id);
    }

    @Transactional
    public void deletar(Usuario usuario){

        if(usuario.getId() == null){
            throw new IllegalArgumentException("User ID must not be null.");
        }

        if(possuiGasto(usuario)){
            throw new OperacaoNaoPermitidaException("It is not allowed to delete a user who has expenses.");
        }

        usuarioRepository.delete(usuario);
    }

    public List<Gasto> listarGastosPorEmail(String email){
        if(email == null || email.isBlank()){
            throw new IllegalArgumentException("Email must not be null or empty");
        }

        return usuarioRepository.listarGastosPorEmail(email);
    }

    @Transactional
    public void atualizar(Usuario usuario){

        if(usuario.getId() == null){
            throw  new IllegalArgumentException("Para atualizar é necessario que o Usuario ja esteja cadastrado.");
        }

        usuarioValidator.validar(usuario);
        usuarioRepository.save(usuario);
    }

    public boolean possuiGasto(Usuario usuario){
        return gastoRepository.existsByUsuario(usuario);
    }


}

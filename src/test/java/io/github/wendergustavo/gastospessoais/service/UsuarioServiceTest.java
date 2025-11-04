package io.github.wendergustavo.gastospessoais.service;

import io.github.wendergustavo.gastospessoais.entity.Usuario;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    UsuarioService usuarioService;

    @Mock
    UsuarioRepository usuarioRepository;

    Usuario usuarioParaRetornar;

    @BeforeEach
    void setUp(){
        usuarioParaRetornar = new Usuario();
        usuarioParaRetornar.setNome("Wender");
        usuarioParaRetornar.setEmail("teste@gmail.com");
        usuarioParaRetornar.setSenha("123456789");
        usuarioParaRetornar.setId(UUID.randomUUID());
        UUID id = usuarioParaRetornar.getId();

    }

    @Test
    @DisplayName("Deve salvar um usuario usando o mockito com sucesso")
    void deveSalvarUmUsuario(){

        Mockito.when(usuarioRepository.save(Mockito.any())).thenReturn(usuarioParaRetornar);

        var usuarioParaSalvar = new Usuario();
        usuarioParaSalvar.setNome("Camilo");
        usuarioParaSalvar.setEmail("teste2@gmail.com");
        usuarioParaSalvar.setSenha("123456789");

        var usuarioSalvo = usuarioRepository.save(usuarioParaSalvar);

        assertNotNull(usuarioSalvo);
        assertEquals("Wender", usuarioSalvo.getNome());
        assertEquals("teste@gmail.com",usuarioSalvo.getEmail());

        Mockito.verify(usuarioRepository).save(Mockito.any());

    }


}
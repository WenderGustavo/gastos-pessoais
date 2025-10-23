package io.github.wendergustavo.gastospessoais.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class UsuarioTest {

    @Test
    public void testCriarUsuario(){
        Usuario usuario = new Usuario();
        usuario.setNome("Wender");
        usuario.setEmail("nem12@gmail.com");
        usuario.setSenha("nem123");

        assertEquals("Wender",usuario.getNome());
        assertEquals("nem12@gmail.com",usuario.getEmail());
        assertEquals("nem123",usuario.getSenha());

    }

}

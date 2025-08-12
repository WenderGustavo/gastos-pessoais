package io.github.wendergustavo.gastospessoais.Model;


import org.junit.jupiter.api.Test;
public class UsuarioTest {

    @Test
    public void testCriarUsuario(){
        Usuario usuario = new Usuario();
        usuario.setNome("Wender");
        usuario.setEmail("nem12@gmail.com");
        usuario.setSenha("nem123");

        assert usuario.getNome().equals("Wender");
        assert usuario.getEmail().equals("nem12@gmail.com");
        assert usuario.getSenha().equals("nem123");

    }

}

package io.github.wendergustavo.gastospessoais.configuration;

import io.github.wendergustavo.gastospessoais.entity.Roles;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import io.github.wendergustavo.gastospessoais.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AdminSeederConfig implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (usuarioRepository.findByEmail("admin@.com").isEmpty()) {
            log.info("Criando usuário ADMIN inicial...");

            Usuario admin = new Usuario();
            admin.setNome("Admin");
            admin.setEmail("admin@.com");
            admin.setSenha(passwordEncoder.encode("12345678"));
            admin.setRole(Roles.ADMIN);

            usuarioRepository.save(admin);
            log.info("Usuário ADMIN criado com sucesso!");
        } else {
            log.info("Admin já existe. Seed ignorada.");
        }

        if (usuarioRepository.findByEmail("user@.com").isEmpty()) {
            log.info("Criando usuário USER inicial...");

            Usuario user = new Usuario();
            user.setNome("User");
            user.setEmail("user@.com");
            user.setSenha(passwordEncoder.encode("12345678"));
            user.setRole(Roles.USER);

            usuarioRepository.save(user);
            log.info("Usuário USER criado com sucesso!");
        } else {
            log.info("User já existe. Seed ignorada.");
        }
    }

}
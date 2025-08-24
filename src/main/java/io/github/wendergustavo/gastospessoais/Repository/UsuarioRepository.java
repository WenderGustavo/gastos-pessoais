package io.github.wendergustavo.gastospessoais.Repository;

import io.github.wendergustavo.gastospessoais.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
}

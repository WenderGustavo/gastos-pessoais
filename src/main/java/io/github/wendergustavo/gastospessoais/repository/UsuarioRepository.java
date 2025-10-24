package io.github.wendergustavo.gastospessoais.repository;

import io.github.wendergustavo.gastospessoais.entity.Gasto;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByEmail(String email);

    @Query("""
    SELECT g
    FROM Gasto g
    JOIN g.usuario u
    WHERE u.email = :email
    ORDER BY g.dataGasto DESC
    """)
    List<Gasto> listarGastosPorEmail(@Param("email") String email);

}

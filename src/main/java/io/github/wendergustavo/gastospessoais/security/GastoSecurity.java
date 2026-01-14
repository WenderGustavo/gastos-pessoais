package io.github.wendergustavo.gastospessoais.security;

import io.github.wendergustavo.gastospessoais.dto.usuario.UsuarioDetailsDTO;
import io.github.wendergustavo.gastospessoais.repository.GastoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GastoSecurity {

    private final GastoRepository gastoRepository;

    public boolean isOwner(UUID gastoId) {
        var auth = (CustomAuthentication) SecurityContextHolder
                .getContext().getAuthentication();

        UUID usuarioId = ((UsuarioDetailsDTO) auth.getPrincipal()).id();

        return gastoRepository.existsByIdAndUsuarioId(gastoId, usuarioId);
    }
}

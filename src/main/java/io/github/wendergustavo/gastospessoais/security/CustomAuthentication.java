package io.github.wendergustavo.gastospessoais.security;

import io.github.wendergustavo.gastospessoais.dto.usuario.UsuarioDetailsDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthentication implements Authentication {

    private final UsuarioDetailsDTO usuarioDTO;
    private final Collection<? extends GrantedAuthority> authorities;

    private boolean authenticated;

    public CustomAuthentication(UsuarioDetailsDTO usuarioDTO, Collection<? extends GrantedAuthority> authorities) {
        this.usuarioDTO = usuarioDTO;
        this.authorities = authorities;
        this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return usuarioDTO;
    }

    @Override
    public Object getPrincipal() {
        return usuarioDTO;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return usuarioDTO.email();
    }

}

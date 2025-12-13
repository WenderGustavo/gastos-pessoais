package io.github.wendergustavo.gastospessoais.security;
import io.github.wendergustavo.gastospessoais.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;
import java.util.Collections;


@RequiredArgsConstructor
public class CustomAuthentication implements Authentication
{

    private final Usuario usuario;
    private boolean authenticated;


    @Override public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()));
    }
    @Override public Object getCredentials()
    {
        return null;
    }

    @Override
    public Object getDetails()
    {
        return usuario;
    }

    @Override public Object getPrincipal()
    {
        return usuario;
    }

    @Override public boolean isAuthenticated()
    {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException
    {
        this.authenticated = isAuthenticated;
    }

    @Override public String getName()
    {
        return usuario.getEmail();
    }

    public Usuario getUsuario()
    {
        return usuario;
    }
}
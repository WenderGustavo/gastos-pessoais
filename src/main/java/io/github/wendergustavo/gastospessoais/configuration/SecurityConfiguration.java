package io.github.wendergustavo.gastospessoais.configuration;

import io.github.wendergustavo.gastospessoais.security.CustomAuthenticationProvider;
import io.github.wendergustavo.gastospessoais.security.JwtAuthenticationEntryPoint;
import io.github.wendergustavo.gastospessoais.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    private static final String[] PUBLIC_ENDPOINTS = {

            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs",
            "/api-docs/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/swagger-resources",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/index.html",
            "/swagger-ui/swagger-initializer.js",
            "/health",
            "/actuator/health",

            "/auth/login",
            "/auth/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                        .requestMatchers(HttpMethod.POST, "/usuarios").hasAuthority("ADMIN")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove o prefixo ROLE_
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(customAuthenticationProvider));
    }
}
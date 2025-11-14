package io.github.wendergustavo.gastospessoais.configuration;

import io.github.wendergustavo.gastospessoais.security.CustomUserDetailsService;
import io.github.wendergustavo.gastospessoais.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(configurer -> {
                    configurer.loginPage("/login");
                })
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/login/**").permitAll()
                            .requestMatchers(HttpMethod.POST,"/usuarios/**").permitAll()
                            .requestMatchers("/auth/**").permitAll()
                            .requestMatchers("/usuarios/**").hasRole("ADMIN")
                            .requestMatchers("/gastos").hasAnyRole("USER","ADMIN");

                    authorize.anyRequest().authenticated();
                })
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    public UserDetailsService  userDetailsService(UsuarioService usuarioService){

        return new CustomUserDetailsService(usuarioService);
    }



}

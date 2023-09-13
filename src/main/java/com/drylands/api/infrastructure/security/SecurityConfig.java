package com.drylands.api.infrastructure.security;

import com.drylands.api.services.DetalhesUsuarioService;
import com.drylands.api.services.TokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig {

    private final TokenService tokenService;
    private final DetalhesUsuarioService detalhesUsuarioService;

    public SecurityConfig(TokenService tokenService, DetalhesUsuarioService detalhesUsuarioService) {
        this.tokenService = tokenService;
        this.detalhesUsuarioService = detalhesUsuarioService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AutenticacaoTokenFiltro tokenAuthenticationFilter() {
        return new AutenticacaoTokenFiltro(tokenService, detalhesUsuarioService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling()
                .and()
                .authorizeRequests(
                        authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .cors(Customizer.withDefaults())
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
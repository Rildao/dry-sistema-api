package com.drylands.api.services.impl;

import com.drylands.api.domain.JwtToken;
import com.drylands.api.domain.Usuario;
import com.drylands.api.domain.enums.TokenEnum;
import com.drylands.api.infrastructure.exceptions.BadRequestException;
import com.drylands.api.infrastructure.repositories.TokenRepository;
import com.drylands.api.rest.dtos.request.LoginRequestDTO;
import com.drylands.api.rest.dtos.response.AuthResponseDTO;
import com.drylands.api.services.AuthService;
import com.drylands.api.services.TokenService;
import com.drylands.api.services.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class AuthServiceImpl implements AuthService {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "rt_cookie";

    private final TokenService tokenService;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    public AuthServiceImpl(TokenService tokenService, UsuarioService usuarioService, PasswordEncoder passwordEncoder, TokenRepository tokenRepository) {
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    private Usuario obterUsuarioPrincipal(String email, String senha) {
        Usuario usuario = usuarioService.obterUsuarioPorEmail(email);

        if (usuario == null) throw new BadRequestException("E-mail ou Senha inválidos");
        if (!passwordEncoder.matches(senha, usuario.getSenha())) throw new BadRequestException("E-mail ou Senha inválidos");

        return usuario;
    }

    private JwtToken criarRefreshToken(Usuario usuario) {
        return tokenService.criarToken(usuario, Duration.of(10800000, ChronoUnit.MILLIS), TokenEnum.REFRESH);
    }

    private void adicionarRefreshToken(Usuario usuario) {
        Date expires = new Date();
        JwtToken refreshToken = criarRefreshToken(usuario);

        HttpServletResponse response = Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).map(ServletRequestAttributes::getResponse).orElseThrow(IllegalStateException::new);

        expires.setTime(expires.getTime() + 10800000);

        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", java.util.Locale.US);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        response.setHeader("Set-Cookie", String.format("%s=%s; Expires=%s; Path=/; HttpOnly; SameSite=none; Secure", REFRESH_TOKEN_COOKIE_NAME, refreshToken.getValor(), df.format(expires)));
    }

    private void removerRefreshToken() {
        HttpServletResponse response = Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getResponse).orElseThrow(IllegalStateException::new);
        Date expires = new Date();
        expires.setTime(expires.getTime() + 1);
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", java.util.Locale.US);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        response.setHeader("Set-Cookie", String.format("%s=; Expires=%s; Path=/; HttpOnly; SameSite=none; Secure", REFRESH_TOKEN_COOKIE_NAME, df.format(expires)));
    }

    private AuthResponseDTO obterAuthResponse(Usuario usuario) {
        String accessToken = criarAccessToken(usuario);
        AuthResponseDTO authResponse = new AuthResponseDTO();
        authResponse.setAccessToken(accessToken);
        adicionarRefreshToken(usuario);
        return authResponse;
    }

    private AuthResponseDTO login(Usuario userPrincipal) {
        Usuario usuario = usuarioService.obterUsuarioPorEmail(userPrincipal.getEmail());

//        if (Boolean.FALSE.equals(usuario.getEmailVerificado()))
//            throw new BadRequestException("Usuario não confirmou o E-mail!");

        return obterAuthResponse(usuario);
    }

    public String criarAccessToken(Usuario user) {
        return tokenService.criarValorTokenJwt(user, Duration.of(10800000, ChronoUnit.MILLIS));
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequest) {
        Usuario userPrincipal = obterUsuarioPrincipal(loginRequest.getEmail(), loginRequest.getSenha());
        return login(userPrincipal);
    }

    @Override
    public void logout(Usuario usuario) {
        Optional<JwtToken> optionalRefreshToken = tokenRepository.findByUsuario(usuario);
        if (optionalRefreshToken.isPresent() && optionalRefreshToken.get().getUsuario().getId().equals(usuario.getId())) {
            tokenService.deletarToken(optionalRefreshToken.get());
            removerRefreshToken();
        } else {
            throw new BadRequestException("tokenExpired");
        }
    }
}
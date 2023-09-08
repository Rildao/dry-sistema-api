package com.drylands.api.services.impl;

import com.drylands.api.domain.JwtToken;
import com.drylands.api.domain.Usuario;
import com.drylands.api.domain.enums.TokenEnum;
import com.drylands.api.infrastructure.repositories.TokenRepository;
import com.drylands.api.services.TokenService;
import io.jsonwebtoken.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.secret}")
    private String secret;

    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    private String criarValorTokenJwt(Usuario usuario, Duration expiraEm, Map<String, Object> claims) {
        Date agora = new Date();
        Date dataExpiracao = new Date(agora.getTime() + expiraEm.toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(dataExpiracao)
                .setIssuer("spring template")
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Claims obterTodasClaimsPeloToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private <T> T obterClaimPeloToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = obterTodasClaimsPeloToken(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String obterEmailpeloToken(String token) {
        return obterClaimPeloToken(token, Claims::getSubject);
    }

    @Override
    public String criarValorTokenJwt(Usuario usuario, Duration expiraEm) {
        Map<String, Object> claims = new HashMap<>();

        return criarValorTokenJwt(usuario, expiraEm, claims);
    }

    @Override
    @Transactional
    public JwtToken criarToken(Usuario usuario, Duration expiraEm, TokenEnum tokenTipo) {
        String tokenValor = criarValorTokenJwt(usuario, expiraEm);

        JwtToken jwtToken = new JwtToken();
        jwtToken.setValor(tokenValor);
        jwtToken.setUsuario(usuario);
        jwtToken.setTokenTipo(tokenTipo);

        jwtToken.setCodigoVerificacao(new Random().nextLong(10800000) + 100000);

        return tokenRepository.save(jwtToken);
    }

    @Override
    public boolean validarJwtToken(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Assinatura JWT inválida");
        } catch (MalformedJwtException ex) {
            log.error("Token JWT inválido");
        } catch (ExpiredJwtException ex) {
            log.error("Token JWT expirado");
        } catch (UnsupportedJwtException ex) {
            log.error("Token JWT não compatível");
        } catch (IllegalArgumentException ex) {
            log.error("A string de declarações JWT está vazia.");
        }
        return false;
    }

    @Override
    public void deletarToken(JwtToken jwtToken) {
        tokenRepository.delete(jwtToken);
    }
}
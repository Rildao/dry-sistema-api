package com.drylands.api.jobs;

import com.drylands.api.domain.JwtToken;
import com.drylands.api.infrastructure.repositories.TokenRepository;
import com.drylands.api.services.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class JwtExpiradoJob {

    private final TokenRepository tokenRepository;
    private final TokenService tokenService;

    public JwtExpiradoJob(TokenRepository tokenRepository, TokenService tokenService) {
        this.tokenService = tokenService;
        this.tokenRepository = tokenRepository;
    }

    @Scheduled(fixedDelayString = "10800000")
    public void deletarTokensExpirados() {
        log.warn("JOB: deletar tokens expirados");

        List<JwtToken> expiredTokens = tokenRepository
                .findAll()
                .stream()
                .filter(jwtToken -> !tokenService.validarJwtToken(jwtToken.getValor()))
                .toList();

        tokenRepository.deleteAll(expiredTokens);

        log.info("Os seguintes tokens foram apagados {}", expiredTokens);
    }
}
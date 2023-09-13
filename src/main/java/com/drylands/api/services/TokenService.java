package com.drylands.api.services;

import com.drylands.api.domain.JwtToken;
import com.drylands.api.domain.Usuario;
import com.drylands.api.domain.enums.TokenEnum;

import java.time.Duration;


public interface TokenService {

    String obterEmailpeloToken(String token);
    String criarValorTokenJwt(Usuario usuario, Duration expiraEm);
    JwtToken criarToken(Usuario user, Duration expiraEm, TokenEnum tokenTipo);
    boolean validarJwtToken(String jwtToken);
    void deletarToken(JwtToken jwtToken);
}

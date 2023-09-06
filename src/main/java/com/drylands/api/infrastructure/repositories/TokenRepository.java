package com.drylands.api.infrastructure.repositories;

import com.drylands.api.domain.JwtToken;
import com.drylands.api.domain.Usuario;
import com.drylands.api.domain.enums.TokenEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<JwtToken, Long> {

    Optional<JwtToken> findByUsuario(Usuario usuario);
    Optional<JwtToken> findByValorAndTokenTipo(String value, TokenEnum tokenEnum);
    Optional<JwtToken> findByCodigoVerificacaoAndTokenTipo(Long codigo, TokenEnum tokenEnum);
}
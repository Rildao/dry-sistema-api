package com.drylands.api.services.impl;

import com.drylands.api.domain.JwtToken;
import com.drylands.api.domain.Usuario;
import com.drylands.api.domain.enums.TokenEnum;
import com.drylands.api.infrastructure.exceptions.BadRequestException;
import com.drylands.api.infrastructure.exceptions.NotFoundException;
import com.drylands.api.infrastructure.repositories.TokenRepository;
import com.drylands.api.infrastructure.repositories.UsuarioRepository;
import com.drylands.api.rest.dtos.request.CodigoConfirmacaoRequestDTO;
import com.drylands.api.services.TokenService;
import com.drylands.api.services.UsuarioService;
import com.drylands.api.utils.UtilidadesData;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(TokenService tokenService, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository) {
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Usuario criarUsuario(Usuario usuario) {

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        UtilidadesData.configurarDatasComFusoHorarioBrasileiro(usuario);
        usuario.setPrimeiroAcesso(Boolean.FALSE);

        usuarioRepository.save(usuario);

        tokenService.criarToken(usuario, Duration.of(60000, ChronoUnit.MILLIS ), TokenEnum.ATIVAR_CONTA);

        return usuario;
    }

    @Override
    public Usuario obterUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuario não encontrado!"));
    }

    @Override
    public Usuario obterUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Usuario não encontrado!"));
    }

    @Override
    public Usuario ativarConta(CodigoConfirmacaoRequestDTO codigo) {
        Optional<JwtToken> optionalVerificationToken = tokenRepository.findByCodigoVerificacaoAndTokenTipo(codigo.getCodigo(), TokenEnum.ATIVAR_CONTA);

        if (optionalVerificationToken.isPresent()) {
            Usuario usuario = optionalVerificationToken.get().getUsuario();
            if (!tokenService.validarJwtToken(optionalVerificationToken.get().getValor())) {
                throw new BadRequestException("Código expirado!");
            } else {
                usuarioRepository.save(usuario);
                tokenRepository.delete(optionalVerificationToken.get());
            }
            return usuarioRepository.save(usuario);
        }

        throw new BadRequestException("Sessão expirada!");
    }
}
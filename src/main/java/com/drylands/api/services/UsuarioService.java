package com.drylands.api.services;


import com.drylands.api.domain.Usuario;
import com.drylands.api.rest.dtos.request.CodigoConfirmacaoRequestDTO;

public interface UsuarioService {

    Usuario criarUsuario(Usuario usuario);
    Usuario obterUsuarioPorId(Long id);
    Usuario obterUsuarioPorEmail(String email);
    Usuario ativarConta(CodigoConfirmacaoRequestDTO codigo);
}
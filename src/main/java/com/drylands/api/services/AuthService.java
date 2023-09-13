package com.drylands.api.services;


import com.drylands.api.domain.Usuario;
import com.drylands.api.rest.dtos.request.LoginRequestDTO;
import com.drylands.api.rest.dtos.response.AuthResponseDTO;

public interface AuthService {

    AuthResponseDTO login(LoginRequestDTO loginRequest);
    void logout(Usuario usuario);
}

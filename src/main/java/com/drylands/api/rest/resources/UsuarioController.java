package com.drylands.api.rest.resources;

import com.drylands.api.annotations.CurrentUser;
import com.drylands.api.domain.Usuario;
import com.drylands.api.rest.dtos.response.ApiResponseDTO;
import com.drylands.api.rest.dtos.response.UsuarioResponseDTO;
import com.drylands.api.services.AuthService;
import com.drylands.api.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
public class UsuarioController extends AbstractController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    public UsuarioController(ModelMapper modelMapper, AuthService authService, UsuarioService usuarioService) {
        super(modelMapper);
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    @Operation(tags = "Usuario", summary = "Obter usuario por id")
    public ResponseEntity<UsuarioResponseDTO> obterUsuarioPorId(@PathVariable Long id) {
        return new ResponseEntity<>(mapearDTO(usuarioService.obterUsuarioPorId(id), UsuarioResponseDTO.class), HttpStatus.OK);
    }

    @PostMapping("/logout")
    @Operation(tags = "Usuario", summary = "Finaliza sessão")
    public ResponseEntity<ApiResponseDTO> logout(@CurrentUser UserDetails usuarioDetails) {
        Usuario usuario = usuarioService.obterUsuarioPorEmail(usuarioDetails.getUsername());
        authService.logout(usuario);
        return new ResponseEntity<>(new ApiResponseDTO(true, "Sessão encerrada!"), HttpStatus.OK);
    }
}
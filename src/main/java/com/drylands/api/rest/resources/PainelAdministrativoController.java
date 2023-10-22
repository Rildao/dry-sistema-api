package com.drylands.api.rest.resources;

import com.drylands.api.domain.enums.ETipoVenda;
import com.drylands.api.rest.dtos.response.PainelAdministrativoDTO;
import com.drylands.api.services.PainelAdministrativo;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/painel-administrativo")
public class PainelAdministrativoController extends AbstractController {

    private final PainelAdministrativo painelAdministrativo;

    public PainelAdministrativoController(ModelMapper modelMapper, PainelAdministrativo painelAdministrativo) {
        super(modelMapper);
        this.painelAdministrativo = painelAdministrativo;
    }

    @GetMapping()
    @Operation(tags = "PainelAdministrativo", summary = "Pegar Informações Dashboard")
    public ResponseEntity<PainelAdministrativoDTO> informacoesPainelAdministrativo (@RequestParam(value = "tipoVenda", required = false) ETipoVenda tipoVenda) {
        return new ResponseEntity<>(this.painelAdministrativo.informacoesPainelAdministrativo(tipoVenda), HttpStatus.OK);
    }
}
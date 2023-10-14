package com.drylands.api.rest.resources;

import com.drylands.api.rest.dtos.response.RelatorioDTO;
import com.drylands.api.services.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/relatorio")
public class RelatorioController extends AbstractController {

    private final RelatorioService relatorioService;

    public RelatorioController(ModelMapper modelMapper, RelatorioService relatorioService) {
        super(modelMapper);
        this.relatorioService = relatorioService;
    }

    @GetMapping()
    @Operation(tags = "Relatorio", summary = "Pegar Informações relatorio")
    public ResponseEntity<List<RelatorioDTO>> informacoesPainelAdministrativo (@RequestParam(value = "dataInicio", required = true) String dataInicio,
                                                                               @RequestParam(value = "dataFim", required = true) String dataFim) {
        return new ResponseEntity<>(this.relatorioService.informacoesParaRelatorio(dataInicio, dataFim), HttpStatus.OK);
    }
}
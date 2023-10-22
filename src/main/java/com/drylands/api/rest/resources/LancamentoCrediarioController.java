package com.drylands.api.rest.resources;

import com.drylands.api.rest.dtos.lancamento_crediario.LancamentoCrediarioDTO;

import com.drylands.api.services.LancamentoCrediarioService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lancamento-crediario")
public class LancamentoCrediarioController extends AbstractController {

    private final LancamentoCrediarioService lancamentoCrediarioService;

    public LancamentoCrediarioController(ModelMapper modelMapper, LancamentoCrediarioService lancamentoCrediarioService) {
        super(modelMapper);
        this.lancamentoCrediarioService = lancamentoCrediarioService;
    }

    @PutMapping("/{id}")
    @Operation(tags = "Lancamento", summary = "Atualizar status lancamentos por id")
    public ResponseEntity<LancamentoCrediarioDTO> atualizarStatusLancamentoCrediarioPorId(@PathVariable("id") Long id, @RequestBody LancamentoCrediarioDTO lancamentoCrediarioDTO) {
        return new ResponseEntity<>(mapearDTO(this.lancamentoCrediarioService.atualizarStatusLancamentoCrediarioPorId(id, lancamentoCrediarioDTO), LancamentoCrediarioDTO.class), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(tags = "Lancamento", summary = "Pegar todos os lancamentos por id da venda")
    public ResponseEntity<List<LancamentoCrediarioDTO>> pegarLancamentosPorVendaId(@PathVariable("id") Long id) {
        List<LancamentoCrediarioDTO> lancamentoCrediarioDto = this.lancamentoCrediarioService.pegarLancamentosPorVendaId(id)
                .stream().map(lancamentoCrediario -> mapearDTO(lancamentoCrediario, LancamentoCrediarioDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>((lancamentoCrediarioDto), HttpStatus.OK);
    }
}
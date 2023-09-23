package com.drylands.api.rest.resources;

import com.drylands.api.rest.dtos.notificacao.ListagemNotificacaoDTO;
import com.drylands.api.rest.dtos.notificacao.NotificacaoDTO;
import com.drylands.api.services.NotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificacao")
public class NotificacaoController extends AbstractController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(ModelMapper modelMapper,  NotificacaoService notificacaoService) {
        super(modelMapper);
        this.notificacaoService = notificacaoService;
    }

    @PutMapping("/{id}")
    @Operation(tags = "Notificacao", summary = "Atualizar status notificacao por id")
    public ResponseEntity<NotificacaoDTO> atualizarStatusLancamentoCrediarioPorId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(mapearDTO(this.notificacaoService.atualizarNotificacao(id), NotificacaoDTO.class), HttpStatus.OK);
    }

    @GetMapping
    @Operation(tags = "Notificacao", summary = "Pegar todos os lancamentos por id da venda")
    public ResponseEntity<ListagemNotificacaoDTO> pegarLancamentosPorVendaId(Pageable pageable, @RequestParam(value = "filter", required = false) String filter,
                                                                             @RequestParam(value = "lido", required = false) Boolean lido) {
        return new ResponseEntity<>((this.notificacaoService.listarNotificacoes(filter, lido, pageable)), HttpStatus.OK);
    }
}
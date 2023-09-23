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

import java.math.BigInteger;

@RestController
@RequestMapping("/api/notificacao")
public class NotificacaoController extends AbstractController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(ModelMapper modelMapper,  NotificacaoService notificacaoService) {
        super(modelMapper);
        this.notificacaoService = notificacaoService;
    }

    @PostMapping
    @Operation(tags = "Notificacao", summary = "Criar notificação")
    public ResponseEntity<NotificacaoDTO> criarNotificacao(@RequestBody NotificacaoDTO notificacaoDTO) {
        return new ResponseEntity<>(mapearDTO(this.notificacaoService.criarNotificacao(notificacaoDTO), NotificacaoDTO.class), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(tags = "Notificacao", summary = "Atualizar status notificação por id")
    public ResponseEntity<NotificacaoDTO> atualizarNotificacao(@PathVariable("id") Long id) {
        return new ResponseEntity<>(mapearDTO(this.notificacaoService.atualizarNotificacao(id), NotificacaoDTO.class), HttpStatus.OK);
    }

    @GetMapping
    @Operation(tags = "Notificacao", summary = "Listar notificações com com paginação e filtro")
    public ResponseEntity<ListagemNotificacaoDTO> listarNotificacoes(Pageable pageable, @RequestParam(value = "filter", required = false) String filter,
                                                                             @RequestParam(value = "lido", required = false) Boolean lido) {
        return new ResponseEntity<>((this.notificacaoService.listarNotificacoes(filter, lido, pageable)), HttpStatus.OK);
    }
    @GetMapping("/contagem")
    @Operation(tags = "Notificacao", summary = "Contagem de notificações não lidas")
    public ResponseEntity<BigInteger> listarNotificacoes() {
        return new ResponseEntity<>((this.notificacaoService.contagemDeNotificacoesNaoLidas()), HttpStatus.OK);
    }
}
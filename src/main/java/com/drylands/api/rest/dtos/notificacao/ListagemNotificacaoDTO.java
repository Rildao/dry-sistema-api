package com.drylands.api.rest.dtos.notificacao;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListagemNotificacaoDTO {
    List<NotificacaoDTO> notificacoes;
    Long totalElements;
    int totalPage;
    int size;
}

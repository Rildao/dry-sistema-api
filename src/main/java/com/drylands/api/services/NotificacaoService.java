package com.drylands.api.services;

import com.drylands.api.domain.Notificacao;
import com.drylands.api.rest.dtos.notificacao.ListagemNotificacaoDTO;
import org.springframework.data.domain.Pageable;

public interface NotificacaoService {
    public ListagemNotificacaoDTO listarNotificacoes (String filter, boolean lido, Pageable pageable);
    public Notificacao atualizarNotificacao(Long id);
}
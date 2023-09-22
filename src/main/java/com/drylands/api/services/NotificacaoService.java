package com.drylands.api.services;

import com.drylands.api.domain.Notificacao;
import com.drylands.api.rest.dtos.notificacao.NotificacaoDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificacaoService {
    public List<Notificacao> listarNotificacoes (Pageable pageable);
    public Notificacao atualizarNotificacao(NotificacaoDTO notificacaoDTO);
}
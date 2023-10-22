package com.drylands.api.services;

import com.drylands.api.domain.Notificacao;
import com.drylands.api.rest.dtos.notificacao.ListagemNotificacaoDTO;
import com.drylands.api.rest.dtos.notificacao.NotificacaoDTO;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;

public interface NotificacaoService {
    public Notificacao criarNotificacao(NotificacaoDTO notificacaoDto);
    public ListagemNotificacaoDTO listarNotificacoes (String filter, boolean lido, Pageable pageable);
    public Notificacao atualizarNotificacao(Long id);
    public BigInteger contagemDeNotificacoesNaoLidas();
}
package com.drylands.api.services.impl;

import com.drylands.api.domain.Notificacao;
import com.drylands.api.infrastructure.repositories.VendaRepository;
import com.drylands.api.rest.dtos.notificacao.NotificacaoDTO;
import com.drylands.api.services.LancamentoCrediarioService;
import com.drylands.api.services.NotificacaoService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacaoServiceImpl implements NotificacaoService {

    VendaRepository vendaRepository;

    NotificacaoService notificacaoService;

    private ModelMapper modelMapper;

    public NotificacaoServiceImpl(VendaRepository vendaRepository,
                                  LancamentoCrediarioService lancamentoCrediarioService,
                                  NotificacaoService notificacaoService,
                                  ModelMapper modelMapper) {
        this.notificacaoService = notificacaoService;
        this.vendaRepository = vendaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Notificacao> listarNotificacoes(Pageable pageable) {
        return null;
    }

    @Override
    public Notificacao atualizarNotificacao(NotificacaoDTO notificacaoDTO) {
        return null;
    }
}
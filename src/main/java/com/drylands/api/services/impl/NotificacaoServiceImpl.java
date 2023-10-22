package com.drylands.api.services.impl;

import com.drylands.api.domain.Notificacao;
import com.drylands.api.infrastructure.exceptions.NotFoundException;
import com.drylands.api.infrastructure.repositories.NotificacaoRepository;
import com.drylands.api.infrastructure.repositories.VendaRepository;
import com.drylands.api.rest.dtos.notificacao.ListagemNotificacaoDTO;
import com.drylands.api.rest.dtos.notificacao.NotificacaoDTO;
import com.drylands.api.services.LancamentoCrediarioService;
import com.drylands.api.services.NotificacaoService;
import com.drylands.api.utils.UtilidadesData;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacaoServiceImpl implements NotificacaoService {

    VendaRepository vendaRepository;

    NotificacaoRepository notificacaoRepository;

    private ModelMapper modelMapper;

    public NotificacaoServiceImpl(VendaRepository vendaRepository,
                                  LancamentoCrediarioService lancamentoCrediarioService,
                                  NotificacaoRepository notificacaoRepository,
                                  ModelMapper modelMapper) {
        this.notificacaoRepository = notificacaoRepository;
        this.vendaRepository = vendaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Notificacao criarNotificacao(NotificacaoDTO notificacaoDto) {
        Notificacao notificacao = modelMapper.map(notificacaoDto, Notificacao.class);

        UtilidadesData.configurarDatasComFusoHorarioBrasileiro(notificacao);

        return this.notificacaoRepository.save(notificacao);
    }

    @Override
    @Transactional(readOnly = true)
    public ListagemNotificacaoDTO listarNotificacoes(String filter, boolean lido, Pageable pageable) {
        ListagemNotificacaoDTO listagemClientesPage = new ListagemNotificacaoDTO();
        List<NotificacaoDTO> listaDeNotificacoes = new ArrayList<>();

        Page<Notificacao> page;

        if (StringUtils.hasText(filter)) {
            page =  this.notificacaoRepository.listarNotificacoesComFiltrosEPaginado(lido, filter, pageable);
        } else {
            page = this.notificacaoRepository.listarNotificacoesComFiltrosEPaginado(lido, pageable);
        }

        page.getContent().forEach(notificacao -> {
            NotificacaoDTO notificacaoDTO = modelMapper.map(notificacao, NotificacaoDTO.class);
            listaDeNotificacoes.add(notificacaoDTO);
        });

        listagemClientesPage.setNotificacoes(listaDeNotificacoes);
        listagemClientesPage.setTotalElements(page.getTotalElements());
        listagemClientesPage.setTotalPage(page.getTotalPages());
        listagemClientesPage.setSize(page.getSize());

        return listagemClientesPage;
    }

    @Override
    @Transactional
    public Notificacao atualizarNotificacao(Long id) {
        Optional<Notificacao> notificacao = this.notificacaoRepository.findById(id);

        if (notificacao.isEmpty()) throw new NotFoundException("Notificação não não encontrada.");

        UtilidadesData.configurarDatasComFusoHorarioBrasileiroParaAtualizar(notificacao.get());

        notificacao.get().setLido(Boolean.TRUE);

        return this.notificacaoRepository.save(notificacao.get());
    }

    @Override
    @Transactional(readOnly = true)
    public BigInteger contagemDeNotificacoesNaoLidas() {
        return this.notificacaoRepository.contagemDeNotificacoesNaoLidas();
    }
}
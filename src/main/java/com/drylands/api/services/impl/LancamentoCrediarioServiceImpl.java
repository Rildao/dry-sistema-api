package com.drylands.api.services.impl;

import com.drylands.api.domain.LancamentoCrediario;
import com.drylands.api.infrastructure.repositories.LancamentoCrediarioRepository;
import com.drylands.api.infrastructure.repositories.VendaRepository;
import com.drylands.api.services.LancamentoCrediarioService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LancamentoCrediarioServiceImpl implements LancamentoCrediarioService {

    VendaRepository vendaRepository;

    LancamentoCrediarioRepository lancamentoCrediarioRepository;

    private ModelMapper modelMapper;

    public LancamentoCrediarioServiceImpl(LancamentoCrediarioRepository lancamentoCrediarioRepository,
                                          VendaRepository vendaRepository, ModelMapper modelMapper) {
        this.lancamentoCrediarioRepository = lancamentoCrediarioRepository;
        this.vendaRepository = vendaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public LancamentoCrediario criarLancamentoCrediario(LancamentoCrediario lancamentoCrediario) {
        return this.lancamentoCrediarioRepository.save(lancamentoCrediario);
    }
}

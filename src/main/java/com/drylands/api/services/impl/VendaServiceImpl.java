package com.drylands.api.services.impl;

import com.drylands.api.domain.Venda;
import com.drylands.api.domain.enums.EStatusVenda;
import com.drylands.api.domain.enums.ETipoVenda;
import com.drylands.api.infrastructure.exceptions.NotFoundException;
import com.drylands.api.infrastructure.repositories.VendaRepository;
import com.drylands.api.rest.dtos.venda.ListagemVendaDTO;
import com.drylands.api.rest.dtos.venda.VendaCsvDTO;
import com.drylands.api.rest.dtos.venda.VendaDTO;
import com.drylands.api.services.LancamentoCrediarioService;
import com.drylands.api.services.VendaService;
import com.drylands.api.utils.UtilidadesData;
import com.drylands.api.utils.UtilidadesVendas;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VendaServiceImpl implements VendaService {

    VendaRepository vendaRepository;
    
    LancamentoCrediarioService lancamentoCrediarioService;

    private ModelMapper modelMapper;

    public VendaServiceImpl(VendaRepository vendaRepository, 
                            LancamentoCrediarioService lancamentoCrediarioService,
                            ModelMapper modelMapper) {
        this.lancamentoCrediarioService = lancamentoCrediarioService;
        this.vendaRepository = vendaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Venda criarVenda(VendaDTO vendaDto) {

        UtilidadesVendas.validarVendas(vendaDto);

        if (ETipoVenda.PIX.equals(vendaDto.getTipoVenda()) || ETipoVenda.CARTAO_CREDITO.equals(vendaDto.getTipoVenda()) || ETipoVenda.DINHEIRO.equals(vendaDto.getTipoVenda())) {
            vendaDto.setStatusVenda(EStatusVenda.PAGO.toString());
        } else {
            vendaDto.setStatusVenda(EStatusVenda.ANDAMENTO.toString());
        }

        Venda novaVenda = modelMapper.map(vendaDto, Venda.class);

        novaVenda = this.vendaRepository.save(novaVenda);

        this.lancamentoCrediarioService.gerandoLancamentosParaCrediario(novaVenda);

        UtilidadesData.configurarDatasComFusoHorarioBrasileiro(novaVenda);

        return novaVenda;
    }

    @Override
    @Transactional
    public Venda atualizarVenda(Long id, VendaDTO vendaDto) {

        UtilidadesVendas.validarVendas(vendaDto);

        this.pegarVendaPorId(id);

        Venda vendaUpdated = modelMapper.map(vendaDto, Venda.class);

        UtilidadesData.configurarDatasComFusoHorarioBrasileiroParaAtualizar(vendaUpdated);

        return this.vendaRepository.save(vendaUpdated);
    }

    @Override
    @Transactional(readOnly = true)
    public Venda pegarVendaPorId(Long id) {
        Optional<Venda> venda = this.vendaRepository.findById(id);

        if(venda.isEmpty()) {
            throw new NotFoundException("Venda não encontrada.");
        }

        return venda.get();
    }

    @Override
    @Transactional(readOnly = true)
    public ListagemVendaDTO listarVendas(Long clienteId, Pageable pageable) {
        ListagemVendaDTO listagemVendaPage = new ListagemVendaDTO();
        List<VendaDTO> listaDeVendas = new ArrayList<>();

        if(Objects.nonNull(clienteId)) {
            this.listarVendasPorCliente(listagemVendaPage, listaDeVendas, clienteId, pageable);
        } else {
            this.listarTodasAsVendas(listagemVendaPage, listaDeVendas, pageable);
        }

        return listagemVendaPage;
    }

    @Override
    public List<VendaCsvDTO> listarVendasSemPaginacao() {
        return this.vendaRepository.findAll().stream().map(venda -> modelMapper.map(venda, VendaCsvDTO.class)).collect(Collectors.toList());
    }

    public ListagemVendaDTO listarTodasAsVendas(ListagemVendaDTO ListagemVendaPage,
                                                List<VendaDTO> listaDeVendas,
                                                Pageable pageable) {

        Page<Venda> page = this.vendaRepository.findAllByOrderByDataVendaDesc(pageable);

        page.getContent().forEach(venda -> {
            VendaDTO vendaDto = modelMapper.map(venda, VendaDTO.class);
            listaDeVendas.add(vendaDto);
        });

        ListagemVendaPage.setVendas(listaDeVendas);
        ListagemVendaPage.setTotalElements(page.getTotalElements());
        ListagemVendaPage.setTotalPage(page.getTotalPages());
        ListagemVendaPage.setSize(page.getSize());

        return ListagemVendaPage;
    }

    @Transactional(readOnly = true)
    public ListagemVendaDTO listarVendasPorCliente(ListagemVendaDTO ListagemVendaPage,
                                                   List<VendaDTO> listaDeVendas,
                                                   Long clienteId,
                                                   Pageable pageable) {
        Page<Venda> page = this.vendaRepository.findAllByClienteId(clienteId, pageable);

        page.getContent().forEach(venda -> {
            VendaDTO vendaDto = modelMapper.map(venda, VendaDTO.class);
            listaDeVendas.add(vendaDto);
        });

        ListagemVendaPage.setVendas(listaDeVendas);
        ListagemVendaPage.setTotalElements(page.getTotalElements());
        ListagemVendaPage.setTotalPage(page.getTotalPages());
        ListagemVendaPage.setSize(page.getSize());

        return ListagemVendaPage;
    }

    @Override
    @Transactional
    public void deletarVenda(Long id) {
        this.pegarVendaPorId(id);

        this.lancamentoCrediarioService.deletarLancamentosPorVendaId(id);
        this.vendaRepository.deleteById(id);
    }
}
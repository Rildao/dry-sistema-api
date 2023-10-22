package com.drylands.api.services.impl;

import com.drylands.api.domain.Cliente;
import com.drylands.api.domain.LancamentoCrediario;
import com.drylands.api.domain.Venda;
import com.drylands.api.domain.enums.EStatusVenda;
import com.drylands.api.domain.enums.ETipoVenda;
import com.drylands.api.infrastructure.exceptions.BadRequestException;
import com.drylands.api.infrastructure.exceptions.NotFoundException;
import com.drylands.api.infrastructure.repositories.ClienteRepository;
import com.drylands.api.infrastructure.repositories.VendaRepository;
import com.drylands.api.rest.dtos.cliente.ClienteCsvDTO;
import com.drylands.api.rest.dtos.cliente.ClienteDTO;
import com.drylands.api.rest.dtos.cliente.ClienteVendasDTO;
import com.drylands.api.rest.dtos.cliente.ListagemClienteDTO;
import com.drylands.api.rest.dtos.venda.VendaSimplificadoDTO;
import com.drylands.api.services.ClienteService;
import com.drylands.api.services.LancamentoCrediarioService;
import com.drylands.api.utils.UtilidadesData;
import com.drylands.api.utils.UtilidadesDocumentos;
import com.drylands.api.utils.UtilidadesVendas;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    ClienteRepository clienteRepository;

    VendaRepository vendaRepository;

    LancamentoCrediarioService lancamentoCrediarioService;

    private ModelMapper modelMapper;

    public ClienteServiceImpl(ClienteRepository clienteRepository,
            VendaRepository vendaRepository, LancamentoCrediarioService lancamentoCrediarioService, ModelMapper modelMapper) {
        this.clienteRepository = clienteRepository;
        this.vendaRepository = vendaRepository;
        this.lancamentoCrediarioService = lancamentoCrediarioService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Cliente criarCliente(ClienteDTO clienteDto) {

        Optional<Cliente> cliente = this.clienteRepository.findByCpf(clienteDto.getCpf());

        clienteValidacaoDeCampos(cliente);

        UtilidadesDocumentos.validarCpf(clienteDto.getCpf());

        Cliente novoCliente = modelMapper
                .map(clienteDto, Cliente.class);

        UtilidadesData.configurarDatasComFusoHorarioBrasileiro(novoCliente);

        return this.clienteRepository.save(novoCliente);
    }

    @Override
    @Transactional
    public Cliente criarOuAtualizarClienteComVendas(ClienteVendasDTO clienteVendasDto) {
        Cliente clienteEntidade = new Cliente();

        Optional<Cliente> cliente = this.clienteRepository.findByCpf(clienteVendasDto.getCpf());

        if(Objects.nonNull(clienteVendasDto.getId())) {
            UtilidadesDocumentos.validarCpf(clienteVendasDto.getCpf());

            clienteEntidade = cliente.get();

            clienteEntidade.setNome(clienteVendasDto.getNome());
            clienteEntidade.setCpf(clienteVendasDto.getCpf());
            clienteEntidade.setEndereco(clienteVendasDto.getEndereco());
            clienteEntidade.setTelefone(clienteVendasDto.getTelefone());
            UtilidadesData.configurarDatasComFusoHorarioBrasileiroParaAtualizar(clienteEntidade);
        } else {
            clienteValidacaoDeCampos(cliente);

            clienteEntidade.setNome(clienteVendasDto.getNome());
            clienteEntidade.setCpf(clienteVendasDto.getCpf());
            clienteEntidade.setEndereco(clienteVendasDto.getEndereco());
            clienteEntidade.setTelefone(clienteVendasDto.getTelefone());
            UtilidadesData.configurarDatasComFusoHorarioBrasileiro(clienteEntidade);
        }

        clienteEntidade = this.clienteRepository.save(clienteEntidade);

        relacionarClienteVenda(clienteVendasDto, clienteEntidade);

        return clienteEntidade;
    }

    @Override
    @Transactional
    public Cliente atualizarCliente(Long id, ClienteDTO clienteDto) {

        this.pegarClientePorId(id);

        Cliente clienteUpdated = modelMapper.map(clienteDto, Cliente.class);

        UtilidadesData.configurarDatasComFusoHorarioBrasileiroParaAtualizar(clienteUpdated);

        return this.clienteRepository.save(clienteUpdated);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteVendasDTO pegarClientePorId(Long id) {
        Optional<Cliente> cliente = this.clienteRepository.findById(id);

        if(cliente.isEmpty()) {
            throw new NotFoundException("Cliente não encontrado.");
        }

        ClienteVendasDTO clienteVendasDto = montandoClienteComVendas(id, cliente.get());

        return clienteVendasDto;
    }

    @Override
    @Transactional(readOnly = true)
    public ListagemClienteDTO listarClientes(Pageable pageable, String filter) {
        ListagemClienteDTO listagemClientesPage = new ListagemClienteDTO();
        List<ClienteDTO> listaDeClientes = new ArrayList<>();

        Page<Cliente> page;

        /*
        * TODO: implementar critéria builder
        * */
        if (Objects.nonNull(filter)) {
            page = this.clienteRepository.findAllByFilters(pageable, filter);
        } else {
            page = this.clienteRepository.findAll(pageable);
        }

        page.getContent().forEach(cliente -> {
            ClienteDTO clienteDto = modelMapper.map(cliente, ClienteDTO.class);
            listaDeClientes.add(clienteDto);
        });

        listagemClientesPage.setClientes(listaDeClientes);
        listagemClientesPage.setTotalElements(page.getTotalElements());
        listagemClientesPage.setTotalPage(page.getTotalPages());
        listagemClientesPage.setSize(page.getSize());

        return listagemClientesPage;
    }

    @Override
    public List<ClienteCsvDTO> listarClientesSemPaginacao() {
        return this.clienteRepository.findAll().stream().map(cliente -> modelMapper.map(cliente, ClienteCsvDTO.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletarCliente(Long id) {
        this.pegarClientePorId(id);

        this.lancamentoCrediarioService.deletarLancamentosPorClienteId(id);
        this.vendaRepository.deleteByClienteId(id);
        this.clienteRepository.deleteById(id);
    }

    private static void clienteValidacaoDeCampos(Optional<Cliente> cliente) {
        if(cliente.isPresent()) {
            throw new BadRequestException("Cliente já possui cadastro!");
        }
    }

    private ClienteVendasDTO montandoClienteComVendas(Long id, Cliente cliente) {
        List<Venda> vendas = this.vendaRepository.findAllByClienteIdAndDataCriacaoOrderByDesc(id);

        ClienteVendasDTO clienteVendasDto = new ClienteVendasDTO();
        clienteVendasDto.setId(cliente.getId());
        clienteVendasDto.setNome(cliente.getNome());
        clienteVendasDto.setCpf(cliente.getCpf());
        clienteVendasDto.setEndereco(cliente.getEndereco());
        clienteVendasDto.setTelefone(cliente.getTelefone());
        clienteVendasDto.setDataCriacao(cliente.getDataCriacao());
        clienteVendasDto.setDataAtualizacao(cliente.getDataAtualizacao());

        if(!vendas.isEmpty()) {
            List<VendaSimplificadoDTO> vendasDto = new ArrayList<>();

            vendas.forEach(venda -> {
                VendaSimplificadoDTO vendaDto = modelMapper.map(venda, VendaSimplificadoDTO.class);
                vendasDto.add(vendaDto);
            });

            clienteVendasDto.setVendas(vendasDto);
        }

        return clienteVendasDto;
    }

    private void relacionarClienteVenda(ClienteVendasDTO clienteVendasDto, Cliente cliente) {
        List<Venda> vendas = new ArrayList<>();

        clienteVendasDto.getVendas().forEach(vendaDto -> {
            UtilidadesVendas.validarVendas(vendaDto);

            if(Objects.nonNull(vendaDto.getId())) {
                Venda venda = this.vendaRepository.findById(vendaDto.getId()).get();
                venda.setValorVenda(vendaDto.getValorVenda());
                venda.setStatusVenda(EStatusVenda.valueOf(vendaDto.getStatusVenda()));
                venda.setDiaVencimentoLancamento(vendaDto.getDiaVencimentoLancamento());
                venda.setTipoVenda(vendaDto.getTipoVenda());
                venda.setQuantidadeParcelas(vendaDto.getQuantidadeParcelas());
                venda.setDataVenda(vendaDto.getDataVenda());
                venda.setDescricao(vendaDto.getDescricao());

                UtilidadesData.configurarDatasComFusoHorarioBrasileiroParaAtualizar(venda);

                vendas.add(venda);
            } else {
                UtilidadesVendas.validarVendas(vendaDto);

                Venda venda = modelMapper.map(vendaDto, Venda.class);
                UtilidadesData.configurarDatasComFusoHorarioBrasileiro(venda);

                if (ETipoVenda.PIX.equals(vendaDto.getTipoVenda()) || ETipoVenda.CARTAO_CREDITO.equals(vendaDto.getTipoVenda()) || ETipoVenda.DINHEIRO.equals(vendaDto.getTipoVenda())) {
                    vendaDto.setStatusVenda(EStatusVenda.PAGO.toString());
                } else {
                    vendaDto.setStatusVenda(EStatusVenda.ANDAMENTO.toString());
                }

                venda.setCliente(cliente);
                vendas.add(venda);
            }
        });

        List<Venda> vendaSalvas =  this.vendaRepository.saveAll(vendas);

        vendaSalvas
                .stream()
                .filter(venda -> venda.getTipoVenda().equals(ETipoVenda.CREDIARIO))
                .forEach(venda -> {
                    List<LancamentoCrediario> lancamentoCrediarios = this.lancamentoCrediarioService.pegarLancamentosPorVendaId(venda.getId());
                    if(lancamentoCrediarios.isEmpty()) {
                        this.lancamentoCrediarioService.gerandoLancamentosParaCrediario(venda);
                    }
                });
    }
}

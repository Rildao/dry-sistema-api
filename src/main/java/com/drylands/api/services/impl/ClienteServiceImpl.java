package com.drylands.api.services.impl;

import com.drylands.api.domain.Cliente;
import com.drylands.api.infrastructure.exceptions.BadRequestException;
import com.drylands.api.infrastructure.exceptions.NotFoundException;
import com.drylands.api.infrastructure.repositories.ClienteRepository;
import com.drylands.api.rest.dtos.cliente.ClienteDTO;
import com.drylands.api.rest.dtos.cliente.ListagemClienteDTO;
import com.drylands.api.services.ClienteService;
import com.drylands.api.utils.UtilidadesData;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    ClienteRepository clienteRepository;

    private ModelMapper modelMapper;

    public ClienteServiceImpl(ClienteRepository clienteRepository, ModelMapper modelMapper) {
        this.clienteRepository = clienteRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Cliente criarCliente(ClienteDTO clienteDto) {

        Optional<Cliente> cliente = this.clienteRepository.findByCpf(clienteDto.getCpf());

        clienteValidacaoDeCampos(cliente);

        Cliente novoCliente = modelMapper
                .map(clienteDto, Cliente.class);

        UtilidadesData.configurarDatasComFusoHorarioBrasileiro(novoCliente);

        return this.clienteRepository.save(novoCliente);
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
    public Cliente pegarClientePorId(Long id) {
        Optional<Cliente> cliente = this.clienteRepository.findById(id);

        if(cliente.isEmpty()) {
            throw new NotFoundException("Cliente não encontrado.");
        }

        return cliente.get();
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
    @Transactional
    public void deletarCliente(Long id) {
        this.pegarClientePorId(id);

        this.clienteRepository.deleteById(id);
    }

    private static void clienteValidacaoDeCampos(Optional<Cliente> cliente) {
        if(cliente.isPresent()) {
            throw new BadRequestException("Cliente já possui cadastro!");
        }
    }
}

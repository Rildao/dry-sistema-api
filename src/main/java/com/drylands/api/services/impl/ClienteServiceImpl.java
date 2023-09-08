package com.drylands.api.services.impl;

import com.drylands.api.domain.Cliente;
import com.drylands.api.infrastructure.exceptions.BadRequestException;
import com.drylands.api.infrastructure.exceptions.NotFoundException;
import com.drylands.api.infrastructure.repositories.ClienteRepository;
import com.drylands.api.rest.dtos.cliente.ClienteDTO;
import com.drylands.api.services.ClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
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

        /*
        * TODO: Separar metódos de validação e validar campos.
        * */
        if(cliente.isPresent()) {
            throw new BadRequestException("Cliente já possui cadastro!");
        }

        Cliente novoCliente = modelMapper.map(clienteDto, Cliente.class);

        /*
         * TODO: Após aprovação do PR de datas, utilizar aqui e deixar generico.
         * */
        novoCliente.setDataCriacao(null);
        novoCliente.setDataAtualizacao(null);

        return this.clienteRepository.save(novoCliente);
    }

    @Override
    @Transactional
    public Cliente atualizarCliente(Long id, ClienteDTO clienteDto) {

        this.pegarClientePorId(id);

        Cliente clienteUpdated = modelMapper.map(clienteDto, Cliente.class);

        /*
         * TODO: Após aprovação do PR de datas, jogar esse código em função generica na classe de utilidades.
         * */
        ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("America/Sao_Paulo"));
        Date date = Date.from(zdt.toInstant());

        clienteUpdated.setDataAtualizacao(date);

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
    public Page<Cliente> listarClientes(Pageable pageable) {

        /*
         * TODO: Trasformar lista de entidades em DTO.
         * */
        return this.clienteRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deletarCliente(Long id) {
        this.pegarClientePorId(id);

        this.clienteRepository.deleteById(id);
    }
}

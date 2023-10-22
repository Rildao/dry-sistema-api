package com.drylands.api.services;

import com.drylands.api.domain.Cliente;
import com.drylands.api.rest.dtos.cliente.ClienteCsvDTO;
import com.drylands.api.rest.dtos.cliente.ClienteDTO;
import com.drylands.api.rest.dtos.cliente.ClienteVendasDTO;
import com.drylands.api.rest.dtos.cliente.ListagemClienteDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClienteService {
    public Cliente criarCliente (ClienteDTO cliente);
    public Cliente criarOuAtualizarClienteComVendas(ClienteVendasDTO clienteVendasDto);
    public Cliente atualizarCliente (Long id, ClienteDTO cliente);
    public ClienteVendasDTO pegarClientePorId (Long id);
    public ListagemClienteDTO listarClientes (Pageable pageable, String filter);
    public List<ClienteCsvDTO> listarClientesSemPaginacao();
    public void deletarCliente(Long id);
}

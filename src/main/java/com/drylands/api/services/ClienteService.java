package com.drylands.api.services;

import com.drylands.api.domain.Cliente;
import com.drylands.api.rest.dtos.cliente.ClienteDTO;
import com.drylands.api.rest.dtos.cliente.ListagemClienteDTO;
import org.springframework.data.domain.Pageable;

public interface ClienteService {
    public Cliente criarCliente (ClienteDTO cliente);
    public Cliente atualizarCliente (Long id, ClienteDTO cliente);
    public Cliente pegarClientePorId (Long id);
    public ListagemClienteDTO listarClientes (Pageable pageable);
    public void deletarCliente(Long id);
}

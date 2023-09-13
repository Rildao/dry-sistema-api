package com.drylands.api.services;

import com.drylands.api.domain.Venda;
import com.drylands.api.rest.dtos.venda.ListagemVendaDTO;
import com.drylands.api.rest.dtos.venda.VendaDTO;
import org.springframework.data.domain.Pageable;

public interface VendaService {
    public Venda criarVenda (VendaDTO cliente);
    public Venda atualizarVenda (Long id, VendaDTO vendaDto);
    public Venda pegarVendaPorId (Long id);
    public ListagemVendaDTO listarVendas (Long clienteId, Pageable pageable);
    public void deletarVenda(Long id);
}
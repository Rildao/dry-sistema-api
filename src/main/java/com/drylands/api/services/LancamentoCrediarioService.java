package com.drylands.api.services;

import com.drylands.api.domain.LancamentoCrediario;
import com.drylands.api.domain.Venda;
import com.drylands.api.rest.dtos.lancamento_crediario.LancamentoCrediarioDTO;

import java.util.List;

public interface LancamentoCrediarioService {
    public LancamentoCrediario criarLancamentoCrediario (LancamentoCrediario lancamentoCrediario);
    public List<LancamentoCrediario> pegarLancamentosPorVendaId(Long id);
    public LancamentoCrediario atualizarStatusLancamentoCrediarioPorId(Long id, LancamentoCrediarioDTO lancamentoCrediarioDTO);
    public void gerandoLancamentosParaCrediario(Venda venda);
    public void deletarLancamentosPorClienteId(Long clienteId);
    public void deletarLancamentosPorVendaId(Long vendaId);

}
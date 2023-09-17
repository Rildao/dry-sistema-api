package com.drylands.api.services;

import com.drylands.api.domain.LancamentoCrediario;
import com.drylands.api.domain.Venda;

public interface LancamentoCrediarioService {
    public LancamentoCrediario criarLancamentoCrediario (LancamentoCrediario lancamentoCrediario);
    public void gerandoLancamentosParaCrediario(Venda venda);
}
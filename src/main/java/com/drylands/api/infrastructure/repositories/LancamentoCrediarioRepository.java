package com.drylands.api.infrastructure.repositories;

import com.drylands.api.domain.LancamentoCrediario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LancamentoCrediarioRepository extends JpaRepository<LancamentoCrediario, Long> {
    List<LancamentoCrediario> findAllByVendaId(Long vendaId);

    List<LancamentoCrediario> findAllByVendaIdOrderByDataPagamentoDesc(Long vendaId);

    @Query("SELECT l FROM LancamentoCrediario l WHERE l.statusVenda = ATRASADO")
    List<LancamentoCrediario> listarLancamentosEmAtraso();

    void deleteByVendaClienteId(Long clienteId);

    void deleteByVendaId(Long vendaId);
}
package com.drylands.api.infrastructure.repositories;

import com.drylands.api.domain.LancamentoCrediario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LancamentoCrediarioRepository extends JpaRepository<LancamentoCrediario, Long> {
    List<LancamentoCrediario> findAllByVendaId(Long vendaId);
    void deleteByVendaClienteId(Long clienteId);
    void deleteByVendaId(Long vendaId);
}
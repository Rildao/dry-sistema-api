package com.drylands.api.infrastructure.repositories;

import com.drylands.api.domain.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    Page<Venda> findAllByClienteId(Long id, Pageable pageable);

    @Query(value = "SELECT venda FROM Venda venda " +
            "INNER JOIN venda.cliente c " +
            "WHERE c.id = ?1 ORDER BY venda.dataCriacao DESC")
    List<Venda> findAllByClienteIdAndDataCriacaoOrderByDesc(Long id);
}
package com.drylands.api.infrastructure.repositories;

import com.drylands.api.domain.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    Page<Venda> findAllByClienteId(Long id, Pageable pageable);

    @Query(value = "SELECT venda FROM Venda venda " +
            "INNER JOIN venda.cliente c " +
            "WHERE c.id = ?1 ORDER BY venda.dataCriacao DESC")
    List<Venda> findAllByClienteIdAndDataCriacaoOrderByDesc(Long id);

    void deleteByClienteId(Long id);

    @Query(value = "SELECT v FROM Venda v WHERE v.tipoVenda = CREDIARIO")
    List<Venda> findAllByTypeSaleCredit();

    @Query("SELECT COUNT(v) FROM Venda v WHERE MONTH(v.dataVenda) = MONTH(CURRENT_DATE) AND YEAR(v.dataVenda) = YEAR(CURRENT_DATE)")
    BigInteger totalVendasRealizadas();

    @Query("SELECT SUM(v.valorVenda) FROM Venda v WHERE MONTH(v.dataVenda) = MONTH(CURRENT_DATE) AND YEAR(v.dataVenda) = YEAR(CURRENT_DATE) AND  v.tipoVenda = CREDIARIO")
    BigDecimal valorTotalDeVendasRealizadasNoCrediario();

    @Query("SELECT SUM(v.valorVenda) FROM Venda v WHERE MONTH(v.dataVenda) = MONTH(CURRENT_DATE) AND YEAR(v.dataVenda) = YEAR(CURRENT_DATE) AND  v.tipoVenda = PIX")
    BigDecimal valorTotalDeVendasRealizadasNoPix();

    @Query("SELECT SUM(v.valorVenda) FROM Venda v WHERE MONTH(v.dataVenda) = MONTH(CURRENT_DATE) AND YEAR(v.dataVenda) = YEAR(CURRENT_DATE) AND  v.tipoVenda = CARTAO_CREDITO")
    BigDecimal valorTotalDeVendasRealizadasNoCartao();

    @Query("SELECT SUM(v.valorVenda) FROM Venda v WHERE MONTH(v.dataVenda) = MONTH(CURRENT_DATE) AND YEAR(v.dataVenda) = YEAR(CURRENT_DATE) AND  v.tipoVenda = DINHEIRO")
    BigDecimal valorTotalDeVendasRealizadasNoDinheiro();

}
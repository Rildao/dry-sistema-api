package com.drylands.api.infrastructure.repositories;

import com.drylands.api.domain.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    Page<Venda> findAllByOrderByDataVendaDesc(Pageable pageable);

    Page<Venda> findAllByClienteId(Long id, Pageable pageable);

    @Query(value = "SELECT venda FROM Venda venda " +
            "INNER JOIN venda.cliente c " +
            "WHERE c.id = ?1 ORDER BY venda.dataVenda DESC")
    List<Venda> findAllByClienteIdAndDataCriacaoOrderByDesc(Long id);

    void deleteByClienteId(Long id);

    @Query(value = "SELECT v FROM Venda v WHERE v.tipoVenda = CREDIARIO")
    List<Venda> findAllByTypeSaleCredit();

    @Query("SELECT COUNT(v) FROM Venda v WHERE v.dataVenda BETWEEN ?1 AND ?2")
    BigInteger totalVendasRealizadas(LocalDate dataInicio, LocalDate dataFinal);

    @Query("SELECT SUM(v.valorVenda) FROM Venda v WHERE v.dataVenda BETWEEN ?1 AND ?2")
    BigDecimal valorTotalFaturado(LocalDate dataInicio, LocalDate dataFinal);

    @Query(value = "SELECT EXTRACT(YEAR FROM v.data_venda) AS ano, " +
            "EXTRACT(MONTH FROM v.data_venda) AS mes, " +
            "COUNT(*) AS total_vendas " +
            "FROM venda v " +
            "WHERE v.data_venda BETWEEN TO_DATE(:dataInicio, 'YYYY-MM-DD') " +
            "AND TO_DATE(:dataFinal, 'YYYY-MM-DD') " +
            "GROUP BY EXTRACT(YEAR FROM v.data_venda), EXTRACT(MONTH FROM v.data_venda) " +
            "ORDER BY EXTRACT(YEAR FROM v.data_venda), EXTRACT(MONTH FROM v.data_venda)",
            nativeQuery = true)
    List<Object[]> contagemDeVendasPorMes(@Param("dataInicio") String dataInicio, @Param("dataFinal") String dataFinal);

    @Query(value = "SELECT EXTRACT(YEAR FROM v.data_venda) AS ano, " +
            "EXTRACT(MONTH FROM v.data_venda) AS mes, " +
            "COUNT(*) AS total_vendas " +
            "FROM venda v " +
            "WHERE v.data_venda BETWEEN TO_DATE(:dataInicio, 'YYYY-MM-DD') " +
            "AND TO_DATE(:dataFinal, 'YYYY-MM-DD') " +
            "AND v.tipo_venda = :tipoVenda " +
            "GROUP BY EXTRACT(YEAR FROM v.data_venda), EXTRACT(MONTH FROM v.data_venda) " +
            "ORDER BY EXTRACT(YEAR FROM v.data_venda), EXTRACT(MONTH FROM v.data_venda)",
            nativeQuery = true)
    List<Object[]> contagemDeVendasPorMesComFiltro(@Param("dataInicio") String dataInicio,
                                                   @Param("dataFinal") String dataFinal,
                                                   @Param("tipoVenda") int tipoVenda);

    @Query(value = "SELECT EXTRACT(YEAR FROM v.data_venda) AS ano,\n" +
            "EXTRACT(MONTH FROM v.data_venda) AS mes, " +
            "SUM(v.valor_venda) AS total_faturado " +
            "FROM venda v " +
            "WHERE v.data_venda BETWEEN TO_DATE(:dataInicio, 'YYYY-MM-DD') AND TO_DATE(:dataFinal, 'YYYY-MM-DD') " +
            "AND v.tipo_venda = :tipoVenda " +
            "GROUP BY EXTRACT(YEAR FROM v.data_venda), EXTRACT(MONTH FROM v.data_venda) " +
            "ORDER BY EXTRACT(YEAR FROM v.data_venda), EXTRACT(MONTH FROM v.data_venda) ",
            nativeQuery = true)
    List<Object[]> somatorioDeVendasPorMesPorVenda(@Param("dataInicio") String dataInicio,
                                             @Param("dataFinal") String dataFinal,
                                             @Param("tipoVenda") int tipoVenda);

    @Query(value = "SELECT EXTRACT(YEAR FROM v.data_venda) AS ano, " +
            "EXTRACT(MONTH FROM v.data_venda) AS mes, " +
            "SUM(v.valor_venda) AS total_vendas " +
            "FROM venda v " +
            "WHERE v.data_venda BETWEEN TO_DATE(:dataInicio, 'YYYY-MM-DD') " +
            "AND TO_DATE(:dataFinal, 'YYYY-MM-DD') " +
            "GROUP BY EXTRACT(YEAR FROM v.data_venda), EXTRACT(MONTH FROM v.data_venda) " +
            "ORDER BY EXTRACT(YEAR FROM v.data_venda), EXTRACT(MONTH FROM v.data_venda)",
            nativeQuery = true)
    List<Object[]> somatorioDeVendasPorMes(@Param("dataInicio") String dataInicio, @Param("dataFinal") String dataFinal);

    @Query(value = "SELECT EXTRACT(YEAR FROM v.data_venda) AS ano, " +
            "EXTRACT(MONTH FROM v.data_venda) AS mes, " +
            "COUNT(*) AS total_vendas, " +
            "SUM(v.valor_venda) AS total_vendas " +
            "FROM venda v " +
            "WHERE v.data_venda BETWEEN TO_DATE(:dataInicio, 'YYYY-MM-DD') " +
            "AND TO_DATE(:dataFinal, 'YYYY-MM-DD') " +
            "GROUP BY EXTRACT(YEAR FROM v.data_venda), EXTRACT(MONTH FROM v.data_venda) " +
            "ORDER BY EXTRACT(YEAR FROM v.data_venda), EXTRACT(MONTH FROM v.data_venda)",
            nativeQuery = true)
    List<Object[]> somatorioParaRelatorio(@Param("dataInicio") String dataInicio,
                                                   @Param("dataFinal") String dataFinal);
}
package com.drylands.api.infrastructure.repositories;

import com.drylands.api.domain.Notificacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    @Query(value = "SELECT n FROM Notificacao n " +
            "  JOIN n.venda v " +
            "  JOIN v.cliente c " +
            " WHERE n.lido = ?1 " +
            " ORDER BY n.dataCriacao DESC")
    Page<Notificacao> listarNotificacoesComFiltrosEPaginado(boolean lido, Pageable pageable);

    @Query(value = "SELECT n FROM Notificacao n " +
            "  JOIN n.venda v " +
            "  JOIN v.cliente c " +
            " WHERE n.lido = :lido AND (c.nome ILIKE CONCAT('%', :filter ,'%') OR c.cpf ILIKE CONCAT('%', :filter ,'%') OR c.telefone ILIKE CONCAT('%', :filter ,'%')) " +
            " ORDER BY n.dataCriacao DESC")
    Page<Notificacao> listarNotificacoesComFiltrosEPaginado(boolean lido, String filter, Pageable pageable);

    @Query(value = " SELECT COUNT(n) FROM Notificacao n WHERE n.lido = FALSE ")
    BigInteger contagemDeNotificacoesNaoLidas();

    @Query(value = "SELECT n FROM notificacao n " +
            "INNER JOIN venda v ON v.id = n.venda_id " +
            "INNER JOIN lancamento_crediario lc ON lc.venda_id = v.id " +
            "WHERE v.id = :id " +
            "AND n.tipo_notificacao = 0 " +
            "AND lc.data_pagamento = :dataPagamento", nativeQuery = true)
    Object findFirstByVendaIdAndTipoNotificacaoIsAtraso(@Param("id") Long id, @Param("dataPagamento") LocalDate dataPagamento);

    List<Notificacao> findAllByLidoIsTrue();
}
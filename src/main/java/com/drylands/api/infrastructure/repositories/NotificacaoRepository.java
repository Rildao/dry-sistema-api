package com.drylands.api.infrastructure.repositories;

import com.drylands.api.domain.Notificacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    @Query(value = "SELECT n FROM Notificacao n " +
            "  JOIN n.venda v " +
            "  JOIN v.cliente c " +
            " WHERE n.lido = ?1 AND (c.nome = ?2 OR c.cpf = ?2 OR c.telefone = ?2) ")
    Page<Notificacao> listarNotificacoesComFiltrosEPaginado(boolean lido, String filter, Pageable pageable);
}
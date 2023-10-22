package com.drylands.api.infrastructure.repositories;

import com.drylands.api.domain.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCpf(String cpf);
    @Query(value = "SELECT c FROM Cliente c " +
            " WHERE c.nome ILIKE CONCAT('%', :filter ,'%') " +
            " OR c.cpf LIKE CONCAT('%', :filter ,'%')" +
            " OR c.telefone LIKE CONCAT('%', :filter ,'%') " +
            " OR c.endereco LIKE CONCAT('%', :filter ,'%')")
    Page<Cliente> findAllByFilters(Pageable pageable, String filter);

    @Query(value = "SELECT COUNT(c) FROM Cliente c WHERE c.dataCriacao BETWEEN ?1 AND ?2")
    BigInteger totalDeClientes(Date dataInicio, Date dataFinal);

    @Query(value = "SELECT EXTRACT(YEAR FROM c.data_criacao) AS ano, " +
            "EXTRACT(MONTH FROM c.data_criacao) AS mes, " +
            "COUNT(*) AS total_clientes " +
            "FROM cliente c " +
            "WHERE c.data_criacao BETWEEN TO_DATE(:dataInicio, 'YYYY-MM-DD') " +
            "AND TO_DATE(:dataFinal, 'YYYY-MM-DD') " +
            "GROUP BY EXTRACT(YEAR FROM c.data_criacao), EXTRACT(MONTH FROM c.data_criacao) " +
            "ORDER BY EXTRACT(YEAR FROM c.data_criacao), EXTRACT(MONTH FROM c.data_criacao)",
            nativeQuery = true)
    List<Object[]> contagemDeClientesPorMes(@Param("dataInicio") String dataInicio, @Param("dataFinal") String dataFinal);
}
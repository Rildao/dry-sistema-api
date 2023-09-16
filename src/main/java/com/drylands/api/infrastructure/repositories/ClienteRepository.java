package com.drylands.api.infrastructure.repositories;

import com.drylands.api.domain.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCpf(String cpf);
    @Query(value = "SELECT c FROM Cliente c " +
            " WHERE c.nome ILIKE CONCAT('%', :filter ,'%') " +
            " OR c.cpf LIKE CONCAT('%', :filter ,'%') ")
    Page<Cliente> findAllByFilters(Pageable pageable, String filter);

    @Query(value = "SELECT COUNT(*) FROM cliente", nativeQuery = true)
    BigInteger totalDeClientes();
}
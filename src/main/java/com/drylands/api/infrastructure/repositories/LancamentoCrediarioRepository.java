package com.drylands.api.infrastructure.repositories;

import com.drylands.api.domain.LancamentoCrediario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LancamentoCrediarioRepository extends JpaRepository<LancamentoCrediario, Long> { }
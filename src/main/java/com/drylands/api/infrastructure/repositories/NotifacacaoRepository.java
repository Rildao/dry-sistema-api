package com.drylands.api.infrastructure.repositories;

import com.drylands.api.domain.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifacacaoRepository extends JpaRepository<Notificacao, Long> { }
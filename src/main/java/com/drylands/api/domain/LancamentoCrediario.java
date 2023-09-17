package com.drylands.api.domain;

import com.drylands.api.domain.enums.EStatusVenda;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "lancamento_crediario")
public class LancamentoCrediario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "lc_seq")
    @SequenceGenerator(name = "lc_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name="venda_id", nullable = false)
    private Venda venda;

    @Column(name = "valor_parcela", nullable = false)
    private float valorParcela;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDate dataPagamento;

    @Column(name = "status_venda", nullable = false)
    private EStatusVenda statusVenda;
}

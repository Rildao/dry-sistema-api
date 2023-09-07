package com.drylands.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "client_seq")
    @SequenceGenerator(name = "client_seq", allocationSize = 1)
    private Long id;

    private String nome;

    private String cpf;

    private String telefone;

    private String endereco;

    @Column(name = "data_criacao")
    private Date dataCriacao;

    @Column(name = "data_atualizacao")
    private Date dataAtualizacao;
}
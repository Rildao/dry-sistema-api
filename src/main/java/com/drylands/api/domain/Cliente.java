package com.drylands.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cliente")
public class Cliente extends EntidadeAbstrata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "client_seq")
    @SequenceGenerator(name = "client_seq", allocationSize = 1)
    private Long id;

    private String nome;

    private String cpf;

    private String telefone;

    private String endereco;
}
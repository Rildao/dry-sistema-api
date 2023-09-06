package com.drylands.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String senha;

    private Boolean primeiro_acesso;

    private Date data_criacao;

    private Date data_atualizacao;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<JwtToken> jwtTokens;
}
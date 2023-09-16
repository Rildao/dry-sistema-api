package com.drylands.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "usuarios")
public class Usuario extends EntidadeAbstrata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String senha;

    @Column(name = "primeiro_acesso")
    private Boolean primeiroAcesso;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<JwtToken> jwtTokens;
}
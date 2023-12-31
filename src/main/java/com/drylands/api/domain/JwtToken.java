package com.drylands.api.domain;

import com.drylands.api.domain.enums.TokenEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "jwt_token_seq")
    @SequenceGenerator(name = "jwt_token_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TokenEnum tokenTipo;

    private String valor;
    private Long codigoVerificacao;

    @ManyToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "usuario_id")
    private Usuario usuario;

}

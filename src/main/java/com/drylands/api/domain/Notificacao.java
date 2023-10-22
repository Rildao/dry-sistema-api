package com.drylands.api.domain;

import com.drylands.api.domain.enums.ETipoNotificacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "notificacao")
public class Notificacao extends EntidadeAbstrata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "not_seq")
    @SequenceGenerator(name = "not_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;

    @Column(name = "mensagem", length = 2056, nullable = false)
    private String mensagem;

    @Column(name = "lido", nullable = false)
    private Boolean lido;

    @Column(name = "tipo_notificacao")
    ETipoNotificacao tipoNotificacao;
}
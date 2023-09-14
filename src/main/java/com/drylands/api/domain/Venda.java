package com.drylands.api.domain;

import com.drylands.api.domain.enums.EStatusVenda;
import com.drylands.api.domain.enums.ETipoVenda;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "venda")
public class Venda extends EntidadeAbstrata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "venda_seq")
    @SequenceGenerator(name = "venda_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name="cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "tipo_venda")
    private ETipoVenda tipoVenda;

    @Column(name = "quantidade_parcelas")
    private float quantidadeParcelas;

    @Column(name = "valor_venda")
    private float valorVenda;

    @Column(name = "status_venda")
    private EStatusVenda statusVenda;

    @Column(name = "data_venda")
    private Date dataVenda;
}

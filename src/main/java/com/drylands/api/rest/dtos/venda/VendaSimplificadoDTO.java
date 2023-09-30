package com.drylands.api.rest.dtos.venda;

import com.drylands.api.domain.enums.ETipoVenda;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class VendaSimplificadoDTO {

    private Long id;

    private ETipoVenda tipoVenda;

    private float quantidadeParcelas;

    private float valorVenda;

    private String statusVenda;

    private LocalDate dataVenda;

    private Date dataCriacao;

    private Date dataAtualizacao;

    private int diaVencimentoLancamento;

    private String descricao;
}

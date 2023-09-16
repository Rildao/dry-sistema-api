package com.drylands.api.rest.dtos.venda;

import com.drylands.api.domain.enums.EStatusVenda;
import com.drylands.api.domain.enums.ETipoVenda;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class VendaSimplificadoDTO {

    private Long id;

    private ETipoVenda tipoVenda;

    private float quantidadeParcelas;

    private float valorVenda;

    private EStatusVenda statusVenda;

    private Date dataVenda;

    private Date dataCriacao;

    private Date dataAtualizacao;

    private Date dataVencimentoLancamento;
}

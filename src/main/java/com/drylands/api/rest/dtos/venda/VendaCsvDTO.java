package com.drylands.api.rest.dtos.venda;

import com.drylands.api.domain.enums.ETipoVenda;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VendaCsvDTO {

    private ETipoVenda tipoVenda;

    private float quantidadeParcelas;

    private float valorVenda;

    private String statusVenda;

    private LocalDate dataVenda;

    private int diaVencimentoLancamento;

    private String descricao;
}

package com.drylands.api.rest.dtos.lancamento_crediario;

import com.drylands.api.domain.enums.EStatusVenda;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LancamentoCrediarioDTO {

    private Long id;

    private float valorParcela;

    private LocalDate dataPagamento;

    private EStatusVenda statusVenda;
}
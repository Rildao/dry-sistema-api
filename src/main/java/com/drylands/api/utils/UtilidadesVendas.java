package com.drylands.api.utils;

import com.drylands.api.domain.enums.ETipoVenda;
import com.drylands.api.infrastructure.exceptions.BadRequestException;
import com.drylands.api.rest.dtos.venda.VendaDTO;
import com.drylands.api.rest.dtos.venda.VendaSimplificadoDTO;

import java.util.Objects;

public class UtilidadesVendas {
    public static void validarVendas(VendaDTO vendaDto) {

        if(Objects.isNull(vendaDto.getValorVenda()) || Objects.equals(vendaDto.getValorVenda(), 0)) throw new BadRequestException("Valor da venda não pode estar zerado");

        if(vendaDto.getTipoVenda().equals(ETipoVenda.CREDIARIO) && (Objects.isNull(vendaDto.getQuantidadeParcelas()) || Objects.equals(vendaDto.getQuantidadeParcelas(), 0))) throw new BadRequestException("Digite a quantidade de parcelas da compra no crediário.");
    }

    public static void validarVendas(VendaSimplificadoDTO vendaDto) {

        if(Objects.isNull(vendaDto.getValorVenda()) || Objects.equals(vendaDto.getValorVenda(), 0)) throw new BadRequestException("Valor da venda não pode estar zerado");

        if(vendaDto.getTipoVenda().equals(ETipoVenda.CREDIARIO) && (Objects.isNull(vendaDto.getQuantidadeParcelas()) || Objects.equals(vendaDto.getQuantidadeParcelas(), 0))) throw new BadRequestException("Digite a quantidade de parcelas da compra no crediário.");
    }
}

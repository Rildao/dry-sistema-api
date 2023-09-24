package com.drylands.api.rest.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
public class PainelAdministrativoDTO {
    BigInteger totalClientes;
    BigInteger totalVendas;
    BigDecimal totalValorCrediario;
    BigDecimal totalValorPix;
    BigDecimal totalValorCartao;
    BigDecimal totalValorDinheiro;
    List<IndicadorMesDTO> indicadorVendasPorMes;
    List<IndicadorMesDTO> indicadorClientesPorMes;
}

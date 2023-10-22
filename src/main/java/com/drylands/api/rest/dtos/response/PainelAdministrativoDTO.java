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
    BigDecimal totalValorFaturado;
    IndicadoresVendasPorMesDTO indicadoresVendasPorMes;
    IndicadoresVendasPorMesDTO indicadoresTotalFaturadoPorMes;
    List<IndicadorMesDTO> indicadorClientesPorMes;
}

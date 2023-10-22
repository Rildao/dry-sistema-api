package com.drylands.api.rest.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IndicadoresVendasPorMesDTO {
    List<IndicadorMesDTO> indicadorValorCrediarioPorMes;
    List<IndicadorMesDTO> indicadorValorCartaoDeCreditoPorMes;
    List<IndicadorMesDTO> indicadorValorPorMesPixPorMes;
    List<IndicadorMesDTO> indicadorValorDinheiroPorMes;
}
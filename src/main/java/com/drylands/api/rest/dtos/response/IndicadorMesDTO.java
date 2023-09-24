package com.drylands.api.rest.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndicadorMesDTO  implements Comparable<IndicadorMesDTO> {
    Integer ano;
    Integer mes;
    Double total;

    @Override
    public int compareTo(IndicadorMesDTO outroDto) {
        int anoComparacao = this.getAno().compareTo(outroDto.getAno());
        if (anoComparacao != 0) {
            return anoComparacao;
        }

        return this.getMes().compareTo(outroDto.getMes());
    }
}

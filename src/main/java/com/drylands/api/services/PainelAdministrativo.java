package com.drylands.api.services;

import com.drylands.api.domain.enums.ETipoVenda;
import com.drylands.api.rest.dtos.response.PainelAdministrativoDTO;

public interface PainelAdministrativo {
    PainelAdministrativoDTO informacoesPainelAdministrativo(ETipoVenda tipoVenda);
}

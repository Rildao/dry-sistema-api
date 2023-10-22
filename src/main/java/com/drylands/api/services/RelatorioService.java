package com.drylands.api.services;

import com.drylands.api.rest.dtos.response.RelatorioDTO;

import java.util.List;

public interface RelatorioService {
    List<RelatorioDTO> informacoesParaRelatorio(String dataInicio, String dataFim);
}

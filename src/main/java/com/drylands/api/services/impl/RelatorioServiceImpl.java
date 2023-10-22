package com.drylands.api.services.impl;

import com.drylands.api.infrastructure.repositories.VendaRepository;
import com.drylands.api.rest.dtos.response.IndicadorMesDTO;
import com.drylands.api.rest.dtos.response.RelatorioDTO;
import com.drylands.api.services.RelatorioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioServiceImpl implements RelatorioService {
    VendaRepository vendaRepository;


    public RelatorioServiceImpl(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RelatorioDTO> informacoesParaRelatorio(String dataInicioStr, String dataFimStr) {
        List<RelatorioDTO> relatorioDTO = new ArrayList<>();

        LocalDate dataInicio = LocalDate.parse(dataInicioStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate dataFinal = LocalDate.parse(dataFimStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return this.criarIndicadores(relatorioDTO, dataInicio, dataFinal);
    }

    private List<RelatorioDTO> criarIndicadores (List<RelatorioDTO> relatorioDTO, LocalDate dataInicioStr,  LocalDate dataFimStr) {
        return this.montarIndicadorVenda(relatorioDTO, dataInicioStr, dataFimStr);
    }

    private List<RelatorioDTO> montarIndicadorVenda(List<RelatorioDTO> relatorioDTO, LocalDate dataInicio,  LocalDate dataFinal) {
        List<Object[]> totalVendasCrediarioObjetos = this.vendaRepository.somatorioParaRelatorio(dataInicio.toString(), dataFinal.toString());
        return this.montarDtoIndicadores(relatorioDTO, dataFinal, dataInicio, totalVendasCrediarioObjetos);
    }

    private List<RelatorioDTO> montarDtoIndicadores(List<RelatorioDTO> relatorioDTO, LocalDate dataFinal, LocalDate dataInicio, List<Object[]> totalClientesObjetos) {
        Map<Integer, Map<Integer, RelatorioDTO>> vendasPorMesMap = new HashMap<>();
        totalClientesObjetos.forEach(objeto -> {
            RelatorioDTO relatorioDto = new RelatorioDTO();

            Double anoParaConverter = (Double) objeto[0];
            Double mesParaConverter = (Double) objeto[1];

            Integer ano = anoParaConverter.intValue();
            Integer mes = mesParaConverter.intValue();

            relatorioDto.setAno(ano);
            relatorioDto.setMes(mes);
            relatorioDto.setQtdVendas(Double.valueOf(objeto[2].toString()));
            relatorioDto.setTotal(Double.valueOf(objeto[3].toString()));

            vendasPorMesMap.computeIfAbsent(ano, k -> new HashMap<>()).put(mes, relatorioDto);
            relatorioDTO.add(relatorioDto);
        });

        int inicioAno = dataInicio.getYear();
        int finalAno = dataFinal.getYear();

        for (int ano = inicioAno; ano <= finalAno; ano++) {
            final Integer finalAnoInteravel = ano;

            int inicioMes = (ano == inicioAno) ? dataInicio.getMonthValue() : 1;
            int finalMes = (ano == finalAno) ? dataFinal.getMonthValue() : 12;
            for (int mes = inicioMes; mes <= finalMes; mes++) {
                final Integer finalMesInteravel = mes;

                vendasPorMesMap.computeIfAbsent(finalAnoInteravel, k -> new HashMap<>()).computeIfAbsent(finalMesInteravel, k -> {
                    RelatorioDTO dtoMesVazio = new RelatorioDTO();
                    dtoMesVazio.setAno(finalAnoInteravel);
                    dtoMesVazio.setMes(finalMesInteravel);
                    dtoMesVazio.setTotal(Double.valueOf(0));
                    dtoMesVazio.setQtdVendas(Double.valueOf(0));

                    return dtoMesVazio;
                });
            }
        }

        for (Map.Entry<Integer, Map<Integer, RelatorioDTO>> entry : vendasPorMesMap.entrySet()) {
            relatorioDTO.addAll(entry.getValue().values());
        }

        relatorioDTO.sort(IndicadorMesDTO::compareTo);

        return relatorioDTO.stream().distinct().collect(Collectors.toList());
    }
}
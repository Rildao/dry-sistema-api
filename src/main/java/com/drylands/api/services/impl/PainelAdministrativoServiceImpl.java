package com.drylands.api.services.impl;

import com.drylands.api.domain.enums.ETipoVenda;
import com.drylands.api.infrastructure.repositories.ClienteRepository;
import com.drylands.api.infrastructure.repositories.VendaRepository;
import com.drylands.api.rest.dtos.response.IndicadorMesDTO;
import com.drylands.api.rest.dtos.response.PainelAdministrativoDTO;
import com.drylands.api.services.PainelAdministrativo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PainelAdministrativoServiceImpl implements PainelAdministrativo {
    ClienteRepository clienteRepository;
    VendaRepository vendaRepository;


    public PainelAdministrativoServiceImpl(ClienteRepository clienteRepository, VendaRepository vendaRepository) {
        this.clienteRepository = clienteRepository;
        this.vendaRepository = vendaRepository;
    }

    @Override
    public PainelAdministrativoDTO informacoesPainelAdministrativo(ETipoVenda tipoVenda) {
        PainelAdministrativoDTO painelAdministrativoDto = new PainelAdministrativoDTO();

        this.metricasMensais(painelAdministrativoDto);
        this.criarIndicadores(painelAdministrativoDto, tipoVenda);

        return painelAdministrativoDto;
    }

    private PainelAdministrativoDTO metricasMensais(PainelAdministrativoDTO painelAdministrativoDto) {
        BigInteger totalClientes = this.clienteRepository.totalDeClientes();
        BigInteger totalVendasRealizadas = this.vendaRepository.totalVendasRealizadas();
        BigDecimal totalValorCrediario = this.vendaRepository.valorTotalDeVendasRealizadasNoCrediario();
        BigDecimal totalValorPix = this.vendaRepository.valorTotalDeVendasRealizadasNoPix();
        BigDecimal totalValorCartao = this.vendaRepository.valorTotalDeVendasRealizadasNoCartao();
        BigDecimal totalValorDinheiro = this.vendaRepository.valorTotalDeVendasRealizadasNoDinheiro();

        painelAdministrativoDto.setTotalClientes(Objects.nonNull(totalClientes) ? totalClientes : BigInteger.ZERO);
        painelAdministrativoDto.setTotalVendas(Objects.nonNull(totalVendasRealizadas) ? totalVendasRealizadas : BigInteger.ZERO);
        painelAdministrativoDto.setTotalValorCrediario(Objects.nonNull(totalValorCrediario) ? totalValorCrediario : BigDecimal.ZERO);
        painelAdministrativoDto.setTotalValorPix(Objects.nonNull(totalValorPix) ? totalValorPix : BigDecimal.ZERO);
        painelAdministrativoDto.setTotalValorCartao(Objects.nonNull(totalValorCartao) ? totalValorCartao : BigDecimal.ZERO);
        painelAdministrativoDto.setTotalValorDinheiro(Objects.nonNull(totalValorDinheiro) ? totalValorDinheiro : BigDecimal.ZERO);

        return painelAdministrativoDto;
    }

    private void criarIndicadores(PainelAdministrativoDTO painelAdministrativoDto, ETipoVenda tipoVenda) {
        LocalDate dataFinal = LocalDate.now();
        LocalDate dataInicio = dataFinal.minusMonths(12);

        this.montarIndicadorVenda(painelAdministrativoDto, dataFinal, dataInicio, tipoVenda);
        this.montarIndicadorCliente(painelAdministrativoDto, dataFinal, dataInicio);
        this.montarFaturamentoVenda(painelAdministrativoDto, dataFinal, dataInicio, tipoVenda);
    }

    private void montarFaturamentoVenda(PainelAdministrativoDTO painelAdministrativoDto,
                                        LocalDate dataFinal,
                                        LocalDate dataInicio,
                                        ETipoVenda tipoVenda) {
        List<IndicadorMesDTO> indicadorVendasLista = new ArrayList<>();

        List<Object[]> totalVendasObjetos;

        if (Objects.nonNull(tipoVenda)) {
            totalVendasObjetos = this.vendaRepository.somatorioDeVendasPorMesPorVenda(dataInicio.toString(), dataFinal.toString(), tipoVenda.ordinal());
        } else {
            totalVendasObjetos = this.vendaRepository.somatorioDeVendasPorMes(dataInicio.toString(), dataFinal.toString());
        }

        List<IndicadorMesDTO> indicadorFaturamentoListaDto = this.montarDtoIndicadores(dataFinal, dataInicio, indicadorVendasLista, totalVendasObjetos);

        painelAdministrativoDto.setIndicadorTotalFaturadoPorMes(indicadorFaturamentoListaDto);
    }

    private void montarIndicadorVenda(PainelAdministrativoDTO painelAdministrativoDto, LocalDate dataFinal, LocalDate dataInicio, ETipoVenda tipoVenda) {
        List<IndicadorMesDTO> indicadorVendasLista = new ArrayList<>();

        List<Object[]> totalVendasObjetos;

        if (Objects.nonNull(tipoVenda)) {
            totalVendasObjetos = this.vendaRepository.contagemDeVendasPorMesComFiltro(dataInicio.toString(), dataFinal.toString(), tipoVenda.ordinal());
        } else {
            totalVendasObjetos = this.vendaRepository.contagemDeVendasPorMes(dataInicio.toString(), dataFinal.toString());
        }

        List<IndicadorMesDTO> indicadorVendasListaDto = this.montarDtoIndicadores(dataFinal, dataInicio, indicadorVendasLista, totalVendasObjetos);

        painelAdministrativoDto.setIndicadorVendasPorMes(indicadorVendasListaDto);
    }

    private void montarIndicadorCliente(PainelAdministrativoDTO painelAdministrativoDto, LocalDate dataFinal, LocalDate dataInicio) {
        List<IndicadorMesDTO> indicadorVendasLista = new ArrayList<>();

        List<Object[]> totalVendasObjetos = this.clienteRepository.contagemDeClientesPorMes(dataInicio.toString(), dataFinal.toString());

        List<IndicadorMesDTO> indicadorClientesListaDto = this.montarDtoIndicadores(dataFinal, dataInicio, indicadorVendasLista, totalVendasObjetos);

        painelAdministrativoDto.setIndicadorClientesPorMes(indicadorClientesListaDto);
    }

    private List<IndicadorMesDTO> montarDtoIndicadores(LocalDate dataFinal, LocalDate dataInicio, List<IndicadorMesDTO> indicadorLista, List<Object[]> totalClientesObjetos) {

        Map<Integer, Map<Integer, IndicadorMesDTO>> vendasPorMesMap = new HashMap<>();
        totalClientesObjetos.forEach(objeto -> {
            IndicadorMesDTO indicadorVendaDto = new IndicadorMesDTO();

            Double anoParaConverter = (Double) objeto[0];
            Double mesParaConverter = (Double) objeto[1];

            Integer ano = anoParaConverter.intValue();
            Integer mes = mesParaConverter.intValue();

            indicadorVendaDto.setAno(ano);
            indicadorVendaDto.setMes(mes);
            indicadorVendaDto.setTotal(Double.valueOf(objeto[2].toString()));

            vendasPorMesMap.computeIfAbsent(ano, k -> new HashMap<>()).put(mes, indicadorVendaDto);
            indicadorLista.add(indicadorVendaDto);
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
                    IndicadorMesDTO dtoMesVazio = new IndicadorMesDTO();
                    dtoMesVazio.setAno(finalAnoInteravel);
                    dtoMesVazio.setMes(finalMesInteravel);
                    dtoMesVazio.setTotal(Double.valueOf(0));

                    return dtoMesVazio;
                });
            }
        }

        for (Map.Entry<Integer, Map<Integer, IndicadorMesDTO>> entry : vendasPorMesMap.entrySet()) {
            indicadorLista.addAll(entry.getValue().values());
        }

        indicadorLista.sort(IndicadorMesDTO::compareTo);
        List<IndicadorMesDTO> indicadorVendasListaDto = indicadorLista.stream().distinct().collect(Collectors.toList());
        return indicadorVendasListaDto;
    }
}
package com.drylands.api.services.impl;

import com.drylands.api.domain.enums.ETipoVenda;
import com.drylands.api.infrastructure.repositories.ClienteRepository;
import com.drylands.api.infrastructure.repositories.VendaRepository;
import com.drylands.api.rest.dtos.response.IndicadorMesDTO;
import com.drylands.api.rest.dtos.response.IndicadoresVendasPorMesDTO;
import com.drylands.api.rest.dtos.response.PainelAdministrativoDTO;
import com.drylands.api.services.PainelAdministrativo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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

        LocalDate dataFinal = LocalDate.now();
        LocalDate dataInicio = dataFinal.minusMonths(12);

        this.metricasMensais(painelAdministrativoDto, dataInicio, dataFinal);
        this.criarIndicadores(painelAdministrativoDto, tipoVenda, dataInicio, dataFinal);

        return painelAdministrativoDto;
    }

    private PainelAdministrativoDTO metricasMensais(PainelAdministrativoDTO painelAdministrativoDto, LocalDate dataInicio,  LocalDate dataFinal) {
        Date inicioEmDate = Date.from(dataInicio.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date finalEmDate =  Date.from(dataFinal.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        BigInteger totalClientes = this.clienteRepository.totalDeClientes(inicioEmDate, finalEmDate);
        BigInteger totalVendasRealizadas = this.vendaRepository.totalVendasRealizadas(dataInicio, dataFinal);
        BigDecimal totalValorFaturado = this.vendaRepository.valorTotalFaturado(dataInicio, dataFinal);

        painelAdministrativoDto.setTotalClientes(Objects.nonNull(totalClientes) ? totalClientes : BigInteger.ZERO);
        painelAdministrativoDto.setTotalVendas(Objects.nonNull(totalVendasRealizadas) ? totalVendasRealizadas : BigInteger.ZERO);
        painelAdministrativoDto.setTotalValorFaturado(Objects.nonNull(totalValorFaturado) ? totalValorFaturado : BigDecimal.ZERO);

        return painelAdministrativoDto;
    }

    private void criarIndicadores(PainelAdministrativoDTO painelAdministrativoDto, ETipoVenda tipoVenda, LocalDate dataInicio,  LocalDate dataFinal) {
        this.montarIndicadorVenda(painelAdministrativoDto, dataFinal, dataInicio, tipoVenda);
        this.montarIndicadorCliente(painelAdministrativoDto, dataFinal, dataInicio);
        this.montarFaturamentoVenda(painelAdministrativoDto, dataFinal, dataInicio, tipoVenda);
    }

    private void montarFaturamentoVenda(PainelAdministrativoDTO painelAdministrativoDto,
                                        LocalDate dataFinal,
                                        LocalDate dataInicio,
                                        ETipoVenda tipoVenda) {

        IndicadoresVendasPorMesDTO indicadoresFaturadoPorMesDto = new IndicadoresVendasPorMesDTO();

        List<Object[]> totalFaturadoCrediarioObjetos = this.vendaRepository.somatorioDeVendasPorMesPorVenda(dataInicio.toString(), dataFinal.toString(), ETipoVenda.CREDIARIO.ordinal());
        List<IndicadorMesDTO> indicadorFaturadoPorMesCrediario = this.montarDtoIndicadores(dataFinal, dataInicio, totalFaturadoCrediarioObjetos);

        List<Object[]> totalFaturadoCartaoDeCreditoObjetos = this.vendaRepository.somatorioDeVendasPorMesPorVenda(dataInicio.toString(), dataFinal.toString(), ETipoVenda.CARTAO_CREDITO.ordinal());
        List<IndicadorMesDTO> indicadorFaturadoPorMesCartaoDeCredito = this.montarDtoIndicadores(dataFinal, dataInicio, totalFaturadoCartaoDeCreditoObjetos);

        List<Object[]> totalVFaturadoPixObjetos = this.vendaRepository.somatorioDeVendasPorMesPorVenda(dataInicio.toString(), dataFinal.toString(), ETipoVenda.PIX.ordinal());
        List<IndicadorMesDTO> indicadorFaturadoPorMesPix = this.montarDtoIndicadores(dataFinal, dataInicio, totalVFaturadoPixObjetos);

        List<Object[]> totalFaturadoDinheiroObjetos = this.vendaRepository.somatorioDeVendasPorMesPorVenda(dataInicio.toString(), dataFinal.toString(), ETipoVenda.DINHEIRO.ordinal());
        List<IndicadorMesDTO> indicadorFaturadoPorMesDinheiro = this.montarDtoIndicadores(dataFinal, dataInicio, totalFaturadoDinheiroObjetos);

        indicadoresFaturadoPorMesDto.setIndicadorValorCrediarioPorMes(indicadorFaturadoPorMesCrediario);
        indicadoresFaturadoPorMesDto.setIndicadorValorCartaoDeCreditoPorMes(indicadorFaturadoPorMesCartaoDeCredito);
        indicadoresFaturadoPorMesDto.setIndicadorValorPorMesPixPorMes(indicadorFaturadoPorMesPix);
        indicadoresFaturadoPorMesDto.setIndicadorValorDinheiroPorMes(indicadorFaturadoPorMesDinheiro);

        painelAdministrativoDto.setIndicadoresTotalFaturadoPorMes(indicadoresFaturadoPorMesDto);
    }

    private void montarIndicadorVenda(PainelAdministrativoDTO painelAdministrativoDto, LocalDate dataFinal, LocalDate dataInicio, ETipoVenda tipoVenda) {

        IndicadoresVendasPorMesDTO indicadoresVendasPorMesDto = new IndicadoresVendasPorMesDTO();

        List<Object[]> totalVendasCrediarioObjetos = this.vendaRepository.contagemDeVendasPorMesComFiltro(dataInicio.toString(), dataFinal.toString(), ETipoVenda.CREDIARIO.ordinal());
        List<IndicadorMesDTO> indicadorVendaPorMesCrediario = this.montarDtoIndicadores(dataFinal, dataInicio, totalVendasCrediarioObjetos);

        List<Object[]> totalVendasCartaoDeCreditoObjetos = this.vendaRepository.contagemDeVendasPorMesComFiltro(dataInicio.toString(), dataFinal.toString(), ETipoVenda.CARTAO_CREDITO.ordinal());
        List<IndicadorMesDTO> indicadorVendaPorMesCartaoDeCredito = this.montarDtoIndicadores(dataFinal, dataInicio, totalVendasCartaoDeCreditoObjetos);

        List<Object[]> totalVendasPixObjetos = this.vendaRepository.contagemDeVendasPorMesComFiltro(dataInicio.toString(), dataFinal.toString(), ETipoVenda.PIX.ordinal());
        List<IndicadorMesDTO> indicadorVendaPorMesPix = this.montarDtoIndicadores(dataFinal, dataInicio, totalVendasPixObjetos);

        List<Object[]> totalVendasDinheiroObjetos = this.vendaRepository.contagemDeVendasPorMesComFiltro(dataInicio.toString(), dataFinal.toString(), ETipoVenda.DINHEIRO.ordinal());
        List<IndicadorMesDTO> indicadorVendaPorMesDinheiro = this.montarDtoIndicadores(dataFinal, dataInicio, totalVendasDinheiroObjetos);

        indicadoresVendasPorMesDto.setIndicadorValorCrediarioPorMes(indicadorVendaPorMesCrediario);
        indicadoresVendasPorMesDto.setIndicadorValorCartaoDeCreditoPorMes(indicadorVendaPorMesCartaoDeCredito);
        indicadoresVendasPorMesDto.setIndicadorValorPorMesPixPorMes(indicadorVendaPorMesPix);
        indicadoresVendasPorMesDto.setIndicadorValorDinheiroPorMes(indicadorVendaPorMesDinheiro);

        painelAdministrativoDto.setIndicadoresVendasPorMes(indicadoresVendasPorMesDto);
    }

    private void montarIndicadorCliente(PainelAdministrativoDTO painelAdministrativoDto, LocalDate dataFinal, LocalDate dataInicio) {
        List<IndicadorMesDTO> indicadorVendasLista = new ArrayList<>();

        List<Object[]> totalVendasObjetos = this.clienteRepository.contagemDeClientesPorMes(dataInicio.toString(), dataFinal.toString());

        List<IndicadorMesDTO> indicadorClientesListaDto = this.montarDtoIndicadores(dataFinal, dataInicio, totalVendasObjetos);

        painelAdministrativoDto.setIndicadorClientesPorMes(indicadorClientesListaDto);
    }

    private List<IndicadorMesDTO> montarDtoIndicadores(LocalDate dataFinal, LocalDate dataInicio, List<Object[]> totalClientesObjetos) {
        List<IndicadorMesDTO> indicadorLista = new ArrayList<>();

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
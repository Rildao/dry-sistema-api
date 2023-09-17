package com.drylands.api.services.impl;

import com.drylands.api.infrastructure.repositories.ClienteRepository;
import com.drylands.api.infrastructure.repositories.VendaRepository;
import com.drylands.api.rest.dtos.response.PainelAdministrativoDTO;
import com.drylands.api.services.PainelAdministrativo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

@Service
public class PainelAdministrativoServiceImpl implements PainelAdministrativo {
    ClienteRepository clienteRepository;
    VendaRepository vendaRepository;


    public PainelAdministrativoServiceImpl(ClienteRepository clienteRepository, VendaRepository vendaRepository) {
        this.clienteRepository = clienteRepository;
        this.vendaRepository = vendaRepository;
    }

    @Override
    public PainelAdministrativoDTO informacoesPainelAdministrativo() {
        PainelAdministrativoDTO painelAdministrativoDto = new PainelAdministrativoDTO();

        this.metricasMensais(painelAdministrativoDto);

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
}

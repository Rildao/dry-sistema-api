package com.drylands.api.services.impl;

import com.drylands.api.infrastructure.repositories.ClienteRepository;
import com.drylands.api.rest.dtos.response.PainelAdministrativoDTO;
import com.drylands.api.services.PainelAdministrativo;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class PainelAdministrativoServiceImpl implements PainelAdministrativo {
    ClienteRepository clienteRepository;

    public PainelAdministrativoServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public PainelAdministrativoDTO informacoesPainelAdministrativo() {
        BigInteger totalClientes = this.clienteRepository.totalDeClientes();

        PainelAdministrativoDTO painelAdministrativoDto = new PainelAdministrativoDTO();

        painelAdministrativoDto.setTotalClientes(totalClientes);

        return painelAdministrativoDto;
    }
}

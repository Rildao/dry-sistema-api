package com.drylands.api.services.impl;

import com.drylands.api.domain.LancamentoCrediario;
import com.drylands.api.domain.Venda;
import com.drylands.api.domain.enums.EStatusVenda;
import com.drylands.api.domain.enums.ETipoVenda;
import com.drylands.api.infrastructure.repositories.LancamentoCrediarioRepository;
import com.drylands.api.infrastructure.repositories.VendaRepository;
import com.drylands.api.services.LancamentoCrediarioService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


@Service
public class LancamentoCrediarioServiceImpl implements LancamentoCrediarioService {

    VendaRepository vendaRepository;

    LancamentoCrediarioRepository lancamentoCrediarioRepository;

    private ModelMapper modelMapper;

    public LancamentoCrediarioServiceImpl(LancamentoCrediarioRepository lancamentoCrediarioRepository,
                                          VendaRepository vendaRepository, ModelMapper modelMapper) {
        this.lancamentoCrediarioRepository = lancamentoCrediarioRepository;
        this.vendaRepository = vendaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public LancamentoCrediario criarLancamentoCrediario(LancamentoCrediario lancamentoCrediario) {
        return this.lancamentoCrediarioRepository.save(lancamentoCrediario);
    }

    @Override
    public void deletarLancamentosPorClienteId(Long clienteId) {
        this.lancamentoCrediarioRepository.deleteByVendaClienteId(clienteId);
    }

    @Override
    public void deletarLancamentosPorVendaId(Long vendaId) {
        this.lancamentoCrediarioRepository.deleteByVendaId(vendaId);

    }

    @Override
    public void gerandoLancamentosParaCrediario(Venda venda) {
        if (venda.getTipoVenda().equals(ETipoVenda.CREDIARIO)) {
            float valorParcela = venda.getValorVenda()/venda.getQuantidadeParcelas();

            LocalDate dataBaseParaLancamentos;

            if (Objects.nonNull(venda.getDiaVencimentoLancamento())) {
                int diaVencimento = venda.getDiaVencimentoLancamento();

                LocalDate dataVenda = venda.getDataVenda();

                LocalDate proximoDiaVencimento = LocalDate.of(dataVenda.getYear(), dataVenda.getMonth(), diaVencimento);

                if (proximoDiaVencimento.isBefore(dataVenda)) {
                    proximoDiaVencimento = proximoDiaVencimento.plus(1, ChronoUnit.MONTHS);
                }

                dataBaseParaLancamentos = proximoDiaVencimento;
            } else {
                dataBaseParaLancamentos = venda.getDataVenda();
            }

            LocalDate dataUltimoLancamento = dataBaseParaLancamentos;

            for (float qtdLancamentos = 0; qtdLancamentos < venda.getQuantidadeParcelas(); qtdLancamentos++) {
                LancamentoCrediario lancamentoCrediario = new LancamentoCrediario();

                lancamentoCrediario.setVenda(venda);
                lancamentoCrediario.setValorParcela(valorParcela);
                lancamentoCrediario.setDataPagamento(dataUltimoLancamento);
                lancamentoCrediario.setStatusVenda(EStatusVenda.ANDAMENTO);

                this.criarLancamentoCrediario(lancamentoCrediario);

                dataUltimoLancamento = dataUltimoLancamento.plus(1, ChronoUnit.MONTHS);
            }
        }
    }
}

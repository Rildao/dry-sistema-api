package com.drylands.api.services.impl;

import com.drylands.api.domain.LancamentoCrediario;
import com.drylands.api.domain.Venda;
import com.drylands.api.domain.enums.EStatusVenda;
import com.drylands.api.domain.enums.ETipoVenda;
import com.drylands.api.infrastructure.exceptions.NotFoundException;
import com.drylands.api.infrastructure.repositories.LancamentoCrediarioRepository;
import com.drylands.api.infrastructure.repositories.VendaRepository;
import com.drylands.api.rest.dtos.lancamento_crediario.LancamentoCrediarioDTO;
import com.drylands.api.services.LancamentoCrediarioService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


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
    @Transactional
    public LancamentoCrediario atualizarStatusLancamentoCrediarioPorId(Long id, LancamentoCrediarioDTO lancamentoCrediarioDTO) {
        Optional<LancamentoCrediario> lancamentoCrediarioOptional = this.lancamentoCrediarioRepository.findById(id);

        if(lancamentoCrediarioOptional.isEmpty()) throw new NotFoundException("Lancamento não encontrada.");

        LancamentoCrediario lancamentoCrediario = lancamentoCrediarioOptional.get();

        if(lancamentoCrediarioDTO.getStatusVenda().equals(EStatusVenda.PAGO)) {
            List<LancamentoCrediario> lancamentos = this.lancamentoCrediarioRepository.findAllByVendaId(lancamentoCrediario.getVenda().getId());

            Venda venda = lancamentoCrediario.getVenda();
            lancamentoCrediario.setStatusVenda(EStatusVenda.PAGO);

            boolean algumLancamentoEmAtraso = lancamentos.stream()
                    .filter(lan -> !lan.getId().equals(id))
                    .anyMatch(lancamento -> lancamento.getStatusVenda().equals(EStatusVenda.ATRASADO));

            boolean todosPago = lancamentos.stream()
                    .filter(lan -> !lan.getId().equals(id))
                    .allMatch(lancamento -> lancamento.getStatusVenda().equals(EStatusVenda.PAGO));

            if (algumLancamentoEmAtraso) {
                venda.setStatusVenda(EStatusVenda.ATRASADO);
                this.vendaRepository.save(venda);
            } else if(todosPago) {
                venda.setStatusVenda(EStatusVenda.PAGO);
                this.vendaRepository.save(venda);
            }
        } else {
            lancamentoCrediario.setStatusVenda(lancamentoCrediarioDTO.getStatusVenda());
        }

        return this.lancamentoCrediarioRepository.save(lancamentoCrediario);
    }

    @Override
    public List<LancamentoCrediario> pegarLancamentosPorVendaId(Long id) {
        return this.lancamentoCrediarioRepository.findAllByVendaIdOrderByDataPagamentoDesc(id);
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

                LocalDate proximoDiaVencimento = null;

                if(!Objects.equals(venda.getDiaVencimentoLancamento(), 0)) {
                    proximoDiaVencimento = LocalDate.of(dataVenda.getYear(), dataVenda.getMonth(), diaVencimento);

                    if (proximoDiaVencimento.isBefore(dataVenda)) proximoDiaVencimento = proximoDiaVencimento.plus(1, ChronoUnit.MONTHS);
                } else {
                    proximoDiaVencimento = dataVenda;
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

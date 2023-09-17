package com.drylands.api.jobs;

import com.drylands.api.domain.LancamentoCrediario;
import com.drylands.api.domain.Venda;
import com.drylands.api.domain.enums.EStatusVenda;
import com.drylands.api.infrastructure.repositories.LancamentoCrediarioRepository;
import com.drylands.api.infrastructure.repositories.VendaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class VendasJobs {

    private final LancamentoCrediarioRepository lancamentoCrediarioRepository;
    private final VendaRepository vendaRepository;


    public VendasJobs(LancamentoCrediarioRepository lancamentoCrediarioRepository, VendaRepository vendaRepository) {
        this.lancamentoCrediarioRepository = lancamentoCrediarioRepository;
        this.vendaRepository = vendaRepository;
    }

    @Scheduled(cron = "0 0 5 * * *")
    public void verificarLancamentosCrediarioEmAtraso() {
        log.warn("JOB: verificar lançamentos em atraso");

        List<LancamentoCrediario> lancamentoCrediarios = this.lancamentoCrediarioRepository.findAll();

        lancamentoCrediarios.forEach(lancamentoCrediario -> {
            LocalDate dataPagamento = lancamentoCrediario.getDataPagamento();
            LocalDate hoje = LocalDate.now();

            if (dataPagamento.isBefore(hoje)) {
                lancamentoCrediario.setStatusVenda(EStatusVenda.ATRASADO);

                this.lancamentoCrediarioRepository.save(lancamentoCrediario);

                log.info("Status do lançamento {} foi alterado para atrasado.", lancamentoCrediario);
            }
        });
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void verificarStatusDaVenda() {
        log.warn("JOB: verificar vendas em atraso");

        List<Venda> vendas = this.vendaRepository.findAllByTypeSaleCredit();

        vendas.forEach(venda -> {
            List<LancamentoCrediario> lancamentoCrediarios = this.lancamentoCrediarioRepository.findAllByVendaId(venda.getId());

            boolean todosEmAtraso = true;

            for (LancamentoCrediario lancamento : lancamentoCrediarios) {
                if (!lancamento.getStatusVenda().equals(EStatusVenda.ATRASADO)) {
                    todosEmAtraso = false;
                    break;
                }
            }

            if (todosEmAtraso) {
                venda.setStatusVenda(EStatusVenda.ATRASADO);
                this.vendaRepository.save(venda);
                log.info("Status da venda {} foi alterado para atrasado.", venda);
            }
        });
    }
}
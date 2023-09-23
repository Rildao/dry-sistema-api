package com.drylands.api.jobs;

import com.drylands.api.domain.LancamentoCrediario;
import com.drylands.api.domain.Notificacao;
import com.drylands.api.domain.enums.EStatusVenda;
import com.drylands.api.infrastructure.repositories.ClienteRepository;
import com.drylands.api.infrastructure.repositories.LancamentoCrediarioRepository;
import com.drylands.api.infrastructure.repositories.NotificacaoRepository;
import com.drylands.api.infrastructure.repositories.VendaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class NotificacaoJobs {

    private final NotificacaoRepository notificacaoRepository;

    private final LancamentoCrediarioRepository lancamentoCrediarioRepository;

    private final VendaRepository vendaRepository;


    private static final String MENSAGEM_ATRASO = "Parcela programa para {data} em atraso";

    private final ClienteRepository clienteRepository;

    public NotificacaoJobs(NotificacaoRepository notificacaoRepository, LancamentoCrediarioRepository lancamentoCrediarioRepository, VendaRepository vendaRepository, ClienteRepository clienteRepository) {
        this.notificacaoRepository = notificacaoRepository;
        this.lancamentoCrediarioRepository = lancamentoCrediarioRepository;
        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void criarNotifacoes() {
        log.warn("JOB: criar notificaçoes ");

        List<LancamentoCrediario> lancamentos = this.lancamentoCrediarioRepository.listarLancamentosEmAtraso();

        lancamentos.forEach(lancamento ->  {
            if(lancamento.getStatusVenda().equals(EStatusVenda.ATRASADO)) {
                Notificacao notificacao = new Notificacao();

                DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                String data = lancamento.getVenda().getDataVenda().format(formatador);

                notificacao.setMensagem(MENSAGEM_ATRASO.replace("{data}", data));
                notificacao.setLido(Boolean.FALSE);
                notificacao.setVenda(lancamento.getVenda());

                notificacao = this.notificacaoRepository.save(notificacao);

                log.warn("JOB: notificação {} criada com sucesso. ", notificacao);
            }
        });
    }
}
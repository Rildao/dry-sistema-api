package com.drylands.api.jobs;

import com.drylands.api.domain.LancamentoCrediario;
import com.drylands.api.domain.Notificacao;
import com.drylands.api.domain.enums.EStatusVenda;
import com.drylands.api.domain.enums.ETipoNotificacao;
import com.drylands.api.infrastructure.repositories.ClienteRepository;
import com.drylands.api.infrastructure.repositories.LancamentoCrediarioRepository;
import com.drylands.api.infrastructure.repositories.NotificacaoRepository;
import com.drylands.api.infrastructure.repositories.VendaRepository;
import com.drylands.api.utils.UtilidadesData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class NotificacaoJobs {

    private final NotificacaoRepository notificacaoRepository;

    private final LancamentoCrediarioRepository lancamentoCrediarioRepository;

    private final VendaRepository vendaRepository;

    private static final String MENSAGEM_ATRASO = "Parcela a ser paga em {data} por {cliente} atrasada.";

    private static final String MENSAGEM_ALERTA = "Parcela a ser paga em {data} por {cliente} perto do prazo de vencimento.";

    private final ClienteRepository clienteRepository;

    public NotificacaoJobs(NotificacaoRepository notificacaoRepository, LancamentoCrediarioRepository lancamentoCrediarioRepository, VendaRepository vendaRepository, ClienteRepository clienteRepository) {
        this.notificacaoRepository = notificacaoRepository;
        this.lancamentoCrediarioRepository = lancamentoCrediarioRepository;
        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    public void criarNotifacoesDeAtraso() {
        log.warn("JOB: criar notificaçoes de atraso de pagamento");

        List<LancamentoCrediario> lancamentos = this.lancamentoCrediarioRepository.listarLancamentosEmAtraso();

        lancamentos.forEach(lancamento ->  {
            Object notificacao = this.notificacaoRepository.findFirstByVendaIdAndTipoNotificacaoIsAtraso(lancamento.getVenda().getId(), lancamento.getDataPagamento());

            if(Objects.isNull(notificacao)) {
                Notificacao novaNotificacao = new Notificacao();

                DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String data = lancamento.getDataPagamento().format(formatador);

                novaNotificacao.setMensagem(MENSAGEM_ATRASO.replace("{data}", data).replace("{cliente}", lancamento.getVenda().getCliente().getNome()));
                novaNotificacao.setLido(Boolean.FALSE);
                novaNotificacao.setTipoNotificacao(ETipoNotificacao.ATRASO);
                novaNotificacao.setVenda(lancamento.getVenda());
                UtilidadesData.configurarDatasComFusoHorarioBrasileiro(novaNotificacao);

                novaNotificacao = this.notificacaoRepository.save(novaNotificacao);

                log.warn("JOB: notificação de atraso {} criada com sucesso. ", novaNotificacao);
            }
        });
    }

    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void criarNotifacoesDeAlerta() {
        log.warn("JOB: criar notificaçoes de alerta ");

        List<LancamentoCrediario> lancamentos = this.lancamentoCrediarioRepository.findAll();

        lancamentos.forEach(lancamento ->  {
            LocalDate dataPagamento = lancamento.getDataPagamento();
            LocalDate hoje = LocalDate.now();

            if (hoje.equals(dataPagamento.minusDays(1)) && lancamento.getStatusVenda().equals(EStatusVenda.ANDAMENTO)) {
                Notificacao notificacao = new Notificacao();

                DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String data = lancamento.getDataPagamento().format(formatador);

                notificacao.setMensagem(MENSAGEM_ALERTA.replace("{data}", data).replace("{cliente}", lancamento.getVenda().getCliente().getNome()));
                notificacao.setLido(Boolean.FALSE);
                notificacao.setTipoNotificacao(ETipoNotificacao.ALERTA);
                notificacao.setVenda(lancamento.getVenda());
                UtilidadesData.configurarDatasComFusoHorarioBrasileiro(notificacao);

                notificacao = this.notificacaoRepository.save(notificacao);

                log.warn("JOB: notificação de alerta {} criada com sucesso. ", notificacao);
            }
        });
    }

    @Scheduled(cron = "0 0 0 * * 0")
    @Transactional
    public void apagarNotificacoesLidas() {
        log.warn("JOB: apagar notificaçoes lidas ");

        List<Notificacao> notificacaos = this.notificacaoRepository.findAllByLidoIsTrue();

        notificacaos.forEach(notificacao ->  {
            this.notificacaoRepository.delete(notificacao);
            log.warn("JOB: notificação de alerta {} criada com sucesso. ", notificacao);
        });
    }
}
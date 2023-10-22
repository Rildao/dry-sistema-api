package com.drylands.api.rest.dtos.notificacao;

import com.drylands.api.domain.Venda;
import com.drylands.api.domain.enums.ETipoNotificacao;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class NotificacaoDTO {

    private Long id;

    private Venda venda;

    private String mensagem;

    private Boolean lido;

    private ETipoNotificacao tipoNotificacao;

    private Date dataCriacao;

    private Date dataAtualizacao;
}

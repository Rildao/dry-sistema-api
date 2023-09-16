package com.drylands.api.rest.dtos.cliente;

import com.drylands.api.rest.dtos.venda.VendaSimplificadoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ClienteVendasDTO {
    private Long id;

    private String nome;

    private String cpf;

    private String telefone;

    private String endereco;

    private List<VendaSimplificadoDTO> vendas;

    private Date dataCriacao;

    private Date dataAtualizacao;
}

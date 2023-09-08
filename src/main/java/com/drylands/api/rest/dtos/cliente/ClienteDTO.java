package com.drylands.api.rest.dtos.cliente;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ClienteDTO {
    private String id;

    private String nome;

    private String cpf;

    private String telefone;

    private String endereco;

    private Date dataCriacao;

    private Date dataAtualizacao;
}
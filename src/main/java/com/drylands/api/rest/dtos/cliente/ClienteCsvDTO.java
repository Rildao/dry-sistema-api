package com.drylands.api.rest.dtos.cliente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteCsvDTO {
    private String id;

    private String nome;

    private String cpf;

    private String telefone;

    private String endereco;
}
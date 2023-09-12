package com.drylands.api.rest.dtos.cliente;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ClienteDTO {
    private String id;

    @NotBlank(message = "Nome do cliente não pode ser vazio!")
    private String nome;

    private String cpf;

    private String telefone;

    private String endereco;

    private Date dataCriacao;

    private Date dataAtualizacao;
}
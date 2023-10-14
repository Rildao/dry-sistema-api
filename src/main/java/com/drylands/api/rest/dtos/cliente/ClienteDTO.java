package com.drylands.api.rest.dtos.cliente;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ClienteDTO {
    private String id;

    @NotBlank(message = "O nome do cliente não pode estar em branco.")
    private String nome;

    private String cpf;

    private String telefone;

    private String endereco;

    private Date dataCriacao;

    private Date dataAtualizacao;
}
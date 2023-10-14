package com.drylands.api.rest.dtos.cliente;

import com.drylands.api.rest.dtos.venda.VendaSimplificadoDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ClienteVendasDTO {
    private Long id;

    @NotBlank(message = "O nome do cliente não pode estar em branco.")
    private String nome;

    private String cpf;

    private String telefone;

    private String endereco;

    private List<VendaSimplificadoDTO> vendas;

    private Date dataCriacao;

    private Date dataAtualizacao;
}

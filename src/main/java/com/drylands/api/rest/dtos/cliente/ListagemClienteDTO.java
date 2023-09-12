package com.drylands.api.rest.dtos.cliente;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListagemClienteDTO {
    List<ClienteDTO> clientes;
    Long totalElements;
    int totalPage;
    int size;
}

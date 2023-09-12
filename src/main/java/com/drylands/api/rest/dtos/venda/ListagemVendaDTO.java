package com.drylands.api.rest.dtos.venda;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListagemVendaDTO {
    List<VendaDTO> vendas;
    Long totalElements;
    int totalPage;
    int size;
}

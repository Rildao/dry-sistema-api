package com.drylands.api.infrastructure.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractException extends RuntimeException {

    private String erro;
    private String descricao;

    protected AbstractException(String erro, String descricao) {
        this.erro = erro;
        this.descricao = descricao;
    }
}

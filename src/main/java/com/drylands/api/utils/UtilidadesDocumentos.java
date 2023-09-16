package com.drylands.api.utils;

import br.com.caelum.stella.validation.CPFValidator;
import com.drylands.api.infrastructure.exceptions.BadRequestException;
import org.springframework.util.StringUtils;

public class UtilidadesDocumentos {
    public static void validarCpf(String cpf) {
        if(!StringUtils.hasText(cpf)) throw new BadRequestException("Ops! Digite o CPF do cliente.");

        CPFValidator cpfValidator = new CPFValidator();
        try {
            cpfValidator.assertValid(cpf);
        } catch(Exception e){
            e.printStackTrace();
            throw new BadRequestException("Ops! Parece que o CPF que você digitou não é válido.");
        }
    }
}

package com.drylands.api.rest.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDTO {

    @Email(message = "E-mail inválido!")
    private String email;

    @Pattern(message = "Formato de senha inválido", regexp = "^(?=.*[\\d])(?=.*[A-Z])(?=.*[a-z])[\\w!@#$%^&*]{8,}$")
    private String senha;
}
package com.drylands.api.rest.resources;

import com.drylands.api.infrastructure.exceptions.BadRequestException;
import com.drylands.api.infrastructure.exceptions.NotFoundException;
import com.drylands.api.rest.dtos.response.MensagemErroDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<MensagemErroDTO> badRequest(BadRequestException ex) {
        return new ResponseEntity<>(new MensagemErroDTO(HttpStatus.BAD_REQUEST.value(), ex.getErro(), ex.getDescricao()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<MensagemErroDTO> notFound(NotFoundException ex) {
        return new ResponseEntity<>(new MensagemErroDTO(HttpStatus.BAD_REQUEST.value(), ex.getErro(), ex.getDescricao()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<MensagemErroDTO> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));

        return new ResponseEntity<>(new MensagemErroDTO(HttpStatus.BAD_REQUEST.value(), errors, "Erro de validação"), HttpStatus.BAD_REQUEST);
    }
}
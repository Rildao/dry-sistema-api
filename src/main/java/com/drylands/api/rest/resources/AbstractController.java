package com.drylands.api.rest.resources;


import org.modelmapper.ModelMapper;

public abstract class AbstractController {

    private final ModelMapper modelMapper;

    protected AbstractController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <T, U> U mapearDTO(T source, Class<U> destinationType) {
        return modelMapper.map(source, destinationType);
    }
}

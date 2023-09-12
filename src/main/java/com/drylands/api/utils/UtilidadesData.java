package com.drylands.api.utils;

import com.drylands.api.domain.Cliente;
import com.drylands.api.domain.Usuario;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class UtilidadesData {
    public static void configurarDatasComFusoHorarioBrasileiro(Usuario usuario) {
        ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("America/Sao_Paulo"));
        Date date = Date.from(zdt.toInstant());

        usuario.setDataCriacao(date);
        usuario.setDataAtualizacao(date);
    }

    public static void configurarDatasComFusoHorarioBrasileiro(Cliente entity) {
        ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("America/Sao_Paulo"));
        Date date = Date.from(zdt.toInstant());

        entity.setDataCriacao(date);
        entity.setDataAtualizacao(date);
    }

    public static void configurarDatasComFusoHorarioBrasileiroParaAtualizar(Cliente entity) {
        ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("America/Sao_Paulo"));
        Date date = Date.from(zdt.toInstant());

        entity.setDataAtualizacao(date);
    }
}

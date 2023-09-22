package com.drylands.api.utils;

import com.drylands.api.domain.EntidadeAbstrata;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class UtilidadesData {
    public static <T extends EntidadeAbstrata> void configurarDatasComFusoHorarioBrasileiro(T entidade) {
        ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("America/Sao_Paulo"));
        Date date = Date.from(zdt.toInstant());

        entidade.setDataCriacao(date);
        entidade.setDataAtualizacao(date);
    }

    public static <T extends EntidadeAbstrata> void configurarDatasComFusoHorarioBrasileiroParaAtualizar(T entidade) {
        ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("America/Sao_Paulo"));
        Date date = Date.from(zdt.toInstant());

        entidade.setDataAtualizacao(date);
    }
}

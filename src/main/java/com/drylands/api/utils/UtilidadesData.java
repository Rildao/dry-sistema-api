package com.drylands.api.utils;

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
}

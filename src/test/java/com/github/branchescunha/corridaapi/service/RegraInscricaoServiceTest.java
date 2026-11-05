package com.github.branchescunha.corridaapi.service;

import com.github.branchescunha.corridaapi.enums.Categoria;
import com.github.branchescunha.corridaapi.enums.Percurso;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegraInscricaoServiceTest {

    private final RegraInscricaoService service =
            new RegraInscricaoService();

    @Test
    void deveCalcularIdadeQuandoAniversarioFoiAntesDaProva() {
        LocalDate nascimento =
                LocalDate.of(2000, 3, 13);

        assertEquals(
                27,
                service.calcularIdadeNaProva(nascimento)
        );
    }

    @Test
    void deveCalcularIdadeQuandoAniversarioForNoDiaDaProva() {
        LocalDate nascimento =
                LocalDate.of(2000, 3, 14);

        assertEquals(
                27,
                service.calcularIdadeNaProva(nascimento)
        );
    }

    @Test
    void deveCalcularIdadeQuandoAniversarioForDepoisDaProva() {
        LocalDate nascimento =
                LocalDate.of(2000, 3, 15);

        assertEquals(
                26,
                service.calcularIdadeNaProva(nascimento)
        );
    }

    @Test
    void deveClassificar17AnosComoJuvenil() {
        LocalDate nascimento =
                LocalDate.of(2009, 3, 15);

        assertEquals(
                Categoria.JUVENIL,
                service.calcularCategoria(nascimento)
        );
    }

    @Test
    void deveClassificar18AnosComoAdulto() {
        LocalDate nascimento =
                LocalDate.of(2009, 3, 14);

        assertEquals(
                Categoria.ADULTO,
                service.calcularCategoria(nascimento)
        );
    }

    @Test
    void deveClassificar39AnosComoAdulto() {
        LocalDate nascimento =
                LocalDate.of(1987, 3, 15);

        assertEquals(
                Categoria.ADULTO,
                service.calcularCategoria(nascimento)
        );
    }

    @Test
    void deveClassificar40AnosComoMaster() {
        LocalDate nascimento =
                LocalDate.of(1987, 3, 14);

        assertEquals(
                Categoria.MASTER,
                service.calcularCategoria(nascimento)
        );
    }

    @Test
    void deveClassificar59AnosComoMaster() {
        LocalDate nascimento =
                LocalDate.of(1967, 3, 15);

        assertEquals(
                Categoria.MASTER,
                service.calcularCategoria(nascimento)
        );
    }

    @Test
    void deveClassificar60AnosComoSenior() {
        LocalDate nascimento =
                LocalDate.of(1967, 3, 14);

        assertEquals(
                Categoria.SENIOR,
                service.calcularCategoria(nascimento)
        );
    }

    @Test
    void devePriorizarIdadeMinimaAntesDaRegraDos10Km() {
        LocalDate nascimento =
                LocalDate.of(2013, 3, 15);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.validarParticipacao(
                                nascimento,
                                Percurso.KM_10
                        )
                );

        assertEquals(
                "A idade mínima para participar é 14 anos.",
                exception.getMessage()
        );
    }

    @Test
    void devePermitir5KmParaParticipanteCom14Anos() {
        LocalDate nascimento =
                LocalDate.of(2013, 3, 14);

        assertDoesNotThrow(
                () -> service.validarParticipacao(
                        nascimento,
                        Percurso.KM_5
                )
        );
    }

    @Test
    void deveRecusar10KmParaParticipanteCom17Anos() {
        LocalDate nascimento =
                LocalDate.of(2009, 3, 15);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.validarParticipacao(
                        nascimento,
                        Percurso.KM_10
                )
        );
    }

    @Test
    void devePermitir10KmParaParticipanteCom18Anos() {
        LocalDate nascimento =
                LocalDate.of(2009, 3, 14);

        assertDoesNotThrow(
                () -> service.validarParticipacao(
                        nascimento,
                        Percurso.KM_10
                )
        );
    }
}
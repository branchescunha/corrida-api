package com.github.branchescunha.corridaapi.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegraInscricaoNormalizacaoTest {

    private final RegraInscricaoService service =
            new RegraInscricaoService();

    @Test
    void deveNormalizarEspacosDoNome() {
        String nome = service.normalizarNome(
                "   André     Vinícius   "
        );

        assertEquals(
                "André Vinícius",
                nome
        );
    }

    @Test
    void deveValidarTamanhoDepoisDeNormalizarNome() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.normalizarNome("   A   ")
        );
    }

    @Test
    void deveNormalizarEmailAntesDeValidar() {
        String email = service.normalizarEmail(
                "   Ana.Souza@Email.COM   "
        );

        assertEquals(
                "ana.souza@email.com",
                email
        );
    }

    @Test
    void deveRecusarEmailInvalidoDepoisDaNormalizacao() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.normalizarEmail(
                        "   email-invalido   "
                )
        );
    }
}